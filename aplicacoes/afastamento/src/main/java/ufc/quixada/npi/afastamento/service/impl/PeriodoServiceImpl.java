package ufc.quixada.npi.afastamento.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.service.PeriodoService;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.GenericRepository;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class PeriodoServiceImpl extends GenericServiceImpl<Periodo> implements PeriodoService {

	@Inject
	private GenericRepository<Periodo> periodoRepository;

	@Override
	public Periodo getPeriodo(Integer ano, Integer semestre) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ano", ano);
		params.put("semestre", semestre);
		return periodoRepository.find(QueryType.JPQL, "select p from Periodo p where p.ano = :ano and p.semestre = :semestre", params).get(0);
	}
}
