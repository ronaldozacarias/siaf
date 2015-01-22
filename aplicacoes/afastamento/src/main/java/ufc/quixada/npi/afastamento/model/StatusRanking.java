package ufc.quixada.npi.afastamento.model;

public enum StatusRanking {
	
	ABERTO("ABERTO"), ENCERRADO("ENCERRADO"), ACEITO("ACEITO"), NAO_ACEITO("NÃO ACEITO"), CLASSIFICADO("CLASSIFICADO"), DESCLASSIFICADO("DESCLASSIFICADO");
	
	private String descricao;

	private StatusRanking(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}

}
