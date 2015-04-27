package ufc.quixada.npi.afastamento.model;

public enum StatusReserva {
	
	ABERTO("ABERTO"), ENCERRADO("ENCERRADO"), AFASTADO("AFASTADO"), NAO_ACEITO("NÃO ACEITO"), 
	CANCELADO("CANCELADO"), CANCELADO_COM_PUNICAO("CANCELADO COM PUNIÇÃO"), NEGADO("NEGADO");
	
	private String descricao;

	private StatusReserva(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}

}
