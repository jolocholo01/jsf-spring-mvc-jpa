package com.mz.sistema.gestao.escolar.servico.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	static final String HTML = String.join(System.getProperty("line.separator"),

			"<div style='background: #f7f9fb;color: rgb(0, 0, 0) !important;  padding-bottom: 2%;padding-top: 2%;'><div style='width: 75%; margin:auto; background: #fff; border-radius: 15px;'>"
					+ "<table border='0' cellpadding='0' cellspacing='0' width='100%'"
					+ " style='border-collapse: collapse; border-spacing: 0; padding: 0; table-layout: auto'>"
					+ " <tbody>" + "  <tr style='padding: 0'><td valign='middle'>"
					+ "<div style='background: rgb(2, 2, 8); padding: 10px; color: white; text-align: center; font-weight: 900; font-size: 36px; border-top-left-radius: 15px; border-top-right-radius: 15px;'>"
					+ " Sistema de Gestão Escolar</div>"
					+ "<div style='font-size: 114%; text-align: justify; padding: 4%;font-family: -webkit-body;'>"
					+ "<p>Caro(a) <b> {usuario.nome} </b></p>"
					+ "Recebeu este email porque pediu para redefinir a sua senha no SIGE. Para proceder com a redefinição da sua senha, clique no link que se segue: <br /><br />"
					+ "<div style='text-align: center;'><a style='color: #00b2b2;text-decoration: none;' href='{linkEnviado}?key={emailFormatoHash}&token={parametro}'>{link}</a></div>"
					+ "<br /> Este link estará disponível por 24 horas. Assim que terminar o periodo, o link ficará desactivado e terá que fazer um novo pedido de redefinição de senha. <br /> <br />"
					+ "Se o link acima não funcionar, copie o endereço abaixo e cole na barra de endereços do seu navegador de internet:<br /><br />"
					+ "<div style='text-align: justify;'>  <a style='color: #00b2b2;text-decoration: none;'"
					+ "href='{linkEnviado}?key={emailFormatoHash}&token={parametro}'>{linkEnviado}?key={emailFormatoHash}&token={parametro}</a>"
					+ "</div> <br /> <br /> <b> NOTA"
					+ "IMPORTANTE:</b> <br /> Tem-se registado muitos casos em que o usuário confunde-se após alterar a sua senha, acabando por tentar"
					+ "introduzir a senha no lugar do usuário. A opção ALTERAR SENHA abrange somente a sua senha, isto é, a única informação que é"
					+ "alterada é a sua senha. O seu usuário é e irá sempre ser o mesmo, sendo que somente a sua senha é que é diferente. Ao entrar no"
					+ "sistema, use o mesmo usuário que sempre usou e a sua nova senha.<p>"
					+ "<b>Aviso: </b> <ul style='text-decoration: none;'>"
					+ "<li>Este email é enviado de forma automática pelo SIGE.</li> <br />"
					+ "<li>Esta conta de e-mail NÃO É MONITORADA, então, por favor, NÃO RESPONDA a este e-mail.</li>"
					+ "</ul></p></div>"
					+ "<div align='center' valign='middle' style='border-collapse: collapse width:100%; !important; padding: 0 15px 35px; word-break: break-word;' >"
					+ "<div align='center' valign='middle'"
					+ "style='border-collapse: collapse !important; border-top-color: #ccc; border-top-style: solid; border-top-width: 1px; padding: 30px 0 20px; word-break: break-word'>"
					+ " Atenciosamente, Equipa de Desenvolvimento de SIGE <br/>  ©&#160;<span> {ano}"
					+ "</span>&#160;&#160;|&#160; SIGE</div></div></td></tr></tbody></table></div>" + " </div>");

	@Override
	// @Scheduled(fixedDelay = 5000)
	public void enviarEmail(String assunto, String texto, List<File> anexos, List<String> destinatarios) {
		try {
			// Create a Properties object to contain connection configuration
			// information.
			//sigescmz@gmail.com sigescmz2016
			//sigesmoz@gmail.com  sigesmoz2018 
			String email="sigesmoz@gmail.com";
			String password="sigesmoz2018";
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
			msg.setFrom(new InternetAddress(email, "SIGE"));
			StringBuilder builder = new StringBuilder();
			builder.append(destinatarios);
			String emails = builder.toString().replace("[", "").replace("]", "");
			// System.out.println("E- mail do destinatario:" + destinatarios);
			// System.out.println("E- mail:" + emails);
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emails));
			msg.setSubject(assunto);
			// msg.setHeader("", "");
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
			transport.connect("smtp.gmail.com", email, password);

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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String ano = sdf.format(new Date());
		String texto = HTML;
		System.out.println("Templete do Html Enviado : " + linkEnviado);
		String emailFormatoHas = recoperarSenha.getCodigo();
		texto = texto.replace("{usuario.nome}", usuario.getNome());
		texto = texto.replace("{parametro}", recoperarSenha.getParametro());
		texto = texto.replace("{emailFormatoHash}", emailFormatoHas);
		texto = texto.replace("{link}", url.replace("http://", "").replace("https://", ""));
		texto = texto.replace("{linkEnviado}", linkEnviado);
		texto = texto.replace("{ano}", ano);
		try {
			List<String> emails = new ArrayList<>();
			emails.add(usuario.getEmail());
			enviarEmail(assunto, texto, null, emails);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
