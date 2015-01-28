package ufc.quixada.npi.afastamento.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import ufc.quixada.npi.afastamento.model.Papel;
import ufc.quixada.npi.afastamento.service.PapelService;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.GenericRepository;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class PapelServiceImpl extends GenericServiceImpl<Papel> implements
		PapelService {

	@Inject
	private GenericRepository<Papel> papelRepository;

	@Override
	public Papel getPapel(String papel) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("papel", papel);
		return papelRepository.findFirst(QueryType.JPQL, "select p from Papel p where p.nome = :papel", params, -1);
	}
}
