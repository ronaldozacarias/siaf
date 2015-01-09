package ufc.quixada.npi.afastamento.service;

import java.util.List;

import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Reserva;

public interface ReservaService {

	void salvar(Reserva afastamento);
	
	List<Reserva> getReservasByProfessor(String siape);
	
	List<Reserva> getReservasByPeriodo(Integer ano, Integer semestre);
	
	boolean isPeriodoEncerrado(Integer ano, Integer semestre);
	
	void inserirPeriodo(Integer anoInicio, Integer semestreInicio, Integer anoTermino, Integer semestreTermino);
	
	Integer getSemestreAtual();
	
	Integer getAnoAtual();
	
	boolean hasReservaEmAberto(Professor professor);


}
