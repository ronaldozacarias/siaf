package ufc.quixada.npi.afastamento.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import ufc.quixada.npi.afastamento.model.Afastamento;
import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Ranking;
import ufc.quixada.npi.afastamento.model.Reserva;
import ufc.quixada.npi.afastamento.model.StatusReserva;
import ufc.quixada.npi.afastamento.model.TuplaRanking;
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
	public List<TuplaRanking> visualizarRanking(Integer ano, Integer semestre) {
		Periodo periodo = afastamentoService.getPeriodoByAnoSemestre(ano, semestre);
		List<TuplaRanking> tuplaAtual = getRanking(periodo).getTuplas();
		for(int i = 0; i < tuplaAtual.size(); i++) {
			Periodo periodoInicio = afastamentoService.getPeriodoByAnoSemestre(tuplaAtual.get(i).getReserva().getAnoInicio(), tuplaAtual.get(i).getReserva().getSemestreInicio());
			Periodo periodoTermino = afastamentoService.getPeriodoByAnoSemestre(tuplaAtual.get(i).getReserva().getAnoTermino(), tuplaAtual.get(i).getReserva().getSemestreTermino());
			for(;periodoInicio != null && !periodoInicio.equals(periodoTermino); periodoInicio = afastamentoService.getPeriodoPosterior(periodoInicio.getAno(), periodoInicio.getSemestre())) {
				List<TuplaRanking> tuplaPeriodo = getClassificados(getRanking(periodoInicio).getTuplas());
				boolean encontrou = false;
				for(TuplaRanking tupla : tuplaPeriodo) {
					if(tupla.getReserva().equals(tuplaAtual.get(i).getReserva())) {
						encontrou = true;
						break;
					}
				}
				if(!encontrou) {
					tuplaAtual.get(i).setStatus(StatusReserva.DESCLASSIFICADO);
				}
				
			}
		}
		return tuplaAtual;
		
	}
	
	private List<TuplaRanking> getClassificados(List<TuplaRanking> tuplaRanking) {
		List<TuplaRanking> result = new ArrayList<TuplaRanking>();
		for(TuplaRanking tupla : tuplaRanking) {
			if(!tupla.getStatus().equals(StatusReserva.DESCLASSIFICADO) && !tupla.getStatus().equals(StatusReserva.NAO_ACEITO)) {
				result.add(tupla);
			}
		}
		return result;
	}
	
	private Ranking getRanking(Periodo periodo) {
		Ranking ranking = new Ranking();
		ranking.setPeriodo(periodo);
		
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
		List<Reserva> reservas = reservaService.getReservasByPeriodo(periodo.getAno(), periodo.getSemestre());
		List<TuplaRanking> tuplas = new ArrayList<TuplaRanking>();
		for (Reserva reserva : reservas) {
			TuplaRanking tupla = new TuplaRanking();
			tupla.setReserva(reserva);
			tupla.setPeriodo(periodo);
			tupla.setProfessor(reserva.getProfessor().getNome());
			tupla.setSemestresSolicitados(getSemestresSolicitados(reserva));
			tupla.setSemestresAtivos(calculaSemestres(reserva.getProfessor().getAnoAdmissao(), reserva.getProfessor().getSemestreAdmissao(), 
					reserva.getAnoInicio(), reserva.getSemestreInicio()) - 1);
			tupla.setSemestresAfastados(getSemestresAfastados(reserva));
			tupla.setStatus(reserva.getStatus());
			Float t = Float.valueOf(tupla.getSemestresAtivos());
			Float a = Float.valueOf(tupla.getSemestresAfastados());
			Float s = afastamentoService.getAfastamentosByProfessor(reserva.getProfessor().getSiape()).isEmpty() ? 2 : Float.valueOf(tupla.getSemestresSolicitados());
			Integer semContratacao = calculaSemestres(reserva.getProfessor().getAnoAdmissao(), reserva.getProfessor().getSemestreAdmissao(), 
					reserva.getAnoInicio(), reserva.getSemestreInicio()) - 1;
			Float p = semContratacao >= 6.0f ? 0.0f : (6.0f - semContratacao);
			
			Float pontuacao = (t - a) / (5.0f * a + s + p);
			tupla.setPontuacao(pontuacao);
			
			tuplas.add(tupla);
		}
		Collections.sort(tuplas, new Comparator<TuplaRanking>() {
	        @Override
	        public int compare(TuplaRanking  ranking1, TuplaRanking  ranking2)
	        {
	            return  ranking1.getPontuacao().compareTo(ranking2.getPontuacao());
	        }
	    });
		Collections.reverse(tuplas);
		int vagas = periodo.getVagas();
		for (TuplaRanking tupla : tuplas) {
			if(tupla.getStatus().equals(StatusReserva.ACEITO) || tupla.getStatus().equals(StatusReserva.ENCERRADO)) {
				vagas--;
			}
		}
		for (TuplaRanking tupla : tuplas) {
			if(tupla.getStatus().equals(StatusReserva.ABERTO)) {
				if(vagas > 0) {
					tupla.setStatus(StatusReserva.CLASSIFICADO);
					vagas--;
				} else {
					tupla.setStatus(StatusReserva.DESCLASSIFICADO);
				}
			}
		}
		
		ranking.setTuplas(tuplas);
		return ranking;
		
	}
	
	private Integer getSemestresSolicitados(Reserva reserva) {
		return calculaSemestres(reserva.getAnoInicio(), reserva.getSemestreInicio(), reserva.getAnoTermino(), reserva.getSemestreTermino());
	}
	
	private Integer getSemestresAfastados(Reserva reserva) {
		List<Afastamento> afastamentos = afastamentoService.getAfastamentosAnteriores(reserva);
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
