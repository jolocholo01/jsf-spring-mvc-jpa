package com.mz.sistema.gestao.escolar.converter;

import java.util.Date;

/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.Aluno;
import com.mz.sistema.gestao.escolar.modelo.Matricula;

@FacesConverter("matriculaPickListConverter")
public class MatriculaPickListConverter implements Converter {


	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String valor) {
		if (valor.equals("") || !valor.contains("#")) {
			return null;
		}
		Matricula matricula = new Matricula();

		String[] propriedades = valor.split("#");
		// Date dates=Date valor;
		if (!propriedades[0].isEmpty()) {
			matricula.setId(new Integer(propriedades[0]));
		}
		
		if (!propriedades[1].isEmpty()) {
			Aluno aluno = new Aluno();
			   aluno.setNome(propriedades[1]);
			matricula.setAluno(aluno);
		}

		return matricula;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if (obj == null || !(obj instanceof Matricula)) {
			return "";
		}

		Matricula matricula = (Matricula) obj;

		String id = matricula.getId() == null ? "" : matricula.getId().toString();
		Date dataNascimento = matricula.getAluno() == null || matricula.getAluno().getDataNascimento() == null ? null
				: matricula.getAluno().getDataNascimento();
		String nomeAluno = matricula.getAluno() == null || matricula.getAluno().getNome() == null ? ""
				: matricula.getAluno().getNome();

		return id + "#" +  dataNascimento +" - "+nomeAluno;
	}

}
