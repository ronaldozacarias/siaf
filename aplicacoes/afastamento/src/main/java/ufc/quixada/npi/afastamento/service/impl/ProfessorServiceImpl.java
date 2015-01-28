package ufc.quixada.npi.afastamento.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		return professorRepository.find(QueryType.JPQL, "select p from Professor p where p.usuario.habilitado = TRUE order by p.usuario.nome", null);
	}

	@Override
	public Professor getProfessorByUsuarioId(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ano", id);
		return professorRepository.findFirst(QueryType.JPQL, "select p from Professor p where p.usuario.id = :id", params, -1);
	}

	@Override
	public Integer getTotalProfessores() {
		return professorRepository.find(QueryType.JPQL, "from Professor p where p.usuario.habilitado = TRUE", null).size();
	}

}
