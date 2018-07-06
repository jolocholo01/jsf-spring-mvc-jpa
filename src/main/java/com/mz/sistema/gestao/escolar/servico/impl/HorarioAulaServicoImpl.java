package com.mz.sistema.gestao.escolar.servico.impl;

import java.util.Date;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.HorarioAula;
import com.mz.sistema.gestao.escolar.servico.HorarioAulaServico;

@Service
@Transactional
public class HorarioAulaServicoImpl implements HorarioAulaServico {

	@PersistenceContext
	private EntityManager em;
	@Autowired
	private AuthenticationContext authenticationContext;

	@Override
	public void salvar(HorarioAula horarioAula) {
		em.merge(horarioAula);
	}

	@Override
	public void excluir(HorarioAula horarioAula) {
		em.remove(em.merge(horarioAula));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HorarioAula> obterPorTurno(Long idTurno) {
		List<HorarioAula> horarioAulas = em.createQuery("FROM HorarioAula WHERE turno.id=:idTurno")
				.setParameter("idTurno", idTurno).getResultList();
		if (!horarioAulas.isEmpty()) {
			return horarioAulas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HorarioAula horarioAulaExistenteDaEscola(Long idTurno, Date horaInicial, Integer ordem) {
		Escola escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();
		List<HorarioAula> horarioAulas = em
				.createQuery(
						"FROM HorarioAula WHERE escola.id=:idEscola  AND turno.id=:idTurno AND aulaInicial=:horaInicial AND ordem=:ordem")
				.setParameter("idEscola", escola.getId()).setParameter("idTurno", idTurno)
				.setParameter("horaInicial", horaInicial).setParameter("ordem", ordem).getResultList();
		if (!horarioAulas.isEmpty()) {
			return horarioAulas.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HorarioAula> obterHorarioAulaPorEscolaTurno(Long idTurno) {
		Escola escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();

		List<HorarioAula> horarioAulas = em
				.createQuery("FROM HorarioAula WHERE escola.id=:idEscola AND turno.id=:idTurno ORDER BY ordem")
				.setParameter("idTurno", idTurno).setParameter("idEscola", escola.getId()).getResultList();
		if (!horarioAulas.isEmpty()) {
			return horarioAulas;
		}
		return null;
	}

	@Override
	public List<HorarioAula> obterHorarioAulaPorIdEscolaPoridTurno(Long idTurno, Long idEscola) {

		@SuppressWarnings("unchecked")
		List<HorarioAula> horarioAulas = em
				.createQuery("FROM HorarioAula WHERE escola.id=:idEscola AND turno.id=:IdTurno ORDER BY ordem")
				.setParameter("idEscola", idEscola).setParameter("IdTurno", idTurno).getResultList();
		if (!horarioAulas.isEmpty()) {
			return horarioAulas;
		}
		return null;
	}

	@Override
	public HorarioAula obterHorarioAulaPorId(Long idHorarioAula) {

		@SuppressWarnings("unchecked")
		List<HorarioAula> horarioAulas = em.createQuery("FROM HorarioAula WHERE id=:idH ")
				.setParameter("idH", idHorarioAula).getResultList();
		if (!horarioAulas.isEmpty()) {
			return horarioAulas.get(0);
		}
		return null;
	}

}
