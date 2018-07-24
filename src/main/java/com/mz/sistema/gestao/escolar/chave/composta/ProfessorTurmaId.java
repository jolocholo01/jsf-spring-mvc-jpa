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
public class ProfessorTurmaId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7459936872557380498L;
	@Column(name = "id_turma", nullable = false)
	private Long id_turma;

	@Column(name = "id_disciplina", nullable = false)
	private Long id_disciplina;

	public Long getId_turma() {
		return id_turma;
	}

	public void setId_turma(Long id_turma) {
		this.id_turma = id_turma;
	}

	public Long getId_disciplina() {
		return id_disciplina;
	}

	public void setId_disciplina(Long id_disciplina) {
		this.id_disciplina = id_disciplina;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id_disciplina == null) ? 0 : id_disciplina.hashCode());
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
		if (id_disciplina == null) {
			if (other.id_disciplina != null)
				return false;
		} else if (!id_disciplina.equals(other.id_disciplina))
			return false;
		if (id_turma == null) {
			if (other.id_turma != null)
				return false;
		} else if (!id_turma.equals(other.id_turma))
			return false;
		return true;
	}

}
