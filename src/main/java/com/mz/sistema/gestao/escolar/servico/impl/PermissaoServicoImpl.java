package com.mz.sistema.gestao.escolar.servico.impl;

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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.enumerado.RoleName;
import com.mz.sistema.gestao.escolar.modelo.Permissao;
import com.mz.sistema.gestao.escolar.servico.PermissaoServico;

@Transactional
@Service
public class PermissaoServicoImpl implements PermissaoServico {
	@PersistenceContext
	private EntityManager em;

	@Override
	public void salvar(Permissao permissao) {
		em.merge(permissao);
	}

	public void excluir(Permissao permissao) {
		em.remove(em.merge(permissao));
	}

	@Override
	public Permissao salvarRetornarPermissao(Permissao permissao) {
		em.merge(permissao);
		return permissao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Permissao> listarPermissoesPorDistrito() {
		try {
			List<Permissao> permissaoes = em
					.createQuery("from Permissao WHERE descricao !='ROLE_ALUNO' AND descricao !='ROLE_MINISTRO' "
							+ "AND descricao !='ROLE_DIRECTOR_PROVINCIA' AND descricao !='ROLE_DIRECTOR_DISTRITO' AND descricao !='ROLE_PROGRAMADOR' ORDER BY nome")
					.getResultList();
			if (!permissaoes.isEmpty()) {
				return permissaoes;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Permissao> obterPermissoesPorEscola() {
		try {
			List<Permissao> permissaoes = em
					.createQuery("from Permissao WHERE descricao !='ROLE_ALUNO' AND descricao !='ROLE_MINISTRO' "
							+ "AND descricao !='ROLE_DIRECTOR_PROVINCIA' AND descricao !='ROLE_DIRECTOR_DISTRITO' AND descricao !='ROLE_DIRECTOR' AND descricao !='ROLE_PROGRAMADOR'  ORDER BY nome")
					.getResultList();
			if (!permissaoes.isEmpty()) {
				return permissaoes;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Permissao obterUsuarioPorIdPermicao(long idUsuario) {
		List<Permissao> permissaos = em.createQuery("select permissoes from Usuario where usuario_id=:idUsuario")
				.setParameter("idUsuario", idUsuario).getResultList();

		if (!permissaos.isEmpty()) {
			return permissaos.get(0);
		}
		return null;
	}

	@Override
	public Permissao obterPermisaoPorId(Long id) {
		@SuppressWarnings("unchecked")
		List<Permissao> permissaos = em.createQuery("from Permissao where id=:id").setParameter("id", id)
				.getResultList();

		if (!permissaos.isEmpty()) {
			return permissaos.get(0);
		}
		return null;
	}

	@Override
	public List<Permissao> obterPermissoessPorDscricao(String pesquisa) {
		try {
			@SuppressWarnings("unchecked")
			List<Permissao> permissaoes = em.createQuery("from Permissao WHERE descricao LIKE  '%" + pesquisa
					+ "%' or nome LIKE  '%" + pesquisa + "%' ORDER BY nome").getResultList();
			if (!permissaoes.isEmpty()) {
				return permissaoes;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Permissao obterPermissaoExistente(String nome) {
		try {
			@SuppressWarnings("unchecked")
			List<Permissao> permissaoes = em.createQuery("from Permissao WHERE  nome =:NOME").setParameter("NOME", nome)
					.getResultList();
			if (!permissaoes.isEmpty()) {
				return permissaoes.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Permissao obterPermissaoPorDscricao(RoleName descricao) {
		try {
			@SuppressWarnings("unchecked")
			List<Permissao> permissaoes = em.createQuery("from Permissao WHERE descricao LIKE  '%" + descricao + "%'")
					.getResultList();
			if (!permissaoes.isEmpty()) {
				return permissaoes.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
