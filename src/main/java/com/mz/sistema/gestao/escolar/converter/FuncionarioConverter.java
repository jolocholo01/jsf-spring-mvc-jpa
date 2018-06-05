// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import javax.faces.convert.FacesConverter;

import com.mz.sistema.gestao.escolar.modelo.Funcionario;



@FacesConverter("funcionarioConverter")
public class FuncionarioConverter extends EntityConverter<Funcionario> {
	
	public FuncionarioConverter() {
	super(Funcionario.class);
	}
}
