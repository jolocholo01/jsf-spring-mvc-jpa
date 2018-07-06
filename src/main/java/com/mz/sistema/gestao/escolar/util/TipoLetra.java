package com.mz.sistema.gestao.escolar.util;
/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */
public class TipoLetra {
	
	public static void main(String[] args) {
	
		System.out.println("Nome:" +capitalizeString("agostinho da bartolomeu jolocholo"));
	}
	
	public static String capitalizeString(String texto) {
		try {
			if(texto !=null){
				  char[] chars = texto.toLowerCase().toCharArray();
				  boolean found = false;
				  for (int i = 0; i < chars.length; i++) {
				    if (!found && Character.isLetter(chars[i])) {
				      chars[i] = Character.toUpperCase(chars[i]);
				      found = true;
				    } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
				      found = false;
				    }
				  }
				  return String.valueOf(chars);
				}
		} catch (Exception e) {
			
		}
		return "";
		}
}
