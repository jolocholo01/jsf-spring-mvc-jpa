package com.mz.sistema.gestao.escolar.servico.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.mz.sistema.gestao.escolar.modelo.RecoperarSenha;
import com.mz.sistema.gestao.escolar.modelo.Usuario;
import com.mz.sistema.gestao.escolar.servico.EnvioEmailServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Service
public class EnvioEmailServicoImpl implements EnvioEmailServico {

	@Override
	// @Scheduled(fixedDelay = 5000)
	public void enviarEmail(String assunto, String texto, List<File> anexos, List<String> destinatarios) {
		try {
			// Create a Properties object to contain connection configuration
			// information.
			Properties props = System.getProperties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.port", 587);
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.auth", "true");

			// Create a Session object to represent a mail session with the
			// specified properties.
			Session session = Session.getDefaultInstance(props);

			// Create a message with the specified information.
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("sigescmz@gmail.com", "Sistema de Gestão Escolar"));
			StringBuilder builder = new StringBuilder();
			builder.append(destinatarios);
			String emails = builder.toString().replace("[", "").replace("]", "");
			System.out.println("E- mail do destinatario:" + destinatarios);
			System.out.println("E- mail:" + emails);
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emails));
			msg.setSubject(assunto);
			msg.setContent(texto, "text/html");

			// Add a configuration set header. Comment or delete the
			// next line if you are not using a configuration set
			msg.setHeader("X-SES-CONFIGURATION-SET", "ConfigSet");

			// Create a transport.
			Transport transport = session.getTransport();

			// Send the message.

			System.out.println("Enviando email..."); // Connect to Amazon SES
														// using the SMTP
														// username and password
														// you specified above.
			transport.connect("smtp.gmail.com", "sigescmz@gmail.com", "sigescmz2016");

			// Send the email.
			transport.sendMessage(msg, msg.getAllRecipients());

			System.out.println("E-mail enviado!");
		} catch (Exception e) {
			Mensagem.mensagemErro("Não conseguiu enviar o e - mail, tente  mais tarde. ");
			e.printStackTrace();
		}

	}

	@Override
	@Async
	public void enviarEmailUsuario(Usuario usuario, RecoperarSenha recoperarSenha, String url) {
		String assunto = "Recoperação da Senha";
		String linkEnviado = url.replace("/senha.html", "/nova/senha.html");
		;

		String texto = pegarHtmlEmail("resources/recoperarSenha.html");
		System.out.println("Templete do Html Enviado : " + linkEnviado);
		String emailFormatoHas = recoperarSenha.getCodigo();
		texto = texto.replace("{usuario.nome}", usuario.getNome());
		texto = texto.replace("{parametro}", recoperarSenha.getParametro());
		texto = texto.replace("{emailFormatoHash}", emailFormatoHas);
		texto = texto.replace("{link}", url);
		texto = texto.replace("{linkEnviado}", linkEnviado);
		try {
			List<String> emails = new ArrayList<>();
			emails.add(usuario.getEmail());
			enviarEmail(assunto, texto, null, emails);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String pegarHtmlEmail(String url) {
		InputStream is = getClass().getResourceAsStream(url);
		BufferedInputStream bis = new BufferedInputStream(is);
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int result;
		try {
			result = bis.read();
			while (result != -1) {
				byte b = (byte) result;
				buf.write(b);
				result = bis.read();
			}
			return buf.toString("UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}
