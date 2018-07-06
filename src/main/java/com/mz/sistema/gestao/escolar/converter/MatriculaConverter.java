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

import com.mz.sistema.gestao.escolar.modelo.Matricula;



@FacesConverter("matriculaConverter")
public class MatriculaConverter extends EntityConverter<Matricula> {
	
	public MatriculaConverter() {
	super(Matricula.class);
	}
}
