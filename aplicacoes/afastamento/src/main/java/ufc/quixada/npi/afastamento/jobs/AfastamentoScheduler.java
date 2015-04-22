package ufc.quixada.npi.afastamento.jobs;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import ufc.quixada.npi.afastamento.model.Afastamento;
import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Ranking;
import ufc.quixada.npi.afastamento.model.StatusPeriodo;
import ufc.quixada.npi.afastamento.model.StatusReserva;
import ufc.quixada.npi.afastamento.model.StatusTupla;
import ufc.quixada.npi.afastamento.model.TuplaRanking;
import ufc.quixada.npi.afastamento.service.AfastamentoService;
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
	
	@Inject
	private AfastamentoService afastamentoService;
	
	@Scheduled(cron = "0 0 0 1/1 * ?")
	@CacheEvict(value = {"default", "reservasByProfessor", "periodo", "visualizarRanking", "ranking", "loadProfessor", "professores"}, allEntries = true)
	public void verificaEncerramentoPeriodo() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date ontem = calendar.getTime();
		Periodo periodo = periodoService.getPeriodoByEncerramento(new java.sql.Date(ontem.getTime()));
		if(periodo != null) {
			periodo.setStatus(StatusPeriodo.ENCERRADO);
			periodoService.update(periodo);
			
			Ranking ranking = rankingService.getRanking(periodo);
			for(TuplaRanking tupla : ranking.getTuplas()) {
				if(tupla.getStatus().equals(StatusTupla.ACEITO)) {
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
				if(tupla.getStatus().equals(StatusTupla.CLASSIFICADO)) {
					tupla.getReserva().setStatus(StatusReserva.ACEITO);
					reservaService.update(tupla.getReserva());
					Afastamento afastamento = new Afastamento();
					afastamento.setReserva(tupla.getReserva());
					afastamentoService.save(afastamento);
				} else if(tupla.getStatus().equals(StatusTupla.DESCLASSIFICADO)) {
					tupla.getReserva().setStatus(StatusReserva.NAO_ACEITO);
					reservaService.update(tupla.getReserva());
				}
			}
		}
		
		
	}
	
	

}
