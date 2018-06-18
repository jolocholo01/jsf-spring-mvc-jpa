package com.mz.sistema.gestao.escolar.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Teste {

	public static void main(String args[]) throws ParseException {
		
		String url="/sistema-escolar/academico/director/index.jsf";
		String vals[] = url.split("/academico/");
		String link=vals[1];
		String vals2[] = link.split("/");
		String primeiro=vals2[0];
		
		System.out.println("Link : "+primeiro);
		
		String nom1 = "Agostinho";
		String nom2 = "Macoo";
		List<String> nomes = new ArrayList<>();
		nomes.add(nom2);
		nomes.add(nom1);
		StringBuilder builder = new StringBuilder();
		builder.append(nomes);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String dataFormatoBanco =sdf.format(new Date());
		System.out.println("Senha: "+criptografarSenha("rafael1993"));
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
