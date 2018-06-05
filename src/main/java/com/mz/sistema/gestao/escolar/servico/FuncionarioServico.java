package com.mz.sistema.gestao.escolar.servico;

import java.util.Date;
import java.util.List;

import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;

public interface FuncionarioServico {
	public void salvar(Funcionario professor);
	public void excluir(Funcionario professor);
	public List<Funcionario> obterPorNome(String nome);
	public Funcionario obterFuncionarioPorId(Long idFuncionario);
	public List<Funcionario> listarTodos();
	public List<Funcionario> listarTodosAtivos();
	public Funcionario obterFuncionariosDaEscola(Long idFuncionario, Long idEscola);
	public List<Funcionario> obterPorId(Long idFuncionario);
	public List<FuncionarioEscola> listartodosProfessoresEscolaPorNome(String nome);
	public List<Funcionario> obterFuncionariosPorNomeOuUsuaio(String nomeOuUsuario, Long IdFuncionario, long IdDirecaoDistrital);
	public List<Funcionario> obterFuncionariosPorNomePorUsuaio(String nome, String usuario, long IdDirecaoDistrital);
	public Funcionario obterFuncionarioExistente(String nome, Date dataNascimento, boolean sexo, String nascionalidade,
			String pai, String mae);
	
	Long obterNumeroUltimoFuncionario();
	public Funcionario obterFuncionarioPorIdPorPermissoes(Long idFuncionario);
	public List<Funcionario> obterFuncionariosPorNomeOuUsuaio(String nomeOuUsuario);
	
	

}
