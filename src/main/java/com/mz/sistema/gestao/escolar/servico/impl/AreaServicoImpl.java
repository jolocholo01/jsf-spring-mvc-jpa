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

import com.mz.sistema.gestao.escolar.modelo.Area;
import com.mz.sistema.gestao.escolar.servico.AreaServico;
import com.mz.sistema.gestao.escolar.util.TipoLetra;

@Service
@Transactional
public class AreaServicoImpl implements AreaServico {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void salvar(Area area) {
		area.setDescricao(area.getDescricao().replace("Comunicacao", "Comunicação").replace("Ciencias", "Ciências")
				.replace("Matematica", "Matemática").replace(" E ", " e ").replace(" De ", " de ").replace(" A ", " a ")
				.replace("  ", " ").replace("   ", " ").replace("   ", " ").replace("    ", " ").toUpperCase());
		em.merge(area);
	}

	@Override
	public void excluir(Area area) {
		em.remove(em.merge(area));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Area> obterTodasAreas() {
		List<Area> areas = em.createQuery("FROM Area ").getResultList();
		if (!areas.isEmpty()) {
			return areas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Area> obterTodasAreasPorCiclo(String ciclo) {
		List<Area> areas = em.createQuery("FROM Area WHERE  ciclo like '" + ciclo + "' order by descricao")
				.getResultList();
		if (!areas.isEmpty()) {
			return areas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Area> obterAreasPorPesquisa(String pesquisa) {
		List<Area> areas = em.createQuery("FROM Area WHERE  descricao  like '%" + pesquisa.toUpperCase().trim()
				+ "%' Or ciclo like '%" +  pesquisa.toUpperCase().trim() + "%'").getResultList();
		if (!areas.isEmpty()) {
			return areas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Area obterAreaExistente(String nomeArea, String ciclo) {
		String nome = TipoLetra.capitalizeString(nomeArea).replace("Comunicacao", "Comunicação")
				.replace("Ciencias", "Ciências").replace("Matematica", "Matemática").replace(" E ", " e ")
				.replace(" De ", " de ").replace(" A ", " a ").replace("  ", " ").replace("   ", " ")
				.replace("   ", " ").replace("    ", " ");

		List<Area> areas = em
				.createQuery(
						"FROM Area WHERE descricao LIKE '%" + nome.trim() + "%' AND " + "ciclo LIKE '%" + ciclo + "%'")
				.getResultList();
		if (!areas.isEmpty()) {
			return areas.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Area> obterAreasPorCiclo(String ciclo) {
		List<Area> areas = em
				.createQuery("FROM Area WHERE  ciclo like '%" + ciclo + "%' and ativa=true order by descricao ")
				.getResultList();
		if (!areas.isEmpty()) {
			return areas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Area> obterAreasPorCicloOrdenarPorOrdemDecrescente(String ciclo) {
		List<Area> areas = em
				.createQuery("FROM Area WHERE  ciclo like '%" + ciclo + "%' and ativa=true order by descricao DESC")
				.getResultList();
		if (!areas.isEmpty()) {
			return areas;
		}
		return null;
	}

	@Override
	public List<Area> obterAreasPorCicloOrdenarPorOrdemDecrescenteDiferenteDaArea(String ciclo, String tipoArea) {
		@SuppressWarnings("unchecked")
		List<Area> areas = em.createQuery("FROM Area WHERE  ciclo like '%" + ciclo + "%' and descricao  <> '" + tipoArea
				+ "' and ativa=true order by descricao DESC").getResultList();
		if (!areas.isEmpty()) {
			return areas;
		}
		return null;
	}

}
