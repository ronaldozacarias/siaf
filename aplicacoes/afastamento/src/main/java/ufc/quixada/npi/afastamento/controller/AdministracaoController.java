package ufc.quixada.npi.afastamento.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.quixada.npi.afastamento.model.Afastamento;
import ufc.quixada.npi.afastamento.model.Notificacao;
import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Programa;
import ufc.quixada.npi.afastamento.model.Ranking;
import ufc.quixada.npi.afastamento.model.Reserva;
import ufc.quixada.npi.afastamento.model.StatusReserva;
import ufc.quixada.npi.afastamento.model.TuplaRanking;
import ufc.quixada.npi.afastamento.service.AfastamentoService;
import ufc.quixada.npi.afastamento.service.NotificacaoService;
import ufc.quixada.npi.afastamento.service.PeriodoService;
import ufc.quixada.npi.afastamento.service.ProfessorService;
import ufc.quixada.npi.afastamento.service.RankingService;
import ufc.quixada.npi.afastamento.service.ReservaService;
import ufc.quixada.npi.afastamento.util.Constants;
import br.ufc.quixada.npi.ldap.model.Usuario;
import br.ufc.quixada.npi.ldap.service.UsuarioService;

@Controller
@RequestMapping("administracao")
public class AdministracaoController {

	@Inject
	private ProfessorService professorService;

	@Inject
	private RankingService rankingService;

	@Inject
	private PeriodoService periodoService;

	@Inject
	private ReservaService reservaService;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private NotificacaoService notificacaoService;
	
	@Inject
	private AfastamentoService afastamentoService;

	@RequestMapping(value = "/professores", method = RequestMethod.GET)
	public String listarProfessores(Model model) {
		List<Professor> professores = professorService.findAtivos();
		Collections.sort(professores, new Comparator<Professor>() {
			@Override
			public int compare(Professor professor1, Professor professor2) {
				return professor1.getNome().compareTo(professor2.getNome());
			}
		});
		model.addAttribute("professores", professores);
		return Constants.PAGINA_LISTAR_PROFESSORES;
	}

	@RequestMapping(value = "/atualizar-professores", method = RequestMethod.GET)
	public String atualizaProfessores(Model model, RedirectAttributes redirect, HttpSession session) {

		List<Usuario> usuarios = usuarioService.getByAffiliation(Constants.AFFILIATION_DOCENTE);
		for (Usuario usuario : usuarios) {
			Professor professor = professorService.getByCpf(usuario.getCpf());
			if (professor == null) {
				professor = new Professor();
				professor.setCpf(usuario.getCpf());
				professorService.save(professor);
				Reserva reserva = new Reserva();
				reserva.setProfessor(professor);
				try {
					notificacaoService.notificar(reserva, Notificacao.LISTA_DOCENTES_ATUALIZADAS);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}

		atualizaVagas();
		redirect.addFlashAttribute(Constants.INFO, Constants.MSG_LISTA_PROFESSORES_ATUALIZADO);
		return Constants.REDIRECT_PAGINA_LISTAR_PROFESSORES;
	}

	private void atualizaVagas() {
		Periodo periodoAtual = periodoService.getPeriodoPosterior(periodoService.getPeriodoPosterior(periodoService.getPeriodoAtual()));
		List<Periodo> periodos = periodoService.getPeriodosPosteriores(periodoAtual);
		int vagas = (int) (professorService.findAtivos().size() * 0.15);
		for (Periodo periodo : periodos) {
			periodo.setVagas(vagas);
			periodoService.update(periodo);
		}
	}

	@RequestMapping(value = "/homologacao", method = RequestMethod.GET)
	public String getHomologacao(Model model) {
		Periodo periodo = periodoService.getPeriodoAtual();
		if (periodo != null) {
			periodo = periodoService.getPeriodoPosterior(periodo);
			Ranking ranking = new Ranking();
			ranking.setPeriodo(periodo);
			List<TuplaRanking> tuplas = new ArrayList<TuplaRanking>();
			for(TuplaRanking tupla : rankingService.visualizarRanking(periodo)) {
				if (tupla.getReserva().getStatus().equals(StatusReserva.ABERTO)) {
					if(tupla.getReserva().getAnoInicio().equals(periodo.getAno())
						&& tupla.getReserva().getSemestreInicio().equals(periodo.getSemestre())) {
						tuplas.add(tupla);
					}
				} else {
					tuplas.add(tupla);
				}
			}
			ranking.setTuplas(tuplas);

			List<TuplaRanking> tuplasCanceladasNegadas = rankingService.visualizarRankingByStatusReservaAndPeriodo(
					Arrays.asList(StatusReserva.CANCELADO, StatusReserva.CANCELADO_COM_PUNICAO, StatusReserva.NEGADO), periodo);

			model.addAttribute("tuplasCanceladasNegadas", tuplasCanceladasNegadas);
			model.addAttribute("ranking", ranking);
		}

		return Constants.PAGINA_HOMOLOGAR_RESERVAS;
	}

	@RequestMapping(value = "/periodo", method = RequestMethod.GET)
	public String listarPeriodos(Model model) {
		model.addAttribute("periodos", periodoService.find(Periodo.class));
		model.addAttribute("periodoAtual", periodoService.getPeriodoAtual());
		return Constants.PAGINA_LISTAR_PERIODOS;
	}
	
	@RequestMapping(value = "/atualizarPeriodo", method = RequestMethod.POST)
	public String atualizarPeriodo(@RequestParam("periodoId") Long periodoId, @RequestParam("encerramento") String encerramento, 
			@RequestParam("vagas") Integer vagas, RedirectAttributes redirect, Model model) {
		SimpleDateFormat format = new SimpleDateFormat(br.ufc.quixada.npi.ldap.model.Constants.FORMATO_DATA_NASCIMENTO);
		Date dataEncerramento = null;
		try {
			if (encerramento != null && !encerramento.isEmpty()) {
				dataEncerramento = format.parse(encerramento);
				if(encerramento != null) {
					Date today = format.parse(format.format(new Date()));
					if (dataEncerramento.before(today)) {
						redirect.addFlashAttribute(Constants.ERRO, Constants.MSG_DATA_FUTURA);
						return Constants.REDIRECT_PAGINA_LISTAR_PERIODOS;
					}
				}
			}
		} catch (ParseException e) {
			redirect.addFlashAttribute(Constants.ERRO, Constants.MSG_ERRO_ATUALIZAR_PERIODO);
			return Constants.REDIRECT_PAGINA_LISTAR_PERIODOS;
		}
		
		Periodo periodo = periodoService.find(Periodo.class, periodoId);
		periodo.setEncerramento(dataEncerramento);
		if (vagas != null) {
			periodo.setVagas(vagas);
		}
		
		periodoService.update(periodo);
		
		return Constants.REDIRECT_PAGINA_LISTAR_PERIODOS;
	}

	@RequestMapping(value = "/admissao", method = RequestMethod.POST)
	public String atualizaAdmissao(@RequestParam("id") Long id, @RequestParam("ano") Integer ano,
			@RequestParam("semestre") Integer semestre, Model model) {

		if (id == null || ano == null || semestre == null) {
			model.addAttribute(Constants.ERRO, "Dados inválidos");
			return Constants.PAGINA_LISTAR_PROFESSORES;
		}

		Professor professor = professorService.find(Professor.class, id);
		professor.setAnoAdmissao(ano);
		professor.setSemestreAdmissao(semestre);

		professorService.update(professor);

		Reserva reserva = new Reserva();
		reserva.setProfessor(professor);
		
		List<Professor> professors = professorService.findAtivos();
		model.addAttribute("professores", professors);
		model.addAttribute("info", "Data de admissão do(a) Prof(a) " + professor.getNome() + " atualizada com sucesso.");

		model.addAttribute("info", "Data de admissão do(a) Prof(a) " + professor.getNome() + " atualizada com sucesso.");

		try {
			notificacaoService.notificar(reserva, Notificacao.ADMISSAO_ATUALIZADA);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return Constants.PAGINA_LISTAR_PROFESSORES;
	}
	
	@RequestMapping(value = "/editar-reserva/{id}", method = RequestMethod.GET)
	public String editarReserva(@PathVariable("id") Long id, Model model, HttpSession session, RedirectAttributes redirect) {
		Reserva reserva = reservaService.find(Reserva.class, id);
		if (reserva == null || !reserva.getStatus().equals(StatusReserva.ABERTO)) {
			redirect.addFlashAttribute(Constants.ERRO, Constants.MSG_PERMISSAO_NEGADA);
			return Constants.REDIRECT_PAGINA_GERENCIAR_RESERVAS;
		}
		model.addAttribute("reserva", reserva);
		model.addAttribute("professor", reserva.getProfessor());
		model.addAttribute("programa", Programa.values());
		return Constants.PAGINA_ADMIN_EDITAR_RESERVA;
	}
	
	@RequestMapping(value = "/editar-reserva", method = RequestMethod.POST)
	public String atualizarReserva(@RequestParam("id")Long id,  @RequestParam("anoInicio") Integer anoInicio, @RequestParam("semestreInicio") Integer semestreInicio,
			@RequestParam("anoTermino") Integer anoTermino, @RequestParam("semestreTermino") Integer semestreTermino,
			@RequestParam("programa") Programa programa, @RequestParam("conceito") Integer conceito,
			@RequestParam("instituicao") String instituicao, Model model, RedirectAttributes redirect, HttpSession session) {
		
		redirect.addFlashAttribute("anoInicio", anoInicio);
		redirect.addFlashAttribute("semestreInicio", semestreInicio);
		redirect.addFlashAttribute("anoTermino", anoTermino);
		redirect.addFlashAttribute("semestreTermino", semestreTermino);
		redirect.addFlashAttribute("programaSelecionado", programa);
		redirect.addFlashAttribute("conceito", conceito);
		redirect.addFlashAttribute("instituicao", instituicao);

		if (anoInicio == null || anoTermino == null) {
			redirect.addFlashAttribute(Constants.ERRO, Constants.MSG_CAMPOS_OBRIGATORIOS);
			return Constants.REDIRECT_PAGINA_ADMIN_EDITAR_RESERVA + "/" + id;
		}
		if (anoTermino < anoInicio || (anoInicio.equals(anoTermino) && semestreTermino < semestreInicio)) {
			redirect.addFlashAttribute(Constants.ERRO, Constants.MSG_PERIODO_INVALIDO);
			return Constants.REDIRECT_PAGINA_ADMIN_EDITAR_RESERVA + "/" + id;
		}

		Periodo periodo = periodoService.getPeriodoAtual();
		Integer diferenca = calculaSemestres(periodo.getAno(), periodo.getSemestre(), anoInicio, semestreInicio);

		if (diferenca < 2) {
			redirect.addFlashAttribute(Constants.ERRO, Constants.MSG_SOLICITACAO_FORA_DO_PRAZO);
			return Constants.REDIRECT_PAGINA_ADMIN_EDITAR_RESERVA + "/" + id;
		}
		if ((programa == Programa.MESTRADO || programa == Programa.POS_DOUTORADO)
				&& calculaSemestres(anoInicio, semestreInicio, anoTermino, semestreTermino) + 1 > 4) {
			redirect.addFlashAttribute(Constants.ERRO, Constants.MSG_TEMPO_MAXIMO_MESTRADO);
			return Constants.REDIRECT_PAGINA_ADMIN_EDITAR_RESERVA + "/" + id;
		}
		if (programa == Programa.DOUTORADO && calculaSemestres(anoInicio, semestreInicio, anoTermino, semestreTermino) + 1 > 8) {
			redirect.addFlashAttribute(Constants.ERRO, Constants.MSG_TEMPO_MAXIMO_DOUTORADO);
			return Constants.REDIRECT_PAGINA_ADMIN_EDITAR_RESERVA + "/" + id;
		}
		
		Reserva reserva = reservaService.find(Reserva.class, id);
		reserva.setAnoInicio(anoInicio);
		reserva.setSemestreInicio(semestreInicio);
		reserva.setAnoTermino(anoTermino);
		reserva.setSemestreTermino(semestreTermino);
		reserva.setPrograma(programa);
		if (conceito == null) {
			conceito = 0;
		}
		reserva.setConceitoPrograma(conceito);
		reserva.setInstituicao(instituicao);
		
		reservaService.update(reserva);
		try {
			notificacaoService.notificar(reserva, Notificacao.RESERVA_ATUALIZADA);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		redirect.addFlashAttribute(Constants.INFO, Constants.MSG_RESERVA_ATUALIZADA);
		
		return Constants.REDIRECT_PAGINA_GERENCIAR_RESERVAS;
	}

	/*@RequestMapping(value = "/editar-periodo.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Model editarPeriodo(Model model, @RequestParam("id") Long id, @RequestParam("vagas") Integer vagas,
			@RequestParam("encerramento") String encerramentoString) {

		boolean permitirUpdateEncerramento = false;
		boolean permitirUpdateVagas = false;

		Periodo periodo = periodoService.find(Periodo.class, id);

		Date encerramento = null;

		if (encerramentoString != null && !encerramentoString.isEmpty()) {
			if (periodo.getStatus().equals(StatusPeriodo.ABERTO)) {

				try {
					DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
					DateTime date = dateTimeFormatter.parseDateTime(encerramentoString);
					encerramento = date.toDate();

					SimpleDateFormat format = new SimpleDateFormat(br.ufc.quixada.npi.ldap.model.Constants.FORMATO_DATA_NASCIMENTO);
					Date today;
					today = format.parse(format.format(new Date()));

					if (encerramento.before(today)) {
						model.addAttribute(Constants.ERRO, Constants.MSG_DATA_FUTURA);
						model.addAttribute("periodo", periodo);
						return model;
					}

					permitirUpdateEncerramento = true;
				} catch (ParseException e) {
					model.addAttribute(Constants.ERRO, Constants.MSG_ERRO_ATUALIZAR_PERIODO);
					return model;
				}
			}
		}

		if (vagas == null) {
			vagas = 0;
		}

		Periodo periodoSolicitacao = periodoService.getPeriodo(periodo.getAno() - 1, periodo.getSemestre());
		if (periodoSolicitacao.getStatus().equals(StatusPeriodo.ABERTO)) {
			permitirUpdateVagas = true;
		}

		if (permitirUpdateEncerramento) {
			periodo.setEncerramento(encerramento);
		}

		if (permitirUpdateVagas) {
			periodo.setVagas(vagas);
		}

		if (permitirUpdateEncerramento || permitirUpdateVagas) {
			periodoService.update(periodo);

			model.addAttribute(Constants.INFO, "Período " + periodo.getAno() + "." + periodo.getSemestre() + " atualizado com sucesso.");

		}

		model.addAttribute("periodo", periodo);
		return model;
	}*/

	@RequestMapping(value = "/reservas", method = RequestMethod.GET)
	public String getReservas(Model model) {
		List<Reserva> reservas = reservaService.getAllReservas();
		model.addAttribute("reservas", reservas);
		return Constants.PAGINA_GERENCIAR_RESERVAS;
	}

	@RequestMapping(value = "/atualizarConceito.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Model atualizarConceito(Model model, @RequestParam("id") Long id, @RequestParam("conceito") Integer conceitoPrograma) {
		Reserva reserva = reservaService.find(Reserva.class, id);

		if (conceitoPrograma != null) {
			reserva.setConceitoPrograma(conceitoPrograma);
			reservaService.update(reserva);
			model.addAttribute(Constants.INFO, "Reserva do professor " + reserva.getProfessor().getNome() + " atualizada com sucesso.");
		}
		try {
			notificacaoService.notificar(reserva, Notificacao.ATUALIZACAO_CONCEITO);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		model.addAttribute("reserva", reserva);
		return model;
	}

	@RequestMapping(value = "/atualizarStatusReserva", method = RequestMethod.POST)
	public String atualizarStatusReserva(@RequestParam("idReserva") Long id, @RequestParam("status") String status, Model model,
			RedirectAttributes redirect) {
	
		String[] valor = status.split("-");
		if (id != null & status != null && !status.isEmpty()) {
			Reserva reserva = reservaService.find(Reserva.class, id);
			StatusReserva statusReserva = StatusReserva.valueOf(valor[1]);
			if(reserva.getStatus().equals(StatusReserva.AFASTADO) && !statusReserva.equals(StatusReserva.AFASTADO)) {
				afastamentoService.delete(afastamentoService.getByReserva(reserva));
			}
			if (statusReserva.equals(StatusReserva.AFASTADO) && !reserva.getStatus().equals(StatusReserva.AFASTADO)) {
				Afastamento afastamento = new Afastamento(reserva);
				afastamentoService.save(afastamento);
			}
			reserva.setStatus(statusReserva);
			reservaService.update(reserva);
			try {
				notificacaoService.notificar(reserva, Notificacao.GERENCIAMENTO_DE_RESERVAS);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			redirect.addFlashAttribute(Constants.INFO, Constants.MSG_STATUS_RESERVA_ATUALIZADO);
		}
		return Constants.REDIRECT_PAGINA_HOMOLOGAR_RESERVAS;
	}
	
	private Integer calculaSemestres(Integer anoInicio, Integer semestreInicio, Integer anoTermino, Integer semestreTermino) {
		return ((anoTermino - anoInicio) * 2) + (semestreTermino - semestreInicio);
	}

}
