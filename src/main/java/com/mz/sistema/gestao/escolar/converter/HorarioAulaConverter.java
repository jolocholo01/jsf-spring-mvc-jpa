// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.HorarioAula;



@FacesConverter("horarioAulaConverter")
public class HorarioAulaConverter extends EntityConverter<HorarioAula> {
	
	public HorarioAulaConverter() {
	super(HorarioAula.class);
	}
}
