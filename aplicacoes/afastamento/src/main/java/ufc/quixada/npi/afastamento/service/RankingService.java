package ufc.quixada.npi.afastamento.service;

import java.util.List;

import ufc.quixada.npi.afastamento.model.Ranking;


public interface RankingService {

	List<Ranking> visualizarRanking(Integer ano, Integer semestre);


}
