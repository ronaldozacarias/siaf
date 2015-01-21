package ufc.quixada.npi.afastamento.service;

import java.util.List;

import ufc.quixada.npi.afastamento.model.Afastamento;
import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.Reserva;

public interface AfastamentoService {
	
	List<Afastamento> getAfastamentosByProfessor(String siape);
	
	List<Afastamento> getAfastamentosAnteriores(Reserva reserva);
	
	Periodo getPeriodoByAnoSemestre(Integer ano, Integer semestre);
	
	Periodo getPeriodoAtual();
	
	Integer getSemestreAtual();
	
	Integer getAnoAtual();
	
	Periodo getPeriodoAnterior(Periodo periodo);
	
	Periodo getPeriodoPosterior(Periodo periodo);
	
	boolean isPeriodoEncerrado(Periodo periodo);
	
	void inserirPeriodo(Integer anoInicio, Integer semestreInicio, Integer anoTermino, Integer semestreTermino);
	

}
