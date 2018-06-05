package com.mz.sistema.gestao.escolar.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Mensagem {
	public static void mensagemInfo(String mensagem) {
		try {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, null));
		} catch (Exception e) {
			// TODO: handle exception
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
