package ufc.quixada.npi.afastamento.jobs;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Ranking;
import ufc.quixada.npi.afastamento.model.StatusPeriodo;
import ufc.quixada.npi.afastamento.model.StatusReserva;
import ufc.quixada.npi.afastamento.model.StatusTupla;
import ufc.quixada.npi.afastamento.model.TuplaRanking;
import ufc.quixada.npi.afastamento.service.PeriodoService;
import ufc.quixada.npi.afastamento.service.ProfessorService;
import ufc.quixada.npi.afastamento.service.RankingService;
import ufc.quixada.npi.afastamento.service.ReservaService;
import br.ufc.quixada.npi.ldap.model.Constants;
import br.ufc.quixada.npi.ldap.model.Usuario;
import br.ufc.quixada.npi.ldap.service.UsuarioService;

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
	private UsuarioService usuarioService;
	
	@Inject
	private ProfessorService professorService;
	
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
				} else if(tupla.getStatus().equals(StatusTupla.DESCLASSIFICADO)) {
					tupla.getReserva().setStatus(StatusReserva.NAO_ACEITO);
					reservaService.update(tupla.getReserva());
				}
			}
		}
		adicionaNovosProfessores();
		
	}
	
	private void adicionaNovosProfessores() {
		List<Usuario> usuarios = usuarioService.getByAffiliation(Constants.BASE_USUARIOS, Constants.AFFILIATION_DOCENTE);
		for(Usuario usuario : usuarios) {
			Professor professor = professorService.getByCpf(usuario.getCpf());
			if(professor == null) {
				professor = new Professor();
				professor.setCpf(usuario.getCpf());
				professorService.save(professor);
			}
		}
		atualizaVagas();
		
	}
	
	private void atualizaVagas() {
		Periodo periodoAtual = periodoService.getPeriodoPosterior(periodoService.getPeriodoPosterior(periodoService.getPeriodoAtual()));
		List<Periodo> periodos = periodoService.getPeriodosPosteriores(periodoAtual);
		int vagas = (int) (professorService.findAtivos().size() * 0.15);
		for(Periodo periodo : periodos) {
			periodo.setVagas(vagas);
			periodoService.update(periodo);
		}
	}

}
