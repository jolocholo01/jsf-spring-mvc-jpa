package com.mz.sistema.gestao.escolar.enumerado;

public enum EstadoTransferencia {
	TRANSFERENCIA("Transferência"), 
	CANCELADO("Cancelado"),
	EXCLUIDO("Excluido"),
	TRANSFERIDO("Transferido"), 
	NAO_PERMIDODO("Não pemitido"),
	FINALIZAR("Finalizado"),
	EM_ANALIZE("Em análise");
	private String label;

	private EstadoTransferencia(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
