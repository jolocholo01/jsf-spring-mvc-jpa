// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.enumerado;

public enum Trimestres {
	PRIMEIRO("1º Trimestre"), SEGUNDO("2º Trimestre"), TERCEIRO("3º Trimestre");
	private String label;

	private Trimestres(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
