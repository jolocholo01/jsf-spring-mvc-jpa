
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
public class NotaId implements Serializable {
	private static final long serialVersionUID = -5574118779024461601L;

	@Column(name = "id_matricula", nullable = false)
	private Long id_matricula;
	@Column(name = "id_disciplina", nullable = false)
	private Long id_disciplina;

	
	public Long getId_matricula() {
		return id_matricula;
	}

	public void setId_matricula(Long id_matricula) {
		this.id_matricula = id_matricula;
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
		result = prime * result + ((id_matricula == null) ? 0 : id_matricula.hashCode());
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
		NotaId other = (NotaId) obj;
		if (id_disciplina== null) {
			if (other.id_disciplina != null)
				return false;
		} else if (!id_disciplina.equals(other.id_disciplina))
			return false;
		if (id_matricula == null) {
			if (other.id_matricula != null)
				return false;
		} else if (!id_matricula.equals(other.id_matricula))
			return false;
		return true;
	}

}
