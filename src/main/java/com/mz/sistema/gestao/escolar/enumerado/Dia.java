package com.mz.sistema.gestao.escolar.enumerado;

public enum Dia {
		DOM("Domingo"),
		SEG("Segunda-feira"),
		TER("Terça-feira"),
		QUA("Quarta-feira"),
		QUI("Quinta-feira"),
		SEX("Sexta-feira"),
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
