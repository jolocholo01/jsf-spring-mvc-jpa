// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.bean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.modelo.Calendario;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.modelo.Permissao;
import com.mz.sistema.gestao.escolar.servico.CalendarioServico;
import com.mz.sistema.gestao.escolar.servico.FuncionarioServico;
import com.mz.sistema.gestao.escolar.servico.UsuarioServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Controller
@Named
@SessionScope
public class ProfessorBean {
	private Funcionario professor = new Funcionario();
	private List<Funcionario> professores = new ArrayList<>();
	private Calendario calendario = new Calendario();
	private List<Calendario> calendarios = new ArrayList<>();
	private String ano;

	@Autowired
	private FuncionarioServico professorServico;
	@Autowired
	private CalendarioServico calendarioServico;
	@Autowired
	private UsuarioServico usuarioServico;
	@Autowired
	private AuthenticationContext authenticationContext;
//	@Autowired
//	private GeradorDeRelatoriosServico imprimirServico;

	public void salvar() {
		Permissao permissao = usuarioServico.obterPermicaoPolaDescricao("ROLE_PROFESSOR");
		if (permissao == null) {
			permissao = new Permissao();
			permissao.setNome("ROLE_PROFESSOR");
		}
		professor.getPermissoes().add(permissao);
		professor.setAtivo(true);
		professorServico.salvar(professor);
		Mensagem.mensagemInfo("Professor salvo com sucesso!");
		professor = new Funcionario();
	}

	public void imprimirHorario() throws SQLException {
//		try {
//			//String caminho = Faces.getRealPath("/academico/aluno/relatorio/aluno.jasper");
//			Map<String, Object> parametro = new HashMap<>();
//
//			String relatorio = imprimirServico.imprimirRelatorio(caminho, parametro);
//			//JasperPrintManager.printReport(relatorio, false);
//			// JasperPrintManager.printPages(relatorio, 1, 0, true);
//		} catch (JRException e) {
//			e.printStackTrace();
//		}

	}

	public String consultarCalendadrioEscolar() {
		try {
			Calendario calendario = authenticationContext.getCalendarioEscolar();
			this.ano = calendario.getAno()+"";
			this.calendarios = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/academico/professor/calendario?faces-redirect=true";
	}

	public void buncarCalendarioEscolar() {
		if (this.ano == null) {
			Mensagem.mensagemInfo("Aviso: digite o ano Curricular que deseja pesquisar!");
		}else if (this.ano != null)
		this.calendarios = calendarioServico.obterCalendarioPorPesquisa(ano);
	}

	public Funcionario getProfessor() {
		return professor;
	}

	public void setProfessor(Funcionario professor) {
		this.professor = professor;
	}

	public List<Funcionario> getProfessores() {
		return professores;
	}

	public void setProfessores(List<Funcionario> professores) {
		this.professores = professores;
	}

	

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public Calendario getCalendario() {
		return calendario;
	}

	public void setCalendario(Calendario calendario) {
		this.calendario = calendario;
	}

	public List<Calendario> getCalendarios() {
		return calendarios;
	}

	public void setCalendarios(List<Calendario> calendarios) {
		this.calendarios = calendarios;
	}

}
