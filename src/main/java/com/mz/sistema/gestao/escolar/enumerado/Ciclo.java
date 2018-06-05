// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.enumerado;

public enum Ciclo {
	PRIMEIRO_CICLO("1º CICLO"), 
	SEGUNDO_CICLO("2º CICLO"),
	TODOS("Todos");
	private String label;

	private Ciclo(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
