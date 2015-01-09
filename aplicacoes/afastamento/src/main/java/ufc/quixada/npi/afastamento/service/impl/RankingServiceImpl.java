package ufc.quixada.npi.afastamento.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Ranking;
import ufc.quixada.npi.afastamento.model.Reserva;
import ufc.quixada.npi.afastamento.service.RankingService;
import ufc.quixada.npi.afastamento.service.ReservaService;

@Named
public class RankingServiceImpl implements RankingService {
	
	@Inject
	private ReservaService reservaService;

	@Override
	public List<Ranking> gerarRanking(Integer ano, Integer semestre) {
		List<Reserva> reservas = reservaService.getReservasByPeriodo(ano, semestre);
		List<Ranking> ranking = new ArrayList<Ranking>();
		for (Reserva reserva : reservas) {
			Ranking r = new Ranking();
			r.setProfessor(reserva.getProfessor());
			r.setSemestresSolicitados(calculaSemestresSolicitados(reserva));
			r.setSemestresAtivos(calculaSemestresAtivos(reserva.getProfessor()));
			r.setSemestresAfastados(calculaSemestresAfastados(reserva.getProfessor()));
			Float semAtivos = Float.valueOf(r.getSemestresAtivos());
			Float semAfastados = Float.valueOf(r.getSemestresAfastados());
			Float semSolicitados = Float.valueOf(r.getSemestresSolicitados());
			Float semRestantes = semAtivos > 6.0f ? 0.0f : (6.0f - semAtivos);
			
			Float pontuacao = (semAtivos - semAfastados) / (5.0f * semAfastados + semSolicitados + semRestantes);
			r.setPontuacao(pontuacao);
			
			ranking.add(r);
		}
		Collections.sort(ranking, new Comparator<Ranking>() {
	        @Override
	        public int compare(Ranking  ranking1, Ranking  ranking2)
	        {

	            return  ranking1.getPontuacao().compareTo(ranking2.getPontuacao());
	        }
	    });
		Collections.reverse(ranking);
		return ranking;
		
	}
	
	private Integer calculaSemestresSolicitados(Reserva reserva) {
		return 2;
		//return ((reserva.getAnoTermino() - reserva.getAnoInicio()) * 2) + (reserva.getSemestreTermino() - reserva.getSemestreInicio()) + 1;
	}
	
	private Integer calculaSemestresAtivos(Professor professor) {
		return ((reservaService.getAnoAtual() - professor.getAnoAdmissao()) * 2) + (reservaService.getSemestreAtual() - professor.getSemestreAdmissao());
	}
	
	private Integer calculaSemestresAfastados(Professor professor) {
		return 0;
	}

	

}
