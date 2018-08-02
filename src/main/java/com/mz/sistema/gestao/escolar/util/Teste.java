package com.mz.sistema.gestao.escolar.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Teste {

	public static void main(String args[]) throws ParseException {
		Integer anoInicio, anoFim;
		Integer IdadeInicial = 12, IdadeFinal = 16;
		Date data = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String dataFormatado = sdf.format(data);
		Integer anoAtual = Integer.valueOf(dataFormatado);
		anoInicio = anoAtual - IdadeInicial;
		anoFim = anoAtual - IdadeFinal;

		System.out.println("Joao: Tem "+IdadeInicial+" nasceu no ano: "+anoInicio);
		System.out.println("Joaninha: Tem "+IdadeFinal+" nasceu no ano: "+anoFim);
		
		
		String nom1 = "Agostinho";
		String nom2 = "Macoo";
		List<String> nomes = new ArrayList<>();
		nomes.add(nom2);
		nomes.add(nom1);
		StringBuilder builder = new StringBuilder();
		builder.append(nomes);
		System.out.println("Nomes: " + builder.toString());

		System.out.println("Senha: " + criptografarSenha("001.6046.2018"));
	}

	public static boolean criptografarSenha(String password, String passwordEncode) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(password, passwordEncode);

	}

	public static String criptografarSenha(String password) {
		// BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodePassword = encoder.encode(password);
		return encodePassword;

	}

}
