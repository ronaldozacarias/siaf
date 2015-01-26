package ufc.quixada.npi.afastamento.controller;

import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Programa;
import ufc.quixada.npi.afastamento.model.Ranking;
import ufc.quixada.npi.afastamento.model.Reserva;
import ufc.quixada.npi.afastamento.model.StatusReserva;
import ufc.quixada.npi.afastamento.model.Usuario;
import ufc.quixada.npi.afastamento.service.PeriodoService;
import ufc.quixada.npi.afastamento.service.RankingService;
import ufc.quixada.npi.afastamento.service.ReservaService;
import ufc.quixada.npi.afastamento.service.UsuarioService;
import ufc.quixada.npi.afastamento.util.Constants;

@Controller
@RequestMapping("reserva")
public class ReservaController {
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private ReservaService reservaService;
	
	@Inject
	private RankingService rankingService;
	
	@Inject
	private PeriodoService periodoService;
	
	@RequestMapping(value = "/ranking", method = RequestMethod.GET)
	public String getRanking(Model model, HttpSession session) {
		Periodo periodoAtual = periodoService.getPeriodoAtual();
		model.addAttribute("periodoAtual", periodoAtual);
		model.addAttribute("periodoAnterior", periodoService.getPeriodoAnterior(periodoAtual));
		model.addAttribute("periodoPosterior", periodoService.getPeriodoPosterior(periodoAtual));
		
		return "reserva/ranking";
	}
	
	@RequestMapping(value = "/ranking.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Model ranking(HttpServletRequest request, Model model, HttpSession session) {
		Ranking ranking = new Ranking();
		ranking.setPeriodo(periodoService.getPeriodo(
				Integer.valueOf(request.getParameter("ano")), Integer.valueOf(request.getParameter("semestre"))));
		ranking.setTuplas(rankingService.visualizarRanking(ranking.getPeriodo().getAno(), ranking.getPeriodo().getSemestre()));
		model.addAttribute("ranking", ranking);
		model.addAttribute("periodoAtual", ranking.getPeriodo());
		model.addAttribute("periodoAnterior", periodoService.getPeriodoAnterior(ranking.getPeriodo()));
		model.addAttribute("periodoPosterior", periodoService.getPeriodoPosterior(ranking.getPeriodo()));
		
		return model;
	}
	
	@RequestMapping(value = "/incluir", method = RequestMethod.GET)
	public String incluirForm(Model model, HttpSession session) {
		model.addAttribute("reserva", new Reserva());
		model.addAttribute("professor", getProfessorLogado(session));
		model.addAttribute("programa", Programa.values());
		return "reserva/inclusao";
	}
	
	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	public String incluir(@RequestParam("anoInicio") Integer anoInicio, @RequestParam("semestreInicio") Integer semestreInicio,
			@RequestParam("anoTermino") Integer anoTermino, @RequestParam("semestreTermino") Integer semestreTermino,
			@RequestParam("programa") Programa programa, @RequestParam("conceito") Integer conceito, @RequestParam("instituicao") String instituicao,
			Model model, RedirectAttributes redirect, HttpSession session) {
		
		redirect.addFlashAttribute("anoInicio", anoInicio);
		redirect.addFlashAttribute("semestreInicio", semestreInicio);
		redirect.addFlashAttribute("anoTermino", anoTermino);
		redirect.addFlashAttribute("semestreTermino", semestreTermino);
		redirect.addFlashAttribute("programaSelecionado", programa);
		redirect.addFlashAttribute("conceito", conceito);
		redirect.addFlashAttribute("instituicao", instituicao);
		
		if(anoInicio == null || anoTermino == null || conceito == null || instituicao == null || instituicao.isEmpty()) {
			redirect.addFlashAttribute("erro", "É necessário preencher todas as informações.");
			return "redirect:/reserva/incluir";
		}
		
		Periodo periodo = periodoService.getPeriodoAtual();
		Integer diferenca = calculaDiferenca(periodo.getAno(), periodo.getSemestre(), anoInicio, semestreInicio);
		
		if(diferenca < 2) {
			redirect.addFlashAttribute("erro", "Sua solicitação está fora do prazo permitido.");
			return "redirect:/reserva/incluir";
		}
		if((programa == Programa.MESTRADO || programa == Programa.POS_DOUTORADO) && calculaDiferenca(anoInicio, semestreInicio, anoTermino, semestreTermino) + 1 > 4) {
			redirect.addFlashAttribute("erro", "O tempo máximo para mestrado ou pós-doutorado é de 4 semestres.");
			return "redirect:/reserva/incluir";
		}
		if(programa == Programa.DOUTORADO && calculaDiferenca(anoInicio, semestreInicio, anoTermino, semestreTermino) + 1 > 8) {
			redirect.addFlashAttribute("erro", "O tempo máximo para doutorado é de 8 semestres.");
			return "redirect:/reserva/incluir";
		}
		
		if(reservaService.hasReservaEmAberto(getProfessorLogado(session))) {
			redirect.addFlashAttribute("erro", "Já há uma solicitação de reserva em aberto.");
			return "redirect:/reserva/incluir";
		}
		
		Reserva reserva = new Reserva();
		reserva.setAnoInicio(anoInicio);
		reserva.setSemestreInicio(semestreInicio);
		reserva.setAnoTermino(anoTermino);
		reserva.setSemestreTermino(semestreTermino);
		reserva.setDataSolicitacao(new Date());
		reserva.setPrograma(programa);
		reserva.setProfessor(getProfessorLogado(session));
		reserva.setInstituicao(instituicao);
		reserva.setStatus(StatusReserva.ABERTO);
		
		reservaService.salvar(reserva);
		
		return "redirect:/reserva/ranking";
	}
	
	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String getReservas(Model model, HttpSession session) {
		model.addAttribute("reservas", reservaService.getReservasByProfessor(getProfessorLogado(session).getSiape()));
		model.addAttribute("professor", getProfessorLogado(session));
		return "reserva/lista";
	}
	
	@RequestMapping(value = "/{id}/excluir", method = RequestMethod.GET)
	public String excluir(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirect) {
		Reserva reserva = reservaService.getReservaById(id);
		if(reserva == null || !reserva.getProfessor().equals(getProfessorLogado(session)) || !reserva.getStatus().equals(StatusReserva.ABERTO)) {
			redirect.addFlashAttribute("erro", "Você não tem permissão para excluir essa reserva");
		} else {
			reservaService.delete(reserva);
			redirect.addFlashAttribute("info", "Reserva excluída com sucesso");
		}
		return "redirect:/reserva/listar";
	}
	
	private Usuario getUsuarioLogado(HttpSession session) {
		if (session.getAttribute(Constants.USUARIO_LOGADO) == null) {
			Usuario usuario = usuarioService
					.getUsuarioByLogin(SecurityContextHolder.getContext()
							.getAuthentication().getName());
			session.setAttribute(Constants.USUARIO_LOGADO, usuario);
		}
		return (Usuario) session.getAttribute(Constants.USUARIO_LOGADO);
	}
	
	private Professor getProfessorLogado(HttpSession session) {
		Professor professor = null;
		if (session.getAttribute(Constants.PROFESSOR_LOGADO) == null) {
			Usuario usuario = getUsuarioLogado(session);
			professor = usuarioService.getProfessorByUsuario(usuario);
			session.setAttribute(Constants.PROFESSOR_LOGADO, professor);
		} else {
			professor = (Professor) session.getAttribute(Constants.PROFESSOR_LOGADO);
		}
		return professor;
	}
	
	private Integer calculaDiferenca(Integer anoInicio, Integer semestreInicio, Integer anoTermino, Integer semestreTermino) {
		return ((anoTermino - anoInicio) * 2) + (semestreTermino - semestreInicio);
	}
	
}
