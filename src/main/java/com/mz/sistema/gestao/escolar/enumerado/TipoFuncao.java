package com.mz.sistema.gestao.escolar.enumerado;

public enum TipoFuncao {
	N1("Docente N1"), N2("Docente N2"), N3("Docente N3"), N4("Docente N4"), AGENTE_ESTADO("Agente do estado");
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
