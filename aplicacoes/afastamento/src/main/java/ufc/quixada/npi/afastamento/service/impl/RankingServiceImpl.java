package ufc.quixada.npi.afastamento.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.cache.annotation.Cacheable;

import ufc.quixada.npi.afastamento.model.Afastamento;
import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Programa;
import ufc.quixada.npi.afastamento.model.Ranking;
import ufc.quixada.npi.afastamento.model.Reserva;
import ufc.quixada.npi.afastamento.model.StatusReserva;
import ufc.quixada.npi.afastamento.model.StatusTupla;
import ufc.quixada.npi.afastamento.model.TuplaRanking;
import ufc.quixada.npi.afastamento.service.AfastamentoService;
import ufc.quixada.npi.afastamento.service.PeriodoService;
import ufc.quixada.npi.afastamento.service.RankingService;
import ufc.quixada.npi.afastamento.service.ReservaService;

@Named
public class RankingServiceImpl implements RankingService {
	
	@Inject
	private ReservaService reservaService;
	
	@Inject
	private AfastamentoService afastamentoService;
	
	@Inject
	private PeriodoService periodoService;

	@Override
	@Cacheable("visualizarRanking")
	public List<TuplaRanking> visualizarRanking(Integer ano, Integer semestre) {
		Periodo periodo = periodoService.getPeriodo(ano, semestre);
		List<TuplaRanking> rankingAtual = getRanking(periodo).getTuplas();
		// Para cada período do tempo da reserva de cada solicitação, é verificado se a reserva está dentro das vagas para todos os períodos.
		for(int i = 0; i < rankingAtual.size(); i++) {
			if(!rankingAtual.get(i).getStatus().equals(StatusTupla.AFASTADO)) {
				Periodo periodoInicio = periodoService.getPeriodo(rankingAtual.get(i).getReserva().getAnoInicio(), rankingAtual.get(i).getReserva().getSemestreInicio());
				Periodo periodoTermino = periodoService.getPeriodo(rankingAtual.get(i).getReserva().getAnoTermino(), rankingAtual.get(i).getReserva().getSemestreTermino());
				for(;periodoInicio != null && !periodoInicio.equals(periodoService.getPeriodoPosterior(periodoTermino)); periodoInicio = periodoService.getPeriodoPosterior(periodoInicio)) {
					//List<TuplaRanking> tuplaPeriodo = getClassificados(getRanking(periodoInicio).getTuplas());
					List<TuplaRanking> tuplaPeriodo = getClassificados(getRanking(periodoInicio).getTuplas());
					boolean encontrou = false;
					for(TuplaRanking tupla : tuplaPeriodo) {
						if(tupla.getReserva().equals(rankingAtual.get(i).getReserva())) {
							encontrou = true;
							break;
						}
					}
					if(!encontrou) {
						
						if(rankingAtual.get(i).getReserva().getStatus().equals(StatusReserva.CANCELADO)) {
							rankingAtual.get(i).setStatus(StatusTupla.CANCELADO);
						} else if(rankingAtual.get(i).getReserva().getStatus().equals(StatusReserva.CANCELADO_COM_PUNICAO)) {
							rankingAtual.get(i).setStatus(StatusTupla.CANCELADO_COM_PUNICAO);
						} else if(rankingAtual.get(i).getReserva().getStatus().equals(StatusReserva.NAO_ACEITO)) {
							rankingAtual.get(i).setStatus(StatusTupla.NAO_ACEITO);
						} else if(rankingAtual.get(i).getReserva().getStatus().equals(StatusReserva.NEGADO)) {
							rankingAtual.get(i).setStatus(StatusTupla.NEGADO);
						} else {
							rankingAtual.get(i).setStatus(StatusTupla.DESCLASSIFICADO);
						}
					}
					
				}
			}
		}
		return rankingAtual;
		
	}
	
	private List<TuplaRanking> getClassificados(List<TuplaRanking> tuplaRanking) {
		List<TuplaRanking> result = new ArrayList<TuplaRanking>();
		for(TuplaRanking tupla : tuplaRanking) {
			if(!tupla.getStatus().equals(StatusTupla.DESCLASSIFICADO) && !tupla.getStatus().equals(StatusTupla.NEGADO) &&
					!tupla.getStatus().equals(StatusTupla.CANCELADO) && !tupla.getStatus().equals(StatusTupla.CANCELADO_COM_PUNICAO)) {
				result.add(tupla);
			}
		}
		return result;
	}
	
	@Override
	@Cacheable("ranking")
	public Ranking getRanking(Periodo periodo) {
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
			tupla.setSs(getSemestresSolicitados(reserva));
			tupla.setT(calculaSemestres(reserva.getProfessor().getAnoAdmissao(), reserva.getProfessor().getSemestreAdmissao(), 
					reserva.getAnoInicio(), reserva.getSemestreInicio()) - 1);
			tupla.setA(getSemestresAfastados(reserva));
			Float t = Float.valueOf(tupla.getT());
			Float a = Float.valueOf(tupla.getA());
			Float s = a == 0.0 ? 2 : Float.valueOf(tupla.getSs());
			tupla.setS(s.intValue());
			Integer semContratacao = calculaSemestres(reserva.getProfessor().getAnoAdmissao(), reserva.getProfessor().getSemestreAdmissao(), 
					reserva.getAnoInicio(), reserva.getSemestreInicio()) - 1;
			Float p = semContratacao >= 6.0f ? 0.0f : (6.0f - semContratacao);
			tupla.setP(p.intValue());
			Float pontuacao = (t - a) / (5.0f * a + s + p);
			tupla.setPontuacao(pontuacao);
			
			tuplas.add(tupla);
		}
		Collections.sort(tuplas, new Comparator<TuplaRanking>() {
	        @Override
	        public int compare(TuplaRanking  ranking1, TuplaRanking  ranking2)
	        {
	        	if(ranking1.getPontuacao().compareTo(ranking2.getPontuacao()) == 0.0f) {
	        		if(ranking1.getReserva().getPrograma().equals(ranking2.getReserva().getPrograma())) {
	        			if(ranking1.getReserva().getConceitoPrograma() != null && ranking2.getReserva().getConceitoPrograma() != null) {
		        			if(ranking1.getReserva().getConceitoPrograma().equals(ranking2.getReserva().getConceitoPrograma())) {
		        				return ranking2.getReserva().getProfessor().getDataNascimento().compareTo(ranking1.getReserva().getProfessor().getDataNascimento());
		        			}
		        			return ranking1.getReserva().getConceitoPrograma().compareTo(ranking2.getReserva().getConceitoPrograma());
		        		} else {
		        			return ranking2.getReserva().getProfessor().getDataNascimento().compareTo(ranking1.getReserva().getProfessor().getDataNascimento());
		        		}
	        		}
	        		if(ranking1.getReserva().getPrograma().equals(Programa.MESTRADO)) {
	        			return 1;
	        		}
	        		if(ranking2.getReserva().getPrograma().equals(Programa.MESTRADO)) {
	        			return -1;
	        		}
	        		if(ranking1.getReserva().getPrograma().equals(Programa.DOUTORADO)) {
	        			return 1;
	        		}
	        		if(ranking2.getReserva().getPrograma().equals(Programa.DOUTORADO)) {
	        			return -1;
	        		}
	        	}
	            return  ranking1.getPontuacao().compareTo(ranking2.getPontuacao());
	        }
	    });
		Collections.reverse(tuplas);
		int vagas = periodo.getVagas();
		
		// Subtrai as vagas já garantidas (afastados)
		for (TuplaRanking tupla : tuplas) {
			//Periodo periodoInicio = periodoService.getPeriodo(tupla.getReserva().getAnoInicio(), tupla.getReserva().getSemestreInicio());
			if(tupla.getReserva().getStatus().equals(StatusReserva.AFASTADO)) {
				vagas--;
			}
		}
		
		// Classifica o ranking
		for (TuplaRanking tupla : tuplas) {
			//Periodo periodoInicio = periodoService.getPeriodo(tupla.getReserva().getAnoInicio(), tupla.getReserva().getSemestreInicio());
			if(tupla.getReserva().getStatus().equals(StatusReserva.AFASTADO)) {
				tupla.setStatus(StatusTupla.AFASTADO);
			} else if(tupla.getReserva().getStatus().equals(StatusReserva.ABERTO)) {
				if(vagas > 0) {
					tupla.setStatus(StatusTupla.CLASSIFICADO);
					vagas--;
				} else {
					tupla.setStatus(StatusTupla.DESCLASSIFICADO);
				}
			} else if(tupla.getReserva().getStatus().equals(StatusReserva.NAO_ACEITO)) {
				tupla.setStatus(StatusTupla.NAO_ACEITO);
			} else if(tupla.getReserva().getStatus().equals(StatusReserva.ENCERRADO)) {
				tupla.setStatus(StatusTupla.ENCERRADO);
			} else if(tupla.getReserva().getStatus().equals(StatusReserva.CANCELADO)) {
				tupla.setStatus(StatusTupla.CANCELADO);
			} else if(tupla.getReserva().getStatus().equals(StatusReserva.CANCELADO_COM_PUNICAO)) {
				tupla.setStatus(StatusTupla.CANCELADO_COM_PUNICAO);
			} else if(tupla.getReserva().getStatus().equals(StatusReserva.NEGADO)) {
				tupla.setStatus(StatusTupla.NEGADO);
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
		Periodo periodo = periodoService.getPeriodo(reserva.getAnoInicio(), reserva.getSemestreInicio());
		Integer semestresAfastado = 0;
		for(Afastamento afastamento : afastamentos) {
			semestresAfastado = semestresAfastado + calculaSemestres(afastamento.getReserva().getAnoInicio(), afastamento.getReserva().getSemestreInicio(), 
					afastamento.getReserva().getAnoTermino(), afastamento.getReserva().getSemestreTermino());
		}
		Integer punicao = reservaService.getReservasAnterioresComPunicao(reserva.getProfessor(), periodo).size();
		semestresAfastado = semestresAfastado + punicao;
		return semestresAfastado;
	}
	
	private Integer calculaSemestres(Integer anoInicio, Integer semestreInicio, Integer anoTermino, Integer semestreTermino) {
		return ((anoTermino - anoInicio) * 2) + (semestreTermino - semestreInicio) + 1;
	}	

}
