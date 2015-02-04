package ufc.quixada.npi.afastamento.service;

import br.ufc.quixada.npi.ldap.model.Usuario;

public interface UserService {
	
	Usuario getByCpf(String cpf);

}
