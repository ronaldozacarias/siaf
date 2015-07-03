package ufc.quixada.npi.afastamento.service;

import java.util.Map;

import ufc.quixada.npi.afastamento.model.Periodo;

public interface RelatorioService {
	
	public Map<String, Integer> getRelatorioReservasByPeriodo(Periodo inicio, Periodo termino);

}
