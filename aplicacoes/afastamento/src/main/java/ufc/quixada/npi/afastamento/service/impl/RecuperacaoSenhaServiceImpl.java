package ufc.quixada.npi.afastamento.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;

import ufc.quixada.npi.afastamento.model.RecuperacaoSenha;
import ufc.quixada.npi.afastamento.service.RecuperacaoSenhaService;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.model.Email;
import br.ufc.quixada.npi.repository.GenericRepository;
import br.ufc.quixada.npi.service.EmailService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class RecuperacaoSenhaServiceImpl extends GenericServiceImpl<RecuperacaoSenha> implements RecuperacaoSenhaService {

	@Inject
	private GenericRepository<RecuperacaoSenha> recuperacaoSenhaRepository;
	
	@Inject
	private EmailService emailService;
	
	@Override
	public void recuperarSenha(RecuperacaoSenha recuperacao) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("usuario", recuperacao.getUsuario().getId());
		List<RecuperacaoSenha> result = recuperacaoSenhaRepository.find(QueryType.JPQL, "from RecuperacaoSenha where usuario.id = :usuario", params);
		if(result != null) {
			for(RecuperacaoSenha rs : result) {
				recuperacaoSenhaRepository.delete(rs);
			}
		}
		
		recuperacaoSenhaRepository.save(recuperacao);
		
		Email email = new Email();
		email.setFrom("siaf");
		email.setSubject("SiAf - Recuperação de Senha");
		email.setTo(recuperacao.getUsuario().getEmail());
		email.setText("Você solicitou a mudança de senha no sistema SiAf: http://localhost:8080/siaf/recuperacao/" + recuperacao.getCodigo());
		try {
			emailService.sendEmail(email);
		} catch (MessagingException e) {
		}
	}

	@Override
	public RecuperacaoSenha getRecuperacaoByCodigo(String codigo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigo", codigo);
		return recuperacaoSenhaRepository.findFirst(QueryType.JPQL, "from RecuperacaoSenha where codigo = :codigo", params, -1);
	}

}
