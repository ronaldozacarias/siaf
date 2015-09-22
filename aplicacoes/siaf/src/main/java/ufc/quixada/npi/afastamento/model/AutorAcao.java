package ufc.quixada.npi.afastamento.model;

public enum AutorAcao {
	
	PROFESSOR("PROFESSOR"), ADMINISTRADOR("ADMINISTRADOR"), SISTEMA("SISTEMA");
	
	private String descricao;

	private AutorAcao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}

}
