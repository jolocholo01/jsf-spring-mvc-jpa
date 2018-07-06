package com.mz.sistema.gestao.escolar.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Funcionario extends Usuario {

	private static final long serialVersionUID = 9098783299869624631L;

	private String localNascimento;
	private String numeroDocumento;
	private String localEmissao;
	private String morada;
	private Date dataEmissao;
	private Double salario;
	private Date dataCadastro;
	private Date horaCadastro = new Date();
	private Distrital direcaoDistrital;
	private Date dataAlocacao;
	private Pais pais;
	private String numero;
	private String avenida;

	private Set<ProfessorTurma> professorTurma = new HashSet<>();
	private List<FuncionarioEscola> funcionarioEscolas = new ArrayList<>();
	private String formacao;
	private String observacao;
	private String funcao;
	private Double cargaHoraria;
	
	
	public String getFormacao() {
		return formacao;
	}

	public void setFormacao(String formacao) {
		this.formacao = formacao;
	}

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	@Temporal(TemporalType.DATE)
	public Date getDataAlocacao() {
		return dataAlocacao;
	}

	public void setDataAlocacao(Date dataAlocacao) {
		this.dataAlocacao = dataAlocacao;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "professor")
	public Set<ProfessorTurma> getProfessorTurma() {
		return professorTurma;
	}

	public void setProfessorTurma(Set<ProfessorTurma> professorTurma) {
		this.professorTurma = professorTurma;
	}

	@OneToMany(mappedBy = "funcionario")
	public List<FuncionarioEscola> getFuncionarioEscolas() {
		return funcionarioEscolas;
	}

	public void setFuncionarioEscolas(List<FuncionarioEscola> funcionarioEscolas) {
		this.funcionarioEscolas = funcionarioEscolas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
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
		Usuario other = (Usuario) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	@ManyToOne
	@JoinColumn(name = "id_direcao_distrital")
	public Distrital getDirecaoDistrital() {
		return direcaoDistrital;
	}

	public void setDirecaoDistrital(Distrital direcaoDistrital) {
		this.direcaoDistrital = direcaoDistrital;
	}

	@Column(name = "local_nascimento")
	public String getLocalNascimento() {
		return localNascimento;
	}

	public void setLocalNascimento(String localNascimento) {
		this.localNascimento = localNascimento;
	}

	@ManyToOne
	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	@Column(name = "numero_documento", length = 20)
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	@Temporal(TemporalType.DATE)
	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	@Column(name = "local_emissao", length = 30)
	public String getLocalEmissao() {
		return localEmissao;
	}

	public void setLocalEmissao(String localEmissao) {
		this.localEmissao = localEmissao;
	}

	public String getMorada() {
		return morada;
	}

	public void setMorada(String morada) {
		this.morada = morada;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Temporal(TemporalType.TIME)
	public Date getHoraCadastro() {
		return horaCadastro;
	}

	public void setHoraCadastro(Date horaCadastro) {
		this.horaCadastro = horaCadastro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}

	public String getAvenida() {
		return avenida;
	}

	public void setAvenida(String avenida) {
		this.avenida = avenida;
	}

	public Double getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(Double cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	
}
