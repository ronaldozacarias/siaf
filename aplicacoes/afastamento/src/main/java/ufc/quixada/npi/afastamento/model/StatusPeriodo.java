package ufc.quixada.npi.afastamento.model;

public enum StatusPeriodo {
	
	ABERTO("ABERTO"), ENCERRADO("ENCERRADO");
	
	private String descricao;

	private StatusPeriodo(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}

}
