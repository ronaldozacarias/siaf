package ufc.quixada.npi.afastamento.model;

public enum StatusReserva {
	
	ABERTO("ABERTO"), ENCERRADO("ENCERRADO");
	
	private String descricao;

	private StatusReserva(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}

}
