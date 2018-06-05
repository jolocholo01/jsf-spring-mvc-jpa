// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.enumerado;

//com.mz.sistema.gestao.escolar.enumerado.TipoClasse
public enum TipoClasse {
	aOITAVA_CLASSE("8ª CLASSE"),
	bNONA_CLASSE("9ª CLASSE"),
	cDECIMA_TCLASSE("10ª CLASSE"),
	dDECIMA_PRIMEIRA("11ª CLASSE"),
	eDECIMA_SEGUNDA("12ª CLASSE"),
	TODAS("Todas");
	private String label;


	private TipoClasse(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	
}
