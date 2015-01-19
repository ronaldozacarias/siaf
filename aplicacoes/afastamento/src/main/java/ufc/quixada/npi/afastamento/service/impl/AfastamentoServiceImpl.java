package ufc.quixada.npi.afastamento.service.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.GenericRepository;
import ufc.quixada.npi.afastamento.model.Afastamento;
import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Reserva;
import ufc.quixada.npi.afastamento.service.AfastamentoService;

@Named
public class AfastamentoServiceImpl implements AfastamentoService {
	
	@Inject
	private GenericRepository<Afastamento> afastamentoRepository;
	
	@Inject
	private GenericRepository<Periodo> periodoRepository;

	@Override
	public List<Afastamento> getAfastamentosByProfessor(String siape) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("siape", siape);
		return afastamentoRepository.find(QueryType.JPQL, "from Afastamento where reserva.professor.siape = :siape", params);
	}

	@Override
	public Periodo getPeriodoByAnoSemestre(Integer ano, Integer semestre) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ano", ano);
		params.put("semestre", semestre);
		return periodoRepository.findFirst(QueryType.JPQL, "from Periodo where ano = :ano and semestre = :semestre", params, 0);
	}
	
	@Override
	public boolean isPeriodoEncerrado(Integer ano, Integer semestre) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ano", ano);
		params.put("semestre", semestre);
		return periodoRepository.find(QueryType.JPQL, "from Periodo where ano = :ano and semestre = :semestre"
				+ " and status = 'ENCERRADO'", params).size() > 0;
	}

	@Override
	public void inserirPeriodo(Integer anoInicio, Integer semestreInicio,
			Integer anoTermino, Integer semestreTermino) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Integer getSemestreAtual() {
		Calendar calendar = Calendar.getInstance();
		if(calendar.get(Calendar.MONTH) < 6) {
			return 1;
		}
		return 2;
	}
	
	@Override
	public Integer getAnoAtual() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}

	@Override
	public Periodo getPeriodoAnterior(Integer ano, Integer semestre) {
		if(semestre == 2) {
			return getPeriodoByAnoSemestre(ano, 1);
		}
		return getPeriodoByAnoSemestre(ano - 1, 2);
	}

	@Override
	public Periodo getPeriodoPosterior(Integer ano, Integer semestre) {
		if(semestre == 1) {
			return getPeriodoByAnoSemestre(ano, 2);
		}
		return getPeriodoByAnoSemestre(ano + 1, 1);
	}

	@Override
	public List<Afastamento> getAfastamentosAnteriores(Reserva reserva) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("siape", reserva.getProfessor().getSiape());
		params.put("anoInicio", reserva.getAnoInicio());
		params.put("semestreInicio", reserva.getSemestreInicio());
		return afastamentoRepository.find(QueryType.JPQL, "from Afastamento where reserva.professor.siape = :siape "
				+ "and (reserva.anoInicio < :anoInicio or (reserva.anoInicio = :anoInicio and reserva.semestreInicio < :semestreInicio))", params);
	}

}
