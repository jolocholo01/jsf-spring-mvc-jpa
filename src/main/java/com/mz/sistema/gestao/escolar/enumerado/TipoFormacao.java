// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.enumerado;

public enum TipoFormacao {
	BASICO(""),
	MEDIO1(""),
	MEDIO2(""),
	MEDIO3(""),
	SUPRIOR1("");
	
	private String label;
	private TipoFormacao(String label) {
		this.label=label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
