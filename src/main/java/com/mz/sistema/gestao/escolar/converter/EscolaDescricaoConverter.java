package com.mz.sistema.gestao.escolar.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
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

import com.mz.sistema.gestao.escolar.modelo.Escola;

@FacesConverter("escolaDescricaoConverter")
public class EscolaDescricaoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String valor) {
		if (valor.equals("") || !valor.contains("#")) {
			return null;
		}
		Escola escola = new Escola();

		String[] propriedades = valor.split("#");
		if (!propriedades[0].isEmpty()) {
			escola.setId(new Long(propriedades[0]));
		}
		if (!propriedades[1].isEmpty()) {
			escola.setDescricao(propriedades[1]);
		}

		return escola;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if (obj == null || !(obj instanceof Escola)) {
			return "";
		}

		Escola escola = (Escola) obj;
		long id;
		if (escola.getId() == 0) {
			id = 0;
		} else {
			id = escola.getId();
		}
		String descricao = escola.getDescricao() == null ? "" : escola.getDescricao();
		return id + "#" + descricao;
	}

}
