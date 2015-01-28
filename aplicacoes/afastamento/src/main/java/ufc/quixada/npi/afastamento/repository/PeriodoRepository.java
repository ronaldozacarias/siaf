package ufc.quixada.npi.afastamento.repository;

import java.util.Map;

import ufc.quixada.npi.afastamento.model.Periodo;
import br.ufc.quixada.npi.repository.GenericRepository;

public interface PeriodoRepository extends GenericRepository<Periodo> {

	void updateVagas(String queryName, Map<String, Object> namedParams);

}
