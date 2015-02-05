package ufc.quixada.npi.afastamento.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import ufc.quixada.npi.afastamento.model.Afastamento;
import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Reserva;
import ufc.quixada.npi.afastamento.service.AfastamentoService;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.GenericRepository;

@Named
public class AfastamentoServiceImpl implements AfastamentoService {
	
	@Inject
	private GenericRepository<Afastamento> afastamentoRepository;
	
	@Override
	public List<Afastamento> getAfastamentosByProfessor(Professor professor) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cpf", professor.getCpf());
		return afastamentoRepository.find(QueryType.JPQL, "from Afastamento where reserva.professor.cpf = :cpf", params);
	}


	@Override
	public List<Afastamento> getAfastamentosAnteriores(Reserva reserva) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cpf", reserva.getProfessor().getCpf());
		params.put("anoInicio", reserva.getAnoInicio());
		params.put("semestreInicio", reserva.getSemestreInicio());
		return afastamentoRepository.find(QueryType.JPQL, "from Afastamento where reserva.professor.cpf = :cpf "
				+ "and (reserva.anoInicio < :anoInicio or (reserva.anoInicio = :anoInicio and reserva.semestreInicio < :semestreInicio))", params);
	}

}
