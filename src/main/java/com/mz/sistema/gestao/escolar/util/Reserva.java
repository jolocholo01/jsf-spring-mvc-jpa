package com.mz.sistema.gestao.escolar.util;

import java.io.Serializable;
import java.util.Date;

public class Reserva implements Serializable {

	private static final long serialVersionUID = -1933067875408679149L;


	private Date dataPrevisao;
	private Date horaprevista;
	
	public Reserva() {

	}

	
	public Reserva(Date dataPrevisao, Date horaprevista) {
		this.dataPrevisao = dataPrevisao;
		this.horaprevista = horaprevista;
	}


	public Date getDataPrevisao() {
		return dataPrevisao;
	}


	public void setDataPrevisao(Date dataPrevisao) {
		this.dataPrevisao = dataPrevisao;
	}


	public Date getHoraprevista() {
		return horaprevista;
	}


	public void setHoraprevista(Date horaprevista) {
		this.horaprevista = horaprevista;
	}

	

}
