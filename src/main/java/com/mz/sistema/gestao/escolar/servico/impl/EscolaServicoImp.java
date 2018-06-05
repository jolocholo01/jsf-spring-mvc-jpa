package com.mz.sistema.gestao.escolar.servico.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.modelo.Calendario;
import com.mz.sistema.gestao.escolar.modelo.Classe;
import com.mz.sistema.gestao.escolar.modelo.Distrital;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.modelo.Turno;
import com.mz.sistema.gestao.escolar.servico.EscolaServico;
import com.mz.sistema.gestao.escolar.util.Replace;

@Service
@Transactional
public class EscolaServicoImp implements EscolaServico {

	@PersistenceContext
	private EntityManager em;
	@Autowired
	private AuthenticationContext authenticationContext;

	@Override
	public void salvar(Escola escola) {
		em.merge(escola);

	}

	@Override
	public void excluir(Escola escola) {
		em.remove(em.merge(escola));

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Escola> listarTodas() {
		return em.createQuery("from Escola").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Escola obterEscolaPorId(Long idEscolao) {
		List<Escola> escolas = em.createQuery("from Escola where id=:cod").setParameter("cod", idEscolao)
				.getResultList();
		if (!escolas.isEmpty()) {
			return escolas.get(0);
		} else {
			return null;
		}
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public Escola obterEscolaExistente(Long idDirecaoDitrital, String nomeEscola, String localidade) {
		List<Escola> escolas = em
				.createQuery("from Escola where distrital.id=:cod AND  descricao LIKE '%" + nomeEscola + "%'"
						+ " AND localidade LIKE '%" + localidade + "%'")
				.setParameter("cod", idDirecaoDitrital).getResultList();
		if (!escolas.isEmpty()) {
			return escolas.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Long totalEstudantesDaEscola(Long idEscolo, Integer ano) {
		Long quntidadeAunos;

		quntidadeAunos = (Long) em.createQuery("SELECT COUNT(id) FROM Matricula WHERE escola.id=:idEscola AND ano=:ANO")
				.setParameter("idEscola", idEscolo).setParameter("ANO", ano).getSingleResult();
		if (!quntidadeAunos.equals(null)) {
			return quntidadeAunos;
		}

		return null;
	}

	@Override
	public Long totalProfessoresDaEscola(Long idEscola) {

		return (Long) em
				.createQuery(
						"select COUNT(funcionario.id) from FuncionarioEscola  where (permissao.descricao = 'ROLE_PROFESSOR' OR permissao.descricao = 'ROLE_DIRECTOR_ADJUNTO' OR permissao.descricao = 'ROLE_DIRECTOR') AND escola.id=:idEscola")
				.setParameter("idEscola", idEscola).getSingleResult();
	}

	@Override
	public Long totalTurmaDaEscola(Long idEscola) {
		Long quntidadeTurmas;
		Calendario calendario = authenticationContext.getCalendarioEscolar();
		if (calendario == null) {
		} else if (calendario != null) {
			quntidadeTurmas = (Long) em
					.createQuery("SELECT COUNT(id) from Turma where escola.id=:idEscola AND ano=:Ano ")
					.setParameter("Ano", calendario.getAno()).setParameter("idEscola", idEscola).getSingleResult();
			if (!quntidadeTurmas.equals(null)) {
				return quntidadeTurmas;
			}
		}
		return null;
	}

	@Override
	public Long totalEstudantesAlocadoEmTurmasDaEscola(Long idEscola) {
		Long quantiadeEstudantesAlocadoEmTurmasDaEscola;
		Calendario calendario = authenticationContext.getCalendarioEscolar();
		if (calendario == null) {
		} else if (calendario != null) {
			quantiadeEstudantesAlocadoEmTurmasDaEscola = (Long) em
					.createQuery(
							" SELECT COUNT(id) FROM Matricula WHERE escola.id=:idEscola AND ano=:ANO AND ativo=true AND (turma.id !=null OR turmaDestino.id !=null) ")
					.setParameter("idEscola", idEscola).setParameter("ANO", calendario.getAno()).getSingleResult();
			if (!quantiadeEstudantesAlocadoEmTurmasDaEscola.equals(null)) {
				return quantiadeEstudantesAlocadoEmTurmasDaEscola;
			}
		}
		return null;
	}

	@Override
	public List<Escola> obterTodasEsolasPorDirecaoDistrital() {
		Distrital distrital = authenticationContext.getFuncionarioDirecaoDistritalLogado();
		@SuppressWarnings("unchecked")
		List<Escola> escolas = em.createQuery("from Escola where distrital.id=:cod AND ativa = true order by descricao")
				.setParameter("cod", distrital.getId()).getResultList();
		if (!escolas.isEmpty()) {
			return escolas;
		} else {
			return null;
		}
	}

	@Override
	public List<Escola> obterEscolasPorPesquisa(String pesquisa_paramentro) {
		Distrital distrital = authenticationContext.getFuncionarioDirecaoDistritalLogado();
		String pesquisa = Replace.escola(pesquisa_paramentro);
		@SuppressWarnings("unchecked")
		List<Escola> escolas = em
				.createQuery("from Escola where (descricao like '%" + pesquisa.trim() + "%' OR localidade like '%"
						+ pesquisa.trim() + "%' OR bairro like '%" + pesquisa.trim() + "%' OR avenidaRua like '%"
						+ pesquisa.trim() + "%') AND distrital.id=:cod ORDER BY id ")
				.setParameter("cod", distrital.getId()).getResultList();
		if (!escolas.isEmpty()) {
			return escolas;
		}
		return null;
	}

	@Override
	public List<Classe> obterClassesPorIdEscola(Long idEscola) {
		@SuppressWarnings("unchecked")
		List<Classe> classes = em.createQuery("SELECT classes from Escola where id_escola=:idEscola ")
				.setParameter("idEscola", idEscola).getResultList();
		return classes;

	}

	@Override
	public List<FuncionarioEscola> obterEscolasPorIFuncionario(Long idFuncionario) {
		@SuppressWarnings("unchecked")
		List<FuncionarioEscola> FuncionarioEscolas = em
				.createQuery("FROM FuncionarioEscola f where f.funcionario.id=:idFuncionario ")
				.setParameter("idFuncionario", idFuncionario).getResultList();
		return FuncionarioEscolas;

	}
	@Override
	public List<Escola> obterEscolasSemRepiticaoPorIFuncionario(Long idFuncionario) {
	
		List<Escola> FuncionarioEscolas = em
				.createQuery("SELECT DISTINCT f.escola FROM FuncionarioEscola f where f.funcionario.id=:idFuncionario", Escola.class)
				.setParameter("idFuncionario", idFuncionario).getResultList();
		return FuncionarioEscolas;

	}
	
	@Override
	public List<Turno> obterTurnosPorIdEscola(Long idEscola) {

		@SuppressWarnings("unchecked")
		List<Turno> turnos = em.createQuery("SELECT turnos FROM Escola WHERE id_escola=:idEscola ")
				.setParameter("idEscola", idEscola).getResultList();

		return turnos;
	}

	@Override
	public List<Escola> obterEscolasPorId(Long idEscola) {
		@SuppressWarnings("unchecked")
		List<Escola> escolas = em.createQuery("from Escola where id !=:cod ORDER BY descricao ")
				.setParameter("cod", idEscola).getResultList();
		if (!escolas.isEmpty()) {
			return escolas;
		}
		return null;
	}

	@Override
	public List<Classe> obterClassesPorIdEscolaPorIdClasse(Long idEscola, long idClasse) {
		@SuppressWarnings("unchecked")
		List<Classe> classes = em.createQuery("SELECT classes from Escola where id_escola=:idEscola AND id_classe=:idClasse")
				.setParameter("idEscola", idEscola).setParameter("idClasse", idClasse).getResultList();
		if (!classes.isEmpty()) {
			return classes;
		}
		return null;
	}

	
}
