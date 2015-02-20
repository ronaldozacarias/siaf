package ufc.quixada.npi.afastamento.service;

import java.util.List;

import br.ufc.quixada.npi.service.GenericService;
import ufc.quixada.npi.afastamento.model.Afastamento;
import ufc.quixada.npi.afastamento.model.Reserva;

public interface AfastamentoService extends GenericService<Afastamento> {
	
	List<Afastamento> getAfastamentosAnteriores(Reserva reserva);
	
	Afastamento getByReserva(Reserva reserva);
	
}
