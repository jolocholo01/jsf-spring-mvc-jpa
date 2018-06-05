// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.enumerado;

public enum Tempo {
	PRIMEIRO("1ª Aula"),
	SEGUNDO("2ª Aula"),
	TERCEIRO("3ª Aula"),
	QUARTO("4ª Aula"),
	QUINTO("5ª Aula"),
	SEXTO("6ª Aula");
	
	private String label;
	
	private Tempo(String label) {
		this.label=label;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
}
