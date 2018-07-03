// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.mz.sistema.gestao.escolar.util.DataUtils;

@Entity
@Table(name = "disciplina_classe")
// quem gerencia esta classse e director distrital da educacao
public class DisciplinaClasse implements Serializable {

	private static final long serialVersionUID = 223608832495589283L;
	private static final String FORMATO_DATA_PADRAO = "dd/MM/yyyy";

	private Long id;
	private Classe classe;
	private Disciplina disciplina;
	private Integer credito;
	private Area area;
	private boolean ativa;
	private boolean obrigatoria = true;
	private Funcionario funcCadastro;
	private Funcionario funcAlteraco;
	private Date dataCadastro;
	private Date dataAlteracao;
	private String formataDataCadastro;
	private String formataDataAlteracao;
	
	private boolean disciplinaSeleconadaBoolean = false;

	private String observacao;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "id_classe")
	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	@ManyToOne
	@JoinColumn(name = "id_disciplina")
	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public Integer getCredito() {
		return credito;
	}

	public void setCredito(Integer credito) {
		this.credito = credito;
	}

	@Temporal(TemporalType.DATE)
	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	@Transient
	public String getFormataDataCadastro() {
		if (dataCadastro == null) {
		} else if (dataCadastro != null) {
			formataDataCadastro = DataUtils.obterDataFormatoBanco(dataCadastro, FORMATO_DATA_PADRAO);
		}
		return formataDataCadastro;
	}

	public void setFormataDataCadastro(String formataDataCadastro) {

		this.formataDataCadastro = formataDataCadastro;
	}

	@Temporal(TemporalType.DATE)
	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	@Transient
	public String getFormataDataAlteracao() {
		if (dataAlteracao == null) {
		} else if (dataAlteracao != null) {
			formataDataAlteracao = DataUtils.obterDataFormatoBanco(dataAlteracao, FORMATO_DATA_PADRAO);
		}
		return formataDataAlteracao;
	}

	public void setFormataDataAlteracao(String formataDataAlteracao) {
		this.formataDataAlteracao = formataDataAlteracao;
	}

	public boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DisciplinaClasse other = (DisciplinaClasse) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@ManyToOne
	@JoinColumn(name = "id_func_cad")
	public Funcionario getFuncCadastro() {
		return funcCadastro;
	}

	public void setFuncCadastro(Funcionario funcCadastro) {
		this.funcCadastro = funcCadastro;
	}

	@ManyToOne
	@JoinColumn(name = "id_func_alte")
	public Funcionario getFuncAlteraco() {
		return funcAlteraco;
	}

	public void setFuncAlteraco(Funcionario funcAlteraco) {
		this.funcAlteraco = funcAlteraco;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@ManyToOne
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public boolean isObrigatoria() {
		return obrigatoria;
	}

	public void setObrigatoria(boolean obrigatoria) {
		this.obrigatoria = obrigatoria;
	}
@Transient
	public boolean isDisciplinaSeleconadaBoolean() {
		return disciplinaSeleconadaBoolean;
	}

	public void setDisciplinaSeleconadaBoolean(boolean disciplinaSeleconadaBoolean) {
		this.disciplinaSeleconadaBoolean = disciplinaSeleconadaBoolean;
	}

}
