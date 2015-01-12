package ufc.quixada.npi.afastamento.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.GenericRepository;
import ufc.quixada.npi.afastamento.model.Afastamento;
import ufc.quixada.npi.afastamento.service.AfastamentoService;

@Named
public class AfastamentoServiceImpl implements AfastamentoService {
	
	@Inject
	private GenericRepository<Afastamento> afastamentoRepository;

	@Override
	public List<Afastamento> getAfastamentosByProfessor(String siape) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("siape", siape);
		return afastamentoRepository.find(QueryType.JPQL, "from Afastamento where reserva.professor.siape = :siape", params);
	}

}
