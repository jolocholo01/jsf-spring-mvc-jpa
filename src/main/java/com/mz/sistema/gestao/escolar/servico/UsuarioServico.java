package com.mz.sistema.gestao.escolar.servico;

import java.util.List;

import com.mz.sistema.gestao.escolar.modelo.Permissao;
import com.mz.sistema.gestao.escolar.modelo.Usuario;

public interface UsuarioServico {
	public Usuario salvarUsuario(Usuario usuario);
	public void salvar(Usuario usuario);
	public Usuario obterUsuarioPeloLogin(String login);
	public Usuario obterUsuarioPeloId(Long id);
	public List<Permissao> obterPermicoes();
	public Permissao obterPermicaoPolaDescricao(String nome);
	public List<Usuario> obterUsuarioPorPermicao(long IdPermisao);
	public void criptografarSenha();
	public List<Usuario> obterTodosUsuarios();
	public Usuario obterUsuarioPorEmail(String email);
	public String criptografarSenha(String password);
	public boolean verificarSenhaDigitada(String password, String encodePassword);
	public Usuario verificarUsuarioParaRecoperarSenha(String key, String token);
	


}
