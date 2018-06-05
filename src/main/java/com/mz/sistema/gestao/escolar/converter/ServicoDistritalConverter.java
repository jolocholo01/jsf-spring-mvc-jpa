// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.Distrital;



@FacesConverter("servicoDistritalConverter")
public class ServicoDistritalConverter extends EntityConverter<Distrital> {
	
	public ServicoDistritalConverter() {
	super(Distrital.class);
	}
}
