package com.mz.sistema.gestao.escolar.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.modelo.RecuperarSenha;
import com.mz.sistema.gestao.escolar.modelo.RecuperarSenha;
import com.mz.sistema.gestao.escolar.modelo.Usuario;
import com.mz.sistema.gestao.escolar.servico.EnvioEmailServico;
import com.mz.sistema.gestao.escolar.servico.RecuperarSenhaServico;
import com.mz.sistema.gestao.escolar.servico.UsuarioServico;
import com.mz.sistema.gestao.escolar.util.GeradorCodigo;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Named
@SessionScope
@Controller
public class RecuperarSenhaBean implements Serializable {

	private static final long serialVersionUID = -4362038708006109170L;
	private RecuperarSenha recuperarSenha = new RecuperarSenha();
	private boolean emailEnviadoBoleano = false;
	private boolean informacaoBoleano = false;
	@Autowired
	private RecuperarSenhaServico recuperarSenhaServico;
	@Autowired
	private UsuarioServico usuarioServico;

	@Autowired
	private EnvioEmailServico envioEmailServico;

	// O metodo esta ser chamado no pretty config
	public void iniciarBean() {
		recuperarSenha = new RecuperarSenha();
		emailEnviadoBoleano = false;
		informacaoBoleano = false;

	}

	// O metodo esta ser chamado no pretty config
	public void limparCacheNovaSenhaBean() {
		System.out.println("Chamou o metodo sair");
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		((HttpSession) context.getSession(false)).invalidate();

	}

	public void salvar() {
		// HttpURLConnection httpURLConnection = null;
		// URL url = new URL("");
		// httpURLConnection = url.openConnection();
		// if (httpURLConnection.getResponseCode() == httpURLConnection.HTTP_OK)
		// {
		// // tem internate
		// }

		informacaoBoleano = true;
		try {
			recuperarSenha.setEmail(recuperarSenha.getEmail().toLowerCase().trim());
			Usuario usuario = usuarioServico.obterUsuarioPorEmail(recuperarSenha.getEmail());
			if (usuario == null) {
				Mensagem.mensagemInfo(
						"O e-mail introduzido não existe nos nossos registos! Por favor, faça seu registo no menu dados pessoais.");
				return;
			}
			if (usuario.getEnganoNoEnvioEmail() == null) {

			} else if (usuario.getEnganoNoEnvioEmail() == true) {
				Mensagem.mensagemInfo(
						"Não é possivel enviar o email para este endereço porque não pertence a nenhuma conta de email "
								+ "ou o usuário da conta pediu para que não recebesse emails via o nosso sistema! ");
				return;
			}
			if (usuario.getRecuperarSenha() == null) {
			} else {
				if (usuario.getRecuperarSenha().getId() != null)
					recuperarSenha.setId(usuario.getRecuperarSenha().getId());
			}

			// http://localhost:8080/sistema-escolar/login
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
// http://...
			String urlSenha = request.getRequestURL() + "";
			String url = urlSenha.replace("login/", "").replace(".jsf", ".html").replace(".jsp", ".html");

			Date dataHoje = new Date();
			Calendar dataCalculo = Calendar.getInstance();
			dataCalculo.setTime(dataHoje);
			dataCalculo.add(Calendar.DAY_OF_WEEK, 1);

			recuperarSenha.setDataExpiracao(dataCalculo.getTime());
			String emailEmHash = GeradorCodigo.criptografarEmailParaRecoperarEmail(recuperarSenha.getEmail());
			recuperarSenha.setCodigo(emailEmHash);
			recuperarSenha.setUsuario(usuario);
			recuperarSenha.setAtivo(true);
			recuperarSenha.setParametro(UUID.randomUUID().toString());
			envioEmailServico.enviarEmailUsuario(usuario, recuperarSenha, url);
			// if(enviou==true){
			recuperarSenhaServico.salvar(recuperarSenha);
			System.out.println("Url completo : '" + url.replace("/senha.html", "/nova/senha.html") + "?key="
					+ recuperarSenha.getCodigo() + "&token=" + recuperarSenha.getParametro() + "'");
			
			System.out.println("Id usuario:" + usuario.getId());
			Mensagem.mensagemInfo("Um e-mail foi enviado para " + recuperarSenha.getEmail()
					+ " com instruções para recuperar sua senha.");
			Mensagem.mensagemInfo("Se não receber um e-mail em até 10 minuto, verifique a sua caxa de Lixo/Spam.");

			emailEnviadoBoleano = true;
			recuperarSenha = new RecuperarSenha();
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.mensagemInfo("Não estamos a conseguir restabelecer a conexaco, por favor tente mais tarde. ");
		}
	}

	public void ativarInfo() {
		informacaoBoleano = true;

	}

	
	public RecuperarSenha getRecuperarSenha() {
		return recuperarSenha;
	}

	public void setRecuperarSenha(RecuperarSenha recuperarSenha) {
		this.recuperarSenha = recuperarSenha;
	}

	public boolean isEmailEnviadoBoleano() {
		return emailEnviadoBoleano;
	}

	public void setEmailEnviadoBoleano(boolean emailEnviadoBoleano) {
		this.emailEnviadoBoleano = emailEnviadoBoleano;
	}

	public boolean isInformacaoBoleano() {
		return informacaoBoleano;
	}

	public void setInformacaoBoleano(boolean informacaoBoleano) {
		this.informacaoBoleano = informacaoBoleano;
	}

}
