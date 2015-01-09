package ufc.quixada.npi.afastamento.model;

public enum Programa {
	
	MESTRADO("MESTRADO"), DOUTORADO("DOUTORADO"), POS_DOUTORADO("PÓS DOUTORADO");
	
	private String descricao;

	private Programa(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}

}
