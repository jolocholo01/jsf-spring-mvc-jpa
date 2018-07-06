package com.mz.sistema.gestao.escolar.servico;

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
import com.mz.sistema.gestao.escolar.enumerado.RoleName;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;

public interface FuncionarioEscolaServico {
	public FuncionarioEscola salvar(FuncionarioEscola funcionarioEscola);
	public void excluir(FuncionarioEscola funcionarioEscola);
	public FuncionarioEscola obterDirectorEscolaPorPermicao(RoleName permissao,  Long idEscola);
	public List<FuncionarioEscola> obterDirigentesDaEscolaPorPermicoes(String permissao,String permissao2 ,  Long idEscola);
	public FuncionarioEscola obterFuncionarioEscolaExistente(Long idEscola, Long idFunc, Long idPermissao);
	public List<FuncionarioEscola> obterFuncionarioEscolaPorIdFuncionario(Long idFunc);
	public List<FuncionarioEscola> obterFuncionarioEscolaPorIdFuncionarioPorIdPermissao(Long idFuncionario, Long idPermissao);
	public List<FuncionarioEscola> obterFuncionariosPorEscola(Long idEscola);
	public List<Funcionario> obterFuncionariosPorIdEscola(Long idEscola, String pesquisa);
	public List<FuncionarioEscola> obterFuncionariosPorIdEscola(Long idEscola);
	public FuncionarioEscola obterFuncionarioEscolaPorId(Long id);
	public List<FuncionarioEscola> obterFuncionarioEscolaPorIdEscolaPorIdFuncionario(Long idEscola, Long idFuncionario);
	public FuncionarioEscola obterFuncionarioEscolaPorIdEscolaPorIdFuncionario1(Long idEscola, Long idFuncionario);
	
	

	
}
