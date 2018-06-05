// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.enumerado.Ciclo;
import com.mz.sistema.gestao.escolar.enumerado.TipoClasse;
import com.mz.sistema.gestao.escolar.modelo.Classe;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.servico.ClasseServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Controller
@Named
@SessionScope
public class ClasseBean {

	private Classe classe;
	private Classe classeSelecionadaExclusao;
	private String pesquisa;
	private Date dataAlteracao = new Date();
	private List<Classe> classes = new ArrayList<Classe>();
	private List<TipoClasse> tipoClasses;
	private List<TipoClasse> tipoClassesMedio = Arrays.asList(TipoClasse.dDECIMA_PRIMEIRA, TipoClasse.eDECIMA_SEGUNDA);
	private List<Ciclo> ciclos = Arrays.asList(Ciclo.PRIMEIRO_CICLO, Ciclo.SEGUNDO_CICLO);

	private boolean cadastroClasseBoolean = false;
	private boolean novaClasseBoolean = false;
	private boolean editarClasseBoolean = false;
	private int quantidadeCaracteres;

	private Integer qtdClassesEncontradas = 0;

	@Autowired
	private ClasseServico classeServico;
	@Autowired
	private AuthenticationContext authenticacao;

	public void salvar() {
		Classe classeExistente = classeServico.classeExisente(classe.getDescricao());
		if (classeExistente == null) {
		} else if (classeExistente != null && classe.getId() != classeExistente.getId()) {
			Mensagem.mensagemInfo("Aviso: já existe esta classe cadastrada no sistema.");
			return;
		}
		Funcionario funcionario = authenticacao.getFuncionarioLogado();
		if (classe.getId() == 0) {
			classe.setFuncCadastro(funcionario);

			classe.setTipoEnsino("ENSINO SECUNDÁRIO");
			Mensagem.mensagemInfo("Aviso: " + classe.getSigla() + "ª Classe inserida no sistema com sucesso!");
		} else if (classe.getId() != 0) {
			classe.setFuncAlteraco(funcionario);
			Mensagem.mensagemInfo("Aviso: " + classe.getSigla() + "ª Classe atualizada com sucesso!");
		}
		if (classe.isAtiva() == false) {
			classe.setFuncAtiva(funcionario);
		}
		classeServico.salvar(classe);

		pequisaClasse();
		classe = new Classe();
	}

	public void transformar() {
		this.quantidadeCaracteres = classe.getObservacao().length();

	}

	public Classe getObterSiglaClasse() {
		if (classe == null) {
			return new Classe();
		} else {
			if (classe.getDescricao() == null) {
				classe.setSigla(null);
			} else if (classe.getDescricao().equals("8ª CLASSE")) {
				classe.setSigla(8);
				classe.setOrdem(1);
			} else if (classe.getDescricao().equals("9ª CLASSE")) {
				classe.setSigla(9);
				classe.setOrdem(2);
			} else if (classe.getDescricao().equals("10ª CLASSE")) {
				classe.setSigla(10);
				classe.setOrdem(3);
			} else if (classe.getDescricao().equals("11ª CLASSE")) {
				classe.setSigla(11);
				classe.setOrdem(4);
			} else if (classe.getDescricao().equals("12ª CLASSE")) {
				classe.setSigla(12);
				classe.setOrdem(5);
			}
			return classe;
		}

	}

	public String limpar() {
		classe = new Classe();
		cadastroClasseBoolean = true;
		classes = null;
		return "/academico/director-ditrital/classe/classe?faces-redirect=true";
	}

	public String cadastroClasse() {
		this.cadastroClasseBoolean = false;
		this.novaClasseBoolean = false;
		this.qtdClassesEncontradas = 0;
		editarClasseBoolean = false;
		// classes = classeServico.obterTodasClasses();
		classe = null;
		pesquisa = new String();
		classes=new ArrayList<>();

		return "/academico/director-ditrital/classe/cadastro?faces-redirect=true";
	}

	public void novaClasse() {
		this.cadastroClasseBoolean = true;
		this.novaClasseBoolean = true;
		this.editarClasseBoolean = false;
		this.qtdClassesEncontradas = 0;
		classe = new Classe();
		// classes = null;
		// pesquisa = null;
	}

	public void voltarParaPequisaClasse() {
		classe = null;
		this.cadastroClasseBoolean = false;
		this.editarClasseBoolean = false;
		if (novaClasseBoolean == true) {
			novaClasseBoolean = false;
		}
		pequisaClasse();
	}

	public void pequisaClasse() {
		try {
			// System.out.println("\n\nO que foi escrito e: " + pesquisa);
			classes = null;
			if (pesquisa.trim().equals("")) {

			} else {
				classes = classeServico.obterClassePorDescricao(pesquisa.trim());
			}
			if (classes != null) {
				qtdClassesEncontradas = classes.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void editar(Classe classe) {
		this.classe = classe;
		this.classe.setDataAlteracao(new Date());
		this.cadastroClasseBoolean = true;
		if (this.classe.getObservacao() != null)
			this.quantidadeCaracteres = this.classe.getObservacao().length();
	}

	public String cancelar() {
		classe = new Classe();
		return "/academico/director/director.jsf";
	}

	public void prepararParExcluir(Classe classe) {
		this.classeSelecionadaExclusao = classe;

	}

	public void excluir() {
		try {
			classeServico.excluir(this.classeSelecionadaExclusao);
			Mensagem.mensagemInfo("Aviso: classe excluida com sucesso!");
			pequisaClasse();
		} catch (Exception e) {
			Mensagem.mensagemAlerta("Aviso: a classe não foi excluida através da dependência.");
		}

	}

	public Classe getClasse() {
		if (classe != null) {

			if (classe.getCiclo() == null) {
				tipoClasses = null;
			} else {
				if (classe.getCiclo().equals("1º CICLO")) {
					tipoClasses = Arrays.asList(TipoClasse.aOITAVA_CLASSE, TipoClasse.bNONA_CLASSE,
							TipoClasse.cDECIMA_TCLASSE);
				} else if (classe.getCiclo().equals("2º CICLO")) {
					tipoClasses = Arrays.asList(TipoClasse.dDECIMA_PRIMEIRA, TipoClasse.eDECIMA_SEGUNDA);
				}
			}
		}
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	public List<Classe> getClasses() {
		return classes;
	}

	public void setClasses(List<Classe> classes) {
		this.classes = classes;
	}

	public List<TipoClasse> getTipoClasses() {
		return tipoClasses;
	}

	public void setTipoClasses(List<TipoClasse> tipoClasses) {
		this.tipoClasses = tipoClasses;
	}

	public List<TipoClasse> getTipoClassesMedio() {
		return tipoClassesMedio;
	}

	public void setTipoClassesMedio(List<TipoClasse> tipoClassesMedio) {
		this.tipoClassesMedio = tipoClassesMedio;
	}

	public List<Ciclo> getCiclos() {
		return ciclos;
	}

	public void setCiclos(List<Ciclo> ciclos) {
		this.ciclos = ciclos;
	}

	public boolean isCadastroClasseBoolean() {
		return cadastroClasseBoolean;
	}

	public void setCadastroClasseBoolean(boolean cadastroClasseBoolean) {
		this.cadastroClasseBoolean = cadastroClasseBoolean;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public boolean isNovaClasseBoolean() {
		return novaClasseBoolean;
	}

	public void setNovaClasseBoolean(boolean novaClasseBoolean) {
		this.novaClasseBoolean = novaClasseBoolean;
	}

	public boolean isEditarClasseBoolean() {
		return editarClasseBoolean;
	}

	public void setEditarClasseBoolean(boolean editarClasseBoolean) {
		this.editarClasseBoolean = editarClasseBoolean;
	}

	public Integer getQtdClassesEncontradas() {
		return qtdClassesEncontradas;
	}

	public void setQtdClassesEncontradas(Integer qtdClassesEncontradas) {
		this.qtdClassesEncontradas = qtdClassesEncontradas;
	}

	public int getQuantidadeCaracteres() {
		return quantidadeCaracteres;
	}

	public void setQuantidadeCaracteres(int quantidadeCaracteres) {
		this.quantidadeCaracteres = quantidadeCaracteres;
	}

	public Classe getClasseSelecionadaExclusao() {
		return classeSelecionadaExclusao;
	}

	public void setClasseSelecionadaExclusao(Classe classeSelecionadaExclusao) {
		this.classeSelecionadaExclusao = classeSelecionadaExclusao;
	}

}
