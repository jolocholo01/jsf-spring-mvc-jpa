package com.mz.sistema.gestao.escolar.chave.composta;

import java.io.Serializable;

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
import javax.persistence.Embeddable;

@Embeddable
public class ProfessorTurmaId implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7459936872557380498L;
	@Column(name = "id_turma", nullable = false)
	private Integer id_turma;
	

	@Column(name = "id_disciplina", nullable = false)
	private Long id_disciplina_classe;
	
	public Integer getId_turma() {
		return id_turma;
	}

	public void setId_turma(Integer id_turma) {
		this.id_turma = id_turma;
	}

	public Long getId_disciplina_classe() {
		return id_disciplina_classe;
	}

	public void setId_disciplina_classe(Long id_disciplina_classe) {
		this.id_disciplina_classe = id_disciplina_classe;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id_disciplina_classe == null) ? 0 : id_disciplina_classe.hashCode());
		result = prime * result + ((id_turma == null) ? 0 : id_turma.hashCode());
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
		ProfessorTurmaId other = (ProfessorTurmaId) obj;
		if (id_disciplina_classe == null) {
			if (other.id_disciplina_classe != null)
				return false;
		} else if (!id_disciplina_classe.equals(other.id_disciplina_classe))
			return false;
		if (id_turma == null) {
			if (other.id_turma != null)
				return false;
		} else if (!id_turma.equals(other.id_turma))
			return false;
		return true;
	}

	
	

	

}
