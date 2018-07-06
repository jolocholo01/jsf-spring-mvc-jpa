package com.mz.sistema.gestao.escolar.modelo;

import java.io.Serializable;
/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class Endereco implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 3562967193761898823L;
	private Distrito distrito;

	@ManyToOne
	public Distrito getDistrito() {
		return distrito;
	}

	public void setDistrito(Distrito distrito) {
		this.distrito = distrito;
	}

}
