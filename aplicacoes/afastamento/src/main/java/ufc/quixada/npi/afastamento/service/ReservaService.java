package ufc.quixada.npi.afastamento.service;

import java.util.List;

import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Reserva;

public interface ReservaService {

	void salvar(Reserva afastamento);
	
	List<Reserva> getReservasByProfessor(String siape);
	
	List<Reserva> getReservasByPeriodo(Integer ano, Integer semestre);
	
	boolean hasReservaEmAberto(Professor professor);


}
