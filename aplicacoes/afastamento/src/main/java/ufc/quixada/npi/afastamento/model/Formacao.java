package ufc.quixada.npi.afastamento.model;

public enum Formacao {
	
	MESTRADO("MESTRADO"), DOUTORADO("DOUTORADO"), POS_DOUTORADO("PÓS DOUTORADO");
	
	private String descricao;

	private Formacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}

}
