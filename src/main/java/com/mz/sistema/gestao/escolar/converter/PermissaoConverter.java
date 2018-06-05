// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.Permissao;



@FacesConverter("permissaoConverter")
public class PermissaoConverter extends EntityConverter<Permissao> {
	
	public PermissaoConverter() {
	super(Permissao.class);
	}
}
