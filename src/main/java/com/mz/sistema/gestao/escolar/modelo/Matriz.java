package com.mz.sistema.gestao.escolar.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Matriz implements Serializable {

	private static final long serialVersionUID = 71322530428224681L;
	private long id;
	private Integer ano;
	private String curso;
	private List<DisciplinaClasse> disciplinaClasses;
	private Classe classe;
	private Escola escola;
	private Area area;
	private String tipoArea;
	private String ciclo;
	private boolean ativa;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	@ManyToMany
	@JoinTable(name = "matriz_disciplina_classe", joinColumns = {
			@JoinColumn(name = "id_matriz") }, inverseJoinColumns = { @JoinColumn(name = "id_disciplina_classe") })
	public List<DisciplinaClasse> getDisciplinaClasses() {
		return disciplinaClasses;
	}

	public void setDisciplinaClasses(List<DisciplinaClasse> disciplinaClasses) {
		this.disciplinaClasses = disciplinaClasses;
	}

	@ManyToOne
	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	public boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	@ManyToOne
	@JoinColumn(name = "id_area")
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
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
		Matriz other = (Matriz) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	@ManyToOne
	@JoinColumn(name = "id_escola")
	public Escola getEscola() {
		return escola;
	}

	public void setEscola(Escola escola) {
		this.escola = escola;
	}

	public String getCiclo() {
		return ciclo;
	}

	public void setCiclo(String ciclo) {
		this.ciclo = ciclo;
	}

	public String getTipoArea() {
		return tipoArea;
	}

	public void setTipoArea(String tipoArea) {
		this.tipoArea = tipoArea;
	}

}
