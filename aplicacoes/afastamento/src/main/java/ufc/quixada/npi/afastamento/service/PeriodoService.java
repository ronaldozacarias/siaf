package ufc.quixada.npi.afastamento.service;

import java.sql.Date;

import ufc.quixada.npi.afastamento.model.Periodo;
import br.ufc.quixada.npi.service.GenericService;

public interface PeriodoService extends GenericService<Periodo>{
	
	Periodo getPeriodo(Integer ano, Integer semestre);
	
	Periodo getPeriodoByEncerramento(Date encerramento);
	
	Periodo getPeriodoAtual();
	
	Periodo getPeriodoAnterior(Periodo periodo);
	
	Periodo getPeriodoPosterior(Periodo periodo);
	
	Integer getSemestreAtual();
	
	Integer getAnoAtual();

	Periodo getUltimoPeriodoEncerrado();
}
