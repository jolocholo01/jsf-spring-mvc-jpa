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

import com.mz.sistema.gestao.escolar.modelo.Turma;

@FacesConverter("turmaConverter")
public class TurmaConverter extends EntityConverter<Turma> {

	public TurmaConverter() {
		super(Turma.class);
	}
}
