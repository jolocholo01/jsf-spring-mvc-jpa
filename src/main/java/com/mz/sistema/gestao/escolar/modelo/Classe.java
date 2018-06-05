// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.mz.sistema.gestao.escolar.util.DataUtils;

@Entity
public class Classe implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2631752484338873809L;
	private long id;
	private String ciclo;
	private String descricao;
	private Integer sigla;
	private Integer ordem;// =DataUtils.obterSiglaClasse(descricao);
	private String tipoEnsino;
	private Funcionario funcCadastro;
	private Funcionario funcAlteraco;
	private boolean ativa = true;
	private Funcionario funcAtiva;
	private List<Turma> turmas = new ArrayList<>();
	private Date dataCadastro = new Date();
	private String formataDataCadastro;
	private Date dataAlteracao;
	private String formataDataAlteracao;
	private String observacao;
	private static final String FORMATO_DATA_PADRAO = "dd/MM/yyyy";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCiclo() {
		return ciclo;
	}

	public void setCiclo(String ciclo) {
		this.ciclo = ciclo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getSigla() {
		return sigla;
	}

	public void setSigla(Integer sigla) {
		this.sigla = sigla;
	}

	public boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	public String getTipoEnsino() {
		return tipoEnsino;
	}

	public void setTipoEnsino(String tipoEnsino) {
		this.tipoEnsino = tipoEnsino;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "classe")
	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	@Temporal(TemporalType.DATE)
	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	@ManyToOne
	public Funcionario getFuncCadastro() {
		return funcCadastro;
	}

	public void setFuncCadastro(Funcionario funcCadastro) {
		this.funcCadastro = funcCadastro;
	}

	@ManyToOne
	public Funcionario getFuncAlteraco() {
		return funcAlteraco;
	}

	public void setFuncAlteraco(Funcionario funcAlteraco) {
		this.funcAlteraco = funcAlteraco;
	}

	@Temporal(TemporalType.DATE)
	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
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

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@ManyToOne
	public Funcionario getFuncAtiva() {
		return funcAtiva;
	}

	public void setFuncAtiva(Funcionario funcAtiva) {
		this.funcAtiva = funcAtiva;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
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
		Classe other = (Classe) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
