// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.chave.composta;

import java.io.Serializable;

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
	
	
	@Column(name = "id_escola", nullable = false)
	private long id_escola;
	
	@Column(name = "id_disciplina", nullable = false)
	private Integer id_disciplina;
	
	public Integer getId_turma() {
		return id_turma;
	}

	public void setId_turma(Integer id_turma) {
		this.id_turma = id_turma;
	}

	
	public long getId_escola() {
		return id_escola;
	}

	public void setId_escola(long id_escola) {
		this.id_escola = id_escola;
	}

	public Integer getId_disciplina() {
		return id_disciplina;
	}

	public void setId_disciplina(Integer id_disciplina) {
		this.id_disciplina = id_disciplina;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id_disciplina == null) ? 0 : id_disciplina.hashCode());
		result = prime * result + (int) (id_escola ^ (id_escola >>> 32));
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
		if (id_escola != other.id_escola)
			return false;
		if (id_turma == null) {
			if (other.id_turma != null)
				return false;
		} else if (!id_turma.equals(other.id_turma))
			return false;
		return true;
	}

	

	

}
