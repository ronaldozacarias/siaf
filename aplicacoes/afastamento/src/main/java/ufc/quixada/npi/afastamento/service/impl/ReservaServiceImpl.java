package ufc.quixada.npi.afastamento.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Reserva;
import ufc.quixada.npi.afastamento.model.StatusPeriodo;
import ufc.quixada.npi.afastamento.model.StatusReserva;
import ufc.quixada.npi.afastamento.service.PeriodoService;
import ufc.quixada.npi.afastamento.service.ProfessorService;
import ufc.quixada.npi.afastamento.service.ReservaService;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.GenericRepository;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class ReservaServiceImpl extends GenericServiceImpl<Reserva> implements ReservaService {

	@Inject
	private GenericRepository<Reserva> reservaRepository;

	@Inject
	private PeriodoService periodoService;

	@Inject
	private ProfessorService professorService;

	@Override
	@CacheEvict(value = {"default", "reservasByProfessor", "periodo", "visualizarRanking", "loadProfessor", "professores"}, allEntries = true)
	public void salvar(Reserva reserva) {
		int vagas = professorService.findAtivos().size();
		for (int ano = reserva.getAnoInicio(); ano <= reserva.getAnoTermino(); ano++) {
			Periodo periodo = new Periodo();
			periodo.setVagas((int) (vagas * 0.15));
			periodo.setAno(ano);
			periodo.setStatus(StatusPeriodo.ABERTO);
			if (ano == reserva.getAnoInicio() && reserva.getSemestreInicio() == 2) {
				periodo.setSemestre(2);
				if (periodoService.getPeriodo(periodo.getAno(), periodo.getSemestre()) == null) {
					periodoService.save(periodo);
				}
				continue;
			}
			if (ano == reserva.getAnoTermino() && reserva.getSemestreTermino() == 1) {
				periodo.setSemestre(1);
				if (periodoService.getPeriodo(periodo.getAno(), periodo.getSemestre()) == null) {
					periodoService.save(periodo);
				}
				break;
			}
			periodo.setSemestre(1);
			if (periodoService.getPeriodo(periodo.getAno(), periodo.getSemestre()) == null) {
				periodoService.save(periodo);
			}

			periodo = new Periodo();
			periodo.setAno(ano);
			periodo.setSemestre(2);
			periodo.setVagas((int) (vagas * 0.15));
			periodo.setStatus(StatusPeriodo.ABERTO);
			if (periodoService.getPeriodo(periodo.getAno(), periodo.getSemestre()) == null) {
				periodoService.save(periodo);
			}
		}
		reservaRepository.save(reserva);

	}

	@Override
	@Cacheable("reservasByProfessor")
	public List<Reserva> getReservasByProfessor(Professor professor) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cpf", professor.getCpf());
		return reservaRepository.find(QueryType.JPQL,
				"from Reserva where professor.cpf = :cpf order by anoInicio DESC, semestreInicio DESC", params);
	}

	@Override
	public boolean hasReservaEmAberto(Professor professor) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cpf", professor.getCpf());
		return reservaRepository.find(QueryType.JPQL, "from Reserva where status = 'ABERTO' and professor.cpf = :cpf",
				params).size() > 0;
	}

	@Override
	public Reserva getReservaById(Long id) {
		return reservaRepository.find(Reserva.class, id);
	}

	@Override
	public List<Reserva> getReservasAnterioresComPunicao(Professor professor, Periodo periodo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cpf", professor.getCpf());
		params.put("ano", periodo.getAno());
		params.put("semestre", periodo.getSemestre());
		params.put("status", StatusReserva.CANCELADO_COM_PUNICAO);
		return reservaRepository.find(QueryType.JPQL, "from Reserva where status = :status and professor.cpf = :cpf and (anoTermino < :ano or (anoTermino = :ano and semestreTermino < :semestre))", params);
	}

	@Override
	@CacheEvict(value = { "default", "reservasByProfessor", "periodo", "visualizarRanking", "ranking", "loadProfessor",
			"professores" }, allEntries = true, beforeInvocation = true)
	public void atualizar(Reserva reserva) {
		update(reserva);
	}

	@Override
	public List<Reserva> getReservasByStatus(StatusReserva status) {
			Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", status);
		return reservaRepository.find(QueryType.JPQL,
				"from Reserva where status = :status order by anoInicio DESC, semestreInicio DESC", params);
	}

	@Override
	public List<Reserva> getReservasByStatusReservaAndPeriodo(StatusReserva statusReserva, Periodo periodo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", statusReserva);
		params.put("ano", periodo.getAno());
		params.put("semestre", periodo.getSemestre());
		return reservaRepository.find(QueryType.JPQL, "from Reserva where status = :status and anoTermino <= :ano and semestreTermino <= :semestre order by anoInicio, semestreInicio", params);
	}

}
