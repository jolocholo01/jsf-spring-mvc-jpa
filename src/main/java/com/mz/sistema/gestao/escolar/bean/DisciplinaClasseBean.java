// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.modelo.Area;
import com.mz.sistema.gestao.escolar.modelo.Classe;
import com.mz.sistema.gestao.escolar.modelo.Disciplina;
import com.mz.sistema.gestao.escolar.modelo.DisciplinaClasse;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.servico.AreaServico;
import com.mz.sistema.gestao.escolar.servico.ClasseServico;
import com.mz.sistema.gestao.escolar.servico.DisciplinaClasseServico;
import com.mz.sistema.gestao.escolar.servico.DisciplinaServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;
import com.mz.sistema.gestao.escolar.util.Replace;

@Named
@Controller
@SessionScope
public class DisciplinaClasseBean {
	private DisciplinaClasse disciplinaClasse;
	private DisciplinaClasse disciplinaClasseExclusao;
	private List<DisciplinaClasse> disciplinaClasses = new ArrayList<>();
	private List<Classe> classes = new ArrayList<>();
	private List<Disciplina> disciplinas = new ArrayList<>();
	private List<Area> areas = new ArrayList<>();
	private long idClasse;

	private boolean novaDisciplinaClasseBoolean = false;
	private boolean cadastroDisciplinaClasseBoolean = false;
	private boolean editarDisciplinaClasseBoolean = false;
	private int quantidadeCaracteres;

	@Autowired
	private DisciplinaClasseServico disciplinaClasseServico;
	@Autowired
	private ClasseServico classeServico;

	@Autowired
	private DisciplinaServico disciplinaServico;

	@Autowired
	private AreaServico areaServico;

	@Autowired
	private AuthenticationContext authenticationContext;
	private int qtdDisciplinaClasseEncontradas;

	public void salvar() {
		DisciplinaClasse disciplinaClasseExistente = disciplinaClasseServico.disciplinasClasseExistente(
				disciplinaClasse.getClasse().getId(), disciplinaClasse.getDisciplina().getId(),
				disciplinaClasse.getArea().getId());

		if (disciplinaClasseExistente != null && disciplinaClasseExistente.getId() != disciplinaClasse.getId()) {
			Mensagem.mensagemInfo("Já existe esta disciplina alocado nesta classe!");
			return;
		}
		DisciplinaClasse classeSelecionada = new DisciplinaClasse();
		classeSelecionada.setClasse(disciplinaClasse.getClasse());
		Funcionario funcionario = authenticationContext.getFuncionarioLogado();
		if (disciplinaClasse.getId() == null) {
			disciplinaClasse.setFuncCadastro(funcionario);
			disciplinaClasseServico.salvar(disciplinaClasse);
			Mensagem.mensagemInfo("Aviso: Disciplina alocada na classe com sucesso!");

		} else if (disciplinaClasse.getId() != null) {
			disciplinaClasse.setFuncAlteraco(funcionario);
			disciplinaClasseServico.salvar(disciplinaClasse);
			Mensagem.mensagemInfo("Aviso: Disciplina atulizada na classe com sucesso!");

		}
		disciplinaClasse = new DisciplinaClasse();
		disciplinaClasse.setClasse(classeSelecionada.getClasse());
		disciplinaClasse.setDataCadastro(new Date());
		pesquisaDeDisciplinaNaClasse();
	}

	// inicializacao do prytty config
	public void iniciarBean() {
		disciplinasANDClasses();
	}

	public void novaAlocacao() {
		this.cadastroDisciplinaClasseBoolean = true;
		this.novaDisciplinaClasseBoolean = true;
		this.setQtdDisciplinaClasseEncontradas(0);
		this.editarDisciplinaClasseBoolean = false;
		disciplinaClasse = new DisciplinaClasse();
		disciplinaClasse.setDataCadastro(new Date());

	}

	public void editar(DisciplinaClasse disciplinaClasse) {
		this.disciplinaClasse = disciplinaClasse;
		this.cadastroDisciplinaClasseBoolean = true;
		this.novaDisciplinaClasseBoolean = false;
		this.editarDisciplinaClasseBoolean = true;
		obterQtdCarateres();

	}

	public void prepararParaExcluir(DisciplinaClasse disciplinaClasse) {
		this.disciplinaClasseExclusao = disciplinaClasse;
		this.disciplinaClasseExclusao.getDisciplina()
				.setDescricao(Replace.rescreverTexto(this.disciplinaClasseExclusao.getDisciplina().getDescricao()));
		this.disciplinaClasseExclusao.getArea()
				.setDescricao(Replace.rescreverTexto(this.disciplinaClasseExclusao.getArea().getDescricao()));
		this.disciplinaClasseExclusao.getClasse()
				.setDescricao(this.disciplinaClasseExclusao.getClasse().getDescricao().toLowerCase());
	}

	public void excluir() {
		try {
			disciplinaClasseServico.excluir(this.disciplinaClasseExclusao);
			Mensagem.mensagemInfo("Aviso: Disciplna excluida com sucesso!");
			System.out.println("Chamou a funaco!");
			pesquisaDeDisciplinaNaClasse();

		} catch (Exception e) {
			Mensagem.mensagemErro("ERRO: a disciplina não foi excluida através da dependência.");
		}
	}

	private void disciplinasANDClasses() {
		try {
			classes = classeServico.obterTodasClassesAtivas();
			disciplinas = disciplinaServico.obterDisciplinasAtivas();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void voltarParaPequisaAlocacao() {
		disciplinaClasse = null;
		this.cadastroDisciplinaClasseBoolean = false;
		this.editarDisciplinaClasseBoolean = false;
		if (novaDisciplinaClasseBoolean == true) {
			novaDisciplinaClasseBoolean = false;
		}
		// carregar lista de alguma coisa

	}

	public void pesquisaDeDisciplinaNaClasse() {
		if (idClasse == 0) {

		} else {
			disciplinaClasses = disciplinaClasseServico.obterDisciplinasPorClasse(idClasse);
			if (disciplinaClasses == null) {
			} else {
				qtdDisciplinaClasseEncontradas = disciplinaClasses.size();
			}
		}

	}

	public String cadastroDisciplinaClasseDirectorDistrital() {
		this.cadastroDisciplinaClasseBoolean = false;
		this.novaDisciplinaClasseBoolean = false;
		this.setQtdDisciplinaClasseEncontradas(0);
		this.editarDisciplinaClasseBoolean = false;
		disciplinaClasse = null;
		disciplinaClasses = new ArrayList<>();
		idClasse = 0;
		return "/academico/director-ditrital/classe/disciplina?faces-redirect=true";
	}
	public String cadastroDisciplinaClasseDirector() {
		this.cadastroDisciplinaClasseBoolean = false;
		this.novaDisciplinaClasseBoolean = false;
		this.setQtdDisciplinaClasseEncontradas(0);
		this.editarDisciplinaClasseBoolean = false;
		disciplinaClasse = null;
		disciplinaClasses = new ArrayList<>();
		idClasse = 0;
		return "/academico/director/classe/disciplina?faces-redirect=true";
	}

	public void buscarAreas() {

		areas = areaServico.obterAreasPorCiclo(disciplinaClasse.getClasse().getCiclo());
		System.out.println("Ciclo:" + disciplinaClasse.getClasse().getCiclo());
		System.out.println("id:" + disciplinaClasse.getClasse().getId());
	}

	public void obterQtdCarateres() {
		if (disciplinaClasse.getObservacao() != null)
			this.quantidadeCaracteres = disciplinaClasse.getObservacao().length();

	}

	public DisciplinaClasse getDisciplinaClasse() {
		return disciplinaClasse;
	}

	public void setDisciplinaClasse(DisciplinaClasse disciplinaClasse) {
		this.disciplinaClasse = disciplinaClasse;
	}

	public List<DisciplinaClasse> getDisciplinaClasses() {
		return disciplinaClasses;
	}

	public void setDisciplinaClasses(List<DisciplinaClasse> disciplinaClasses) {
		this.disciplinaClasses = disciplinaClasses;
	}

	public boolean isNovaDisciplinaClasseBoolean() {
		return novaDisciplinaClasseBoolean;
	}

	public void setNovaDisciplinaClasseBoolean(boolean novaDisciplinaClasseBoolean) {
		this.novaDisciplinaClasseBoolean = novaDisciplinaClasseBoolean;
	}

	public boolean isCadastroDisciplinaClasseBoolean() {
		return cadastroDisciplinaClasseBoolean;
	}

	public void setCadastroDisciplinaClasseBoolean(boolean cadastroDisciplinaClasseBoolean) {
		this.cadastroDisciplinaClasseBoolean = cadastroDisciplinaClasseBoolean;
	}

	public boolean isEditarDisciplinaClasseBoolean() {
		return editarDisciplinaClasseBoolean;
	}

	public void setEditarDisciplinaClasseBoolean(boolean editarDisciplinaClasseBoolean) {
		this.editarDisciplinaClasseBoolean = editarDisciplinaClasseBoolean;
	}

	public int getQuantidadeCaracteres() {
		return quantidadeCaracteres;
	}

	public void setQuantidadeCaracteres(int quantidadeCaracteres) {
		this.quantidadeCaracteres = quantidadeCaracteres;
	}

	public int getQtdDisciplinaClasseEncontradas() {
		return qtdDisciplinaClasseEncontradas;
	}

	public void setQtdDisciplinaClasseEncontradas(int qtdDisciplinaClasseEncontradas) {
		this.qtdDisciplinaClasseEncontradas = qtdDisciplinaClasseEncontradas;
	}

	public List<Classe> getClasses() {
		return classes;
	}

	public void setClasses(List<Classe> classes) {
		this.classes = classes;
	}

	public List<Disciplina> getDisciplinas() {
		return disciplinas;
	}

	public void setDisciplinas(List<Disciplina> disciplinas) {
		this.disciplinas = disciplinas;
	}

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

	public long getIdClasse() {
		return idClasse;
	}

	public void setIdClasse(long idClasse) {
		this.idClasse = idClasse;
	}

	public DisciplinaClasse getDisciplinaClasseExclusao() {
		return disciplinaClasseExclusao;
	}

	public void setDisciplinaClasseExclusao(DisciplinaClasse disciplinaClasseExclusao) {
		this.disciplinaClasseExclusao = disciplinaClasseExclusao;
	}

}
