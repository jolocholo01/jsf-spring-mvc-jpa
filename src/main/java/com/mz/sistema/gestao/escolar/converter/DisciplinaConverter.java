// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.Disciplina;

@FacesConverter("disciplinaConverter")
public class DisciplinaConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String valor) {
		if (valor.equals("") || !valor.contains("#")) {
			return null;
		}
		Disciplina disciplina = new Disciplina();

		String[] propriedades = valor.split("#");
		if (!propriedades[0].isEmpty()) {
			disciplina.setId(new Integer(propriedades[0]));
		}
		if (!propriedades[1].isEmpty()) {
			disciplina.setDescricao(propriedades[1]);
		}

		return disciplina;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if (obj == null || !(obj instanceof Disciplina)) {
			return "";
		}

		Disciplina disciplina = (Disciplina) obj;
		long id;
		if (disciplina.getId() == 0) {
			id = 0;
		} else {
			id = disciplina.getId();
		}
		String descricao = disciplina.getDescricao() == null ? "" : disciplina.getDescricao();

		return id + "#" + descricao;
	}

}
