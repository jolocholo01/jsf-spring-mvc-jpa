package com.mz.sistema.gestao.escolar.servico.impl;

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

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.modelo.Permissao;
import com.mz.sistema.gestao.escolar.modelo.Usuario;
import com.mz.sistema.gestao.escolar.servico.UsuarioServico;
import com.mz.sistema.gestao.escolar.util.DataUtils;

@Service
@Transactional
public class UsuarioServicoImpl implements UsuarioServico {
	private static final String FORMATA_DATA_NESCIMENTO_PARA_SENHA_PADRAO = "ddMMyyyy";

	@PersistenceContext
	private EntityManager em;

	@Override
	public void salvar(Usuario usuario) {
		em.merge(usuario);
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		usuario = em.merge(usuario);
		return usuario;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Usuario obterUsuarioPeloLogin(String login) {
		List<Usuario> usuarios = em.createQuery("from Usuario u join fetch u.permissoes where u.login = :login")
				.setParameter("login", login).getResultList();

		if (usuarios.isEmpty()) {
			return null;
		} else {
			return usuarios.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Permissao> obterPermicoes() {
		return em.createQuery("from Permissao").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Permissao obterPermicaoPolaDescricao(String descricao) {
		List<Permissao> permissaos = em.createQuery("from Permissao where descricao like '%" + descricao + "%'")
				.getResultList();

		if (!permissaos.isEmpty()) {
			return permissaos.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> obterUsuarioPorPermicao(long IdPermisao) {
		List<Usuario> permissaos = em.createQuery("select usuarios from Permissao where permissoes_id=:IdPermisao")
				.setParameter("IdPermisao", IdPermisao).getResultList();

		if (!permissaos.isEmpty()) {
			return permissaos;
		}
		return null;
	}

	@Override
	public Usuario obterUsuarioPeloId(Long id) {
		@SuppressWarnings("unchecked")
		List<Usuario> usuarios = em.createQuery("from Usuario u join fetch u.permissoes where u.id = :id")
				.setParameter("id", id).getResultList();

		if (usuarios.isEmpty()) {
			return null;
		} else {
			return usuarios.get(0);
		}
	}

	@Override
	public Usuario obterUsuarioPorEmail(String email) {
		@SuppressWarnings("unchecked")
		List<Usuario> usuarios = em.createQuery("from Usuario u join fetch u.permissoes where u.email =:Email ")
				.setParameter("Email", email).getResultList();
		if (usuarios.isEmpty()) {
			return null;
		} else {
			return usuarios.get(0);
		}
	}

	@Override
	public List<Usuario> obterTodosUsuarios() {
		@SuppressWarnings("unchecked")
		List<Usuario> usuarios = em.createQuery("from Usuario u join fetch u.permissoes").getResultList();
		if (usuarios.isEmpty()) {
			return null;
		} else {
			return usuarios;
		}
	}

	@Override
	// @Scheduled(fixedDelay = 30000)
	public void criptografarSenha() {
		List<Usuario> usuarios = obterTodosUsuarios();

		for (Usuario usuario : usuarios) {
			System.out.println("\n\n-------------------NOME DO USUARIO :" + usuario.getNome().toUpperCase()
					+ "-------------------------");
			System.out.println("Data de nascimeno.:  " + DataUtils.obterDataFormatoBanco(usuario.getDataNascimento(),
					FORMATA_DATA_NESCIMENTO_PARA_SENHA_PADRAO));
			if (usuario.isSexo() == true) {
				System.out.println("Sexo..............:  " + "Masculino");
			} else if (usuario.isSexo() == false) {
				System.out.println("Sexo..............:  " + "Femenino");
			}
			System.out.println("Usuario...........:  " + usuario.getLogin());
			// String senha =
			// DataUtils.obterDataFormatoBanco(usuario.getDataNascimento(),
			// FORMATO_BANCO);
			System.out.println("Senha.............:  " + usuario.getSenha());
			String senhaCriptografada = criptografarSenha(usuario.getSenha());
			String senhaCriptografadaObtidoPorLogin = criptografarSenha(usuario.getLogin());
			if (usuario.getSenha().equals(senhaCriptografadaObtidoPorLogin)) {
				System.out.println("Senha ja foi Criptgrafada.");
				System.out.println("Sao iguais");
			} else {
				System.out.println("Senha Criptgrafada.: '" + senhaCriptografada + "'");
			}
		}
	}

	@Override
	public String criptografarSenha(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodePassword = encoder.encode(password);
		return encodePassword;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Usuario verificarUsuarioParaRecoperarSenha(String key, String token) {
		List<Usuario> usuarios = em
				.createQuery(
						" FROM Usuario  WHERE recuperarSenha.codigo=:Codigo AND recuperarSenha.parametro=:Parametro AND recuperarSenha.dataExpiracao > NOW()")
				.setParameter("Codigo", key).setParameter("Parametro", token).getResultList();
		if (!usuarios.isEmpty()) {
			return usuarios.get(0);
		}
		return null;

	}

	@Override
	public boolean verificarSenhaDigitada(String senhaFornecida, String senhaBanco) {
		DataUtils.vericicarSenhaUsuarioSelecionado(senhaFornecida);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(senhaFornecida, senhaBanco);
	}

}
