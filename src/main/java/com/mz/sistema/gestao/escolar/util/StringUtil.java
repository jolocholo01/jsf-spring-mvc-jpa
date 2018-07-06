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
public class StringUtil {
	public static String preencherZerosAEsquerda(String valor, int quantidade){
		int zerosNecessarios = quantidade - valor.length();
		for(int i = 0; i < zerosNecessarios; i++){
			valor = "0" + valor;
		}
		
		return valor;
	}
	public static void main(String[] args) {
		System.out.println(""+preencherZerosAEsquerda("98", 10));
	}
}
