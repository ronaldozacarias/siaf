package ufc.quixada.npi.afastamento.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Reserva;
import ufc.quixada.npi.afastamento.model.StatusPeriodo;
import ufc.quixada.npi.afastamento.service.PeriodoService;
import ufc.quixada.npi.afastamento.service.ReservaService;
import ufc.quixada.npi.afastamento.service.UsuarioService;
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
	private UsuarioService usuarioService;
	
	@Override
	public void salvar(Reserva reserva) {
		int vagas = usuarioService.getQuantidadeProfessor();
		for (int ano = reserva.getAnoInicio(); ano <= reserva.getAnoTermino(); ano++) {
			Periodo periodo = new Periodo();
			periodo.setVagas((int)(vagas * 0.15));
			periodo.setAno(ano);
			periodo.setStatus(StatusPeriodo.ABERTO);
			if (ano == reserva.getAnoInicio() && reserva.getSemestreInicio() == 2) {
				periodo.setSemestre(2);
				periodoService.save(periodo);
				continue;
			}
			if (ano == reserva.getAnoTermino() && reserva.getSemestreTermino() == 1) {
				periodo.setSemestre(1);
				periodoService.save(periodo);
				break;
			}
			periodo.setSemestre(1);
			periodoService.save(periodo);
			
			periodo = new Periodo();
			periodo.setAno(ano);
			periodo.setSemestre(2);
			periodo.setVagas(vagas);
			periodo.setStatus(StatusPeriodo.ABERTO);
			periodoService.save(periodo);
		}
		reservaRepository.save(reserva);
		
	}

	@Override
	public List<Reserva> getReservasByProfessor(String siape) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("siape", siape);
		return reservaRepository.find(QueryType.JPQL, "from Reserva where professor.siape = :siape", params);
	}

	@Override
	public boolean hasReservaEmAberto(Professor professor) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("siape", professor.getSiape());
		return reservaRepository.find(QueryType.JPQL, "from Reserva where status = 'ABERTO' and professor.siape = :siape", params).size() > 0;
	}

	@Override
	public List<Reserva> getReservasByPeriodo(Integer ano, Integer semestre) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ano", ano);
		params.put("semestre", semestre);
		return reservaRepository.find(QueryType.JPQL, "from Reserva where :ano >= anoInicio and :ano <= anoTermino and id not in (select id from Reserva where "
				+ "(anoInicio = :ano and semestreInicio > :semestre) or (anoTermino = :ano and semestreTermino < :semestre))", params);
	}

	@Override
	public Reserva getReservaById(Long id) {
		return reservaRepository.find(Reserva.class, id);
	}

}
