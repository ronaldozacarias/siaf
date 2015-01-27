package ufc.quixada.npi.afastamento.service;

import ufc.quixada.npi.afastamento.model.RecuperacaoSenha;
import ufc.quixada.npi.afastamento.model.Usuario;
import br.ufc.quixada.npi.service.GenericService;

public interface RecuperacaoSenhaService extends GenericService<RecuperacaoSenha> {
	
	void recuperarSenha(RecuperacaoSenha recuperacao);
	
	RecuperacaoSenha getRecuperacaoByCodigo(String codigo);
	
	RecuperacaoSenha getRecuperacaoByUsuario(Usuario usuario);

}
