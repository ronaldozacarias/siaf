package ufc.quixada.npi.afastamento.service;

import java.util.List;

import ufc.quixada.npi.afastamento.model.Afastamento;

public interface AfastamentoService {
	
	List<Afastamento> getAfastamentosByProfessor(String siape);

}
