package ufc.quixada.npi.afastamento.service;

import java.util.List;

import br.ufc.quixada.npi.service.GenericService;
import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Reserva;

public interface ReservaService extends GenericService<Reserva> {

	void salvar(Reserva afastamento);
	
	List<Reserva> getReservasByProfessor(Professor professor);
	
	List<Reserva> getReservasByPeriodo(Integer ano, Integer semestre);
	
	boolean hasReservaEmAberto(Professor professor);
	
	Reserva getReservaById(Long id);
	
	List<Reserva> getReservasAnterioresComPunicao(Professor professor, Periodo periodo);

}
