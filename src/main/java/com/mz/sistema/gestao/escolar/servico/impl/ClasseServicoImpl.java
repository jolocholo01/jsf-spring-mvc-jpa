package com.mz.sistema.gestao.escolar.servico.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.modelo.Classe;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.servico.ClasseServico;

@Service
@Transactional
public class ClasseServicoImpl implements ClasseServico {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private AuthenticationContext autenticacao;

	@Override
	public void salvar(Classe classe) {
		em.merge(classe);
	}

	@Override
	public void excluir(Classe classe) {
		em.remove(em.merge(classe));

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Classe> obterTodasClasses() {
		List<Classe> classes = em.createQuery("From Classe ORDER BY sigla").getResultList();
		if (!classes.isEmpty()) {
			return classes;
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Classe> obterTodasClassesAtivas() {
		List<Classe> classes = em.createQuery("From Classe where ativa = true ORDER BY sigla ").getResultList();
		if (!classes.isEmpty()) {
			return classes;
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Classe obterClassePorId(Long idClasse) {
		List<Classe> classes = em.createQuery("From Classe where id=:idClasse").setParameter("idClasse", idClasse)
				.getResultList();
		if (!classes.isEmpty()) {
			return classes.get(0);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Classe classeExisente(String descricao) {
		if (descricao.equals("aOITAVA_CLASSE")) {
			descricao = "8ª CLASSE";
		}
		if (descricao.equals("bNONA_CLASSE")) {
			descricao = "9ª CLASSE";

		}
		if (descricao.equals("cDECIMA_TCLASSE")) {
			descricao = "10ª CLASSE";
		}
		if (descricao.equals("dDECIMA_PRIMEIRA")) {
			descricao = "11ª CLASSE";
		}
		if (descricao.equals("eDECIMA_SEGUNDA")) {
			descricao = "12ª CLASSE";
		}
		List<Classe> classes = em.createQuery("From Classe where descricao=:tipoClasse")
				.setParameter("tipoClasse", descricao).getResultList();
		if (!classes.isEmpty()) {
			return classes.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Classe> obterClassesPorCiclo(String ciclo) {
		return em.createQuery("From Classe where ciclo =:Ciclo and ativa=true ORDER BY sigla ")
				.setParameter("Ciclo", ciclo).getResultList();
	}

	@Override
	public Classe obterClassePorId(long idClasse) {
		@SuppressWarnings("unchecked")
		List<Classe> classes = em.createQuery("From Classe where id=:idClasse").setParameter("idClasse", idClasse)
				.getResultList();
		if (!classes.isEmpty()) {
			return classes.get(0);
		}
		return null;
	}

	@Override
	public List<Classe> obterClassePorDescricao(String descricao_parametro) {

		String descricao = descricao_parametro.trim();
		@SuppressWarnings("unchecked")
		List<Classe> classes = em.createQuery("From Classe where (descricao like '%"
				+ descricao.replace("8 classe", "8ª classe").replace("9 classe", "9ª classe")
						.replace("10 classe", "10ª classe").replace("11 classe", "11ª classe")
						.replace("12 classe", "12ª classe").toUpperCase()
				+ "%' or tipoEnsino like '%" + descricao.toUpperCase() + "%' or ciclo like '%" + descricao.toUpperCase()
				+ "%') ORDER BY sigla").getResultList();
		if (!classes.isEmpty()) {
			return classes;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Classe> obterClassesPorEscola() {
		Escola escola=autenticacao.getFuncionarioEscolaLogada().getEscola();
		List<Classe> classes = null;
		
			classes = em.createQuery("SELECT classes FROM Escola WHERE id_escola=:IdEscola")
					.setParameter("IdEscola", escola.getId()).getResultList();
			if (!classes.isEmpty()) {
				return classes;
			}
		
		return null;
	}

}
