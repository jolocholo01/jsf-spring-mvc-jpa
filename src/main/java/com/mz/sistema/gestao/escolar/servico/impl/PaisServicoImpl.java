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

import com.mz.sistema.gestao.escolar.enumerado.Continente;
import com.mz.sistema.gestao.escolar.modelo.Pais;
import com.mz.sistema.gestao.escolar.servico.PaisServico;

@Service
@Transactional
public class PaisServicoImpl implements PaisServico {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void salvar(Pais pais) {
		em.merge(pais);
	}

	public void excluir(Pais pais) {
		em.remove(em.merge(pais));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pais> obterPaisPorContinente(Continente continente) {
		List<Pais> pais = em.createQuery("from Pais where continente = :uf order by nome")
				.setParameter("uf", continente).getResultList();
		if (!pais.isEmpty()) {
			return pais;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pais> obterTodosPaises() {
		return em.createQuery("from Pais  order by nome").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pais> obterPaisPorPesquisa(String pesquisa) {

		String continente = pesquisa.toUpperCase().replace("IA", "IÁ");
		List<Pais> pais = em.createQuery("FROM Pais WHERE nome LIKE '%" + pesquisa
				+ "%' or continente LIKE '%" + continente + "%' order by nome").getResultList();
		if (!pais.isEmpty()) {
			return pais;
		}
		return null;
	}

	@Override
	public Pais obterPaisExistente(Continente continente, String nome) {
		@SuppressWarnings("unchecked")
		List<Pais> paises = em.createQuery("from Pais where continente =:continente and nome =:NOME")
				.setParameter("continente", continente).setParameter("NOME", nome).getResultList();
		if (!paises.isEmpty()) {
			return paises.get(0);
		}
		return null;
	}

}
