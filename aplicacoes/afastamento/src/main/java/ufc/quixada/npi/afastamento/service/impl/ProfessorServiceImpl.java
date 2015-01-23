












package ufc.quixada.npi.afastamento.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.service.ProfessorService;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.GenericRepository;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class ProfessorServiceImpl extends GenericServiceImpl<Professor> implements ProfessorService {

	@Inject
	private GenericRepository<Professor> professorRepository;
	
	@Override
	public List<Professor> findOrder() {
		return professorRepository.find(QueryType.JPQL, "select p from Professor p order by p.usuario.nome", null);
	}

}
