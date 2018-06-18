// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.autenticacao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.mz.sistema.gestao.escolar.modelo.Aluno;
import com.mz.sistema.gestao.escolar.modelo.Calendario;
import com.mz.sistema.gestao.escolar.modelo.Distrital;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.modelo.Matricula;
import com.mz.sistema.gestao.escolar.modelo.ProfessorTurma;
import com.mz.sistema.gestao.escolar.modelo.Usuario;
import com.mz.sistema.gestao.escolar.servico.CalendarioServico;
import com.mz.sistema.gestao.escolar.servico.EscolaServico;
import com.mz.sistema.gestao.escolar.servico.FuncionarioEscolaServico;
import com.mz.sistema.gestao.escolar.servico.FuncionarioServico;
import com.mz.sistema.gestao.escolar.servico.MatriculaServico;
import com.mz.sistema.gestao.escolar.servico.ProfessorTurmaServico;

@Named
@Component("authenticationContext")
public class AuthenticationContext {

	private Matricula matriculaEscolaLogada;
	private Funcionario funcionarioLogado;
	private FuncionarioEscola funcionarioEscola;
	private List<Escola> escolas;
	private Escola escola;
	private FuncionarioEscola funcionarioEscolaLogada;
	private List<FuncionarioEscola> funcionariosEscolas;
	private Distrital funcionarioDirecaoDistritalLogado;
	public boolean funcionarioAlocadoAMaisDeUmaEscola = false;
	public boolean funcionarioAlocadoAMaisDeUmaPermisao = false;
	private Integer quantidadeCategorias = 0;
	private Integer quantidadeEscolasFunciobario = 0;

	private List<ProfessorTurma> professorTurmas = new ArrayList<ProfessorTurma>();
	@Autowired
	private EscolaServico escolaServico;

	@Autowired
	private MatriculaServico matriculaServico;

	@Autowired
	private CalendarioServico calendarioServico;

	@Autowired
	private ProfessorTurmaServico professorTurmaServico;
	@Autowired
	private FuncionarioEscolaServico funcionarioEscolaServico;
	@Autowired
	private FuncionarioServico funcionarioServico;

	public Usuario getUsuarioLogado() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();

		return (Usuario) authentication.getPrincipal();

	}

	public void buscarEscolaFuncionarioLogado() {
		try {
			SecurityContext context = SecurityContextHolder.getContext();
			Authentication authentication = context.getAuthentication();

			Usuario usuario = (Usuario) authentication.getPrincipal();
			funcionarioLogado = funcionarioServico.obterFuncionarioPorIdPorPermissoes(usuario.getId());

			escolas = new ArrayList<>();
			if (funcionarioLogado != null)
				escolas = escolaServico.obterEscolasSemRepiticaoPorIFuncionario(funcionarioLogado.getId());

			// funcionariosEscolas =
			// escolaServico.obterEscolasPorIFuncionario(funcionarioLogado.getId());
			quantidadeCategorias = 0;
			if (!escolas.isEmpty()) {
				if (escolas.size() == 1) {
					funcionariosEscolas = funcionarioEscolaServico.obterFuncionarioEscolaPorIdEscolaPorIdFuncionario(
							escolas.get(0).getId(), funcionarioLogado.getId());
					if (!funcionariosEscolas.isEmpty()) {
						funcionarioEscolaLogada = funcionariosEscolas.get(0);
						quantidadeCategorias = funcionariosEscolas.size();
					}

				} else if (escolas.size() > 1) {
					quantidadeCategorias = escolas.size();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void alterarVinculo() {
		try {
			SecurityContext context = SecurityContextHolder.getContext();
			Authentication authentication = context.getAuthentication();
			Usuario usuario = (Usuario) authentication.getPrincipal();
			funcionarioLogado = funcionarioServico.obterFuncionarioPorIdPorPermissoes(usuario.getId());
			escolas = new ArrayList<>();
			if (funcionarioLogado != null)
				escolas = escolaServico.obterEscolasSemRepiticaoPorIFuncionario(funcionarioLogado.getId());
			escola = new Escola();
			funcionarioAlocadoAMaisDeUmaEscola = false;
			funcionarioAlocadoAMaisDeUmaPermisao = false;
			if (!escolas.isEmpty()) {
				if (escolas.size() == 1) {
					funcionarioAlocadoAMaisDeUmaPermisao = true;
				} else if (escolas.size() > 1) {
					funcionarioAlocadoAMaisDeUmaEscola = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selecaoEscola() {
		try {
			SecurityContext context = SecurityContextHolder.getContext();
			Authentication authentication = context.getAuthentication();

			quantidadeEscolasFunciobario = 0;
			Usuario usuario = (Usuario) authentication.getPrincipal();
			funcionarioLogado = funcionarioServico.obterFuncionarioPorIdPorPermissoes(usuario.getId());
			if(funcionarioLogado !=null)
			funcionariosEscolas = funcionarioEscolaServico
					.obterFuncionarioEscolaPorIdEscolaPorIdFuncionario(escola.getId(), funcionarioLogado.getId());
			if (!funcionariosEscolas.isEmpty()) {

				System.out.println("Quantidade de escolas:" + funcionariosEscolas.size());
				quantidadeEscolasFunciobario = funcionariosEscolas.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String autenticacaoPorPermissao() {
		try {
			SecurityContext context = SecurityContextHolder.getContext();
			Authentication authentication = context.getAuthentication();
			quantidadeEscolasFunciobario = 0;
			Usuario usuario = (Usuario) authentication.getPrincipal();
			funcionarioLogado = funcionarioServico.obterFuncionarioPorIdPorPermissoes(usuario.getId());

			funcionarioAlocadoAMaisDeUmaEscola = false;
			if(funcionarioLogado !=null)
			funcionariosEscolas = funcionarioEscolaServico
					.obterFuncionarioEscolaPorIdEscolaPorIdFuncionario(escola.getId(), funcionarioLogado.getId());
			if (!funcionariosEscolas.isEmpty()) {

				System.out.println("Quantidade de escolas:" + funcionariosEscolas.size());
				quantidadeEscolasFunciobario = funcionariosEscolas.size();
				if (funcionariosEscolas.size() == 1) {
					quantidadeCategorias = escolas.size();
					funcionarioEscolaLogada = funcionariosEscolas.get(0);

					if (funcionarioEscolaLogada.getPermissao().getDescricao().toString().toString()
							.contains("ROLE_SECRETARIO")) {
						return "/academico/secretario/index?faces-redirect=true";
					} else if (funcionarioEscolaLogada.getPermissao().getDescricao().toString()
							.contains("ROLE_PROFESSOR")) {
						return "/academico/professor/index?faces-redirect=true";
					} else if (funcionarioEscolaLogada.getPermissao().getDescricao().toString()
							.contains("ROLE_DIRECTOR_ADJUNTO")) {
						return "/academico/adjunto/index?faces-redirect=true";
					} else if (funcionarioEscolaLogada.getPermissao().getDescricao().toString()
							.contains("ROLE_DIRECTOR")) {
						return "/academico/director/index?faces-redirect=true";
					}
				} else if (funcionariosEscolas.size() > 1) {
					funcionarioAlocadoAMaisDeUmaPermisao = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String autenticar() {
		try {
			if (!funcionariosEscolas.isEmpty()) {

				if (funcionariosEscolas.size() > 1) {
					funcionarioEscolaLogada = funcionarioEscolaServico
							.obterFuncionarioEscolaPorId(funcionarioEscola.getId());
					funcionarioAlocadoAMaisDeUmaPermisao = true;
				}
			}

			if (funcionarioEscolaLogada.getPermissao().getDescricao().toString().contains("ROLE_SECRETARIO")) {
				return "/academico/secretario/index?faces-redirect=true";
			} else if (funcionarioEscolaLogada.getPermissao().getDescricao().toString().contains("ROLE_PROFESSOR")) {
				return "/academico/professor/index?faces-redirect=true";
			} else if (funcionarioEscolaLogada.getPermissao().getDescricao().toString()
					.contains("ROLE_DIRECTOR_ADJUNTO")) {
				return "/academico/adjunto/index?faces-redirect=true";
			} else if (funcionarioEscolaLogada.getPermissao().getDescricao().toString().contains("ROLE_DIRECTOR")) {
				return "/academico/director/index?faces-redirect=true";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void buscarFuncionarioLogado() {
		try {
			SecurityContext context = SecurityContextHolder.getContext();
			Authentication authentication = context.getAuthentication();

			Usuario usuario = (Usuario) authentication.getPrincipal();
			funcionarioLogado = funcionarioServico.obterFuncionarioPorIdPorPermissoes(usuario.getId());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscarUsuarioDirecaoDistritalLogado() {
		try {
			SecurityContext context = SecurityContextHolder.getContext();
			Authentication authentication = context.getAuthentication();
			Usuario usuario = (Usuario) authentication.getPrincipal();
			funcionarioLogado = funcionarioServico.obterFuncionarioPorIdPorPermissoes(usuario.getId());
			if (funcionarioLogado == null) {

			} else
				funcionarioDirecaoDistritalLogado = funcionarioLogado.getDirecaoDistrital();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscarMatriculaEscolaLogada() {
		try {
			this.matriculaEscolaLogada = null;
			SecurityContext context = SecurityContextHolder.getContext();
			Authentication authentication = context.getAuthentication();

			Aluno aluno = (Aluno) authentication.getPrincipal();
			Calendario calendarioEscolar = getCalendarioEscolar();
			if (calendarioEscolar != null)
				this.matriculaEscolaLogada = matriculaServico.obterMatriculaPorIdAlunoPorAno(aluno.getId(),
						calendarioEscolar.getAno());
			if (this.matriculaEscolaLogada == null) {
				List<Matricula> matriculas = matriculaServico.obterMatriculasPorIdAluno(aluno.getId());
				for (Matricula matricula : matriculas) {
					this.matriculaEscolaLogada = matricula;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscarDisciplinasLecionadosPorProfessoresDaTurma() {
		this.professorTurmas = new ArrayList<>();
		try {

			if (this.matriculaEscolaLogada == null) {

			} else if (this.matriculaEscolaLogada.getTurma() == null) {

			} else if (this.matriculaEscolaLogada.getTurma() != null) {
				Calendario calendarioEscolar = getCalendarioEscolar();
				if (calendarioEscolar != null)
					this.professorTurmas = professorTurmaServico.obterProfessorTurmaPorTurmaOrdenarPorIdDisciplina(
							this.matriculaEscolaLogada.getTurma().getId());
				System.out.println("Ano Escolar:" + calendarioEscolar.getAno());
				if (this.professorTurmas == null) {
					this.professorTurmas = new ArrayList<>();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Calendario getCalendarioEscolar() {
		try {
			Calendario calendarioEscolar = calendarioServico.obterCalendarioVigente();
			return calendarioEscolar;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Calendario();
	}

	public void logout()  {
		try {
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			((HttpSession) context.getSession(false)).invalidate();

			context.redirect(
					context.encodeResourceURL(((HttpServletRequest) context.getRequest()).getContextPath() + "/login.jsp"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Matricula getMatriculaEscolaLogada() {
		return matriculaEscolaLogada;
	}

	public void setMatriculaEscolaLogada(Matricula matriculaEscolaLogada) {
		this.matriculaEscolaLogada = matriculaEscolaLogada;
	}

	public List<ProfessorTurma> getProfessorTurmas() {
		return professorTurmas;
	}

	public void setProfessorTurmas(List<ProfessorTurma> professorTurmas) {
		this.professorTurmas = professorTurmas;
	}

	public boolean isFuncionarioAlocadoAMaisDeUmaEscola() {
		return funcionarioAlocadoAMaisDeUmaEscola;
	}

	public void setFuncionarioAlocadoAMaisDeUmaEscola(boolean funcionarioAlocadoAMaisDeUmaEscola) {
		this.funcionarioAlocadoAMaisDeUmaEscola = funcionarioAlocadoAMaisDeUmaEscola;
	}

	public FuncionarioEscola getFuncionarioEscolaLogada() {
		return funcionarioEscolaLogada;
	}

	public void setFuncionarioEscolaLogada(FuncionarioEscola funcionarioEscolaLogada) {
		this.funcionarioEscolaLogada = funcionarioEscolaLogada;
	}

	public Funcionario getFuncionarioLogado() {
		return funcionarioLogado;
	}

	public void setFuncionarioLogado(Funcionario funcionarioLogado) {
		this.funcionarioLogado = funcionarioLogado;
	}

	public Distrital getFuncionarioDirecaoDistritalLogado() {
		return funcionarioDirecaoDistritalLogado;
	}

	public void setFuncionarioDirecaoDistritalLogado(Distrital funcionarioDirecaoDistritalLogado) {
		this.funcionarioDirecaoDistritalLogado = funcionarioDirecaoDistritalLogado;
	}

	public Integer getQuantidadeCategorias() {
		return quantidadeCategorias;
	}

	public void setQuantidadeCategorias(Integer quantidadeCategorias) {
		this.quantidadeCategorias = quantidadeCategorias;
	}

	public List<FuncionarioEscola> getFuncionariosEscolas() {
		return funcionariosEscolas;
	}

	public void setFuncionariosEscolas(List<FuncionarioEscola> funcionariosEscolas) {
		this.funcionariosEscolas = funcionariosEscolas;
	}

	public FuncionarioEscola getFuncionarioEscola() {
		return funcionarioEscola;
	}

	public void setFuncionarioEscola(FuncionarioEscola funcionarioEscola) {
		this.funcionarioEscola = funcionarioEscola;
	}

	public boolean isFuncionarioAlocadoAMaisDeUmaPermisao() {
		return funcionarioAlocadoAMaisDeUmaPermisao;
	}

	public void setFuncionarioAlocadoAMaisDeUmaPermisao(boolean funcionarioAlocadoAMaisDeUmaPermisao) {
		this.funcionarioAlocadoAMaisDeUmaPermisao = funcionarioAlocadoAMaisDeUmaPermisao;
	}

	public List<Escola> getEscolas() {
		return escolas;
	}

	public void setEscolas(List<Escola> escolas) {
		this.escolas = escolas;
	}

	public Escola getEscola() {
		return escola;
	}

	public void setEscola(Escola escola) {
		this.escola = escola;
	}

	public Integer getQuantidadeEscolasFunciobario() {
		return quantidadeEscolasFunciobario;
	}

	public void setQuantidadeEscolasFunciobario(Integer quantidadeEscolasFunciobario) {
		this.quantidadeEscolasFunciobario = quantidadeEscolasFunciobario;
	}

}
