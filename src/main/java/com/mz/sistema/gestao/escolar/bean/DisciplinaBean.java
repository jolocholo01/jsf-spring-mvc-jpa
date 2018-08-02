package com.mz.sistema.gestao.escolar.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.modelo.Disciplina;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.servico.DisciplinaServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;
import com.mz.sistema.gestao.escolar.util.Replace;

@Named
@Controller
@SessionScope
public class DisciplinaBean implements Serializable {

	private static final long serialVersionUID = -1205249789398248234L;
	private Disciplina disciplina;
	private Disciplina disciplinaSelecionada;
	private Disciplina disciplinaExclusao;
	private List<Disciplina> disciplinas = new ArrayList<>();

	private boolean cadastroDisciplinaBoolean = false;
	private boolean novaDisciplinaBoolean = false;
	private boolean editarDisciplinaBoolean = false;
	private int quantidadeCaracteres;

	private Integer qtdDisciplinasEncontradas = 0;
	private String pesquisa = new String();

	@Autowired
	private DisciplinaServico disciplinaServico;

	@Autowired
	private AuthenticationContext authenticacao;

	public void salvar() {
		try {
			Disciplina disciplinaExistente = disciplinaServico.disciplinaExisente(disciplina.getDescricao());
			if (disciplinaExistente != null && disciplina.getId() != disciplinaExistente.getId()) {
				Mensagem.mensagemInfo("AVISO: já existe esta disciplina cadastrada no sistema.");
				return;
			}
			Funcionario funcionario = authenticacao.getFuncionarioLogado();
			if (disciplina.getId() == null) {
				if (funcionario != null)
					disciplina.setFuncCadastro(funcionario);
				disciplina = disciplinaServico.salvarRetornar(disciplina);
				disciplina.setCodigo(setarCodigoDisciplina(disciplina.getDescricao(), disciplina.getId()));

				disciplinaServico.salvar(disciplina);
				Mensagem.mensagemInfo("AVISO: disciplina cadastrada com sucesso!");
				disciplina.setDataCadastro(new Date());
				disciplina = new Disciplina();
				this.quantidadeCaracteres = 0;
			} else if (disciplina.getId() != null) {
				if (funcionario != null)
					disciplina.setFuncAlteracao(funcionario);
				disciplina.setCodigo(setarCodigoDisciplina(disciplina.getDescricao(), disciplina.getId()));
				disciplina.setDataAlteracao(new Date());
				disciplinaServico.salvar(disciplina);
				voltarParaPequisa();
				Mensagem.mensagemInfo("AVISO: disciplina atualizada com sucesso!");

			}

		} catch (Exception e) {
			Mensagem.mensagemErro("ERRO: houve um erro ao cadastrar disciplina!");
		}
	}

	private String setarCodigoDisciplina(String disciplina1, Long id) {
		disciplina1 = disciplina1.replace("    ", "").replace("   ", "").replace("  ", "").replace(" ", "");
		String disciplina = disciplina1.substring(0, 3).toUpperCase();

		if (disciplina.equals("ED.")) {
			disciplina = disciplina1.substring(0, 4).toUpperCase();
		}
		if (disciplina.equals("ED. ")) {
			disciplina = disciplina1.substring(0, 5).toUpperCase();
		}
		if (id < 100 && id > 9) {
			disciplina = disciplina + "0" + id;
		} else if (id < 9) {
			disciplina = disciplina + "00" + id;
		} else {
			disciplina = disciplina + "" + id;
		}
		return disciplina;
	}

	public void obterQtdCarateres() {
		if (disciplina.getObservacao() == null) {
		} else
			this.quantidadeCaracteres = disciplina.getObservacao().length();

	}

	public String cadastroDisciplina() {
		inicializarDados();
		return "/academico/director-ditrital/disciplina/cadastro?faces-redirect=true";
	}

	public void novaDisciplina() {
		this.cadastroDisciplinaBoolean = true;
		this.novaDisciplinaBoolean = true;
		editarDisciplinaBoolean = false;
		this.quantidadeCaracteres = 0;
		// pesquisa = null;
		disciplina = new Disciplina();
		disciplina.setDataCadastro(new Date());
		// disciplinas = null;

	}

	public void voltarParaPequisa() {
		disciplina = null;
		editarDisciplinaBoolean = false;
		this.cadastroDisciplinaBoolean = false;
		if (novaDisciplinaBoolean == true) {
			novaDisciplinaBoolean = false;
		}
		try {
			if (!disciplinas.isEmpty()) {
				buscarDisciplina();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void prepararParExcluir(Disciplina disciplina) {
		this.disciplinaExclusao = disciplina;

	}

	public void excluir() {
		try {
			this.disciplinaExclusao.setDescricao(this.disciplinaExclusao.getDescricao().toUpperCase());
			disciplinaServico.excluir(this.disciplinaExclusao);
			Mensagem.mensagemInfo("AVISO: disciplina excluida com sucesso!");
			buscarDisciplina();
		} catch (Exception e) {
			Mensagem.mensagemAlerta("ATENÇÃO: a disciplina não foi excluida através da dependência.");
		}
	}

	public void buscarDisciplina() {
		if (pesquisa == null) {
			disciplinas = new ArrayList<>();
		} else {
			disciplinas = disciplinaServico.obterDisciplinaPorPesquisa(pesquisa.trim());
		}
		if (disciplinas == null) {
			this.qtdDisciplinasEncontradas = 0;
		} else {
			this.qtdDisciplinasEncontradas = disciplinas.size();
		}
	}

	private void inicializarDados() {
		cadastroDisciplinaBoolean = false;
		novaDisciplinaBoolean = false;
		editarDisciplinaBoolean = false;
		qtdDisciplinasEncontradas = 0;
		disciplina = null;
		disciplinas = new ArrayList<>();
	}

	public void editar(Disciplina disciplina) {
		this.disciplina = disciplina;
		// this.disciplina.setCiclo(disciplina.getArea().getCiclo());
		editarDisciplinaBoolean = true;
		cadastroDisciplinaBoolean = true;
		novaDisciplinaBoolean = false;
		obterQtdCarateres();

	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public List<Disciplina> getDisciplinas() {
		return disciplinas;
	}

	public void setDisciplinas(List<Disciplina> disciplinas) {
		this.disciplinas = disciplinas;
	}

	public Disciplina getDisciplinaSelecionada() {
		return disciplinaSelecionada;
	}

	public void setDisciplinaSelecionada(Disciplina disciplinaSelecionada) {
		this.disciplinaSelecionada = disciplinaSelecionada;
	}

	public boolean isCadastroDisciplinaBoolean() {
		return cadastroDisciplinaBoolean;
	}

	public void setCadastroDisciplinaBoolean(boolean cadastroDisciplinaBoolean) {
		this.cadastroDisciplinaBoolean = cadastroDisciplinaBoolean;
	}

	public boolean isNovaDisciplinaBoolean() {
		return novaDisciplinaBoolean;
	}

	public void setNovaDisciplinaBoolean(boolean novaDisciplinaBoolean) {
		this.novaDisciplinaBoolean = novaDisciplinaBoolean;
	}

	public boolean isEditarDisciplinaBoolean() {
		return editarDisciplinaBoolean;
	}

	public void setEditarDisciplinaBoolean(boolean editarDisciplinaBoolean) {
		this.editarDisciplinaBoolean = editarDisciplinaBoolean;
	}

	public Integer getQtdDisciplinasEncontradas() {
		return qtdDisciplinasEncontradas;
	}

	public void setQtdDisciplinasEncontradas(Integer qtdDisciplinasEncontradas) {
		this.qtdDisciplinasEncontradas = qtdDisciplinasEncontradas;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public int getQuantidadeCaracteres() {
		return quantidadeCaracteres;
	}

	public void setQuantidadeCaracteres(int quantidadeCaracteres) {
		this.quantidadeCaracteres = quantidadeCaracteres;
	}

	public Disciplina getDisciplinaExclusao() {
		return disciplinaExclusao;
	}

	public void setDisciplinaExclusao(Disciplina disciplinaExclusao) {
		this.disciplinaExclusao = disciplinaExclusao;
	}

}
