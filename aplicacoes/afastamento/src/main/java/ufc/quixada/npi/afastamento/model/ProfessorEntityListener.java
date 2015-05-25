package ufc.quixada.npi.afastamento.model;

import java.util.Date;

import javax.persistence.PostLoad;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import ufc.quixada.npi.afastamento.util.Constants;
import br.ufc.quixada.npi.ldap.model.Affiliation;
import br.ufc.quixada.npi.ldap.model.Usuario;
import br.ufc.quixada.npi.ldap.service.UsuarioService;

public class ProfessorEntityListener implements ApplicationContextAware {
	
	private static ApplicationContext context;
	
	@PostLoad
	@Cacheable("loadProfessor")
	public void loadProfessor(Professor professor) {
		UsuarioService usuarioService = (UsuarioService) context.getBean(UsuarioService.class);
		Usuario usuario = usuarioService.getByCpf(professor.getCpf());
		professor.setNome(usuario.getNome());
		professor.setEmail(usuario.getEmail());
		professor.setDataNascimento(usuario.getNascimento());
		professor.setSiape(usuario.getSiape());
	
		Date admissao = null;
		Date saida = null;
		for(Affiliation affiliation : usuario.getAffiliations()) {
			if(Constants.AFFILIATION_DOCENTE.equals(affiliation.getNome())) {
				admissao = affiliation.getDataEntrada();
				saida = affiliation.getDataSaida();
			}
		}
		professor.setDataAdmissao(admissao);
		professor.setDataSaida(saida);
	}
	
	public ApplicationContext getApplicationContext() {
        return context;
    }
 
    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        context = ctx;
    }

}
