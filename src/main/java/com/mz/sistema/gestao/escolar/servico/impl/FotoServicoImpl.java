package com.mz.sistema.gestao.escolar.servico.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.modelo.Foto;
import com.mz.sistema.gestao.escolar.servico.FotoServico;

@Service
@Transactional
public class FotoServicoImpl implements FotoServico {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Foto salvar(Foto foto) {
		foto = em.merge(foto);
		return foto;

	}

	@Override
	public Foto obterFotoPorIdUsuario(Long idUsuario) {
		@SuppressWarnings("unchecked")
		List<Foto> fotos = em.createQuery("FROM Foto WHERE usuario.id=:IdUsuario").setParameter("IdUsuario", idUsuario)
				.getResultList();
		if (!fotos.isEmpty()) {
			return fotos.get(0);
		}
		return null;
	}

}
