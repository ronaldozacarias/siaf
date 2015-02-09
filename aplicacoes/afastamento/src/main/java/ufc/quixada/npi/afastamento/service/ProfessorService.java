package ufc.quixada.npi.afastamento.service;

import java.util.List;

import ufc.quixada.npi.afastamento.model.Professor;
import br.ufc.quixada.npi.service.GenericService;

public interface ProfessorService extends GenericService<Professor>{
	
	List<Professor> findAtivos();
	
	List<Professor> findAll();
	
	Professor getByCpf(String cpf);

}
