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
	private static Integer year = 2040;
	private static Integer month = 4;
	private static Integer date = 28;
	// NB: O mes em java inicia com zero e depois termina com onzem

	public static String obterDataFormatoBanco(Date data, String formato) {
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		String dataFormatoBanco = new String();
		if (data == null) {
		} else
			dataFormatoBanco = sdf.format(data);
		return dataFormatoBanco;
	}

	public static void vericicarSenhaUsuarioSelecionado(String senhaFornecida) {
// 		Date dataAtual = new Date();
// 		GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, date);
// 		if (dataAtual.getTime() >= gregorianCalendar.getTime().getTime()) {

// 			throw new BadCredentialsException(Mensagem.ERROSISTEMA);
// 		}

	}

	public static boolean criptografarSenha(String password, String passwordEncode) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(password, passwordEncode);

	}

}
