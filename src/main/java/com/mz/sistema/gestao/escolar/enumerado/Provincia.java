// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.enumerado;

public enum Provincia {	
	CABO_DELEGADO("Cabo Delegado"),
	GAZA("Gaza"),
	INHAMBANE("Inhambane"),
	MANICA("Manica"),	
	MAPUTO("Maputo"),
	NAMPULA("Nampula"),
	NIASSA("Niassa"),
	SOFALA("Sofala"),
	TETE("Tete"),
	ZAMBEZIA("Zambézia");

	
	private String label;
	
	private Provincia(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
