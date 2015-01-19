package ufc.quixada.npi.afastamento.controller;

import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Programa;
import ufc.quixada.npi.afastamento.model.Ranking;
import ufc.quixada.npi.afastamento.model.Reserva;
import ufc.quixada.npi.afastamento.model.StatusReserva;
import ufc.quixada.npi.afastamento.service.AfastamentoService;
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
	private AfastamentoService afastamentoService;
	
	@RequestMapping(value = "/ranking", method = RequestMethod.GET)
	public String getRanking(Model model, HttpSession session) {
		//model.addAttribute("tuplas", rankingService.visualizarRanking(afastamentoService.getAnoAtual(), afastamentoService.getSemestreAtual()));
		model.addAttribute("periodoAtual", 
				afastamentoService.getPeriodoByAnoSemestre(afastamentoService.getAnoAtual(), afastamentoService.getSemestreAtual()));
		model.addAttribute("periodoAnterior", 
				afastamentoService.getPeriodoAnterior(afastamentoService.getAnoAtual(), afastamentoService.getSemestreAtual()));
		model.addAttribute("periodoPosterior", 
				afastamentoService.getPeriodoPosterior(afastamentoService.getAnoAtual(), afastamentoService.getSemestreAtual()));
		
		return "reserva/ranking";
	}
	
	@RequestMapping(value = "/ranking.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Model ranking(HttpServletRequest request, Model model, HttpSession session) {
		Ranking ranking = new Ranking();
		ranking.setPeriodo(afastamentoService.getPeriodoByAnoSemestre(
				Integer.valueOf(request.getParameter("ano")), Integer.valueOf(request.getParameter("semestre"))));
		ranking.setTuplas(rankingService.visualizarRanking(ranking.getPeriodo().getAno(), ranking.getPeriodo().getSemestre()));
		model.addAttribute("ranking", ranking);
		model.addAttribute("periodoAtual", ranking.getPeriodo());
		model.addAttribute("periodoAnterior", 
				afastamentoService.getPeriodoAnterior(ranking.getPeriodo().getAno(), ranking.getPeriodo().getSemestre()));
		model.addAttribute("periodoPosterior", 
				afastamentoService.getPeriodoPosterior(ranking.getPeriodo().getAno(), ranking.getPeriodo().getSemestre()));
		
		return model;
	}
	
	@RequestMapping(value = "/incluir", method = RequestMethod.GET)
	public String incluirForm(Model model, HttpSession session) {
		model.addAttribute("reserva", new Reserva());
		model.addAttribute("professor", getUsuarioLogado(session));
		model.addAttribute("programa", Programa.values());
		return "reserva/inclusao";
	}
	
	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	public String incluir(@RequestParam("ano-inicio") Integer anoInicio, @RequestParam("semestre-inicio") Integer semestreInicio,
			@RequestParam("ano-termino") Integer anoTermino, @RequestParam("semestre-termino") Integer semestreTermino,
			@RequestParam("programa") Programa programa, Model model, RedirectAttributes redirect, HttpSession session) {
		
		Integer diferenca = calculaDiferenca(afastamentoService.getAnoAtual(), afastamentoService.getSemestreAtual(), anoInicio, semestreInicio);
		if(diferenca <= 2) {
			if(afastamentoService.isPeriodoEncerrado(afastamentoService.getAnoAtual(), afastamentoService.getSemestreAtual())) {
				diferenca--;
			}
		}
		if(diferenca < 2) {
			// Solicitação fora do prazo
			return "redirect://reserva/incluir";
		}
		if((programa == Programa.MESTRADO || programa == Programa.POS_DOUTORADO) && calculaDiferenca(anoInicio, semestreInicio, anoTermino, semestreTermino) + 1 > 4) {
			// Excedido tempo para mestrado ou pós doutorado
			return "redirect://reserva/incluir";
		}
		if(programa == Programa.DOUTORADO && calculaDiferenca(anoInicio, semestreInicio, anoTermino, semestreTermino) + 1 > 8) {
			// Excedido tempo para doutorado
			return "redirect://reserva/incluir";
		}
		
		if(reservaService.hasReservaEmAberto(getUsuarioLogado(session))) {
			// Já existe reserva em aberto para esse professor
			return "redirect://reserva/incluir";
		}
		
		Reserva reserva = new Reserva();
		reserva.setAnoInicio(anoInicio);
		reserva.setSemestreInicio(semestreInicio);
		reserva.setAnoTermino(anoTermino);
		reserva.setSemestreTermino(semestreTermino);
		reserva.setDataSolicitacao(new Date());
		reserva.setPrograma(programa);
		reserva.setProfessor(getUsuarioLogado(session));
		reserva.setStatus(StatusReserva.ABERTO);
		
		reservaService.salvar(reserva);
		
		return "reserva/ranking";
	}
	
	@RequestMapping(value = "/reservas", method = RequestMethod.GET)
	public String getReservas(Model model, HttpSession session) {
		model.addAttribute("reservas", reservaService.getReservasByProfessor(getUsuarioLogado(session).getSiape()));
		model.addAttribute("professor", getUsuarioLogado(session));
		return "reserva/reservas";
	}
	
	private Professor getUsuarioLogado(HttpSession session) {
		if (session.getAttribute(Constants.USUARIO_LOGADO) == null) {
			Professor professor = usuarioService
					.getUsuarioByLogin(SecurityContextHolder.getContext()
							.getAuthentication().getName());
			session.setAttribute(Constants.USUARIO_LOGADO, professor);
		}
		return (Professor) session.getAttribute(Constants.USUARIO_LOGADO);
	}
	
	private Integer calculaDiferenca(Integer anoInicio, Integer semestreInicio, Integer anoTermino, Integer semestreTermino) {
		return ((anoTermino - anoInicio) * 2) + (semestreTermino - semestreInicio);
	}
	
}
