package ufc.quixada.npi.afastamento.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.service.PeriodoService;
import ufc.quixada.npi.afastamento.service.RelatorioService;

@Controller
@RequestMapping("administracao/relatorio")
public class RelatorioController {

	@Inject
	private RelatorioService relatorioService;

	@Inject
	private PeriodoService periodoService;

	@RequestMapping(value = "/reservas-by-periodo.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Model getRelatorioReservas(HttpServletRequest request,
			Model model) {
		Periodo inicio = periodoService.getPeriodo(
				Integer.valueOf(request.getParameter("anoInicio")),
				Integer.valueOf(request.getParameter("semestreInicio")));
		Periodo termino = periodoService.getPeriodo(
				Integer.valueOf(request.getParameter("anoTermino")),
				Integer.valueOf(request.getParameter("semestreTermino")));
		model.addAttribute("relatorio",
				relatorioService.getRelatorioReservasByPeriodo(inicio, termino));
		return model;
	}
}
