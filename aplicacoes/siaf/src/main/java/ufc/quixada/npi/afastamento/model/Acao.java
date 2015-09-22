package ufc.quixada.npi.afastamento.model;

public enum Acao {
	
	CRIACAO("CRIAÇÃO"), EDICAO("EDIÇÃO"), INCLUSAO_RANKING("INCLUSÃO NO RANKING"), CANCELAMENTO("CANCELAMENTO"),
	CANCELAMENTO_COM_PUNICAO("CANCELAMENTO COM PUNIÇÃO"), NEGACAO("NEGAÇÃO"), AFASTAMENTO("AFASTAMENTO"),
	NAO_ACEITACAO("NÃO ACEITAÇÃO"), ENCERRAMENTO("ENCERRAMENTO");
	
	private String descricao;

	private Acao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}
	
	public static Acao getByStatusReserva(StatusReserva status) {
		switch (status) {
			case AFASTADO:
				return AFASTAMENTO;
			case CANCELADO:
				return CANCELAMENTO;
			case CANCELADO_COM_PUNICAO:
				return CANCELAMENTO_COM_PUNICAO;
			case NEGADO:
				return NEGACAO;
			default:
				return null;
		}
	}

}
