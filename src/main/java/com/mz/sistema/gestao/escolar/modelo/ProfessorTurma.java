package com.mz.sistema.gestao.escolar.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.mz.sistema.gestao.escolar.chave.composta.ProfessorTurmaId;

@Entity
@Table(name = "professor_turma")
public class ProfessorTurma implements Serializable {

	private static final long serialVersionUID = 223608832495589283L;

	private ProfessorTurmaId id;
	private Disciplina disciplina;
	private Turma turma;
	private Funcionario professor;
	private Escola escola;
	private Date inicioAlocacao;
	private Date fimAlocacao;
	private Integer credito;
	private boolean fechada = false;
	private String horario;
	private Sala sala;
	private String  elecionadaTurnoExtra;

	@EmbeddedId
	@AttributeOverrides({ @AttributeOverride(name = "id_turma", column = @Column(name = "id_turma", nullable = false)),
			@AttributeOverride(name = "id_disciplina", column = @Column(name = "id_disciplina", nullable = false)),
			@AttributeOverride(name = "id_escola", column = @Column(name = "id_escola", nullable = false)), })
	public ProfessorTurmaId getId() {
		return id;
	}

	public void setId(ProfessorTurmaId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_turma", nullable = false, insertable = false, updatable = false)
	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_professor")
	public Funcionario getProfessor() {
		return professor;
	}

	public void setProfessor(Funcionario professor) {
		this.professor = professor;
	}

	@Column(name = "inicio_alocacao")
	@Temporal(TemporalType.DATE)
	public Date getInicioAlocacao() {
		return inicioAlocacao;
	}

	public void setInicioAlocacao(Date inicioAlocacao) {
		this.inicioAlocacao = inicioAlocacao;
	}

	@Column(name = "fim_alocacao")
	@Temporal(TemporalType.DATE)
	public Date getFimAlocacao() {
		return fimAlocacao;
	}

	public void setFimAlocacao(Date fimAlocacao) {
		this.fimAlocacao = fimAlocacao;
	}

	public Integer getCredito() {
		return credito;
	}

	public void setCredito(Integer credito) {
		this.credito = credito;
	}

	public boolean isFechada() {
		return fechada;
	}

	public void setFechada(boolean fechada) {
		this.fechada = fechada;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_escola", nullable = false, insertable = false, updatable = false)
	public Escola getEscola() {
		return escola;
	}

	public void setEscola(Escola escola) {
		this.escola = escola;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_disciplina", nullable = false, insertable = false, updatable = false)
	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_sala")
	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	public String getElecionadaTurnoExtra() {
		return elecionadaTurnoExtra;
	}

	public void setElecionadaTurnoExtra(String elecionadaTurnoExtra) {
		this.elecionadaTurnoExtra = elecionadaTurnoExtra;
	}

}
