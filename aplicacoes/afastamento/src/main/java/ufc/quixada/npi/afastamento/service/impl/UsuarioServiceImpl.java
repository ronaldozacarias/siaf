package ufc.quixada.npi.afastamento.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Usuario;
import ufc.quixada.npi.afastamento.service.UsuarioService;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.GenericRepository;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class UsuarioServiceImpl extends GenericServiceImpl<Usuario> implements UsuarioService {
	
	@Inject
	private GenericRepository<Usuario> usuarioRepository;
	
	@Inject
	private GenericRepository<Professor> professorRepository;
	
	@Override
	public Usuario getUsuarioByLogin(String login) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("login", login);
		return usuarioRepository.find(QueryType.JPQL, "from Usuario where login = :login", params).get(0);
	}

	@Override
	public int getQuantidadeProfessor() {
		return professorRepository.find(QueryType.JPQL, "from Professor", null).size();
	}

	@Override
	public Professor getProfessorByUsuario(Usuario usuario) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("usuario_id", usuario.getId());
		List<Professor> result = professorRepository.find(QueryType.JPQL, "from Professor where usuario.id = :usuario_id", params);
		if(!result.isEmpty()) {
			return result.get(0);
		}
		return null;
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
