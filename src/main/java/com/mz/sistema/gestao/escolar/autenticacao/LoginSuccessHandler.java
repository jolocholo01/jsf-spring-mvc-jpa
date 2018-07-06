
package com.mz.sistema.gestao.escolar.autenticacao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.mz.sistema.gestao.escolar.bean.BuscarFotoBean;
import com.mz.sistema.gestao.escolar.bean.ProfessorTurmaBean;
import com.mz.sistema.gestao.escolar.modelo.Usuario;
@Named
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	private Integer contador = 0;

	@Autowired
	private ProfessorTurmaBean professorTurmaBean;
	
	@Autowired
	private BuscarFotoBean buscarFotoBean;

	@Autowired
	private AuthenticationContext authenticationContext;

	@SuppressWarnings("unused")
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
			throws IOException {
		Set<String> permissoes = AuthorityUtils.authorityListToSet(auth.getAuthorities());

		// System.out.println("Permissoes: " + permissoes);
		Usuario detais = (Usuario) auth.getPrincipal();
		// System.out.println("Nome do Ususario: " + detais.getNome());

		// TODO-da proxima verificar que o usuario tem uma ou mais permisao!
		// Usuario que tem uma permissao depois de logar no sistema vai
		// diretamente para a pagina especifica. O usuario que tem a mais de uma
		// permissao depois de logar Ele ter� uma pagina ou dialog para
		// selecionar o perfil que tem no sistema, vai ser redirecionado para a
		// pagina padrao.
		buscarFotoBean.atualizarFotoBoolean = false;
		authenticationContext.funcionarioAlocadoAMaisDeUmaEscola = false;
		authenticationContext.funcionarioAlocadoAMaisDeUmaPermisao = false;
		buscarFotoBean.getRetornarFoto();
		authenticationContext.setFuncionariosEscolas(new ArrayList<>());
		authenticationContext.setEscolas(new ArrayList<>());
		buscarFotoBean.setProgramadorBoolean(false);
		
		if (!permissoes.contains("ROLE_ALUNO") && !permissoes.contains("ROLE_DIRECTOR_DISTRITO")) {
			authenticationContext.buscarEscolaFuncionarioLogado();

		}
		contador = 0;
		for (String permissao : permissoes) {
			// if (detais.isAtivo() == true) {
			contador++;
			// }
		}
		Integer contadorEscolas = 0;
		if (!authenticationContext.getEscolas().isEmpty()) {
			contadorEscolas = authenticationContext.getEscolas().size();
		}
		if (contador == 1) {

			if (contadorEscolas > 1) {
				authenticationContext.funcionarioAlocadoAMaisDeUmaEscola = true;
				response.sendRedirect(request.getContextPath() + "/categoria/index.jsf");

			}

			else if (permissoes.contains("ROLE_ALUNO")) {
				response.sendRedirect(request.getContextPath() + "/aluno/index.jsf");
			} else if (permissoes.contains("ROLE_SECRETARIO")) {

				response.sendRedirect(request.getContextPath() + "/secretario/index.jsf");
			} else if (permissoes.contains("ROLE_PROFESSOR")) {
				professorTurmaBean.iniciarBean();
				response.sendRedirect(request.getContextPath() + "/professor/index.jsf");
			} else if (permissoes.contains("ROLE_DIRECTOR_ADJUNTO")) {

				response.sendRedirect(request.getContextPath() + "/director-adjunto/index.jsf");
			} else if (permissoes.contains("ROLE_DIRECTOR")) {
				response.sendRedirect(request.getContextPath() + "/director/index.jsf");
			} else if (permissoes.contains("ROLE_DIRECTOR_DISTRITO")) {
				response.sendRedirect(request.getContextPath() + "/director-ditrital/index.jsf");
			}else if (permissoes.contains("ROLE_PROGRAMADOR")) {
				buscarFotoBean.setProgramadorBoolean(true);
				response.sendRedirect(request.getContextPath() + "/programador/index.jsf");
			}
			System.out.println("Permissoes: " + permissoes);
			System.out.println("Nome do Ususario: " + detais.getNome());
			System.out.println("O contador de Permissao " + contador);
			contador = 0;

		}
		if (contador > 1) {
			if (contadorEscolas > 1) {
				authenticationContext.funcionarioAlocadoAMaisDeUmaEscola = true;
			}else if (contadorEscolas == 1) {
				authenticationContext.funcionarioAlocadoAMaisDeUmaPermisao = true;
			}
			response.sendRedirect(request.getContextPath() + "/categoria/index.jsf");

		}
	}

	public Integer getContador() {
		return contador;
	}

	public void setContador(Integer contador) {
		this.contador = contador;
	}

}
