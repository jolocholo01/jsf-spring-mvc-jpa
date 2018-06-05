// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.enumerado;

public enum TipoDocumento {
	BILHETE("Bilhete de Identidade"), CEDULA("C�dula Pessoal"), PASSAPORTE("Passa Porte");
	private String label;

	private TipoDocumento(String label) {
		this.setLabel(label);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
