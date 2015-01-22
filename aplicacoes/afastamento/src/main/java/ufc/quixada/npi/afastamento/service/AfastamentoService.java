package ufc.quixada.npi.afastamento.service;

import java.util.List;

import ufc.quixada.npi.afastamento.model.Afastamento;
import ufc.quixada.npi.afastamento.model.Reserva;

public interface AfastamentoService {
	
	List<Afastamento> getAfastamentosByProfessor(String siape);
	
	List<Afastamento> getAfastamentosAnteriores(Reserva reserva);
	
}
