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

import com.mz.sistema.gestao.escolar.modelo.Classe;



@FacesConverter("classeConverter")
public class ClasseConverter extends EntityConverter<Classe> {
	
	public ClasseConverter() {
	super(Classe.class);
	}
}
