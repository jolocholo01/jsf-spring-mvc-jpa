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

import com.mz.sistema.gestao.escolar.modelo.Turma;
import com.mz.sistema.gestao.escolar.servico.TurmaServico;

@Service
@Transactional
public class TurmaServicoImpl implements TurmaServico {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void salvar(Turma turma) {
		em.merge(turma);
	}

	@Override
	public Turma salvarRetornarTurma(Turma turma) {
		turma = em.merge(turma);
		return turma;
	}

	@Override
	public void excluir(Turma turma) {
		turma = obterTurmaPorIdLiftJoinFetch(turma.getId());
		em.remove(em.merge(turma));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Turma obterTurmaPorId(Long idTurma) {
		List<Turma> turmas = em.createQuery("From Turma t where t.id=:idTurma")
				.setParameter("idTurma", idTurma).getResultList();
		if (!turmas.isEmpty()) {
			return turmas.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Turma obterTurmaPorIdLiftJoinFetch(Long idTurma) {
		List<Turma> turmas = em.createQuery("From Turma t left join fetch t.horarios  where t.id=:idTurma")
				.setParameter("idTurma", idTurma).getResultList();
		if (!turmas.isEmpty()) {
			return turmas.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Turma> obterTurmaPorIdClasse(Long idClasse, Integer ano) {
		return em.createQuery("From Turma t  where t.classe.id=:idClasse AND t.ano=:ano ORDER BY t.descricao")
				.setParameter("idClasse", idClasse).setParameter("ano", ano).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Turma> obterTurmaPorClasseTurno(long idClasse, Long idTurno, Integer ano) {
		List<Turma> turmas = em
				.createQuery(
						"From Turma t  where t.classe.id=:idClasse AND t.turno.id=:idTurno AND t.ano=:ano ORDER BY t.descricao ")
				.setParameter("idClasse", idClasse).setParameter("idTurno", idTurno).setParameter("ano", ano)
				.getResultList();
		if (!turmas.isEmpty()) {
			return turmas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Turma> obterTurmaPorIdTurno(Long idTurno, Integer ano) {
		List<Turma> turmas = em
				.createQuery("From Turma t  where t.turno.id=:idTurno AND t.ano=:ano ORDER BY t.descricao ")
				.setParameter("idTurno", idTurno).setParameter("ano", ano).getResultList();
		if (!turmas.isEmpty()) {
			return turmas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Turma> obterTurmaPorDescricao(String descricao, Integer ano) {
		List<Turma> turmas = em
				.createQuery("From Turma t where t.descricao='" + descricao.toUpperCase() + "' AND t.ano=:ano ")
				.setParameter("ano", ano).getResultList();
		if (!turmas.isEmpty()) {
			return turmas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Turma> obterTurmaPorDescricaoTurno(String descricao, Long idTurno, Integer ano) {
		List<Turma> turmas = em
				.createQuery("From Turma t  where t.turno.id=:idTurno AND t.ano=:ano AND t.descricao='"
						+ descricao.toUpperCase() + "'")
				.setParameter("idTurno", idTurno).setParameter("ano", ano).getResultList();
		if (!turmas.isEmpty()) {
			return turmas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Turma> obterTurmaPorClasseDescricao(long idClasse, String descricao, Integer ano) {
		List<Turma> turmas = em
				.createQuery("From Turma t  where t.classe.id=:idClasse AND t.ano=:ano AND t.descricao='"
						+ descricao.toUpperCase() + "'")
				.setParameter("idClasse", idClasse).setParameter("ano", ano).getResultList();
		if (!turmas.isEmpty()) {
			return turmas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Turma> obterTurmaPorClasseTurnoDescricao(long idClasse, Long idTurno, String descricao, Integer ano) {
		List<Turma> turmas = em
				.createQuery("From Turma t  where t.classe.id=:idClasse AND t.ano=:ano AND t.descricao='"
						+ descricao.toUpperCase() + "' AND t.turno.id=:idTurno")
				.setParameter("idClasse", idClasse).setParameter("ano", ano).setParameter("idTurno", idTurno)
				.getResultList();
		if (!turmas.isEmpty()) {
			return turmas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Turma> obterTurmaPorClassePorEscola(long idClasse, Long idTurma, Integer ano, Long idEscola) {
		List<Turma> turmas;
		turmas = em
				.createQuery("FROM Turma t  WHERE t.id !=:IDTurma AND t.classe.id=:IdClasse "
						+ " AND t.ano=:ANO AND t.escola.id=:IdEscola ORDER BY t.turno.id")
				.setParameter("IDTurma", idTurma).setParameter("IdClasse", idClasse).setParameter("IdEscola", idEscola)
				.setParameter("ANO", ano).getResultList();
		if (!turmas.isEmpty()) {
			return turmas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Turma> obterTurmasPorClasseCurso(long idClasse, String curso, Integer ano, Long idEscola) {
		List<Turma> turmas;
		turmas = em
				.createQuery("FROM Turma t   WHERE  t.classe.id=:IdClasse "
						+ " AND t.ano=:ANO AND t.escola.id=:IdEscola AND t.turno.curso=:CURSO ORDER BY t.descricao")
				.setParameter("IdClasse", idClasse).setParameter("IdEscola", idEscola).setParameter("CURSO", curso)
				.setParameter("ANO", ano).getResultList();
		if (!turmas.isEmpty()) {
			return turmas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Turma> obterTurmasPorClasseAreaCurso(long idClasse, String tipoArea, String curso, Integer ano,
			Long idEscola) {
		List<Turma> turmas;
		turmas = em
				.createQuery("FROM Turma t  WHERE t.area=:AREA AND t.classe.id=:IdClasse "
						+ " AND t.ano=:ANO AND t.escola.id=:IdEscola AND t.turno.curso=:CURSO ORDER BY t.descricao")
				.setParameter("CURSO", curso).setParameter("AREA", tipoArea).setParameter("IdClasse", idClasse)
				.setParameter("IdEscola", idEscola).setParameter("ANO", ano).getResultList();
		if (!turmas.isEmpty()) {
			return turmas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Turma turmaExistente(Integer ano, Long idTurno, long idSala, Long idEscola) {
		List<Turma> turmas = em
				.createQuery(
						"FROM Turma t WHERE t.ano=:ANO AND t.turno.id=:idTurno  AND t.sala.id=:idSala AND t.escola.id=:IdEscola")
				.setParameter("ANO", ano).setParameter("idTurno", idTurno).setParameter("idSala", idSala)
				.setParameter("IdEscola", idEscola).getResultList();
		if (!turmas.isEmpty()) {
			return turmas.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Turma> obterTurmasPorClasseAreaCursoDiferenteIdTurma(long idClasse, String tipoArea, String curso,
			Integer ano, Long idEscola, Long idTurma) {
		List<Turma> turmas;
		turmas = em
				.createQuery("FROM Turma t  WHERE id !=:idTurma AND t.area=:AREA AND t.classe.id=:IdClasse "
						+ " AND t.ano=:ANO AND t.escola.id=:IdEscola AND t.turno.curso=:CURSO ORDER BY t.descricao")
				.setParameter("CURSO", curso).setParameter("AREA", tipoArea).setParameter("IdClasse", idClasse)
				.setParameter("IdEscola", idEscola).setParameter("ANO", ano).setParameter("idTurma", idTurma)
				.getResultList();
		if (!turmas.isEmpty()) {
			return turmas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Turma> obterTurmasPorClasseCursoDiferenteIdTurma(long idClasse, String curso, Integer ano,
			Long idEscola, Long idTurma) {
		List<Turma> turmas;
		turmas = em
				.createQuery("FROM Turma t  WHERE id !=:idTurma  AND t.classe.id=:IdClasse "
						+ " AND t.ano=:ANO AND t.escola.id=:IdEscola AND t.turno.curso=:CURSO ORDER BY t.descricao")
				.setParameter("CURSO", curso).setParameter("IdClasse", idClasse).setParameter("IdEscola", idEscola)
				.setParameter("ANO", ano).setParameter("idTurma", idTurma).getResultList();
		if (!turmas.isEmpty()) {
			return turmas;
		}
		return null;
	}

	@Override
	public Turma obterTurmaExistentePorDscricao(String descricao, Long idClasse, String CURSO, Integer ANO,
			Long idEscola) {
		@SuppressWarnings("unchecked")
		List<Turma> list = em
				.createQuery("FROM Turma t WHERE t.descricao like '%" + descricao
						+ "%' AND t.classe.id =:idClasse AND t.curso =:CURSO AND t.ano =:ANO AND t.escola.id =:idEscola")
				.setParameter("idClasse", idClasse).setParameter("CURSO", CURSO).setParameter("ANO", ANO)
				.setParameter("idEscola", idEscola).getResultList();
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Long obterNumeroReciboUltimaTurma() {
		Long numeroRecibo = (Long) em.createQuery("select max(cast(numero as long)) from Turma")
				.getSingleResult();
		return numeroRecibo;
	}

}
