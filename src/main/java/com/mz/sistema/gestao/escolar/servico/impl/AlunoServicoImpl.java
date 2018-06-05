package com.mz.sistema.gestao.escolar.servico.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.modelo.Aluno;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.servico.AlunoServico;
import com.mz.sistema.gestao.escolar.util.DataUtils;

@Service
@Transactional
public class AlunoServicoImpl implements AlunoServico {

	@PersistenceContext
	private EntityManager em;
	private static final String FORMATO_BANCO = "yyyy-MM-dd";

	@Autowired
	private AuthenticationContext authenticationContext;

	@Override
	public Aluno salvarRetornarAluno(Aluno aluno) {
		aluno = em.merge(aluno);
		return aluno;
	}

	@Override
	public void salvar(Aluno aluno) {
		em.merge(aluno);

	}

	@Override
	public void excluir(Aluno aluno) {

		em.createNativeQuery("DELETE FROM aluno WHERE id = ?").setParameter(1, aluno.getId()).executeUpdate();

		em.createNativeQuery("DELETE FROM usuario_permissao WHERE  id_usuario = ?").setParameter(1, aluno.getId())
				.executeUpdate();
		em.createNativeQuery("DELETE FROM usuario WHERE  id= ?").setParameter(1, aluno.getId()).executeUpdate();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Aluno> listartodos() {

		return em.createQuery("from Aluno").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Aluno obterAlunoPorId(Long idAluno) {
		List<Aluno> alunos = em.createQuery("from Aluno a join fetch a.permissoes   where a.id=:idAluno").setParameter("idAluno", idAluno)
				.getResultList();
		if (!alunos.isEmpty()) {
			return alunos.get(0);
		}
		return null;
	}

	@Override
	public List<Aluno> obterAlunosPorNome(String nome) {
		@SuppressWarnings("unchecked")
		List<Aluno> alunos = em.createQuery("from Aluno a join fetch a.permissoes where a.nome like '%" + nome + "%' ").getResultList();
		if (!alunos.isEmpty()) {
			return alunos;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Aluno> obterAlunosPorPesquisa(String pesquisa) {

		FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
		Escola escola = funcionarioEscola.getEscola();

		List<Aluno> alunos = em
				.createQuery("from Aluno a join fetch a.permissoes where (a.nome like '%" + pesquisa.toUpperCase().trim()
						+ "%' OR a.login like '%" + pesquisa.trim() + "%') AND a.escola.id=:codEscola ")
				.setParameter("codEscola", escola.getId()).getResultList();
		if (!alunos.isEmpty()) {
			return alunos;
		} else {
			return null;
		}
	}

	@Override
	public Aluno obterAlunoExistente(String nome, Date dataNascimento, boolean sexo, String nascionalidade, String pai,
			String mae) {
		try {
			@SuppressWarnings("unchecked")
			List<Aluno> funcionarios = em
					.createQuery("From Aluno a join fetch a.permissoes where a.nome LIKE '%" + nome.trim() + "%' AND  date(a.dataNascimento) = '"
							+ DataUtils.obterDataFormatoBanco(dataNascimento, FORMATO_BANCO) + "' AND a.sexo =:sexo "
							+ " AND a.nascionalidade LIKE '%" + nascionalidade.trim() + "%' AND a.pai LIKE '%" + pai.trim()
							+ "%' AND mae LIKE '%" + mae.trim() + "%'")
					.setParameter("sexo", sexo).getResultList();
			if (!funcionarios.isEmpty()) {
				return funcionarios.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
