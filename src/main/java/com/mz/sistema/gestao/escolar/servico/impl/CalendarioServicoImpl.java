package com.mz.sistema.gestao.escolar.servico.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.modelo.Calendario;
import com.mz.sistema.gestao.escolar.servico.CalendarioServico;

@Service
@Transactional
public class CalendarioServicoImpl implements CalendarioServico{

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void salvar(Calendario calendario) {
		em.merge(calendario);
		
	}


	@Override
	public Calendario salvarERetornarCalendario(Calendario calendario) {
		calendario =em.merge(calendario);
		return calendario;
	}

	@Override
	public void excluir(Calendario calendario) {
		em.remove(em.merge(calendario));
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Calendario> listarTodos() {
		List<Calendario>  calendarios=em.createQuery("FROM Calendario ORDER BY ano DESC").getResultList();
		if(!calendarios.isEmpty()){
			return calendarios;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Calendario obterCalendarioPorIdAno(Long idCalendario, Integer ano) {
		List<Calendario> calendarios = em
				.createQuery("FROM Calendario WHERE id=:idCalendario AND ano=:ANO")
				.setParameter("idCalendario", idCalendario).setParameter("ANO", ano).getResultList();
		if (!calendarios.isEmpty()) {
			return calendarios.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Calendario obterCalendarioVigente() {
		List<Calendario> calendarios = em
				.createQuery("FROM Calendario WHERE ativo=true")
				.getResultList();
		if (!calendarios.isEmpty()) {
			return calendarios.get(0);
		}
		//calendarios =new ArrayList<Calendario>();
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Calendario obterCalendarioPorId(long idCalendario) {
		List<Calendario> calendarios = em
				.createQuery("FROM Calendario WHERE id=:idCalendario")
				.setParameter("idCalendario", idCalendario).getResultList();
		if (!calendarios.isEmpty()) {
			return calendarios.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Calendario obterCalendarioPorAno(Integer ano) {
		List<Calendario> calendarios = em
				.createQuery("FROM Calendario WHERE ano=:ANO").setParameter("ANO", ano).getResultList();
		if (!calendarios.isEmpty()) {
			return calendarios.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Calendario obterCalendarioVigente(boolean ativo) {		
		List<Calendario> calendarios = em
				.createQuery("FROM Calendario WHERE ativo=:Ativo").setParameter("Ativo", ativo).getResultList();
		if (!calendarios.isEmpty()) {
			return calendarios.get(0);
		}
		return null;
	}

	@Override
	public List<Calendario> obterCalendarioPorAnoAtivo(Integer ano) {
		@SuppressWarnings("unchecked")
		List<Calendario> calendarios = em
				.createQuery("FROM Calendario WHERE ano=:ANO").setParameter("ANO", ano).getResultList();
		if (!calendarios.isEmpty()) {
			return calendarios;
		}
		return null;
	}


	@Override
	public List<Calendario> obterCalendarioPorPesquisa(String pesquisa) {
		@SuppressWarnings("unchecked")

		List<Calendario> calendarios = em
				.createQuery("FROM Calendario WHERE pesquisa like '%"+pesquisa+"%' ORDER BY ano DESC").getResultList();
		if (!calendarios.isEmpty()) {
			return calendarios;
		}
		return null;
	}



}
