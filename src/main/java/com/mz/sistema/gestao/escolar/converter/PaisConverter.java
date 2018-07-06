package com.mz.sistema.gestao.escolar.converter;

import javax.faces.convert.FacesConverter;

/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */

import com.mz.sistema.gestao.escolar.modelo.Pais;



@FacesConverter("paisConverter")
public class PaisConverter extends EntityConverter<Pais> {
	
	public PaisConverter() {
	super(Pais.class);
	}
}
