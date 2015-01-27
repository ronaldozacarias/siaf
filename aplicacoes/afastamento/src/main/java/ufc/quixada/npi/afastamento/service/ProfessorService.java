package ufc.quixada.npi.afastamento.service;

import java.util.List;

import ufc.quixada.npi.afastamento.model.Professor;
import br.ufc.quixada.npi.service.GenericService;

public interface ProfessorService extends GenericService<Professor>{
	
	List<Professor> findOrder();
	
	Professor getProfessorByUsuarioId(Long id);

}
