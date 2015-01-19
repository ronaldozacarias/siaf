package ufc.quixada.npi.afastamento.service;

import ufc.quixada.npi.afastamento.model.Periodo;
import br.ufc.quixada.npi.service.GenericService;

public interface PeriodoService extends GenericService<Periodo>{
	
	Periodo getPeriodo(Integer ano, Integer semestre);

}
