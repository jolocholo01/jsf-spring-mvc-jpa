package com.mz.sistema.gestao.escolar.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.modelo.RecoperarSenha;
import com.mz.sistema.gestao.escolar.modelo.Usuario;
import com.mz.sistema.gestao.escolar.servico.EnvioEmailServico;
import com.mz.sistema.gestao.escolar.servico.RecoperarSenhaServico;
import com.mz.sistema.gestao.escolar.servico.UsuarioServico;
import com.mz.sistema.gestao.escolar.util.GeradorCodigo;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Named
@SessionScope
@Controller
public class RecoperarSenhaBean implements Serializable {

	private static final long serialVersionUID = -4362038708006109170L;
	private RecoperarSenha recoperarSenha = new RecoperarSenha();
	private boolean emailEnviadoBoleano = false;
	private boolean informacaoBoleano = false;
	@Autowired
	private RecoperarSenhaServico recoperarSenhaServico;
	@Autowired
	private UsuarioServico usuarioServico;

	@Autowired
	private EnvioEmailServico envioEmailServico;

	// O metodo esta ser chamado no pretty config
	public void iniciarBean() {
		recoperarSenha = new RecoperarSenha();
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
		informacaoBoleano = true;
		try {recoperarSenha.setEmail(recoperarSenha.getEmail().toLowerCase().trim());
			Usuario usuario = usuarioServico.obterUsuarioPorEmail(recoperarSenha.getEmail());
			if (usuario == null) {
				Mensagem.mensagemInfo(
						"O e - mail introduzido não existe nos nossos registos! Por favor, faça seu registo no menu dados pessoais.");
				return;
			}
			if (usuario.getRecoperarSenha() == null) {
			} else {
				if (usuario.getRecoperarSenha().getId() != null)
					recoperarSenha.setId(usuario.getRecoperarSenha().getId());
			}

			// http://localhost:8080/sistema-escolar/login
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

			String urlSenha = request.getRequestURL() + "";
			String url = urlSenha.replace("login/", "").replace(".jsf", ".html").replace(".jsp", ".html");

			Date dataHoje = new Date();
			Calendar dataCalculo = Calendar.getInstance();
			dataCalculo.setTime(dataHoje);
			dataCalculo.add(Calendar.DAY_OF_WEEK, 1);

			recoperarSenha.setDataExpiracao(dataCalculo.getTime());
			String emailEmHash = GeradorCodigo
					.criptografarEmailParaRecoperarEmail(recoperarSenha.getEmail());
			recoperarSenha.setCodigo(emailEmHash);
			recoperarSenha.setUsuario(usuario);
			recoperarSenha.setAtivo(true);
			recoperarSenha.setParametro(UUID.randomUUID().toString());
			envioEmailServico.enviarEmailUsuario(usuario, recoperarSenha, url);

			recoperarSenhaServico.salvar(recoperarSenha);
			System.out.println("Ulr de trocar senha : '" + url.replace("/senha.html", "/nova/senha.html") + "?key="
					+ recoperarSenha.getCodigo() + "&token=" + recoperarSenha.getParametro() + "'");
			System.out.println("Id usuario:" + usuario.getId());
			Mensagem.mensagemInfo("Um e-mail foi enviado para " + recoperarSenha.getEmail()
					+ " com instruções para recoperar sua senha.");
			Mensagem.mensagemInfo("Se não receber um e-mail em até 10 minuto, verifique a sua caxa de Lixo/Spam.");
			emailEnviadoBoleano = true;
			recoperarSenha = new RecoperarSenha();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ativarInfo() {
		informacaoBoleano = true;

	}

	public RecoperarSenha getRecoperarSenha() {
		return recoperarSenha;
	}

	public void setRecoperarSenha(RecoperarSenha recoperarSenha) {
		this.recoperarSenha = recoperarSenha;
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
