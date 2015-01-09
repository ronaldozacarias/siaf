package ufc.quixada.npi.afastamento.service;

import java.util.List;

import ufc.quixada.npi.afastamento.model.Ranking;


public interface RankingService {

	List<Ranking> gerarRanking(Integer ano, Integer semestre);


}
