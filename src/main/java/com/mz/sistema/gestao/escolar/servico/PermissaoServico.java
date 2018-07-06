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
import com.mz.sistema.gestao.escolar.modelo.Permissao;

public interface PermissaoServico {
	public Permissao salvarRetornarPermissao(Permissao permissao);	
	public void salvar(Permissao permissao);
	public void excluir(Permissao permissao);
	public List<Permissao> listarPermissoesPorDistrito();	
	public Permissao obterUsuarioPorIdPermicao(long IdPermisao);
	public Permissao obterPermisaoPorId(Long id);
	public List<Permissao> obterPermissoessPorDscricao(String pesquisa);
	public Permissao obterPermissaoExistente(String nome);
	public Permissao obterPermissaoPorDscricao(RoleName roleProgramador);
	public List<Permissao> obterPermissoesPorEscola();
	

}
