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

import com.mz.sistema.gestao.escolar.enumerado.Ciclo;
import com.mz.sistema.gestao.escolar.modelo.Disciplina;
import com.mz.sistema.gestao.escolar.servico.DisciplinaServico;
import com.mz.sistema.gestao.escolar.util.Replace;

@Service
@Transactional
public class DisciplinaServicoImpl implements DisciplinaServico {
	@PersistenceContext
	private EntityManager em;

	@Override
	public void salvar(Disciplina disciplina) {
		disciplina.setDescricao(disciplina.getDescricao().toUpperCase());
		em.merge(disciplina);

	}

	@Override
	public Disciplina salvarRetornar(Disciplina disciplina) {
		disciplina.setDescricao(Replace.letrasMasculas(disciplina.getDescricao().trim()));
		disciplina = em.merge(disciplina);
		return disciplina;
	}

	@Override
	public void excluir(Disciplina disciplina) {
		em.remove(em.merge(disciplina));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Disciplina> obterTodasDisciplinas() {
		List<Disciplina> disciplinas = em.createQuery("FROM Disciplina  ORDER BY descricao").getResultList();
		if (!disciplinas.isEmpty()) {
			return disciplinas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Disciplina> obterDisciplinasAtivas() {
		List<Disciplina> disciplinas = em.createQuery("FROM Disciplina WHERE ativa = true order by descricao")
				.getResultList();
		if (!disciplinas.isEmpty()) {
			return disciplinas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Disciplina> obterDisciplinaPorNome(String disciplina) {
		return em.createQuery("FROM Disciplina WHERE descricao LIKE '%" + disciplina + "%' order by descricao")
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Disciplina> obterDisciplinaPorCiclo(Ciclo ciclo) {
		return em.createQuery("FROM Disciplina WHERE area.ciclo=:Ciclo order by descricao").setParameter("Ciclo", ciclo)
				.getResultList();
	}

	@Override
	public Disciplina obterDisciplinaPorId(Long idDisciplina) {
		@SuppressWarnings("unchecked")
		List<Disciplina> disciplinas = em.createQuery("FROM Disciplina WHERE id=:idDisciplina")
				.setParameter("idDisciplina", idDisciplina).getResultList();
		if (!disciplinas.isEmpty()) {
			return disciplinas.get(0);
		}
		return null;
	}

	@Override
	public Disciplina disciplinaExisente(String descricao_parametro) {
		String descricao = Replace.letrasMasculas(descricao_parametro).trim();
		@SuppressWarnings("unchecked")
		List<Disciplina> disciplinas = em
				.createQuery("FROM Disciplina WHERE descricao like '%" + descricao.toUpperCase() + "%'")
				.getResultList();
		if (!disciplinas.isEmpty()) {
			return disciplinas.get(0);
		}
		return null;
	}

	@Override
	public List<Disciplina> obterDisciplinasPorAreas(Long idArea) {
		@SuppressWarnings("unchecked")
		List<Disciplina> disciplinas = em.createQuery("FROM Disciplina WHERE area.id=:idArea ")
				.setParameter("idArea", idArea).getResultList();
		if (!disciplinas.isEmpty()) {
			return disciplinas;
		}
		return null;
	}

	@Override
	public List<Disciplina> obterDisciplinaPorPesquisa(String pesquisa) {
		String descricao = Replace.letrasMasculas(pesquisa).trim();
		@SuppressWarnings("unchecked")
		List<Disciplina> disciplinas = em.createQuery("FROM Disciplina WHERE  descricao LIKE '%"
				+ descricao.toUpperCase() + "%' OR sigla LIKE '%" + pesquisa + "%' OR codigo LIKE '%" + pesquisa.toUpperCase() + "%' ORDER BY descricao ")
				.getResultList();
		if (!disciplinas.isEmpty()) {
			return disciplinas;
		}
		return null;
	}

}
