package ufc.quixada.npi.afastamento.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Usuario;
import ufc.quixada.npi.afastamento.service.UsuarioService;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.GenericRepository;

@Named
public class UsuarioServiceImpl implements UsuarioService {
	
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
		return professorRepository.find(QueryType.JPQL, "from Professor where usuario.id = :usuario_id", params).get(0);
	}

}
