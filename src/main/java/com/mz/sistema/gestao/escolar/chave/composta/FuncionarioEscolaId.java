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
public class FuncionarioEscolaId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6470205259842762263L;
	@Column(name = "id_funcionario", nullable = false)
	private Long id_funcionario;
	@Column(name = "id_escola", nullable = false)
	private Long id_escola;
	@Column(name = "id_permissao", nullable = false)
	private Long id_permissao;

	public Long getId_funcionario() {
		return id_funcionario;
	}

	public void setId_funcionario(Long id_funcionario) {
		this.id_funcionario = id_funcionario;
	}

	public Long getId_escola() {
		return id_escola;
	}

	public void setId_escola(Long id_escola) {
		this.id_escola = id_escola;
	}

	public Long getId_permissao() {
		return id_permissao;
	}

	public void setId_permissao(Long id_permissao) {
		this.id_permissao = id_permissao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id_escola == null) ? 0 : id_escola.hashCode());
		result = prime * result + ((id_funcionario == null) ? 0 : id_funcionario.hashCode());
		result = prime * result + ((id_permissao == null) ? 0 : id_permissao.hashCode());
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
		FuncionarioEscolaId other = (FuncionarioEscolaId) obj;
		if (id_escola == null) {
			if (other.id_escola != null)
				return false;
		} else if (!id_escola.equals(other.id_escola))
			return false;
		if (id_funcionario == null) {
			if (other.id_funcionario != null)
				return false;
		} else if (!id_funcionario.equals(other.id_funcionario))
			return false;
		if (id_permissao == null) {
			if (other.id_permissao != null)
				return false;
		} else if (!id_permissao.equals(other.id_permissao))
			return false;
		return true;
	}

}
