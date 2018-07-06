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
public class Replace {
	public static String rescreverTexto(String texto) {
		String texto_escrito = TipoLetra.capitalizeString(texto).replace(" Em ", " em ").replace(" De ", " de ").replace(" Do ", " do ")
				.replace(" Da ", " da ").replace(" E ", " e ").replace(" À ", " à ").replace(" Das ", " das ").replace("FISICA", "FÍSICA").replace("PORTUGUES", "PORTUGUÊS")
				.replace("INGLES", "INGLÊS").replace("INTRODUCAO A FILOSOFIA", "INTRODUÇÃO À FILOSOFIA")
				.replace("MATEMATICA", "MATEMÁTICA").replace("EDUCACAO FISICA", "EDUCAÇÃO FÍSICA")
				.replace("HISTORIA", "HISTÓRIA").replace("FRANCES", "FRANCÊS").replace("QUIMICA", "QUÍMICA")
				.replace("EDUCACAO VISUAL", "EDUCAÇÃO VISUAL").replace("ARTES CENICAS", "ARTES CÉNICAS")
				.replace("AGRO-PECUARIA", "AGRO-PECUÁRIA").replace("CAO", "ÇÃO");
		return texto_escrito;
	}
	public static String letrasMasculas(String texto) {
		String texto_escrito =texto.toUpperCase().replace(" Em ", " em ").replace(" De ", " de ").replace(" Do ", " do ")
				.replace(" Da ", " da ").replace(" E ", " e ").replace(" À ", " à ").replace(" Das ", " das ").replace("FISICA", "FÍSICA").replace("PORTUGUES", "PORTUGUÊS")
				.replace("INGLES", "INGLÊS").replace("INTRODUCAO A FILOSOFIA", "INTRODUÇÃO À FILOSOFIA")
				.replace("MATEMATICA", "MATEMÁTICA").replace("EDUCACAO FISICA", "EDUCAÇÃO FÍSICA")
				.replace("HISTORIA", "HISTÓRIA").replace("FRANCES", "FRANCÊS").replace("QUIMICA", "QUÍMICA").replace("QUI", "QUÍ")
				.replace("EDUCACAO VISUAL", "EDUCAÇÃO VISUAL").replace("ARTES CENICAS", "ARTES CÉNICAS")
				.replace("AGRO-PECUARIA", "AGRO-PECUÁRIA").replace("CAO", "ÇÃO").replace("FI", "FÍ");
		return texto_escrito;
	}
	public static String escola(String texto) {
		String texto_escrito = TipoLetra.capitalizeString(texto).replace(" De ", " de ").replace(" Do ", " do ")
				.replace(" Da ", " da ").replace(" E ", " e ").replace(" À ", " à ").replace(" Das ", " das ").replace(" Dos ", " dos ");
		return texto_escrito;
	}
public static void main(String[] args) {
	System.out.println(rescreverTexto("Agro-pecuária"));
}
}
