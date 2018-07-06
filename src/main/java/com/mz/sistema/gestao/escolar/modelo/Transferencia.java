package com.mz.sistema.gestao.escolar.modelo;

import java.util.Date;
/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Transferencia {
	private Long id;
	private Matricula matricula;
	private Date dataCadastro = new Date();
	private Date dataMatricula;
	private String situacao;
	private Escola escolaOrigem;
	private Escola escolaDestino;
	private Turma turmaOrigem;
	private Classe classe;
	private String motivo;
	private boolean finalizada;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "id_matricula")
	public Matricula getMatricula() {
		return matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
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
	@JoinColumn(name = "id_escola_destino")

	public Escola getEscolaDestino() {
		return escolaDestino;
	}

	public void setEscolaDestino(Escola escolaDestino) {
		this.escolaDestino = escolaDestino;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	@ManyToOne
	@JoinColumn(name = "id_turma_origem")
	public Turma getTurmaOrigem() {
		return turmaOrigem;
	}

	public void setTurmaOrigem(Turma turmaOrigem) {
		this.turmaOrigem = turmaOrigem;
	}

	public boolean isFinalizada() {
		return finalizada;
	}

	public void setFinalizada(boolean finalizada) {
		this.finalizada = finalizada;
	}

	@ManyToOne
	@JoinColumn(name = "id_classe")
	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataMatricula() {
		return dataMatricula;
	}

	public void setDataMatricula(Date dataMatricula) {
		this.dataMatricula = dataMatricula;
	}
}
