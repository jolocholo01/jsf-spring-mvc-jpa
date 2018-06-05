// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.Matricula;



@FacesConverter("matriculaConverter")
public class MatriculaConverter extends EntityConverter<Matricula> {
	
	public MatriculaConverter() {
	super(Matricula.class);
	}
}
