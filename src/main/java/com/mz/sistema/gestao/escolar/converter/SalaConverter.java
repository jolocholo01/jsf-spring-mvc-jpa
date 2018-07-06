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

import com.mz.sistema.gestao.escolar.modelo.Sala;



@FacesConverter("salaConverter")
public class SalaConverter extends EntityConverter<Sala> {
	
	public SalaConverter() {
	super(Sala.class);
	}
}
