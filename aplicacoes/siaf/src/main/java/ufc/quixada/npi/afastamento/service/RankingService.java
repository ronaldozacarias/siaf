package ufc.quixada.npi.afastamento.service;

import java.util.List;

import ufc.quixada.npi.afastamento.model.Periodo;
import ufc.quixada.npi.afastamento.model.StatusReserva;
import ufc.quixada.npi.afastamento.model.TuplaRanking;


public interface RankingService {

	List<TuplaRanking> visualizarRanking(Periodo periodo, boolean simulador);
	List<TuplaRanking> visualizarRankingByStatusReservaAndPeriodo(List<StatusReserva> status, Periodo periodo);
}
