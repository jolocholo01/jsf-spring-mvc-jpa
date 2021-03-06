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

import com.mz.sistema.gestao.escolar.modelo.Nota;
import com.mz.sistema.gestao.escolar.servico.NotaServico;

@Service
@Transactional
public class NotaServicoImpl implements NotaServico {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void salvar(Nota nota) {
		em.merge(nota);
	}

	@Override
	public void excluir(Nota nota) {
		
		em.createNativeQuery("DELETE FROM Nota WHERE id_matricula = ? AND id_disciplina = ? ").setParameter(1, nota.getMatricula().getId())
		.setParameter(2, nota.getDisciplina().getId()).executeUpdate();
	

	}

	@Override
	public List<Nota> listarNotas() {
		@SuppressWarnings("unchecked")
		List<Nota> notas = em.createQuery("FROM Nota").getResultList();
		if (!notas.isEmpty()) {
			return notas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Nota> listarNotasAtivas() {
		List<Nota> notas = em.createQuery("FROM Nota WHERE ativo=true").getResultList();
		if (!notas.isEmpty()) {
			return notas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Nota> obterNotaPorTurma(Long idTurma) {
		List<Nota> notas = em.createQuery("FROM Nota WHERE matricula.turma.id=:idTurma")
				.setParameter("idTurma", idTurma).getResultList();
		if (!notas.isEmpty()) {
			return notas;
		}
		return null;
	}

	@Override
	public List<Nota> obterNotasPorIdTurmaEDisciplinaDoProfessor(Long idDisciplina, Long idTurma) {
		@SuppressWarnings("unchecked")
		List<Nota> notas = em
				.createQuery(
						"FROM Nota WHERE disciplina.id=:idDisciplina AND matricula.turma.id=:idTurma  ORDER BY matricula.numeroAlunoTurma")
				.setParameter("idDisciplina", idDisciplina).setParameter("idTurma", idTurma)
				.getResultList();
		if (!notas.isEmpty()) {
			return notas;
		}
		return null;
	}

	@Override
	public List<Nota> obterNotasPorIdTurmaPorDisciplinaDoProfessorPorTrimetres(Long idDisciplina, Long idTurma,
			Long idTrimetre) {
		@SuppressWarnings("unchecked")
		List<Nota> notas = em
				.createQuery(
						"FROM Nota WHERE disciplina.id=:idDisciplina AND matricula.turma.id=:idTurma AND trimestre.id=:idTrimetre ORDER BY matricula.aluno.nome")
				.setParameter("idDisciplina", idDisciplina).setParameter("idTrimetre", idTrimetre)
				.setParameter("idTurma", idTurma).getResultList();
		if (!notas.isEmpty()) {
			return notas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Nota> obterNotaPorIdAluno(Long idAluno, long idClasse, Integer ano) {

		List<Nota> notas = em
				.createQuery(
						"FROM Nota WHERE matricula.aluno.id=:idAluno AND matricula.classe.id=:idClasse AND matricula.ano=:ANO"
								+ " ORDER BY disciplina.descricao")
				.setParameter("idAluno", idAluno).setParameter("idClasse", idClasse).setParameter("ANO", ano)
				.getResultList();
		if (!notas.isEmpty()) {
			return notas;
		}
		return null;
	}

	@Override
	public Double obterMediaTrimestralOuAnulDoAluno(Long idAluno, long idClasse, String tipoMediatrimestre) {

		Double media = (Double) em
				.createQuery("SELECT AVG(" + tipoMediatrimestre
						+ ") As MediaTrimestral FROM Nota WHERE matricula.aluno.id=:idAluno AND matricula.classe.id=:idClasse")
				.setParameter("idAluno", idAluno).setParameter("idClasse", idClasse).getSingleResult();

		return media;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Nota obterEscolaDoAluno(Long idEscola) {

		List<Nota> notas = em.createQuery("FROM Nota WHERE matricula.escola.id=:idEscola")
				.setParameter("idEscola", idEscola).getResultList();
		if (!notas.isEmpty()) {
			return notas.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Nota obterAlunoPorMatriculaPorDisciplinaPorTrimestre(Long idMatricula, Long idDisciplina,
			long idTrimestre) {

		List<Nota> notas = em
				.createQuery("FROM Nota WHERE disciplina.id=:idDisciplina AND matricula.id=:idMatricula"
						+ " AND trimestre.id=:idTrimestre ")
				.setParameter("idDisciplina", idDisciplina).setParameter("idTrimestre", idTrimestre)
				.setParameter("idMatricula", idMatricula).getResultList();
		if (!notas.isEmpty()) {
			return notas.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Nota obterMediaAc(Long idMatricula, Long idDisciplina, long idTrimestre) {
		List<Nota> notas = em
				.createQuery(
						"SELECT (ac1+ac2+ac3+ac4 )/4 AS MEDIATESTE FROM Nota WHERE disciplina.id=:idDisciplina AND matricula.id=:idMatricula"
								+ " AND trimestre.id=:idTrimestre ")
				.setParameter("idDisciplina", idDisciplina).setParameter("idTrimestre", idTrimestre)
				.setParameter("idMatricula", idMatricula).getResultList();
		if (!notas.isEmpty()) {
			return notas.get(0);
		}
		return null;
	}

	@Override
	public List<Nota> obterDisciplinaDoAlunoPorAnoAndId(Long idAluno, Integer ano) {
		@SuppressWarnings("unchecked")
		List<Nota> notas = em.createQuery("FROM Nota WHERE matricula.aluno.id=:IdAuno AND matricula.ano=:ano")
				.setParameter("IdAuno", idAluno).setParameter("ano", ano).getResultList();
		if (!notas.isEmpty()) {
			return notas;
		}
		return null;
	}

	@Override
	public List<Nota> obterNotasPorIdMatricula(Long idMatricula) {
		@SuppressWarnings("unchecked")
		List<Nota> notas = em.createQuery("FROM Nota WHERE matricula.id=:idMatricula ORDER BY disciplina.descricao")
				.setParameter("idMatricula", idMatricula).getResultList();
		if (!notas.isEmpty()) {
			return notas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Nota obterNotasPorIdMatriculaPorDisciplina(Long idMatricula, Long idDisciplina) {

		List<Nota> notas = em
				.createQuery("FROM Nota WHERE disciplina.id=:idDisciplina AND matricula.id=:idMatricula")
				.setParameter("idDisciplina", idDisciplina).setParameter("idMatricula", idMatricula)
				.getResultList();
		if (!notas.isEmpty()) {
			return notas.get(0);
		}
		return null;
	}

}
