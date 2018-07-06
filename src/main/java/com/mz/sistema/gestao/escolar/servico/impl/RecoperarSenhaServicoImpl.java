package com.mz.sistema.gestao.escolar.servico.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.modelo.RecoperarSenha;
import com.mz.sistema.gestao.escolar.servico.RecoperarSenhaServico;

@Service
@Transactional
public class RecoperarSenhaServicoImpl implements RecoperarSenhaServico {
	@PersistenceContext
	private EntityManager em;

	@Override
	public void salvar(RecoperarSenha recoperarSenha) {
		em.merge(recoperarSenha);

	}

	@Override
	public void excluir(RecoperarSenha recoperarSenha) {
		em.remove(em.merge(recoperarSenha));

	}

}
