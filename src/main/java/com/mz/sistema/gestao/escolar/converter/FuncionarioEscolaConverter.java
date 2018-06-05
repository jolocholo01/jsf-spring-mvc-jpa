// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;



@FacesConverter("funcionarioEscolaConverter")
public class FuncionarioEscolaConverter extends EntityConverter<FuncionarioEscola> {
	
	public FuncionarioEscolaConverter() {
	super(FuncionarioEscola.class);
	}
}
