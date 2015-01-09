package ufc.quixada.npi.afastamento.model;

public class Ranking {
	
	private Professor professor;
	
	private Integer semestresAtivos;
	
	private Integer semestresAfastados;
	
	private Integer semestresSolicitados;
	
	private Float pontuacao;

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public Integer getSemestresAtivos() {
		return semestresAtivos;
	}

	public void setSemestresAtivos(Integer semestresAtivos) {
		this.semestresAtivos = semestresAtivos;
	}

	public Integer getSemestresAfastados() {
		return semestresAfastados;
	}

	public void setSemestresAfastados(Integer semestresAfastados) {
		this.semestresAfastados = semestresAfastados;
	}

	public Integer getSemestresSolicitados() {
		return semestresSolicitados;
	}

	public void setSemestresSolicitados(Integer semestresSolicitados) {
		this.semestresSolicitados = semestresSolicitados;
	}

	public Float getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(Float pontuacao) {
		this.pontuacao = pontuacao;
	}
	
	

}
