package ufc.quixada.npi.afastamento.model;

import java.util.List;

public class Ranking {
	
	private Periodo periodo;
	
	private List<TuplaRanking> tuplas;

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	public List<TuplaRanking> getTuplas() {
		return tuplas;
	}

	public void setTuplas(List<TuplaRanking> tuplas) {
		this.tuplas = tuplas;
	}

}
