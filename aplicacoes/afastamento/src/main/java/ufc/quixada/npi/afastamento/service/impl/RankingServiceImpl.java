package ufc.quixada.npi.afastamento.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import ufc.quixada.npi.afastamento.model.Afastamento;
import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Ranking;
import ufc.quixada.npi.afastamento.model.Reserva;
import ufc.quixada.npi.afastamento.service.AfastamentoService;
import ufc.quixada.npi.afastamento.service.RankingService;
import ufc.quixada.npi.afastamento.service.ReservaService;

@Named
public class RankingServiceImpl implements RankingService {
	
	@Inject
	private ReservaService reservaService;
	
	@Inject
	private AfastamentoService afastamentoService;

	@Override
	public List<Ranking> visualizarRanking(Integer ano, Integer semestre) {
		//R = (T – A) / (5 x A + S + P)
		//T – Número de semestres desde a contratação na UFC até o início do afastamento, iniciando no primeiro semestre em que o solicitante teve disciplina alocada no Campus Quixadá;
		//A – Número de semestres em que o docente já esteve afastado para programas de pós-graduação stricto sensu ou pós-doutorados;
		//S – Número de semestres do afastamento reservado/solicitado;
		//P – Número de semestres que faltam para o docente completar três (3) anos de contratação na UFC Quixadá (vale zero se já cumpriu este período).
		
		//No caso de primeiro afastamento, a variável S terá valor dois (2) independente da duração do período reservado/solicitado
		//Em caso de empate na ordem de prioridade estabelecida no Artigo 5o, serão considerados os critérios abaixo, na ordem indicada:
		//I – Mestrado tem maior prioridade que doutorado, e doutorado tem maior prioridade que pós-doutorado.
		//II – Prioridade para programas com melhor conceito.
		//III – Prioridade para o candidato mais velho.
		List<Reserva> reservas = reservaService.getReservasByPeriodo(ano, semestre);
		List<Ranking> ranking = new ArrayList<Ranking>();
		for (Reserva reserva : reservas) {
			Ranking r = new Ranking();
			r.setProfessor(reserva.getProfessor());
			r.setSemestresSolicitados(getSemestresSolicitados(reserva));
			r.setSemestresAtivos(getSemestresAtivos(reserva.getProfessor()));
			r.setSemestresAfastados(getSemestresAfastados(reserva.getProfessor()));
			Float semAtivos = Float.valueOf(r.getSemestresAtivos()); //T
			Float semAfastados = Float.valueOf(r.getSemestresAfastados()); //A
			Float semSolicitados = Float.valueOf(r.getSemestresSolicitados()); //S
			Integer semContratacao = calculaSemestres(reserva.getProfessor().getAnoAdmissao(), reserva.getProfessor().getSemestreAdmissao(), 
					reservaService.getAnoAtual(), reservaService.getSemestreAtual()) - 1;
			Float semRestantes = semContratacao > 6.0f ? 0.0f : (6.0f - semContratacao); //P
			
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
	
	
	
	private Integer getSemestresSolicitados(Reserva reserva) {
		if(afastamentoService.getAfastamentosByProfessor(reserva.getProfessor().getSiape()).isEmpty()) {
			return 2;
		}
		return calculaSemestres(reserva.getAnoInicio(), reserva.getSemestreInicio(), reserva.getAnoTermino(), reserva.getSemestreTermino());
	}
	
	private Integer getSemestresAtivos(Professor professor) {
		return ((reservaService.getAnoAtual() - professor.getAnoAdmissao()) * 2) + (reservaService.getSemestreAtual() - professor.getSemestreAdmissao());
	}
	
	private Integer getSemestresAfastados(Professor professor) {
		List<Afastamento> afastamentos = afastamentoService.getAfastamentosByProfessor(professor.getSiape());
		Integer semestresAfastado = 0;
		for(Afastamento afastamento : afastamentos) {
			semestresAfastado =+ calculaSemestres(afastamento.getReserva().getAnoInicio(), afastamento.getReserva().getSemestreInicio(), 
					afastamento.getReserva().getAnoTermino(), afastamento.getReserva().getSemestreTermino());
		}
		return semestresAfastado;
	}
	
	private Integer calculaSemestres(Integer anoInicio, Integer semestreInicio, Integer anoTermino, Integer semestreTermino) {
		return ((anoTermino - anoInicio) * 2) + (semestreTermino - semestreInicio) + 1;
	}

	

}
