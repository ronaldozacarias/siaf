package ufc.quixada.npi.afastamento.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import ufc.quixada.npi.afastamento.service.UserService;
import ufc.quixada.npi.afastamento.util.Constants;
import br.ufc.quixada.npi.ldap.model.Affiliation;
import br.ufc.quixada.npi.ldap.model.Usuario;
import br.ufc.quixada.npi.ldap.service.UsuarioService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class UserServiceImpl extends GenericServiceImpl<Usuario> implements UserService {
	
	@Inject
	private UsuarioService usuarioService;
	
	@Override
	public Usuario getByCpf(String cpf) {
		return usuarioService.getByCpf(cpf);
	}

	@Override
	public boolean isAdministrador(String cpf) {
		Usuario usuario = usuarioService.getByCpf(cpf);
		for (Affiliation affiliation : usuario.getAffiliations()) {
			if (affiliation.getAuthority().equals(Constants.AFFILIATION_ADMIN_SIAF)) {
				return true;
			}
		}
		return false;
	}

}
