package com.mz.sistema.gestao.escolar.servico.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.modelo.Horario;
import com.mz.sistema.gestao.escolar.servico.HorarioServico;

@Service
@Transactional
public class HorarioServicoImpl implements HorarioServico {
	@PersistenceContext
	private EntityManager em;

	@Override
	public void salvar(Horario horario) {
		em.merge(horario);
	}

	@Override
	public void excluir(Horario horario) {
		em.createNativeQuery("DELETE FROM horario WHERE disciplina_id=? AND turma_id = ?")
				.setParameter(1, horario.getDisciplina().getId()).setParameter(2, horario.getTurma().getId())
				.executeUpdate();

		//em.remove(em.merge(horario));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Horario> obterHorarioPorIdHorarioAulaPoridProfessor(Long idHorarioAula, Long idProfessor, Integer ano) {
		List<Horario> horarios = em
				.createQuery(
						"From Horario h join fetch h.turma.classe where h.horarioAula.id=:idHorarioAula AND h.professor.id=:idProfessor AND h.ano=:ANO ")
				.setParameter("idHorarioAula", idHorarioAula).setParameter("idProfessor", idProfessor)
				.setParameter("ANO", ano).getResultList();
		if (!horarios.isEmpty()) {
			return horarios;
		}
		return null;
	}

	@Override
	public List<Horario> obterHorarioPorIdHorarioAulaPoridTurma(Long idHorarioAula, Integer idTurma) {
		@SuppressWarnings("unchecked")
		List<Horario> horarios = em
				.createQuery("From Horario h  where h.horarioAula.id=:idHorarioAula AND h.turma.id=:idTurma ")
				.setParameter("idHorarioAula", idHorarioAula).setParameter("idTurma", idTurma).getResultList();

		if (!horarios.isEmpty()) {
			return horarios;
		}
		return null;
	}

	@Override
	public List<Horario> obterHorarioPorIdTurno(Long idTurno, Long idEscola, Integer ano) {
		@SuppressWarnings("unchecked")
		List<Horario> horarios = em
				.createQuery(
						"From Horario h  where h.horarioAula.turno.id=:idTurno AND h.horarioAula.escola.id=:idEscola  AND h.ano=:ANO")
				.setParameter("idTurno", idTurno).setParameter("idEscola", idEscola).setParameter("ANO", ano)
				.getResultList();

		if (!horarios.isEmpty()) {
			return horarios;
		}
		return null;
	}

	@Override
	public Horario obterHorarioPorIdHorarioAulaIdProfessorIdTurmaIdDiaSemaIdDiciplina(long idHorarioAula,
			Long idFuncionario, Long idDiaSemana, Integer idTurma, Integer idDisciplina) {
		@SuppressWarnings("unchecked")
		List<Horario> horarios = em
				.createQuery(
						"From Horario h  where h.horarioAula.id=:idHorarioAula AND h.professor.id=:idFuncionario AND h.diaSemana.id=:idDiaSemana "
								+ " AND h.turma.id=:idTurma AND h.disciplina.id=:idDisciplina")
				.setParameter("idHorarioAula", idHorarioAula).setParameter("idFuncionario", idFuncionario)
				.setParameter("idDiaSemana", idDiaSemana).setParameter("idTurma", idTurma)
				.setParameter("idDisciplina", idDisciplina).getResultList();

		if (!horarios.isEmpty()) {
			return horarios.get(0);
		}
		return null;
	}

	@Override
	public Horario obterHorarioPorIdHorarioAulaIdTurmaIdDiaSemaIdDiciplina(long idHorarioAula, Long idDiaSemana,
			Integer idTurma, Integer idDisciplina) {
		@SuppressWarnings("unchecked")
		List<Horario> horarios = em
				.createQuery("From Horario h  where h.horarioAula.id=:idHorarioAula AND h.diaSemana.id=:idDiaSemana "
						+ " AND h.turma.id=:idTurma AND h.disciplina.id=:idDisciplina")
				.setParameter("idHorarioAula", idHorarioAula).setParameter("idDiaSemana", idDiaSemana)
				.setParameter("idTurma", idTurma).setParameter("idDisciplina", idDisciplina).getResultList();

		if (!horarios.isEmpty()) {
			return horarios.get(0);
		}
		return null;
	}

	@Override
	public List<Horario> obterHorarioPorIdTurmaPorIdDiciplina(Integer idTurma, Integer idDisciplina) {
		@SuppressWarnings("unchecked")
		List<Horario> horarios = em
				.createQuery("From Horario h  where h.turma.id=:idTurma AND h.disciplina.id=:idDisciplina")
				.setParameter("idTurma", idTurma).setParameter("idDisciplina", idDisciplina).getResultList();

		if (!horarios.isEmpty()) {
			return horarios;
		}
		return null;
	}

	@Override
	public List<Horario> obterHorarioIdTurma(Integer idTurma) {
		@SuppressWarnings("unchecked")
		List<Horario> horarios = em.createQuery("From Horario h  where h.turma.id=:idTurma ")
				.setParameter("idTurma", idTurma).getResultList();

		if (!horarios.isEmpty()) {
			return horarios;
		}
		return null;
	}

	@Override
	public boolean verificarHorarioMinhaTurma(Integer idTurma) {
		@SuppressWarnings("unchecked")
		List<Horario> horarios = em.createQuery("From Horario h  where h.turma.id=:idTurma ")
				.setParameter("idTurma", idTurma).getResultList();

			return horarios.isEmpty();
	}

	@Override
	public List<Horario> obterHorarioPorIdTurma(Integer idTurma) {
		@SuppressWarnings("unchecked")
		List<Horario> horarios = em.createQuery("From Horario h  where h.turma.id=:idTurma ")
				.setParameter("idTurma", idTurma).getResultList();

			return horarios;
	}

}
