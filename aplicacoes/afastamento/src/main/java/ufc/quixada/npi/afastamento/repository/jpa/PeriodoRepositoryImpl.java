package ufc.quixada.npi.afastamento.repository.jpa;

import java.util.Map;
import java.util.Set;

import javax.inject.Named;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.repository.PeriodoRepository;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class PeriodoRepositoryImpl extends JpaGenericRepositoryImpl<Periodo> implements PeriodoRepository {

	@Transactional	
	public void updateVagas(String queryName, Map<String, Object> namedParams) {
		Query query = super.em.createQuery(queryName);

		if (namedParams != null) {
			Set<String> keys = namedParams.keySet();
			for (String key : keys) {
				query.setParameter(key, namedParams.get(key));
			}
		}
		query.executeUpdate();
	}

}
