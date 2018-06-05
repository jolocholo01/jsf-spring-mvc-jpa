// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.DisciplinaClasse;



@FacesConverter("disciplinaClasseConverter")
public class DisciplinaClasseConverter extends EntityConverter<DisciplinaClasse> {
	
	public DisciplinaClasseConverter() {
	super(DisciplinaClasse.class);
	}
}
