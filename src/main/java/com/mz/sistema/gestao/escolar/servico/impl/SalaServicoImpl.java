package com.mz.sistema.gestao.escolar.servico.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.Sala;
import com.mz.sistema.gestao.escolar.servico.SalaServico;

@Service
@Transactional
public class SalaServicoImpl implements SalaServico {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private AuthenticationContext authenticationContext;

	@Override
	public void salvar(Sala sala) {
		em.merge(sala);
	}

	@Override
	public void excluir(Sala sala) {
		em.remove(em.merge(sala));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sala> obterSalaPorEscolaAtivas() {
		Escola escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();
		List<Sala> salas = em.createQuery("FROM Sala WHERE escola.id=:idEscola and activa=true ORDER BY numero")
				.setParameter("idEscola", escola.getId()).getResultList();
		if (!salas.isEmpty()) {
			return salas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Sala obterSalaPorId(long idSala) {
		List<Sala> salas = em.createQuery("FROM Sala WHERE id=:idSala").setParameter("idSala", idSala).getResultList();
		if (!salas.isEmpty()) {
			return salas.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Sala salaExistenteDaEscola(String sala) {
		Escola escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();
		List<Sala> salas = em.createQuery("FROM Sala WHERE escola.id=:idEscola AND numero =:Sala")
				.setParameter("idEscola", escola.getId()).setParameter("Sala", sala).getResultList();
		if (!salas.isEmpty()) {
			return salas.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sala> obterSalaPorEscolaPorPesquisa(String salaPesquisada, Long idEscola) {
		
		List<Sala> salas = em
				.createQuery("FROM Sala WHERE escola.id=:idEscola AND (numero LIKE '%" + salaPesquisada + "%' or codigo LIKE '%" + salaPesquisada + "%' or descricao LIKE '%" + salaPesquisada + "%') order by numero")
				.setParameter("idEscola", idEscola).getResultList();
		if (!salas.isEmpty()) {
			return salas;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Sala obterSalaPorEscolaPorCodigoANDSala(String pesquisa) {
		Escola escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();
		List<Sala> salas = em
				.createQuery("FROM Sala WHERE escola.id=:idEscola AND (numero LIKE '%" + pesquisa + "%' or codigo LIKE '%" + pesquisa+ "%')")
				.setParameter("idEscola", escola.getId()).getResultList();
		if (!salas.isEmpty()) {
			return salas.get(0);
		}
		return null;
	}
	@Override
	public Long obterNumeroUltimaSalaPorEscola(Long idEscola) {
		
		return (Long) em.createQuery("select max(cast(numero as long)) from Sala where escola.id= :idEscola"
				).setParameter("idEscola", idEscola).getSingleResult();
	}
}
