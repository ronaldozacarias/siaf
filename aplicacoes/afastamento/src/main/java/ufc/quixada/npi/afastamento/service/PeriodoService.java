package ufc.quixada.npi.afastamento.service;

import ufc.quixada.npi.afastamento.model.Periodo;

public interface PeriodoService {
	
	Periodo getPeriodo(Integer ano, Integer semestre);
}
