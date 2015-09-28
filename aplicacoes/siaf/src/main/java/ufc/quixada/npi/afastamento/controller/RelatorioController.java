package ufc.quixada.npi.afastamento.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Ranking;
import ufc.quixada.npi.afastamento.service.PeriodoService;
import ufc.quixada.npi.afastamento.service.RankingService;

@Controller
@RequestMapping("administracao/relatorio")
public class RelatorioController {

	@Inject
	private PeriodoService periodoService;
	
	@Inject
	private RankingService rankingSevice;
	
	@RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
	public String getRelatorios(Model model) {
		return "admin/relatorios";
	}

	@RequestMapping(value = "/ranking", method = RequestMethod.POST)
	public String getRelatorioReservas(@RequestParam("ano") Integer ano, @RequestParam("semestre") Integer semestre) {
		Periodo periodo = periodoService.getPeriodo(ano, semestre);
		Ranking ranking = new Ranking();
		ranking.setPeriodo(periodo);
		ranking.setTuplas(rankingSevice.visualizarRanking(periodo, false));
		return "admin/relatorios";
	}
}
