package ufc.quixada.npi.afastamento.model;

import java.util.Date;

import javax.persistence.PostLoad;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.ufc.quixada.npi.ldap.model.Affiliation;
import br.ufc.quixada.npi.ldap.model.Constants;
import br.ufc.quixada.npi.ldap.model.Usuario;
import br.ufc.quixada.npi.ldap.service.UsuarioService;

public class ProfessorEntityListener {
	
	@PostLoad
	@Cacheable("loadProfessor")
	public void loadProfessor(Professor professor) {
		@SuppressWarnings("resource")
		BeanFactory context = new ClassPathXmlApplicationContext("applicationContext.xml");
		UsuarioService usuarioService = (UsuarioService) context.getBean(UsuarioService.class);
		Usuario usuario = usuarioService.getByCpf(Constants.BASE_USUARIOS, professor.getCpf());
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

}
