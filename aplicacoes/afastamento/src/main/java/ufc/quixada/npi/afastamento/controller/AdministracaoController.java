package ufc.quixada.npi.afastamento.controller;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.LocalDate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Ranking;
import ufc.quixada.npi.afastamento.model.Reserva;
import ufc.quixada.npi.afastamento.model.StatusPeriodo;
import ufc.quixada.npi.afastamento.model.StatusReserva;
import ufc.quixada.npi.afastamento.service.PeriodoService;
import ufc.quixada.npi.afastamento.service.ProfessorService;
import ufc.quixada.npi.afastamento.service.RankingService;
import ufc.quixada.npi.afastamento.service.ReservaService;
import ufc.quixada.npi.afastamento.util.Constants;



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
	
	@RequestMapping(value = "/professores", method = RequestMethod.GET)
	public String listarProfessores(Model model) {
		List<Professor> professors = professorService.findAll();
		model.addAttribute("professores", professors);
		return Constants.PAGINA_LISTAR_PROFESSORES;
	}
	
	@RequestMapping(value = "/reservas", method = RequestMethod.GET)
	public String getReservas(Model model) {
		Periodo periodo = periodoService.getUltimoPeriodoEncerrado();
		if(periodo != null) {
			periodo = periodoService.getPeriodoPosterior(periodo);
			if(periodo != null) {
				periodo = periodoService.getPeriodoPosterior(periodo);
				Ranking ranking = rankingService.getRanking(periodo);
				model.addAttribute("ranking", ranking);
			}
		}
		
		return Constants.PAGINA_GERENCIAR_RESERVAS;
	}
	
	@RequestMapping(value = "/atualizar-ranking", method = RequestMethod.POST)
	@CacheEvict(value = {"default", "reservasByProfessor", "periodo", "visualizarRanking", "ranking", "loadProfessor", "professores"}, allEntries = true)
	public String atualizarRanking(HttpServletRequest request, RedirectAttributes redirect) {
		String[] status = request.getParameterValues("status");
		for(String s : status) {
			String[] valor = s.split("-");
			Reserva reserva = reservaService.find(Reserva.class, Long.parseLong(valor[0]));
			StatusReserva statusReserva = StatusReserva.valueOf(valor[1]);
			reserva.setStatus(statusReserva);
			reservaService.update(reserva);
		}
		
		redirect.addFlashAttribute(Constants.INFO, Constants.MSG_RESERVAS_ATUALIZADAS);
		return Constants.REDIRECT_PAGINA_GERENCIAR_RESERVAS;
	}

	@RequestMapping(value = "/periodos.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Periodo> periodos() {
		return periodoService.find(Periodo.class);
	}
	
	@RequestMapping(value = "/periodo", method = RequestMethod.GET)
	public String listarPeriodos(Model model) {
		model.addAttribute("periodo", new Periodo());
		return Constants.PAGINA_LISTAR_PERIODOS;
	}

	@RequestMapping(value = "/periodo", method = RequestMethod.POST)
	public String listarPeriodos(Model model, @RequestParam("ano") Integer ano, @RequestParam("semestre") Integer semestre) {
		Periodo periodo = periodoService.getPeriodo(ano, semestre);
		
		if(!notNull(periodo)){
			model.addAttribute("message", "Período " + ano + "." + semestre + " não está cadastrado.");
			return Constants.PAGINA_LISTAR_PERIODOS;
		} else if (periodo.getStatus().equals(StatusPeriodo.ENCERRADO)) {
			model.addAttribute("permitirUpdate", false);
			model.addAttribute("periodo", periodo);
			return Constants.PAGINA_LISTAR_PERIODOS;
		}

		boolean permitirUpdate = false;
		boolean permitirUpdateEncerramento = false;
		boolean permitirUpdateVagas = false;
		
		Periodo periodoSolicitacao = periodoService.getPeriodo(periodo.getAno()-1, periodo.getSemestre());	

		if(periodo.getStatus().equals(StatusPeriodo.ABERTO)){
			permitirUpdateEncerramento = true;
		}

		if(periodoSolicitacao.getStatus().equals(StatusPeriodo.ABERTO)){
			permitirUpdateVagas = true;
		}
				
		if(permitirUpdateVagas || permitirUpdateEncerramento){
			permitirUpdate = true;
		}
		
		model.addAttribute("permitirUpdateVagas", 		 permitirUpdateVagas);
		model.addAttribute("permitirUpdateEncerramento", permitirUpdateEncerramento);
		model.addAttribute("permitirUpdate", 			 permitirUpdate);
		model.addAttribute("periodo", periodo);
		return Constants.PAGINA_LISTAR_PERIODOS;
	}
	
	private boolean notNull(Object object){
		return object != null ?  true : false;
	}
	
	//Verifica utilidade?
	private boolean isEnceramentoValido(Date date) {
		LocalDate now = new LocalDate();
		LocalDate enceramento = notNull(date) ? new LocalDate(date): null;

		return (enceramento.isAfter(now) || enceramento.isEqual(now)) ? true : false;
	}

	@RequestMapping(value = "/update-periodo", method = RequestMethod.POST)
	@CacheEvict(value = {"default", "reservasByProfessor", "periodo", "visualizarRanking", "ranking", "loadProfessor", "professores"}, allEntries = true)
	public String listarPeriodos(Model model, RedirectAttributes redirectAttributes, @Valid @ModelAttribute("periodo") Periodo periodoEmAtualizacao, BindingResult result) {

		boolean permitirUpdateEncerramento = false;
		boolean permitirUpdateVagas = false;
		boolean permitirUpdate = false;

		Periodo periodoEmAtualizacaoDoBanco = periodoService.find(Periodo.class, periodoEmAtualizacao.getId());
		Periodo periodoSolicitacao = periodoService.getPeriodo(periodoEmAtualizacao.getAno()-1, periodoEmAtualizacao.getSemestre());	

		if(periodoEmAtualizacaoDoBanco.getStatus().equals(StatusPeriodo.ABERTO)){
			permitirUpdateEncerramento = true;
		}

		if(periodoSolicitacao.getStatus().equals(StatusPeriodo.ABERTO)){
			permitirUpdateVagas = true;
		}
				
		if(permitirUpdateVagas || permitirUpdateEncerramento){
			permitirUpdate = true;
		}
		
		model.addAttribute("permitirUpdate", permitirUpdate);
		model.addAttribute("permitirUpdateVagas", permitirUpdateVagas);
		model.addAttribute("permitirUpdateEncerramento", permitirUpdateEncerramento);

		if(!isEnceramentoValido(periodoEmAtualizacao.getEncerramento())){
			periodoEmAtualizacaoDoBanco.setEncerramento(periodoEmAtualizacao.getEncerramento());
			model.addAttribute("periodo", periodoEmAtualizacaoDoBanco);
			model.addAttribute("errorData", Constants.MSG_DATA_FUTURA);
			return Constants.PAGINA_LISTAR_PERIODOS;
		}

		if (result.hasErrors()) {
			return Constants.PAGINA_LISTAR_PERIODOS;
		}

		if(permitirUpdateEncerramento){
			periodoEmAtualizacaoDoBanco.setEncerramento(periodoEmAtualizacao.getEncerramento());
		}

		if(permitirUpdateVagas){
			periodoEmAtualizacaoDoBanco.setVagas(periodoEmAtualizacao.getVagas());
		}
			
		if(permitirUpdateEncerramento || permitirUpdateVagas){
			periodoService.update(periodoEmAtualizacaoDoBanco);
			model.addAttribute("periodo", periodoEmAtualizacaoDoBanco);
			model.addAttribute(Constants.INFO,"Periodo " +periodoEmAtualizacaoDoBanco.getAno() + "." + periodoEmAtualizacaoDoBanco.getSemestre() + " atualizado com sucesso!");
		}
		
		return Constants.PAGINA_LISTAR_PERIODOS;
	}
	
	@RequestMapping(value = "/admissao", method = RequestMethod.POST)
	@CacheEvict(value = {"default", "reservasByProfessor", "periodo", "visualizarRanking", "ranking", "loadProfessor", "professores"}, allEntries = true)
	public String listarPeriodosq(@RequestParam("id") Long id, @RequestParam("ano") Integer ano, @RequestParam("semestre") Integer semestre, Model model) {

		if (id == null || ano == null || semestre == null) {
			model.addAttribute(Constants.ERRO, "Dados inválidos");
			return Constants.PAGINA_LISTAR_PROFESSORES;
		}
		
		Professor professor = professorService.find(Professor.class, id);
		professor.setAnoAdmissao(ano);
		professor.setSemestreAdmissao(semestre);

		professorService.update(professor);

		return Constants.PAGINA_LISTAR_PROFESSORES;
	}
	

}
