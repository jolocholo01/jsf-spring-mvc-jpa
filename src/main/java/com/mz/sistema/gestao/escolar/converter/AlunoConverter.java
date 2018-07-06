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

import com.mz.sistema.gestao.escolar.modelo.Aluno;




@FacesConverter("alunoConverter")
public class AlunoConverter extends EntityConverter<Aluno> {
	
	public AlunoConverter() {
	super(Aluno.class);
	}
}
