package ufc.quixada.npi.afastamento.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.service.PeriodoService;
import ufc.quixada.npi.afastamento.service.RelatorioService;
import ufc.quixada.npi.afastamento.service.ReservaService;
import br.ufc.quixada.npi.enumeration.QueryType;

@Named
public class RelatorioServiceImpl implements RelatorioService {

	@Inject
	private PeriodoService periodoService;

	@Inject
	private ReservaService reservaService;

	@Override
	@Transactional
	public Map<String, Integer> getRelatorioReservasByPeriodo(
			Periodo inicio, Periodo termino) {
		Map<String, Object> params = new TreeMap<String, Object>();
		params.put("anoInicio", inicio.getAno());
		params.put("semestreInicio", inicio.getSemestre());
		params.put("anoTermino", termino.getAno());
		params.put("semestreTermino", termino.getSemestre());
		List<Periodo> periodos = periodoService
				.find(QueryType.JPQL,
						"from Periodo where ano >= :anoInicio and semestre >= :semestreInicio and ano <= :anoTermino and semestre <= :semestreTermino",
						params);

		Map<String, Integer> map = new HashMap<String, Integer>();
		for(Periodo periodo : periodos) {
			params = new HashMap<String, Object>();
			params.put("ano", periodo.getAno());
			params.put("semestre", periodo.getSemestre());
			map.put(periodo.getAno() + "." + periodo.getSemestre(), reservaService.find(QueryType.JPQL, "from Reserva where :ano >= anoInicio and :semestre >= semestreInicio and :ano <= anoTermino and :semestre <= semestreTermino", params).size());
		}
		Map<String, Integer> treeMap = new TreeMap<String, Integer>(map);
		return treeMap;
	}
}
