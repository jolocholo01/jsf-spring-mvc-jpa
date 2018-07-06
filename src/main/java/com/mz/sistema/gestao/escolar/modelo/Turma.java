package com.mz.sistema.gestao.escolar.modelo;

import java.io.Serializable;
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

@Entity
public class Turma implements Serializable {


	private static final long serialVersionUID = 5027865035062163664L;
	private Integer id;
	private Integer ano;
	private String descricao;
	private String curso;
	private Turno turno;
	private String numero;
	private Escola escola;
	private Sala sala;
	private List<ProfessorTurma> professorTurma ;
	private List<Matricula> alunos;
	private List<Horario> horarios ;
	private Classe classe = new Classe();
	private Integer capacidade;
	private int inscrito;
	private Integer restanteVaga;
	private boolean ativo;
	private boolean exiteHorario;
	private boolean fechada = false;
	private String area;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "descricao")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "turma")
	public List<ProfessorTurma> getProfessorTurma() {
		return professorTurma;
	}

	public void setProfessorTurma(List<ProfessorTurma> professorTurma) {
		this.professorTurma = professorTurma;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "turma")
	public List<Matricula> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Matricula> alunos) {
		this.alunos = alunos;
	}

	@ManyToOne//(fetch = FetchType.LAZY)
	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	@ManyToOne//(fetch = FetchType.LAZY)
	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

	public Integer getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(Integer capacidade) {
		this.capacidade = capacidade;
	}
	
	public int getInscrito() {
		return inscrito;
	}

	public void setInscrito(int inscrito) {
		this.inscrito = inscrito;
	}

	public Integer getRestanteVaga() {
		return restanteVaga;
	}

	public void setRestanteVaga(Integer restanteVaga) {
		this.restanteVaga = restanteVaga;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	@ManyToOne
	@JoinColumn(name = "id_sala")
	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "turma")
	public List<Horario> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<Horario> horarios) {
		this.horarios = horarios;
	}

	public boolean isFechada() {
		return fechada;
	}

	public void setFechada(boolean fechada) {
		this.fechada = fechada;
	}

	@ManyToOne
	public Escola getEscola() {
		return escola;
	}

	public void setEscola(Escola escola) {
		this.escola = escola;
	}

	public boolean isExiteHorario() {
		return exiteHorario;
	}

	public void setExiteHorario(boolean exiteHorario) {
		this.exiteHorario = exiteHorario;
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
		Turma other = (Turma) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	

	
}
