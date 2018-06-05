// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.enumerado;

public enum Opcao {
	PRIMEIRA("A"), 
	SEGUNDA("B"), 
	TERCEIRA("B COM BIOLOGIA"),
	QUARTA("B COM GEOGRAFIA"),
	QUINTA("C");
	
	private String label;

	private Opcao(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
