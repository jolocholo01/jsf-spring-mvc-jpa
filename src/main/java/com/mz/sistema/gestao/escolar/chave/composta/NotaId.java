
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
	private Integer id_matricula;
	@Column(name = "id_disciplina_classe", nullable = false)
	private Long id_disciplina_classe;

	public Integer getId_matricula() {
		return id_matricula;
	}

	public void setId_matricula(Integer id_matricula) {
		this.id_matricula = id_matricula;
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
		if (id_disciplina_classe == null) {
			if (other.id_disciplina_classe != null)
				return false;
		} else if (!id_disciplina_classe.equals(other.id_disciplina_classe))
			return false;
		if (id_matricula == null) {
			if (other.id_matricula != null)
				return false;
		} else if (!id_matricula.equals(other.id_matricula))
			return false;
		return true;
	}

}
