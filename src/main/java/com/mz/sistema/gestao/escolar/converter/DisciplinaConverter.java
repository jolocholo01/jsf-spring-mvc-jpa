// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.Disciplina;



@FacesConverter("disciplinaConverter")
public class DisciplinaConverter extends EntityConverter<Disciplina> {
	
	public DisciplinaConverter() {
	super(Disciplina.class);
	}
}
