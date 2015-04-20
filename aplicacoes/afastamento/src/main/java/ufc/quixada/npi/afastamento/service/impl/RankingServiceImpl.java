package ufc.quixada.npi.afastamento.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		List<Periodo> periodos = periodoService	.getPeriodoAbertos();
		Map<Periodo, List<TuplaRanking>> ranking = new HashMap<Periodo, List<TuplaRanking>>();
		for(Periodo p : periodos) {
			ranking.put(p, new ArrayList<TuplaRanking>());
		}
		
		List<TuplaRanking> tuplas = new ArrayList<TuplaRanking>();
		List<Reserva> reservas = reservaService.getReservasAbertasOuAfastados();
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
	        public int compare(TuplaRanking  tupla1, TuplaRanking  tupla2)
	        {
	        	if(tupla1.getPontuacao().compareTo(tupla2.getPontuacao()) == 0.0f) {
	        		if(tupla1.getReserva().getPrograma().equals(tupla2.getReserva().getPrograma())) {
	        			if(tupla1.getReserva().getConceitoPrograma() != null && tupla2.getReserva().getConceitoPrograma() != null) {
		        			if(tupla1.getReserva().getConceitoPrograma().equals(tupla2.getReserva().getConceitoPrograma())) {
		        				return tupla1.getReserva().getProfessor().getDataNascimento().compareTo(tupla2.getReserva().getProfessor().getDataNascimento());
		        			}
		        			return tupla2.getReserva().getConceitoPrograma().compareTo(tupla1.getReserva().getConceitoPrograma());
		        		} else {
		        			return tupla1.getReserva().getProfessor().getDataNascimento().compareTo(tupla2.getReserva().getProfessor().getDataNascimento());
		        		}
	        		}
	        		if(tupla1.getReserva().getPrograma().equals(Programa.MESTRADO)) {
	        			return -1;
	        		}
	        		if(tupla2.getReserva().getPrograma().equals(Programa.MESTRADO)) {
	        			return 1;
	        		}
	        		if(tupla1.getReserva().getPrograma().equals(Programa.DOUTORADO)) {
	        			return -1;
	        		}
	        		if(tupla2.getReserva().getPrograma().equals(Programa.DOUTORADO)) {
	        			return 1;
	        		}
	        	}
	            return  tupla2.getPontuacao().compareTo(tupla1.getPontuacao());
	        }
	    });
		
		// Coloca primeiramente nos períodos os que já estão classificados
		for(TuplaRanking tupla : tuplas) {
			if(tupla.getReserva().getStatus().equals(StatusReserva.AFASTADO)) {
				Periodo periodoInicio = periodoService.getPeriodo(tupla.getReserva().getAnoInicio(), tupla.getReserva().getSemestreInicio());
				Periodo periodoTermino = periodoService.getPeriodo(tupla.getReserva().getAnoTermino(), tupla.getReserva().getSemestreTermino());
				for(;periodoInicio != null && !periodoInicio.equals(periodoService.getPeriodoPosterior(periodoTermino)); periodoInicio = periodoService.getPeriodoPosterior(periodoInicio)) {
					if(ranking.containsKey(periodoInicio)) {
						tupla.setStatus(StatusTupla.AFASTADO);
						List<TuplaRanking> tuplaPeriodo = ranking.get(periodoInicio);
						tuplaPeriodo.add(tupla);
						ranking.replace(periodoInicio, tuplaPeriodo);
					}
				}
			}
		}
		
		for(TuplaRanking tupla : tuplas) {
			if(!tupla.getReserva().getStatus().equals(StatusReserva.AFASTADO)) {
				boolean classificado = true;
				Periodo periodoInicio = periodoService.getPeriodo(tupla.getReserva().getAnoInicio(), tupla.getReserva().getSemestreInicio());
				Periodo periodoTermino = periodoService.getPeriodo(tupla.getReserva().getAnoTermino(), tupla.getReserva().getSemestreTermino());
				for(;periodoInicio != null && !periodoInicio.equals(periodoService.getPeriodoPosterior(periodoTermino)); periodoInicio = periodoService.getPeriodoPosterior(periodoInicio)) {
					int vagas = periodoInicio.getVagas();
					for(TuplaRanking t : ranking.get(periodoInicio)) {
						if(t.getStatus().equals(StatusTupla.AFASTADO) || t.getStatus().equals(StatusTupla.CLASSIFICADO)) {
							vagas--;
						}
					}
					if(vagas <= 0) {
						tupla.setStatus(StatusTupla.DESCLASSIFICADO);
						classificado = false;
						break;
					}
				}
				if(classificado) {
					tupla.setStatus(StatusTupla.CLASSIFICADO);
				}
				periodoInicio = periodoService.getPeriodo(tupla.getReserva().getAnoInicio(), tupla.getReserva().getSemestreInicio());
				for(;periodoInicio != null && !periodoInicio.equals(periodoService.getPeriodoPosterior(periodoTermino)); periodoInicio = periodoService.getPeriodoPosterior(periodoInicio)) {
					List<TuplaRanking> tuplaPeriodo = ranking.get(periodoInicio);
					tuplaPeriodo.add(tupla);
					ranking.replace(periodoInicio, tuplaPeriodo);
				}
			}
		}
		
		return ranking.get(periodo);
	}
	
	@Override
	@Cacheable("ranking")
	public Ranking getRanking(Periodo periodo) {
		Ranking ranking = new Ranking();
		/*ranking.setPeriodo(periodo);
		
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
		
		int vagas = periodo.getVagas();
		
		// Subtrai as vagas já garantidas (afastados)
		for (TuplaRanking tupla : tuplas) {
			if(tupla.getReserva().getStatus().equals(StatusReserva.AFASTADO)) {
				vagas--;
			}
		}
		
		// Classifica o ranking
		for (TuplaRanking tupla : tuplas) {
			if(tupla.getReserva().getStatus().equals(StatusReserva.AFASTADO)) {
				tupla.setStatus(StatusTupla.AFASTADO);
			} else if(tupla.getReserva().getStatus().equals(StatusReserva.ABERTO)) {
				if(vagas > 0) {
					tupla.setStatus(StatusTupla.CLASSIFICADO);
					vagas--;
				} else {
					tupla.setStatus(StatusTupla.DESCLASSIFICADO);
				}
			}

		}
		
		ranking.setTuplas(tuplas);*/
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
