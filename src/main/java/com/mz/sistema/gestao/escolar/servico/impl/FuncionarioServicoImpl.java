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
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.servico.FuncionarioServico;
import com.mz.sistema.gestao.escolar.util.DataUtils;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Service
@Transactional
public class FuncionarioServicoImpl implements FuncionarioServico {

	@PersistenceContext
	private EntityManager em;
	private static final String FORMATO_BANCO = "yyyy-MM-dd";

	@Autowired
	private AuthenticationContext authenticationContext;
	
	@Override
	public void salvar(Funcionario funcionario) {
		em.merge(funcionario);
	}

	@Override
	public void excluir(Funcionario funcionario) {

		try {
			em.remove(em.merge(funcionario));
		} catch (Exception e) {
			Mensagem.mensagemInfo("Aviso: Não pode excluir o Funcionário pois, existe dependência.");

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Funcionario> obterPorNome(String nome) {
		return em.createQuery("From Funcionario f join fetch f.permissoes where a.nome like '%" + nome + "%'").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Funcionario> listarTodosAtivos() {
		return em.createQuery("From Funcionario f join fetch f.permissoes where f.ativo=true").getResultList();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Funcionario> listarTodos() {
		return em.createQuery("From Funcionario").getResultList();

	}

	@SuppressWarnings("unchecked")
	@Override
	public Funcionario obterFuncionarioPorId(Long idFuncionario) {
		List<Funcionario> funcionarios = em.createQuery("From Funcionario f where f.id=:idFuncionario")
				.setParameter("idFuncionario", idFuncionario).getResultList();

		return funcionarios.get(0);

	}

	

	@SuppressWarnings("unchecked")
	@Override
	public List<FuncionarioEscola> listartodosProfessoresEscolaPorNome(String nome) {
		Escola escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();
		List<FuncionarioEscola> professores = em
				.createQuery("From FuncionarioEscola f where (f.funcionario.nome LIKE '%" + nome.toUpperCase() + "%' or f.funcionario.login LIKE '%" + nome + "%') and escola.id=:IdEscola")
				.setParameter("IdEscola", escola.getId()).getResultList();
		if (!professores.isEmpty()) {
			return professores;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Funcionario obterFuncionariosDaEscola(Long idFuncionario, Long idEscola) {
		List<Funcionario> escolas = em
				.createQuery(
						"select funcionarioEscolas from Escola where id_funcionario=:idFuncionario and id_escola=:idEscola")
				.setParameter("idFuncionario", idFuncionario).setParameter("idEscola", idEscola).getResultList();
		if (!escolas.isEmpty()) {
			return escolas.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Funcionario> obterPorId(Long idFuncionario) {
		List<Funcionario> professores = em.createQuery("From Funcionario f join fetch f.permissoes where f.id=:idFuncionario")
				.setParameter("idFuncionario", idFuncionario).getResultList();
		if (!professores.isEmpty()) {
			return professores;
		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Funcionario> obterFuncionariosPorNomeOuUsuaio(String nomeOuUsuario, Long IdFuncionario, long IdDirecaoDistrital) {
		List<Funcionario> funcionarios = em
				.createQuery("From Funcionario f  where (f.nome LIKE '%" + nomeOuUsuario.toUpperCase() + "%' OR f.login LIKE '%" + nomeOuUsuario
						+ "%' OR f.telefone LIKE '%"+nomeOuUsuario+"%'  OR f.numero LIKE '%"+nomeOuUsuario+"%' ) AND f.direcaoDistrital.id =:IdDirecaoDistrital" + " AND f.id!=:IdFuncionario ORDER BY f.nome")
				.setParameter("IdDirecaoDistrital", IdDirecaoDistrital).setParameter("IdFuncionario", IdFuncionario)
				.getResultList();
		if (!funcionarios.isEmpty()) {
			return funcionarios;
		}
		return funcionarios;

	}@SuppressWarnings("unchecked")
	@Override
	public List<Funcionario> obterFuncionariosPorNomeOuUsuaio(String nomeOuUsuario) {
		List<Funcionario> funcionarios = em
				.createQuery("FROM Funcionario f LEFT JOIN FETCH f.permissoes up WHERE (f.nome LIKE '%" + nomeOuUsuario.toUpperCase() + "%' OR f.login LIKE '%" + nomeOuUsuario
						+ "%' OR f.telefone LIKE '%"+nomeOuUsuario+"%' ) AND up.id=(SELECT p.id FROM Permissao p WHERE p.descricao='ROLE_DIRECTOR_DISTRITO')  ORDER BY f.nome")
				.getResultList();
		if (!funcionarios.isEmpty()) {
			return funcionarios;
		}
		return funcionarios;

	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Funcionario> obterFuncionariosPorNomePorUsuaio(String nome, String usuario, long IdDirecaoDistrital) {
		
		List<Funcionario> funcionarios = em
				.createQuery("From Funcionario  where (f.nome LIKE '%" + nome.toUpperCase() + "%' AND "
						+ "f.login LIKE '%" + usuario + "%' ) AND f.direcaoDistrital.id =:IdDirecaoDistrital")
				.setParameter("IdDirecaoDistrital", IdDirecaoDistrital).getResultList();
		if (!funcionarios.isEmpty()) {
			return funcionarios;
		}
		return funcionarios;

	}

	@Override
	public Funcionario obterFuncionarioExistente(String nome, Date dataNascimento, boolean sexo, String nascionalidade,
			String pai, String mae) {
		try {
			@SuppressWarnings("unchecked")
			List<Funcionario> funcionarios = em
					.createQuery("From Funcionario f where f.nome LIKE '%" + nome.trim() + "%' AND  date(f.dataNascimento) = '"
							+ DataUtils.obterDataFormatoBanco(dataNascimento, FORMATO_BANCO) + "' AND f.sexo =:sexo "
							+ " AND f.nascionalidade LIKE '%" + nascionalidade.trim() + "%' AND f.pai LIKE '%" + pai.trim()
							+ "%' AND f.mae LIKE '%" + mae.trim() + "%'")
					.setParameter("sexo", sexo).getResultList();
			if (!funcionarios.isEmpty()) {
				return funcionarios.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}


	@Override
	public Long obterNumeroUltimoFuncionario() {
		Long funcionario = (Long) em.createQuery("select max(cast(numero as long)) from Funcionario")
				.getSingleResult();
		return funcionario;

	}

	@Override
	public Funcionario obterFuncionarioPorIdPorPermissoes(Long idFuncionario) {
		@SuppressWarnings("unchecked")

			List<Funcionario> funcionarios = em.createQuery("From Funcionario f join fetch f.permissoes where f.id=:idFuncionario")
					.setParameter("idFuncionario", idFuncionario).getResultList();
		if (!funcionarios.isEmpty()) {
			return funcionarios.get(0);
		}
		return null;
	}
}
