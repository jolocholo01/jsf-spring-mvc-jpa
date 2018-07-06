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

import com.mz.sistema.gestao.escolar.modelo.Classe;

@FacesConverter("classeCicloConverter")
public class ClasseCicloConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext context, 
			UIComponent component, String valor) {
		if(valor.equals("") || !valor.contains("#")){
			return null;
		}
				Classe classe = new Classe();
		
		String[] propriedades = valor.split("#");
		if(!propriedades[0].isEmpty()){
			classe.setId(new Long(propriedades[0]));
		}
		if(!propriedades[1].isEmpty()){
			classe.setCiclo(propriedades[1]);
		}
		
		return classe;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if(obj == null || !(obj instanceof Classe)){
			return "";
		}
		
		Classe classe = (Classe) obj;
		long id ;
		if(classe.getId() == 0){
		 id =  0;
		}else{
			id= classe.getId();
		}
		String ciclo = classe.getCiclo() == null ? "" : classe.getCiclo();
		
		return id + "#" + ciclo;
	}

}
