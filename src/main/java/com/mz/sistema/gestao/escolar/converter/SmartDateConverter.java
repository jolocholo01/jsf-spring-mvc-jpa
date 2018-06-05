// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;


public class SmartDateConverter implements Converter {
	private static final DateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");

	private Calendar getDataAtual() {
		Calendar dataAtual = new GregorianCalendar();
		// limpamos informa��es de hora, minuto, segundo
		// e milisegundos
		dataAtual.set(Calendar.HOUR_OF_DAY, 0);
		dataAtual.set(Calendar.MINUTE, 0);
		dataAtual.set(Calendar.SECOND, 0);
		dataAtual.set(Calendar.MILLISECOND, 0);
		return dataAtual;
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null || value.equals("")) {
			return null;
		}
		Date dataConvertida = null;

		value.replace("ddMMyyy", "dd/MM/yyyy");
		dataConvertida = getDataAtual().getTime();

		try {
			dataConvertida = formatador.parse(value);
		} catch (ParseException e) {
			throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data incorreta.",
					"Favor informar uma data correta."));
		}

		return dataConvertida;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		Date data = (Date) value;
		return formatador.format(data);
	}
}