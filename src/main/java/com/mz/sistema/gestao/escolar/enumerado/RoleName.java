// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.enumerado;

public enum RoleName {
	ROLE_ALUNO("ALUNO"), 
	ROLE_SECRETARIO("SECRETARIO"), 
	ROLE_PROFESSOR("PROFESSOR"),
	ROLE_DIRECTOR_ADJUNTO("DIRECTOR ADJUNTO DA ESCOLA"),
	ROLE_DIRECTOR("DIRECTOR DA ESCOLA"),
	ROLE_DIRECTOR_DISTRITO("DIRECTOR DO  DISTRITO"),
	ROLE_PROGRAMADOR("PROGRAMADOR");
	private String label;

	private RoleName(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
