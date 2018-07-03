// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.Permissao;
import com.mz.sistema.gestao.escolar.modelo.Permissao;

@FacesConverter("permissaoConverter")
public class PermissaoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String valor) {
		if (valor.equals("") || !valor.contains("#")) {
			return null;
		}
		Permissao permissao = new Permissao();

		String[] propriedades = valor.split("#");
		if (!propriedades[0].isEmpty()) {
			permissao.setId(new Long(propriedades[0]));
		}
		if (!propriedades[1].isEmpty()) {
			permissao.setNome(propriedades[1]);
		}

		return permissao;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if (obj == null || !(obj instanceof Permissao)) {
			return "";
		}

		Permissao permissao = (Permissao) obj;
		long id;
		if (permissao.getId() == 0) {
			id = 0;
		} else {
			id = permissao.getId();
		}
		String ciclo = permissao.getNome()== null ? "" : permissao.getNome();

		return id + "#" + ciclo;
	}

}
