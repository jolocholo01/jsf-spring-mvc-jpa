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

import com.mz.sistema.gestao.escolar.modelo.RecuperarSenha;
import com.mz.sistema.gestao.escolar.servico.RecuperarSenhaServico;

@Service
@Transactional
public class RecuperarSenhaServicoImpl implements RecuperarSenhaServico {
	@PersistenceContext
	private EntityManager em;

	@Override
	public void salvar(RecuperarSenha recoperarSenha) {
		em.merge(recoperarSenha);

	}

	@Override
	public void excluir(RecuperarSenha recoperarSenha) {
		em.remove(em.merge(recoperarSenha));

	}

}
