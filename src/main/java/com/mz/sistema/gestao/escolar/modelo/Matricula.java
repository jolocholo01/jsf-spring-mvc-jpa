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

import javax.persistence.CascadeType;
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
import javax.persistence.Transient;

@Entity
public class Matricula implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7081685817616021883L;
	private Integer id;
	private Integer ano;
	private Integer acompanhamento;
	private Integer ordem;
	private String numero;
	private Double valor = 0D;
	private Integer numeroAlunoTurma;
	private String numeroRecibo;
	private Date dataMatricula;
	private Date dataEnturmacao;
	private Date horaMatricula = new Date();
	private String curso;
	private Aluno aluno;
	private Classe classe;
	private Turma turma;
	private Escola escola;
	private Escola escolaOrigem;
	private Funcionario funcionario;
	private boolean ativo = false;
	private boolean repetente = false;
	private boolean matriculaSelacionada = false;
	private Date dataDesativacao;
	private Date dataAtualizacao;
	private String observacao;
	private String tipoArea;
	private boolean temTurma;
	// Transferencia de para uma turma
	private Turma turmaDestino;

	private String situacao;
	private String solicitouTranferencia;
	private List<Nota> notas;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Temporal(TemporalType.DATE)
	public Date getDataMatricula() {
		return dataMatricula;
	}

	public void setDataMatricula(Date dataMatricula) {
		this.dataMatricula = dataMatricula;
	}

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "id_aluno")
	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Date getDataDesativacao() {
		return dataDesativacao;
	}

	public void setDataDesativacao(Date dataDesativacao) {
		this.dataDesativacao = dataDesativacao;
	}

	@ManyToOne
	@JoinColumn(name = "idturma")
	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	@ManyToOne
	@JoinColumn(name = "idclasse")
	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	@ManyToOne
	public Escola getEscola() {
		return escola;
	}

	public void setEscola(Escola escola) {
		this.escola = escola;
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
		Matricula other = (Matricula) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public boolean isRepetente() {
		return repetente;
	}

	public void setRepetente(boolean repetente) {
		this.repetente = repetente;
	}

	@ManyToOne
	@JoinColumn(name = "id_turma_destino")
	public Turma getTurmaDestino() {
		return turmaDestino;
	}

	public void setTurmaDestino(Turma turmaDestino) {
		this.turmaDestino = turmaDestino;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	@Temporal(TemporalType.TIME)
	public Date getHoraMatricula() {
		return horaMatricula;
	}

	public void setHoraMatricula(Date horaMatricula) {
		this.horaMatricula = horaMatricula;
	}

	public String getTipoArea() {
		return tipoArea;
	}

	public void setTipoArea(String tipoArea) {
		this.tipoArea = tipoArea;
	}

	public String getNumeroRecibo() {
		return numeroRecibo;
	}

	public void setNumeroRecibo(String numeroRecibo) {
		this.numeroRecibo = numeroRecibo;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "matricula")
	public List<Nota> getNotas() {
		return notas;
	}

	public void setNotas(List<Nota> notas) {
		this.notas = notas;
	}

	@Transient
	public boolean isMatriculaSelacionada() {
		return matriculaSelacionada;
	}

	public void setMatriculaSelacionada(boolean matriculaSelacionada) {
		this.matriculaSelacionada = matriculaSelacionada;
	}

	public boolean isTemTurma() {
		return temTurma;
	}

	public void setTemTurma(boolean temTurma) {
		this.temTurma = temTurma;
	}

	public Integer getAcompanhamento() {
		return acompanhamento;
	}

	public void setAcompanhamento(Integer acompanhamento) {
		this.acompanhamento = acompanhamento;
	}

	@Column(name = "solicitou_tranferencia")
	public String getSolicitouTranferencia() {
		return solicitouTranferencia;
	}

	public void setSolicitouTranferencia(String solicitouTranferencia) {
		this.solicitouTranferencia = solicitouTranferencia;
	}

	public Integer getNumeroAlunoTurma() {
		return numeroAlunoTurma;
	}

	public void setNumeroAlunoTurma(Integer numeroAlunoTurma) {
		this.numeroAlunoTurma = numeroAlunoTurma;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	@ManyToOne
	@JoinColumn(name = "id_escola_origem")
	public Escola getEscolaOrigem() {
		return escolaOrigem;
	}

	public void setEscolaOrigem(Escola escolaOrigem) {
		this.escolaOrigem = escolaOrigem;
	}

	@ManyToOne
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	@Transient
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataEnturmacao() {
		return dataEnturmacao;
	}

	public void setDataEnturmacao(Date dataEnturmacao) {
		this.dataEnturmacao = dataEnturmacao;
	}

}
