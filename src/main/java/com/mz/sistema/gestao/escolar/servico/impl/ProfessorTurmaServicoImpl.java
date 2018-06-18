package com.mz.sistema.gestao.escolar.servico.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.modelo.ProfessorTurma;
import com.mz.sistema.gestao.escolar.servico.ProfessorTurmaServico;

@Service
@Transactional
public class ProfessorTurmaServicoImpl implements ProfessorTurmaServico {

	@PersistenceContext
	private EntityManager em;
	@Autowired
	private AuthenticationContext authenticationContext;

	@Override
	public void salvar(ProfessorTurma professorTurma) {
		em.merge(professorTurma);

	}
	public void excluir(ProfessorTurma professorTurma){

		em.remove(em.merge(professorTurma));
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ProfessorTurma> obterTurmasDoProfessorPorEscolaAno(Long idEscola, Integer ANO) {
		Funcionario professor = authenticationContext.getFuncionarioLogado();
		if (professor == null) {
			return null;
		}
		List<ProfessorTurma> professorTurmas = em
				.createQuery(
						"FROM ProfessorTurma pt join fetch pt.professor join fetch pt.disciplina join fetch pt.turma join fetch pt.turma.classe join fetch pt.turma.turno WHERE pt.escola.id=:idEscola AND pt.professor.id=:idProfessor AND pt.turma.ano=:ANO ORDER BY pt.turma.descricao")
				.setParameter("idEscola", idEscola).setParameter("idProfessor", professor.getId())
				.setParameter("ANO", ANO).getResultList();
		if (!professorTurmas.isEmpty()) {
			return professorTurmas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProfessorTurma> obterProfessorTurmaPorTurma(Integer idTurma) {
		List<ProfessorTurma> professorTurmas = em
				.createQuery(
						"FROM ProfessorTurma pt join fetch pt.professor join fetch pt.disciplina join fetch pt.turma join fetch pt.turma.classe join fetch pt.turma.turno WHERE pt.turma.id=:idTurma ORDER By pt.disciplina.descricao ")
				.setParameter("idTurma", idTurma).getResultList();
		if (!professorTurmas.isEmpty()) {
			return professorTurmas;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ProfessorTurma> obterProfessorTurmaPorTurmaOrdernarPorNome(Integer idTurma) {
		List<ProfessorTurma> professorTurmas = em
				.createQuery(
						"FROM ProfessorTurma pt join fetch pt.professor join fetch pt.disciplina join fetch pt.turma join fetch pt.turma.classe join fetch pt.turma.turno WHERE pt.turma.id=:idTurma ORDER By pt.professor.nome ")
				.setParameter("idTurma", idTurma).getResultList();
		if (!professorTurmas.isEmpty()) {
			return professorTurmas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProfessorTurma> obterProfessorTurmaPorTurmaOrdenarPorIdDisciplina(Integer idTurma) {
		List<ProfessorTurma> professorTurmas = em
				.createQuery(
						"FROM ProfessorTurma pt join fetch pt.professor join fetch pt.disciplina join fetch pt.turma join fetch pt.turma.classe join fetch pt.turma.turno WHERE pt.turma.id=:idTurma ORDER By pt.disciplina.id ")
				.setParameter("idTurma", idTurma).getResultList();
		if (!professorTurmas.isEmpty()) {
			return professorTurmas;
		}
		return null;
	}

	@Override
	public ProfessorTurma obterProfessorTurmaPorIdTurmarPorIdDisciplina(Integer idTurma, Integer idDisciplina) {
		@SuppressWarnings("unchecked")
		List<ProfessorTurma> professorTurmas = em
				.createQuery(
						"FROM ProfessorTurma pt join fetch pt.professor join fetch pt.disciplina join fetch pt.turma join fetch pt.turma.classe  WHERE pt.turma.id=:idTurma and  pt.disciplina.id=:idDisciplina ")
				.setParameter("idTurma", idTurma).setParameter("idDisciplina", idDisciplina).getResultList();
		if (!professorTurmas.isEmpty()) {
			return professorTurmas.get(0);
		}
		return null;
	}

}
