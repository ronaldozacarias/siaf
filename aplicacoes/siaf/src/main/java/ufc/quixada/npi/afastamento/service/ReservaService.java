package ufc.quixada.npi.afastamento.service;

import java.util.List;

import ufc.quixada.npi.afastamento.model.Acao;
import ufc.quixada.npi.afastamento.model.AutorAcao;
import ufc.quixada.npi.afastamento.model.Historico;
import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Professor;
import ufc.quixada.npi.afastamento.model.Reserva;
import ufc.quixada.npi.afastamento.model.StatusReserva;
import br.ufc.quixada.npi.service.GenericService;

public interface ReservaService extends GenericService<Reserva> {

	void salvar(Reserva reserva);
	
	void atualizar(Reserva reserva);
	
	List<Reserva> getReservasByProfessor(Professor professor);
	
	boolean hasReservaEmAberto(Professor professor);
	
	Reserva getReservaById(Long id);
	
	List<Reserva> getReservasAnterioresComPunicao(Professor professor, Periodo periodo);
	
	List<Reserva> getReservasByStatus(StatusReserva status);

	List<Reserva> getReservasByStatusReservaAndPeriodo(StatusReserva statusReserva, Periodo periodo);
	
	List<Reserva> getAllReservas();
	
	List<Reserva> getReservasByStatusReservaAndProfessor(StatusReserva statusReserva, Professor professor);
	
	void salvarHistorico(Reserva reserva, Acao acao, AutorAcao autor, String comentario);

	Historico getUltimaAcao(Reserva reserva, Acao acao);
}
