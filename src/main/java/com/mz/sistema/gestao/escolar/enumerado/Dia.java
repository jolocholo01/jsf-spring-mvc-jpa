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
public enum Dia {
		DOM("Domingo"),
		SEG("Segunda"),
		TER("Terça"),
		QUA("Quarta"),
		QUI("Quinta"),
		SEX("Sexta"),
		SAB("Sábado");
	
	private String label;
	
	private Dia(String label){
		this.label=label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
