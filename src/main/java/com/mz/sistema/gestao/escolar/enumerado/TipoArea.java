package com.mz.sistema.gestao.escolar.enumerado;
/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */
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
