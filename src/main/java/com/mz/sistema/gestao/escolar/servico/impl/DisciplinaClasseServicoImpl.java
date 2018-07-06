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

import com.mz.sistema.gestao.escolar.modelo.DisciplinaClasse;
import com.mz.sistema.gestao.escolar.servico.DisciplinaClasseServico;

@Service
@Transactional
public class DisciplinaClasseServicoImpl implements DisciplinaClasseServico {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void salvar(DisciplinaClasse disciplinaClasse) {
		em.merge(disciplinaClasse);
	}

	@Override
	public void excluir(DisciplinaClasse disciplinaSelecionada) {
		em.remove(em.merge(disciplinaSelecionada));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DisciplinaClasse> obterDisciplinasPorClasse(long idClasse) {
		List<DisciplinaClasse> disciplinaClasses = em
				.createQuery("FROM DisciplinaClasse WHERE classe.id=:idClasse AND ativa=true Order by disciplina.descricao")
				.setParameter("idClasse", idClasse).getResultList();
		if (!disciplinaClasses.isEmpty()) {
			return disciplinaClasses;
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DisciplinaClasse> obterDisciplinasPorClassePorArea(long idClasse, String area) {
		List<DisciplinaClasse> disciplinaClasses = em
				.createQuery("FROM DisciplinaClasse WHERE classe.id=:idClasse AND area.descricao  LIKE '%"
						+ area.trim() + "%' AND ativa=true Order by disciplina.descricao")
				.setParameter("idClasse", idClasse).getResultList();

		return disciplinaClasses;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DisciplinaClasse> obterDisciplinasClassePorArea(Long idArea) {

		// OR disciplina.area.descricao='Tronco Comum'
		List<DisciplinaClasse> disciplinaClasses = em
				.createQuery("FROM DisciplinaClasse WHERE area.id=:idArea Order by disciplina.descricao")
				.setParameter("idArea", idArea).getResultList();
		if (!disciplinaClasses.isEmpty()) {
			return disciplinaClasses;
		}
		return null;
	}

	@Override
	public DisciplinaClasse disciplinasClasseExistente(Long idClasse, Integer idDiscipina, Long idArea) {

		@SuppressWarnings("unchecked")
		List<DisciplinaClasse> disciplinaClasses = em
				.createQuery(
						"FROM DisciplinaClasse WHERE classe.id=:idClasse AND disciplina.id=:idDiscipina AND area.id=:idArea")
				.setParameter("idClasse", idClasse).setParameter("idDiscipina", idDiscipina)
				.setParameter("idArea", idArea).getResultList();
		if (!disciplinaClasses.isEmpty()) {
			return disciplinaClasses.get(0);
		}
		return null;
	}

	@Override
	public DisciplinaClasse obterDisciplinasClassePorId(long id) {
		@SuppressWarnings("unchecked")
		List<DisciplinaClasse> disciplinaClasses = em.createQuery("FROM DisciplinaClasse WHERE id=:idDisciplinaClasse")
				.setParameter("idDisciplinaClasse", id).getResultList();
		if (!disciplinaClasses.isEmpty()) {
			return disciplinaClasses.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DisciplinaClasse> obterTodasDisciplinasClasse() {
		List<DisciplinaClasse> disciplinaClasses = em.createQuery("FROM DisciplinaClasse").getResultList();
		if (!disciplinaClasses.isEmpty()) {
			return disciplinaClasses;
		}
		return null;

	}

	@Override
	public List<DisciplinaClasse> obterDisciplinasPorClasseEArea(long idClasse, Long idArea) {
		@SuppressWarnings("unchecked")
		List<DisciplinaClasse> disciplinaClasses = em
				.createQuery("FROM DisciplinaClasse WHERE classe.id=:IdClasse AND area.id=:IdArea")
				.setParameter("IdClasse", idClasse).setParameter("IdArea", idArea).getResultList();
		if (!disciplinaClasses.isEmpty()) {
			return disciplinaClasses;
		}
		return null;
	}

	@Override
	public List<DisciplinaClasse> obterDisciplinasPorClassePorIdMatriz(long idMatriz) {
		@SuppressWarnings("unchecked")
		List<DisciplinaClasse> disciplinaClasses = em
				.createQuery("SELECT disciplinaClasses FROM Matriz WHERE id_matriz=:idMatriz")
				.setParameter("idMatriz", idMatriz).getResultList();
		if (!disciplinaClasses.isEmpty()) {
			return disciplinaClasses;
		}
		return null;
	}

	@Override
	public List<DisciplinaClasse> obterDisciplinasClassePorIdMatriz(long idMatriz) {
		@SuppressWarnings("unchecked")
		List<DisciplinaClasse> disciplinaClasses = em
				.createQuery("SELECT disciplinaClasses FROM Matriz m WHERE m.id_matriz=:idMatriz")
				.setParameter("idMatriz", idMatriz).getResultList();

		return disciplinaClasses;

	}

}
