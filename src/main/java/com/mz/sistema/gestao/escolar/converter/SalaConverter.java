// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.Sala;



@FacesConverter("salaConverter")
public class SalaConverter extends EntityConverter<Sala> {
	
	public SalaConverter() {
	super(Sala.class);
	}
}
