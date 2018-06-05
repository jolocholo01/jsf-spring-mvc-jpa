package com.mz.sistema.gestao.escolar.converter;

import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.Turma;

@FacesConverter("turmaConverter")
public class TurmaConverter extends EntityConverter<Turma> {

	public TurmaConverter() {
		super(Turma.class);
	}
}
