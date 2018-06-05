package com.mz.sistema.gestao.escolar.servico.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.enumerado.TipoCurso;
import com.mz.sistema.gestao.escolar.enumerado.TipoTurno;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.Turno;
import com.mz.sistema.gestao.escolar.servico.TurnoServico;

@Service
@Transactional
public class TurnoServicoImpl implements TurnoServico {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private AuthenticationContext authenticationContext;

	@Override
	public void salvar(Turno turno) {
		em.merge(turno);
	}

	@Override
	public void excluir(Turno turno) {
		em.remove(em.merge(turno));
	}

	@SuppressWarnings("unchecked")
	public List<Turno> listarTodosTurnos() {

		List<Turno> turnos = em.createQuery("FROM Turno ORDER BY id ").getResultList();
		if (!turnos.isEmpty()) {
			return turnos;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Turno> listarTodosTurnosDaEscola() {
		Escola escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();
		return em.createQuery("SELECT turnos FROM Escola where id_escola=:idEscola")
				.setParameter("idEscola", escola.getId()).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Turno> obterTurnoPorCurso(String curso) {
		return em.createQuery("FROM Turno where curso like '%" + curso + "%'").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Turno obterTurnoPorId(Long idTurno) {
		List<Turno> turnos = em.createQuery("FROM Turno WHERE id=:idTurno").setParameter("idTurno", idTurno)
				.getResultList();
		if (!turnos.isEmpty()) {
			return turnos.get(0);
		}
		return null;
	}

	@Override
	public Turno obterTurnoCursoDiurno(String curso, TipoTurno descricao) {
		@SuppressWarnings("unchecked")
		List<Turno> turnos = em.createQuery("FROM Turno WHERE curso=:curso and descricao !=:descricao")
				.setParameter("curso", curso).setParameter("descricao", descricao).getResultList();
		if (!turnos.isEmpty()) {
			return turnos.get(0);
		}
		return null;
	}

	@Override
	public Turno obterTurnoExistente(TipoTurno descricao) {
		@SuppressWarnings("unchecked")
		List<Turno> turnos = em.createQuery("FROM Turno WHERE descricao =:descricao")
				.setParameter("descricao", descricao).getResultList();
		if (!turnos.isEmpty()) {
			return turnos.get(0);
		}
		return null;
	}

	@Override
	public List<Turno> obterTurnoPorPesquisa(String pesquisa) {

		@SuppressWarnings("unchecked")
		List<Turno> turnos = em.createQuery("FROM Turno WHERE curso like '%" + pesquisa.trim().toUpperCase()
				+ "%' OR descricao like '%" + pesquisa.trim().toUpperCase() + "%' ORDER BY horaInicio").getResultList();
		if (!turnos.isEmpty()) {
			return turnos;
		}
		return null;
	}

}
