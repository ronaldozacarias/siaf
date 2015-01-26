package ufc.quixada.npi.afastamento.service;

import br.ufc.quixada.npi.service.GenericService;
import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Usuario;

public interface UsuarioService extends GenericService<Usuario> {

	Usuario getUsuarioByLogin(String login);
	
	Usuario getUsuarioByEmail(String email);
	
	Professor getProfessorByUsuario(Usuario usuario);
	
	int getQuantidadeProfessor();


}
