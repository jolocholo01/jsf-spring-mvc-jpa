package com.mz.sistema.gestao.escolar.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Escola implements Serializable {

	private static final long serialVersionUID = -124522888085635239L;
	private Long id;
	private String descricao;
	private String codigo;
	private String localidade;
	private String bairro;
	private String email;
	private String avenidaRua;
	private String observacao;
	private Date dataCadastro = new Date();
	private Date dataAlteracao;
	private boolean ativa;
	private Distrital distrital;
	private Set<FuncionarioEscola> funcionarioEscolas;
	private List<Classe> classes;
	private Funcionario funcCadastro;
	private Funcionario funcAlteracao;
	private List<Turno> turnos;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}


	@ManyToMany
	@JoinTable(name = "escola_turno", joinColumns = { @JoinColumn(name = "id_escola") }, inverseJoinColumns = {
			@JoinColumn(name = "id_turno") })
	public List<Turno> getTurnos() {
		return turnos;
	}

	public void setTurnos(List<Turno> turnos) {
		this.turnos = turnos;
	}

	@OneToMany(mappedBy = "escola")
	public Set<FuncionarioEscola> getFuncionarioEscolas() {
		return funcionarioEscolas;
	}

	public void setFuncionarioEscolas(Set<FuncionarioEscola> funcionarioEscolas) {
		this.funcionarioEscolas = funcionarioEscolas;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ManyToOne
	@JoinColumn(name = "id_direcao_distrital")
	public Distrital getDistrital() {
		return distrital;
	}

	public void setDistrital(Distrital distrital) {
		this.distrital = distrital;
	}

	@ManyToOne
	@JoinColumn(name = "func_cadastro")
	public Funcionario getFuncCadastro() {
		return funcCadastro;
	}

	public void setFuncCadastro(Funcionario funcCadastro) {
		this.funcCadastro = funcCadastro;
	}

	@ManyToOne
	@JoinColumn(name = "func_alteracao")
	public Funcionario getFuncAlteracao() {
		return funcAlteracao;
	}

	public void setFuncAlteracao(Funcionario funcAlteracao) {
		this.funcAlteracao = funcAlteracao;
	}

	@ManyToMany
	@JoinTable(name = "escola_classe", joinColumns = { @JoinColumn(name = "id_escola") }, inverseJoinColumns = {
			@JoinColumn(name = "id_classe") })
	public List<Classe> getClasses() {
		return classes;
	}

	public void setClasses(List<Classe> classes) {
		this.classes = classes;
	}

	@Column(name = "av_rua")
	public String getAvenidaRua() {
		return avenidaRua;
	}

	public void setAvenidaRua(String avenidaRua) {
		this.avenidaRua = avenidaRua;
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
		Escola other = (Escola) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

}
