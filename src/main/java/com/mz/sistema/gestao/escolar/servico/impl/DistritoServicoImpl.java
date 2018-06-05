package com.mz.sistema.gestao.escolar.servico.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.enumerado.Provincia;
import com.mz.sistema.gestao.escolar.modelo.Distrito;
import com.mz.sistema.gestao.escolar.servico.DistritoServico;

@Service
@Transactional
public class DistritoServicoImpl implements DistritoServico {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void salvar(Distrito distrito) {
		em.merge(distrito);

	}

	@Override
	public void excluir(Distrito distrito) {
		em.remove(em.merge(distrito));

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Distrito> listarTodas() {
		return em.createQuery("from Distrito order by nome").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Distrito> obterDistritosDaProvincia(String provincia) {

		return em.createQuery("from Distrito where provincia like '%"+provincia+"%' order by nome")
				.getResultList();
	}

	@Override
	public Distrito obterDistritosPorId(Long idDistrito) {
		return (Distrito) em.createQuery("from Distrito where id = :idDistrito").setParameter("idDistrito", idDistrito)
				.getSingleResult();
	}

	@Override
	public Distrito obterDistritoExistente(String provincia, String nome) {
		@SuppressWarnings("unchecked")
		List<Distrito> distritos = em.createQuery("from Distrito where provincia =:Provincia and nome =:NOME")
				.setParameter("Provincia", provincia).setParameter("NOME", nome).getResultList();
		if (!distritos.isEmpty()) {
			return distritos.get(0);
		}
		return null;
	}

}
