package ufc.quixada.npi.afastamento.jobs;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Ranking;
import ufc.quixada.npi.afastamento.model.StatusReserva;
import ufc.quixada.npi.afastamento.model.TuplaRanking;
import ufc.quixada.npi.afastamento.service.PeriodoService;
import ufc.quixada.npi.afastamento.service.RankingService;
import ufc.quixada.npi.afastamento.service.ReservaService;

@Named
@Configurable
@EnableScheduling
public class AfastamentoScheduler {
	
	@Inject
	private PeriodoService periodoService;
	
	@Inject
	private RankingService rankingService;
	
	@Inject
	private ReservaService reservaService;
	
	@Scheduled(cron = "0 0 0 1/1 * ?")
	public void verificaEncerramentoPeriodo() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date ontem = calendar.getTime();
		System.out.println("Ontem: " + ontem);
		Periodo periodo = periodoService.getPeriodo(new java.sql.Date(ontem.getTime()));
		System.out.println("Verificando encerramento...");
		System.out.println(periodo);
		if(periodo != null) {
			periodo.setStatus(StatusReserva.ENCERRADO);
			periodoService.update(periodo);
			
			Ranking ranking = rankingService.getRanking(periodo);
			for(TuplaRanking tupla : ranking.getTuplas()) {
				if(tupla.getStatus().equals(StatusReserva.ACEITO)) {
					Periodo ultimoPeriodo = periodoService.getPeriodo(tupla.getReserva().getAnoTermino(), tupla.getReserva().getSemestreTermino());
					if(ultimoPeriodo.equals(periodo)) {
						tupla.getReserva().setStatus(StatusReserva.ENCERRADO);
						reservaService.update(tupla.getReserva());
					}
				}
			}
			
			periodo = periodoService.getPeriodoPosterior(periodoService.getPeriodoPosterior(periodo));
			ranking = rankingService.getRanking(periodo);
			for(TuplaRanking tupla : ranking.getTuplas()) {
				if(tupla.getStatus().equals(StatusReserva.CLASSIFICADO)) {
					tupla.getReserva().setStatus(StatusReserva.ACEITO);
					reservaService.update(tupla.getReserva());
				} else if(tupla.getStatus().equals(StatusReserva.DESCLASSIFICADO)) {
					tupla.getReserva().setStatus(StatusReserva.NAO_ACEITO);
					reservaService.update(tupla.getReserva());
				}
			}
		}
		
	}

}
