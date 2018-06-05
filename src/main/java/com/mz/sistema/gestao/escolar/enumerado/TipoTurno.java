// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.enumerado;

public enum TipoTurno {
	MANHÃ("Manhã"), TARDE("Tarde"), NOITE("Noite");
	private String label;

	private TipoTurno(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
