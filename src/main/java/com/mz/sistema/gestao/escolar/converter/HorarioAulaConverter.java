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

import com.mz.sistema.gestao.escolar.modelo.HorarioAula;



@FacesConverter("horarioAulaConverter")
public class HorarioAulaConverter extends EntityConverter<HorarioAula> {
	
	public HorarioAulaConverter() {
	super(HorarioAula.class);
	}
}
