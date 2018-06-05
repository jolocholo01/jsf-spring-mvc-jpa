// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.Escola;



@FacesConverter("escolaConverter")
public class EscolaConverter extends EntityConverter<Escola> {
	
	public EscolaConverter() {
	super(Escola.class);
	}
}
