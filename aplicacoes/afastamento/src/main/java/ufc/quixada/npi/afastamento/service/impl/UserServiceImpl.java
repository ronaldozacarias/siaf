package ufc.quixada.npi.afastamento.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import ufc.quixada.npi.afastamento.model.Usuario;
import ufc.quixada.npi.afastamento.service.UserService;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.GenericRepository;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class UserServiceImpl extends GenericServiceImpl<Usuario> implements UserService {
	
	@Inject
	private GenericRepository<Usuario> usuarioRepository;
	
	@Override
	public Usuario getUsuarioByLogin(String login) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("login", login);
		return usuarioRepository.find(QueryType.JPQL, "from Usuario where login = :login", params).get(0);
	}

	@Override
	public Usuario getUsuarioByEmail(String email) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("email", email);
		List<Usuario> result = usuarioRepository.find(QueryType.JPQL, "from Usuario where email = :email", params);
		if(!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

}
