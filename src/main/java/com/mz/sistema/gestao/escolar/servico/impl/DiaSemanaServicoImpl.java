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

import com.mz.sistema.gestao.escolar.modelo.DiaSemana;
import com.mz.sistema.gestao.escolar.servico.DiaSemanaServico;

@Service
@Transactional
public class DiaSemanaServicoImpl implements DiaSemanaServico {
	@PersistenceContext
	private EntityManager em;

	@Override
	public void salvar(DiaSemana diaSemana) {
		em.merge(diaSemana);
		
	}

	@Override
	public void excluir(DiaSemana diaSemana) {
		em.remove(em.merge(diaSemana));
		
	}

	@Override
	public List<DiaSemana> obterDiaSemanaPorPesquisa(String pesquisa) {
		@SuppressWarnings("unchecked")
		List<DiaSemana> diaSemanas = em
				.createQuery("From DiaSemana  where sigla LIKE '%"+pesquisa+"%' OR descricao LIKE '%"+pesquisa+"%' "
						+ "order by ordem")
				.getResultList();

		if (!diaSemanas.isEmpty()) {
			return diaSemanas;
		}
		return null;
	}

	@Override
	public DiaSemana obterDiaSemanaPorSigla(String sigla) {
		@SuppressWarnings("unchecked")
		List<DiaSemana> diaSemanas = em
				.createQuery("From DiaSemana  where sigla=:SIGLA")
				.setParameter("SIGLA", sigla).getResultList();

		if (!diaSemanas.isEmpty()) {
			return diaSemanas.get(0);
		}
		return null;
	}

	@Override
	public DiaSemana obterDiaSemanaExistente(String sigla) {
		@SuppressWarnings("unchecked")
		List<DiaSemana> diaSemanas = em
				.createQuery("From DiaSemana  where sigla=:SIGLA")
				.setParameter("SIGLA", sigla).getResultList();

		if (!diaSemanas.isEmpty()) {
			return diaSemanas.get(0);
		}
		return null;
	}

	
}
