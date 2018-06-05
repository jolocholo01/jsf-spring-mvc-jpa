package com.mz.sistema.gestao.escolar.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Teste {

	public static void main(String args[]) throws ParseException {
		System.out.println(criptografarSenha("4"));
		String nom1 = "Agostinho";
		String nom2 = "Macoo";
		List<String> nomes = new ArrayList<>();
		nomes.add(nom2);
		nomes.add(nom1);
		StringBuilder builder = new StringBuilder();
		builder.append(nomes);
		
		System.out.println(builder.toString().replace("[", "").replace("]", ""));

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
