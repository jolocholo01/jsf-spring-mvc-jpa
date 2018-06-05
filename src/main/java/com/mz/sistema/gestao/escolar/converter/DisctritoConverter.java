// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.Distrito;



@FacesConverter("disctritoConverter")
public class DisctritoConverter extends EntityConverter<Distrito> {
	
	public DisctritoConverter() {
	super(Distrito.class);
	}
}
