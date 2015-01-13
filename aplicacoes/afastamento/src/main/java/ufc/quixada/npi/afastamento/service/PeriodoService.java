package ufc.quixada.npi.afastamento.service;

import br.ufc.quixada.npi.service.GenericService;
import ufc.quixada.npi.afastamento.model.Periodo;

public interface PeriodoService extends GenericService<Periodo>{
	
	Periodo getPeriodo(Integer ano, Integer semestre);
}
