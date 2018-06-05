// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.modelo;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.mz.sistema.gestao.escolar.util.DataUtils;

@SuppressWarnings("serial")
@Entity
@Table(name = "disciplina")
public class Disciplina implements Serializable {

	private static final String FORMATO_DATA_PADRAO = "dd/MM/yyyy";

	private Integer id;
	private String descricao;
	private String sigla;
	private String codigo;
	private boolean ativa;
	private Date dataCadastro = new Date();
	private Funcionario funcCadastro;
	private Funcionario funcAlteracao;
	private Date dataAlteracao;
	private String observacao;
	
	private String formataDataCadastro;
	private String formataDataAlteracao;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	@Column(length = 20)
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Disciplina other = (Disciplina) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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
	public Funcionario getFuncAlteracao() {
		return funcAlteracao;
	}

	public void setFuncAlteracao(Funcionario funcAlteracao) {
		this.funcAlteracao = funcAlteracao;
	}

	@Temporal(TemporalType.DATE)
	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}
