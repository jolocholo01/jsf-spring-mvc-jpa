package com.mz.sistema.gestao.escolar.enumerado;

public enum TipoFuncao {
	N1("N1"), N2("N2"), N3("N3"), N4("N4"), AGENTE_ESTADO("Agente do estado");
	private String label;

	private TipoFuncao(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
