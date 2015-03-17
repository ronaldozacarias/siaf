package ufc.quixada.npi.afastamento.model;

public enum StatusTupla {
	
	ENCERRADO("ENCERRADO"), CLASSIFICADO("CLASSIFICADO"), DESCLASSIFICADO("DESCLASSIFICADO"), ACEITO("ACEITO"), NAO_ACEITO("NÃO ACEITO"), CANCELADO("CANCELADO"), CANCELADO_COM_PUNICAO("CANCELADO COM PUNIÇAO");
	
	private String descricao;

	private StatusTupla(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}

}
