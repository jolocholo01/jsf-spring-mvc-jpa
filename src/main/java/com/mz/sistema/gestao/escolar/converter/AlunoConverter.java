// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.Aluno;




@FacesConverter("alunoConverter")
public class AlunoConverter extends EntityConverter<Aluno> {
	
	public AlunoConverter() {
	super(Aluno.class);
	}
}
