package com.mz.sistema.gestao.escolar.enumerado;

public enum TipoArea {
	tronco_comum("TRONCO COMUM");
	private String label;

	private TipoArea(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
