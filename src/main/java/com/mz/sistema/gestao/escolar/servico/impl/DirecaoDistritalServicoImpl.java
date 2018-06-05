package com.mz.sistema.gestao.escolar.servico.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.modelo.Distrital;
import com.mz.sistema.gestao.escolar.servico.DirecaoDistritalServico;

@Service
@Transactional
public class DirecaoDistritalServicoImpl implements DirecaoDistritalServico {
	@PersistenceContext
	private EntityManager em;

	@Override
	public void salvar(Distrital distrital) {
		em.merge(distrital);
	}

	@Override
	public void excluir(Distrital distrital) {
		em.remove(em.merge(distrital));

	}

	@SuppressWarnings("unchecked")
	@Override
	public Distrital obterDirecaoDistritalPorId(long idDistrital) {
		List<Distrital> distritais = em.createQuery("FROM Distrital WHERE id=:idDistrital")
				.setParameter("idDistrital", idDistrital).getResultList();
		if (!distritais.isEmpty()) {
			return distritais.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Distrital> obterDirecaoDistritalPorIdDirecaoProvincial(long idDirecaoProvincial) {
		List<Distrital> distritais = em.createQuery("FROM Distrital WHERE direcaoProvincial.id=:idDirecaoProvincial")
				.setParameter("idDirecaoProvincial", idDirecaoProvincial).getResultList();
		if (!distritais.isEmpty()) {
			return distritais;
		}
		return null;
	}

	@Override
	public List<Distrital> obterDirecaoDistritalPorIdUsuario(Long idUsuario) {
		@SuppressWarnings("unchecked")
		List<Distrital> distritais = em.createQuery("SELECT distritais FROM Usuario u WHERE u.id=:idUsuario ")
				.setParameter("idUsuario", idUsuario).getResultList();
		if (!distritais.isEmpty()) {
			return distritais;
		}
		return null;
	}

	@Override
	public Distrital obterDirecaoDistritalExistente(Long idDistrito) {
		@SuppressWarnings("unchecked")
		List<Distrital> distritais = em.createQuery("FROM Distrital WHERE endereco.distrito.id=:idDistrito")
				.setParameter("idDistrito", idDistrito).getResultList();
		if (!distritais.isEmpty()) {
			return distritais.get(0);
		}
		
		return null;
	}

	@Override
	public List<Distrital> obterDirecaoDistritalPorDescricao(String pesquisa) {
		@SuppressWarnings("unchecked")
		List<Distrital> distritais = em.createQuery("FROM Distrital WHERE endereco.distrito.nome LIKE '%"+pesquisa+"%' OR descricao LIKE '%"+pesquisa+"%'")
				.getResultList();
		if (!distritais.isEmpty()) {
			return distritais;
		}
		
		return null;
	}

	@Override
	public List<Distrital> obterTodosServicosDistritais() {
		@SuppressWarnings("unchecked")
		List<Distrital> distritais = em.createQuery("FROM Distrital ORDER BY endereco.distrito.nome")
				.getResultList();
		if (!distritais.isEmpty()) {
			return distritais;
		}
		
		return null;
	}

}
