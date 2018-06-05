// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.Turno;

@FacesConverter("turnoConverter")
public class TurnoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String valor) {
		if (valor.equals("") || !valor.contains("#")) {
			return null;
		}
		Turno turno = new Turno();

		String[] propriedades = valor.split("#");
		if (!propriedades[0].isEmpty()) {
			turno.setId(new Long(propriedades[0]));
		}
		if (!propriedades[1].isEmpty()) {
			turno.setCurso(propriedades[1]);
		}

		return turno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if (obj == null || !(obj instanceof Turno)) {
			return "";
		}

		Turno turno = (Turno) obj;
		long id;
		if (turno.getId() == 0) {
			id = 0;
		} else {
			id = turno.getId();
		}
		String curso = turno.getCurso() == null ? "" : turno.getCurso();

		return id + "#" + curso;
	}

}
