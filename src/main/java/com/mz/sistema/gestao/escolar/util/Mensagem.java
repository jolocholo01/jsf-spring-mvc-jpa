package com.mz.sistema.gestao.escolar.util;

import javax.faces.application.FacesMessage;
/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */
import javax.faces.context.FacesContext;

public class Mensagem {
	public static String ERROSISTEMA = "Caro usuário, o prazo de licença grátis ja expirou. Por favor, entre em contacto com o programador de seguinte endereço, telefone: +25844973361 e email: agostinhojolocholo64@gmail.com!";

	public static void mensagemInfo(String mensagem) {
		try {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, null));
		} catch (Exception e) {
			//System.Ne
		}
	}

	public static void mensagemAlerta(String mensagem) {
		try {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, mensagem, null));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void mensagemErro(String mensagem) {
		try {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void mensagemFatal(String mensagem) {
		try {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, mensagem, null));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
