package com.mz.sistema.gestao.escolar.util;

import java.util.Date;
/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

public class GeradorCodigo {

	private static String caracteresSenha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	

	public static void main(String[] args) {
		System.out.println("Codigo: " + gerarCodigoAleatorioSemRepeticao(100L));
		
	}

	public static String gerarCodigoAleatorio(int tamanhoSenha) {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < tamanhoSenha; i++) {
			builder.append(caracteresSenha.charAt((int) (Math.random() * 36)));
		}

		return builder.toString();
	}

	 public static String gerarCodigoAleatorioSemRepeticao(Long idDistrito) {
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String ano = sdf.format(new Date());
        String distrito = null;
        int[] num = new int[2];
        geraCodigo(num, builder);
        while (builder.toString().length() < 4) {
            builder = new StringBuilder();
            geraCodigo(num, builder);
        }
        if (idDistrito < 10) {
            distrito = "00" + idDistrito;
        } else if (idDistrito < 100 && idDistrito >= 10) {
            distrito = "0" + idDistrito;
        } else if (idDistrito >= 100) {
            distrito = "" + idDistrito;
        }
        return distrito + "." + builder.toString() + "." + ano;
    }

    public static void geraCodigo(int[] num, StringBuilder builder) {
    	int numero;
         Random r = new Random();
        for (int i = 0; i < num.length; i++) {
            numero = r.nextInt(60) + 1;
            geraRandom(num, numero, i, r);
        }
        for (int i = 0; i < num.length; i++) {
            builder.append(num[i]);
        }
    }

    public static void geraRandom(int[] num, int numero, int i, Random r) {
        for (int j = 0; j < num.length; j++) {
            if (numero == num[j] && j != i) {
                numero = r.nextInt(60) + 1;
            } else {
                num[i] = numero;
            }
        }
    }
    public static String criptografarEmailParaRecoperarEmail(String email) {
		try {
			MessageDigest algoritmo = MessageDigest.getInstance("SHA-256");
			byte messageDigest[] = algoritmo.digest(email.getBytes("UTF-8"));
			StringBuilder hexString = new StringBuilder();
			for (byte b : messageDigest) {
				hexString.append(String.format("%02X", 0xFF & b));
			}
			return hexString.toString().toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
