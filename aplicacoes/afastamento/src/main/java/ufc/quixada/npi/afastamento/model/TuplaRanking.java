package ufc.quixada.npi.afastamento.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class TuplaRanking {
	
	private String professor;
	
	private Integer semestresAtivos;
	
	private Integer semestresAfastados;
	
	private Integer semestresSolicitados;
	
	private Float pontuacao;
	
	private StatusReserva status;
	
	@JsonIgnore
	private Periodo periodo;
	
	private Reserva reserva;

	public StatusReserva getStatus() {
		return status;
	}

	public void setStatus(StatusReserva status) {
		this.status = status;
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

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((periodo == null) ? 0 : periodo.hashCode());
		result = prime * result + ((reserva == null) ? 0 : reserva.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TuplaRanking other = (TuplaRanking) obj;
		if (periodo == null) {
			if (other.periodo != null)
				return false;
		} else if (!periodo.equals(other.periodo))
			return false;
		if (reserva == null) {
			if (other.reserva != null)
				return false;
		} else if (!reserva.equals(other.reserva))
			return false;
		return true;
	}

	
	
	

}
