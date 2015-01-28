package ufc.quixada.npi.afastamento.service;

import ufc.quixada.npi.afastamento.model.Usuario;
import br.ufc.quixada.npi.service.GenericService;

public interface UsuarioService extends GenericService<Usuario> {

	Usuario getUsuarioByLogin(String login);
	
	Usuario getUsuarioByEmail(String email);
	
}
