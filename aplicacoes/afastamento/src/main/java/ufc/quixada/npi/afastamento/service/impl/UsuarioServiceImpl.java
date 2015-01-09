package ufc.quixada.npi.afastamento.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.service.UsuarioService;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.GenericRepository;

@Named
public class UsuarioServiceImpl implements UsuarioService {
	
	@Inject
	private GenericRepository<Professor> usuarioRepository;
	
	@Override
	public Professor getUsuarioByLogin(String login) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("siape", login);
		return usuarioRepository.find(QueryType.JPQL, "from Professor where siape = :siape", params).get(0);
	}

	@Override
	public int getQuantidadeProfessor() {
		return (Integer) usuarioRepository.find(QueryType.JPQL, "select count(*) from Professor", null).size();
	}

}
