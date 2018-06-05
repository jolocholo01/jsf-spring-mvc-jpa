// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.Classe;



@FacesConverter("classeConverter")
public class ClasseConverter extends EntityConverter<Classe> {
	
	public ClasseConverter() {
	super(Classe.class);
	}
}
