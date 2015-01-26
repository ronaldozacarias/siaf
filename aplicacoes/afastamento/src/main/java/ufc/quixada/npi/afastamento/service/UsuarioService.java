package ufc.quixada.npi.afastamento.service;

import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Usuario;

public interface UsuarioService {

	Usuario getUsuarioByLogin(String login);
	
	Professor getProfessorByUsuario(Usuario usuario);
	
	int getQuantidadeProfessor();


}
