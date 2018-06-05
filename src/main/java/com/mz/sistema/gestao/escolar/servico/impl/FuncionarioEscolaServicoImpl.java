package com.mz.sistema.gestao.escolar.servico.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.enumerado.RoleName;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.servico.FuncionarioEscolaServico;

@Service
@Transactional
public class FuncionarioEscolaServicoImpl implements FuncionarioEscolaServico {

	@PersistenceContext
	private EntityManager em;

	@Override
	public FuncionarioEscola salvar(FuncionarioEscola funcionarioEscola) {
		funcionarioEscola = em.merge(funcionarioEscola);
		return funcionarioEscola;
	}

	@Override
	public void excluir(FuncionarioEscola funcionarioEscola) {
		em.remove(em.merge(funcionarioEscola));
		;
	}

	@Override
	public FuncionarioEscola obterDirectorEscolaPorPermicao(RoleName permissao, Long idEscola) {
		@SuppressWarnings("unchecked")
		List<FuncionarioEscola> funcionarioEscolas = em
				.createQuery("From FuncionarioEscola WHERE permissao.descricao=:permissao AND escola.id=:idEscola")
				.setParameter("permissao", permissao).setParameter("idEscola", idEscola).getResultList();
		if (!funcionarioEscolas.isEmpty()) {
			return funcionarioEscolas.get(0);
		}
		return new FuncionarioEscola();
	}

	@Override
	public FuncionarioEscola obterFuncionarioEscolaExistente(Long idEscola, Long idFunc, Long idPermissao) {
		@SuppressWarnings("unchecked")
		List<FuncionarioEscola> funcionarioEscolas = em
				.createQuery(
						"From FuncionarioEscola WHERE permissao.id=:idPermissao AND escola.id=:idEscola AND funcionario.id=:idFunc")
				.setParameter("idEscola", idEscola).setParameter("idFunc", idFunc)
				.setParameter("idPermissao", idPermissao).getResultList();
		if (!funcionarioEscolas.isEmpty()) {
			return funcionarioEscolas.get(0);
		}
		return null;
	}

	@Override
	public List<FuncionarioEscola> obterFuncionarioEscolaPorIdFuncionario(Long idFunc) {
		@SuppressWarnings("unchecked")
		List<FuncionarioEscola> funcionarioEscolas = em
				.createQuery("From FuncionarioEscola WHERE funcionario.id=:idFunc").setParameter("idFunc", idFunc)
				.getResultList();
		if (!funcionarioEscolas.isEmpty()) {
			return funcionarioEscolas;
		}
		return null;
	}

	@Override
	public List<FuncionarioEscola> obterFuncionarioEscolaPorIdFuncionarioPorIdPermissao(Long idFuncionario,
			Long idPermissao) {
		@SuppressWarnings("unchecked")
		List<FuncionarioEscola> funcionarioEscolas = em
				.createQuery("From FuncionarioEscola WHERE funcionario.id=:idFunc AND permissao.id=:idPermissao")
				.setParameter("idFunc", idFuncionario).setParameter("idPermissao", idPermissao).getResultList();
		if (!funcionarioEscolas.isEmpty()) {
			return funcionarioEscolas;
		}
		return null;
	}

	@Override
	public List<FuncionarioEscola> obterDirigentesDaEscolaPorPermicoes(String permissao, String permissao2,
			Long idEscola) {
		@SuppressWarnings("unchecked")
		List<FuncionarioEscola> funcionarioEscolas = em
				.createQuery("From FuncionarioEscola WHERE (permissao.descricao=:permissao or permissao.descricao=:permissao2) AND escola.id=:idEscola")
				.setParameter("permissao", permissao).setParameter("permissao2", permissao2).setParameter("idEscola", idEscola).getResultList();
		if (!funcionarioEscolas.isEmpty()) {
			return funcionarioEscolas;
		}
		return new ArrayList<>();
	}

	@Override
	public List<FuncionarioEscola> obterFuncionariosPorEscola(Long idEscola) {
		@SuppressWarnings("unchecked")
		List<FuncionarioEscola> funcionarioEscolas = em
				.createQuery("From FuncionarioEscola WHERE permissao.descricao !='ROLE_SECRETARIO' AND  escola.id=:idEscola")
				.setParameter("idEscola", idEscola).getResultList();
		if (!funcionarioEscolas.isEmpty()) {
			return funcionarioEscolas;
		}
		return new ArrayList<>();
	}

	@Override
	public List<FuncionarioEscola> obterFuncionariosPorIdEscola(Long idEscola) {
		@SuppressWarnings("unchecked")
		List<FuncionarioEscola> funcionarioEscolas = em
				.createQuery("From FuncionarioEscola WHERE  escola.id=:idEscola")
				.setParameter("idEscola", idEscola).getResultList();
		if (!funcionarioEscolas.isEmpty()) {
			return funcionarioEscolas;
		}
		return new ArrayList<>();
	}

	@Override
	public List<Funcionario> obterFuncionariosPorIdEscola(Long idEscola, String pesquisa) {
		@SuppressWarnings("unchecked")
		List<Funcionario> funcionarioEscolas = em
				.createQuery("SELECT DISTINCT fe.funcionario FROM FuncionarioEscola fe WHERE (fe.funcionario.nome LIKE '%"+pesquisa.toUpperCase()+"%'  OR fe.funcionario.login LIKE '%"+pesquisa+"%') AND  fe.escola.id=:idEscola and fe.activo=true and fe.permissao.descricao !='ROLE_PROGRAMADOR' order by fe.funcionario.nome")
				.setParameter("idEscola", idEscola).getResultList();
		if (!funcionarioEscolas.isEmpty()) {
			return funcionarioEscolas;
		}
		return new ArrayList<>();
	}

	@Override
	public FuncionarioEscola obterFuncionarioEscolaPorId(Long id) {
		@SuppressWarnings("unchecked")
		List<FuncionarioEscola> funcionarioEscolas = em
				.createQuery("From FuncionarioEscola WHERE  id=:id")
				.setParameter("id", id).getResultList();
		if (!funcionarioEscolas.isEmpty()) {
			return funcionarioEscolas.get(0);
		}
		return new FuncionarioEscola();
	}

	@Override
	public List<FuncionarioEscola> obterFuncionarioEscolaPorIdEscolaPorIdFuncionario(Long idEscola,
			Long idFuncionario) {
		@SuppressWarnings("unchecked")
		List<FuncionarioEscola> funcionarioEscolas = em
				.createQuery("From FuncionarioEscola WHERE funcionario.id=:idFunc AND escola.id=:idEscola")
				.setParameter("idFunc", idFuncionario).setParameter("idEscola", idEscola).getResultList();
		
			return funcionarioEscolas;
		
	}
	
	@Override
	public FuncionarioEscola obterFuncionarioEscolaPorIdEscolaPorIdFuncionario1(Long idEscola,
			Long idFuncionario) {
		@SuppressWarnings("unchecked")
		List<FuncionarioEscola> funcionarioEscolas = em
				.createQuery("From FuncionarioEscola WHERE funcionario.id=:idFunc AND escola.id=:idEscola")
				.setParameter("idFunc", idFuncionario).setParameter("idEscola", idEscola).getResultList();
		
		if (!funcionarioEscolas.isEmpty()) {
			return funcionarioEscolas.get(0);
		}
		return new FuncionarioEscola();
		
	}


}
