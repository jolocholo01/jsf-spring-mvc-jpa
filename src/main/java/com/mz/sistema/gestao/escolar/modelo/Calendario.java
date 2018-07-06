package com.mz.sistema.gestao.escolar.modelo;

import java.io.Serializable;
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Calendario implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4555935258671644773L;
	private long id;
	private Integer ano;
	private String pesquisa;
	private List<Trimestre> trimestres;
	private boolean ativo = false;
	private Date dataResultadoFinal;
	private Funcionario funcionario;
	private Date dataIncioExame1;
	private Date dataFimExame1;
	private Date dataIncioExame2;
	private Date dataFimExame2;
	private Date dataIncio;
	private Date dataFim;
	private Date dataCadastro;
	private Date dataAlteracao;
	private String observacao;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "calendario")
	public List<Trimestre> getTrimestres() {
		return trimestres;
	}

	public void setTrimestres(List<Trimestre> trimestres) {
		this.trimestres = trimestres;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_resultado_final")
	public Date getDataResultadoFinal() {
		return dataResultadoFinal;
	}

	public void setDataResultadoFinal(Date dataResultadoFinal) {
		this.dataResultadoFinal = dataResultadoFinal;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_incio_exame1")
	public Date getDataIncioExame1() {
		return dataIncioExame1;
	}

	public void setDataIncioExame1(Date dataIncioExame1) {
		this.dataIncioExame1 = dataIncioExame1;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_fim_exame1")
	public Date getDataFimExame1() {
		return dataFimExame1;
	}

	public void setDataFimExame1(Date dataFimExame1) {
		this.dataFimExame1 = dataFimExame1;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_incio_exame2")
	public Date getDataIncioExame2() {
		return dataIncioExame2;
	}

	public void setDataIncioExame2(Date dataIncioExame2) {
		this.dataIncioExame2 = dataIncioExame2;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_fim_exame2")
	public Date getDataFimExame2() {
		return dataFimExame2;
	}

	public void setDataFimExame2(Date dataFimExame2) {
		this.dataFimExame2 = dataFimExame2;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_inicio")
	public Date getDataIncio() {
		return dataIncio;
	}

	public void setDataIncio(Date dataIncio) {
		this.dataIncio = dataIncio;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_fim")
	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
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
		Calendario other = (Calendario) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_cadastro")
	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_alteracao")
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

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	@ManyToOne
	@JoinColumn(name="id_func_cad")
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

}
