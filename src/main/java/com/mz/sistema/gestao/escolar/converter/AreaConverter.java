// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.Area;



@FacesConverter("areaConverter")
public class AreaConverter extends EntityConverter<Area> {
	
	public AreaConverter() {
	super(Area.class);
	}
}
