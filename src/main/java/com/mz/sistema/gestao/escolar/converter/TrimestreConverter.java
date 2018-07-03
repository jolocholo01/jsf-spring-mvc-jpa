package com.mz.sistema.gestao.escolar.converter;

import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.Trimestre;

@FacesConverter("trimestreConverter")
public class TrimestreConverter extends EntityConverter<Trimestre> {

	public TrimestreConverter() {
		super(Trimestre.class);
	}
}
