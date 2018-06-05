// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.Turma;

@FacesConverter("turmaInscrictoConverter")
public class TurmaInscrictoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String valor) {
		if (valor.equals("") || !valor.contains("#")) {
			return null;
		}
		Turma turma = new Turma();

		String[] propriedades = valor.split("#");
		if (!propriedades[0].isEmpty()) {
			turma.setId(new Integer(propriedades[0]));
		}
		if (!propriedades[1].isEmpty()) {
			turma.setInscrito(new Integer(propriedades[1]));
		}

		return turma;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if (obj == null || !(obj instanceof Turma)) {
			return "";
		}

		Turma turma = (Turma) obj;
		Integer id;
		if (turma.getId() == 0) {
			id = 0;
		} else {
			id = turma.getId();
		}
		Integer inscrito;
		if (turma.getInscrito() == 0) {
			inscrito = 0;
		} else {
			inscrito = turma.getInscrito();
		}

		return id + "#" + inscrito;
	}

}
