package com.mz.sistema.gestao.escolar.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.modelo.Disciplina;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.servico.DisciplinaServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;
import com.mz.sistema.gestao.escolar.util.Replace;

@Named
@Controller
@SessionScope
public class AreaCurricularBean implements Serializable {

	private static final long serialVersionUID = -1205249789398248234L;
	private Disciplina disciplina;
	private Disciplina disciplinaSelecionada;
	private Disciplina disciplinaExclusao;
	private List<Disciplina> disciplinas = new ArrayList<>();

	private boolean cadastroAreaCurricularBoolean = false;
	private boolean novaAreaCurricularBoolean = false;
	private boolean editarAreaCurricularBoolean = false;
	private int quantidadeCaracteres;

	private Integer qtdAreasCurricularesEncontradas = 0;
	private String pesquisa = new String();

	@Autowired
	private DisciplinaServico disciplinaServico;

	@Autowired
	private AuthenticationContext authenticacao;

	public void salvar() {
		try {
			Disciplina disciplinaExistente = disciplinaServico.disciplinaExisente(disciplina.getDescricao());
			if (disciplinaExistente != null && disciplina.getId() != disciplinaExistente.getId()) {
				Mensagem.mensagemInfo("Aviso: já existe esta disciplina cadastrada no sistema.");
				return;
			}
			Funcionario funcionario = authenticacao.getFuncionarioLogado();
			if (disciplina.getId() == null) {
				if (funcionario != null)
					disciplina.setFuncCadastro(funcionario);
				disciplina.setCodigo(disciplina.getDescricao().substring(0, 3).toUpperCase());
				disciplina = disciplinaServico.salvarRetornar(disciplina);
				setarCodigoDisciplina();
				
				disciplinaServico.salvar(disciplina);
				Mensagem.mensagemInfo("Aviso: disciplina cadastrada com sucesso!");
			} else if (disciplina.getId() != null) {
				if (funcionario != null)
					disciplina.setFuncAlteracao(funcionario);
				setarCodigoDisciplina();
				
				disciplinaServico.salvar(disciplina);
				Mensagem.mensagemInfo("Aviso: disciplina atualizada com sucesso!");

			}
			disciplina = new Disciplina();
			this.quantidadeCaracteres = 0;
		} catch (Exception e) {
			Mensagem.mensagemErro("ERRO: houve um erro ao cadastrar disciplina!");
		}
	}

	private void setarCodigoDisciplina() {
		if(disciplina.getId()<100 && disciplina.getId()>9){
			disciplina.setCodigo(disciplina.getDescricao().substring(0, 3).toUpperCase()+"0"+disciplina.getId());
		}else if(disciplina.getId()<9){
			disciplina.setCodigo(disciplina.getDescricao().substring(0, 3).toUpperCase()+"00"+disciplina.getId());
		}else{
			disciplina.setCodigo(disciplina.getDescricao().substring(0, 3).toUpperCase()+""+disciplina.getId());
		}
	}

	public void obterQtdCarateres() {
		if (disciplina.getObservacao() == null) {
		} else
			this.quantidadeCaracteres = disciplina.getObservacao().length();

	}

	public String cadastroAreaCurricular() {
		inicializarDados();
		return "/academico/director-ditrital/area/cadastro?faces-redirect=true";
	}

	public void novaAreaCurricular() {
		this.cadastroAreaCurricularBoolean = true;
		this.novaAreaCurricularBoolean = true;
		editarAreaCurricularBoolean = false;
		this.quantidadeCaracteres = 0;
		// pesquisa = null;
		disciplina = new Disciplina();
		disciplinas = null;

	}

	public void voltarParaPequisa() {
		disciplina = null;
		editarAreaCurricularBoolean = false;
		this.cadastroAreaCurricularBoolean = false;
		if (novaAreaCurricularBoolean == true) {
			novaAreaCurricularBoolean = false;
		}

		buscarAreaCurricular();

	}

	public void prepararParExcluir(Disciplina disciplina) {
		this.disciplinaExclusao = disciplina;
		this.disciplinaExclusao.setDescricao(Replace.rescreverTexto(disciplina.getDescricao()));

	}

	public void excluir() {
		try {
			this.disciplinaExclusao.setDescricao(this.disciplinaExclusao.getDescricao().toUpperCase());
			disciplinaServico.excluir(this.disciplinaExclusao);
			Mensagem.mensagemInfo("Aviso: disciplina excluida com sucesso!");
			buscarAreaCurricular();
		} catch (Exception e) {
			Mensagem.mensagemInfo("Aviso: a disciplina não foi excluida através da dependência.");
		}
	}

	public void buscarAreaCurricular() {
		if (pesquisa == null) {
			disciplinas = new ArrayList<>();
		} else {
			disciplinas = disciplinaServico.obterDisciplinaPorPesquisa(pesquisa.trim());
		}
		if (disciplinas == null) {
			this.qtdAreasCurricularesEncontradas = 0;
		} else {
			this.qtdAreasCurricularesEncontradas = disciplinas.size();
		}
	}

	private void inicializarDados() {
		cadastroAreaCurricularBoolean = false;
		novaAreaCurricularBoolean = false;
		editarAreaCurricularBoolean = false;
		qtdAreasCurricularesEncontradas = 0;
		disciplina = null;
		disciplinas = new ArrayList<>();
	}

	public void editar(Disciplina disciplina) {
		this.disciplina = disciplina;
		// this.disciplina.setCiclo(disciplina.getArea().getCiclo());
		editarAreaCurricularBoolean = true;
		cadastroAreaCurricularBoolean = true;
		novaAreaCurricularBoolean = false;
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

	public boolean isCadastroAreaCurricularBoolean() {
		return cadastroAreaCurricularBoolean;
	}

	public void setCadastroAreaCurricularBoolean(boolean cadastroAreaCurricularBoolean) {
		this.cadastroAreaCurricularBoolean = cadastroAreaCurricularBoolean;
	}

	public boolean isNovaAreaCurricularBoolean() {
		return novaAreaCurricularBoolean;
	}

	public void setNovaAreaCurricularBoolean(boolean novaAreaCurricularBoolean) {
		this.novaAreaCurricularBoolean = novaAreaCurricularBoolean;
	}

	public boolean isEditarAreaCurricularBoolean() {
		return editarAreaCurricularBoolean;
	}

	public void setEditarAreaCurricularBoolean(boolean editarAreaCurricularBoolean) {
		this.editarAreaCurricularBoolean = editarAreaCurricularBoolean;
	}

	public Integer getQtdAreasCurricularesEncontradas() {
		return qtdAreasCurricularesEncontradas;
	}

	public void setQtdAreasCurricularesEncontradas(Integer qtdAreasCurricularesEncontradas) {
		this.qtdAreasCurricularesEncontradas = qtdAreasCurricularesEncontradas;
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
