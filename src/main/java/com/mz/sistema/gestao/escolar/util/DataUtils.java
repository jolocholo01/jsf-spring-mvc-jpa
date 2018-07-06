package com.mz.sistema.gestao.escolar.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DataUtils {
	public static String obterDataFormatoBanco(Date data, String formato) {
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		String dataFormatoBanco = new String();
		if (data == null) {
		} else
			dataFormatoBanco = sdf.format(data);
		return dataFormatoBanco;
	}
	public static void compararDatasHojeDataPrevisao(){
		Date dataAtual = new Date();
		GregorianCalendar gregorianCalendar = new GregorianCalendar(2019, 05, 31);
		Date date=gregorianCalendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatoBanco = new String();
		dataFormatoBanco = sdf.format(date);
		System.out.println("Data:"+ dataFormatoBanco);
		
		if (dataAtual.getTime() >= gregorianCalendar.getTime().getTime()) {
			System.out.println("Ja chegou o dia!");
			throw new BadCredentialsException("Caro usuário, o prazo de licença grátis ja expirou. Por favor, entre em contacto com o programador de seguinte endereço: telefone: +25844973361 e email: agostinhojolocholo64@gmail.com.");
		} else {
			System.out.println("Ainda nao chegou o dia");
		}


	}
	public static boolean criptografarSenha(String password, String passwordEncode) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(password, passwordEncode);

	}
	public static void main(String[] args) {
		System.out.println();
	}
}
