package com.mz.sistema.gestao.escolar.servico.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.mz.sistema.gestao.escolar.modelo.RecoperarSenha;
import com.mz.sistema.gestao.escolar.modelo.Usuario;
import com.mz.sistema.gestao.escolar.servico.EnvioEmailServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Service
public class EnvioEmailServicoImpl2 /*implements EnvioEmailServico*/ {

	
//	@Override
	// @Scheduled(fixedDelay = 5000)
	public void enviarEmail(String assunto, String texto, List<File> anexos, String... destinatarios) {
		try {
			JavaMailSenderImpl enviarEmail = new JavaMailSenderImpl();
			enviarEmail.setHost("smtp.gmail.com");
			enviarEmail.setPort(587);
			enviarEmail.setProtocol("smtp");
			enviarEmail.setUsername("sigescmz@gmail.com");
			enviarEmail.setPassword("sigescmz2016");
			enviarEmail.setDefaultEncoding("utf-8");

			Properties properties = new Properties();
			properties.setProperty("username", "sigescmz@gmail.com");
			properties.setProperty("password", "sigescmz2016");
			properties.setProperty("mail.smtp.auth", "true");
			properties.setProperty("mail.smtp.starttls.enable", "true");
			properties.setProperty("mail.transport.protocol", "smtp");
			enviarEmail.setJavaMailProperties(properties);

			MimeMessage msg = enviarEmail.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			helper.setFrom("sigescmz@gmail.com");
			helper.setSubject(assunto);
			helper.setText(texto, true);

			for (String destinatario : destinatarios) {
				helper.addTo(destinatario);
			}
			for (String destinatario : destinatarios) {
				helper.addTo(destinatario);
			}

			if (anexos != null && !anexos.isEmpty()) {
				for (File anexo : anexos) {
					FileSystemResource attachment = new FileSystemResource(anexo);
					helper.addAttachment(anexo.getName(), attachment);
				}
			}
			enviarEmail.send(msg);
			System.out.println("Enviando email...");
		} catch (Exception e) {
			Mensagem.mensagemErro("Não conseguiu enviar o e - mail, tente  mais tarde. ");
			e.printStackTrace();
		}

	}

	//@Override
	@Async
	public void enviarEmailUsuario(Usuario usuario, RecoperarSenha recoperarSenha, String url) {
		String assunto = "Recoperação da Senha";
		String linkEnviado = url.replace("/senha.html", "/nova/senha.html");;
		
		String texto = pegarHtmlEmail("resources/recoperarSenha.html");
		System.out.println("Templete do Html Enviado : " + linkEnviado);
		String emailFormatoHas = recoperarSenha.getCodigo();
		texto = texto.replace("{usuario.nome}", usuario.getNome());
		texto = texto.replace("{parametro}", recoperarSenha.getParametro());
		texto = texto.replace("{emailFormatoHash}", emailFormatoHas);
		texto = texto.replace("{link}", url);
		texto = texto.replace("{linkEnviado}", linkEnviado);
		try {
			enviarEmail(assunto, texto, null, usuario.getEmail());
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
