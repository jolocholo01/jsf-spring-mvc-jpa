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

import com.mz.sistema.gestao.escolar.modelo.Horario;



@FacesConverter("horarioConverter")
public class HorarioConverter extends EntityConverter<Horario> {
	
	public HorarioConverter() {
	super(Horario.class);
	}
}
