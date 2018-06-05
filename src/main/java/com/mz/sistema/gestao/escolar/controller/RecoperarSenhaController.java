package com.mz.sistema.gestao.escolar.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mz.sistema.gestao.escolar.modelo.RecoperarSenha;
import com.mz.sistema.gestao.escolar.modelo.Usuario;
import com.mz.sistema.gestao.escolar.repositorio.RecoperarSenhaRepositorio;
import com.mz.sistema.gestao.escolar.repositorio.UsuarioRepositorio;
import com.mz.sistema.gestao.escolar.servico.UsuarioServico;

@Controller
@RequestMapping(value = "/senha.html")
public class RecoperarSenhaController {
	private static final String SENHA_VIEW = "recoperarSenha";

	@Autowired
	private RecoperarSenhaRepositorio recoperarSenhaRepositorio;
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	@Autowired
	private UsuarioServico usuarioServico;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView novaSenha(@RequestParam(value = "key") String key,
			@RequestParam(value = "token") String token) {

		ModelAndView mv = new ModelAndView(SENHA_VIEW);
		try {
			Usuario usuario = usuarioServico.verificarUsuarioParaRecoperarSenha(key, token);
			if (usuario == null) {
				mv.addObject(new Usuario());
			} else if (usuario != null) {
				System.out.println("Nome: " + usuario.getNome());
				System.out.println("Email: " + usuario.getEmail());
				System.out.println("Id :" + usuario.getId());
				mv.addObject("usuario", usuario);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView atualizarSenhaUsuario(@Valid @ModelAttribute Usuario usuario, Errors errors,
			RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView(SENHA_VIEW);
		if (errors.hasErrors()) {
			return mv;
		}
		try {
			if (usuario.getId() == null) {
				
				mv.addObject("mensagemExp",
						"Aviso: o endereço de recoperação de Senha já expirou. Por favor, Faça um novo pedido.");

			} else {
				 System.out.println("Nova Senha: " + usuario.getNovaSenha());
				 System.out.println("Nova Senha Confirmada: " +
				 usuario.getConfirmaSenha());
				System.out.println("Id :" + usuario.getId());

				Usuario usuarioSelecionado = usuarioRepositorio.findById(usuario.getId()).get();// findOne(usuario.getId());

				if (usuario.getNovaSenha().equals(usuario.getConfirmaSenha()) && usuario.getNovaSenha().length() >= 4
						&& usuarioSelecionado.getRecoperarSenha().isAtivo()) {

					String novaSenhaCriptografada = usuarioServico.criptografarSenha(usuario.getNovaSenha());
					System.out.println("Senha Criptgrafada.: '" + novaSenhaCriptografada + "'");
					usuarioSelecionado.setSenhaAlterada(true);
					usuarioSelecionado.setSenha(novaSenhaCriptografada);
					usuarioRepositorio.save(usuarioSelecionado);
					usuarioSelecionado = usuarioRepositorio.findById(usuario.getId()).get();
					//mv.addObject("mensagemSucesso", "Aviso: a sua senha foi alterada com sucesso!");

					// mv.addObject("usuario", usuarioSelecionado);
					RecoperarSenha recoperarSenha = new RecoperarSenha();
					recoperarSenha.setId(usuarioSelecionado.getRecoperarSenha().getId());
					recoperarSenha.setCodigo(usuarioSelecionado.getRecoperarSenha().getCodigo());
					recoperarSenha.setDataExpiracao(usuarioSelecionado.getRecoperarSenha().getDataExpiracao());
					recoperarSenha.setParametro(usuarioSelecionado.getRecoperarSenha().getParametro());
					recoperarSenha.setUsuario(usuarioSelecionado);
					recoperarSenha.setAtivo(false);
					recoperarSenhaRepositorio.save(recoperarSenha);
					mv.addObject("mensagemSucesso", "Aviso: a sua senha foi alterada com sucesso!");
					 System.out.println("Nova Senha Confirmada: " + usuario.getConfirmaSenha());

				} else {
					if (!usuario.getNovaSenha().equals(usuario.getConfirmaSenha())) {
						mv.addObject("mensagemErro", "Aviso: A SENHA deve ser igual a CONFIRMACAO!");
					} else if (usuario.getNovaSenha().length() < 4 && !usuario.getNovaSenha().trim().equals("")
							&& !usuario.getConfirmaSenha().trim().equals("")) {
						mv.addObject("mensagemErro", "Aviso: A SENHA tem que possuir no mínimo 4 caracteres!");
					} else if (usuario.getNovaSenha().trim().equals("") || usuario.getNovaSenha() == null) {
						mv.addObject("mensagemErro", "Aviso: A SENHA não pode ser vazia!");
					} else if (!usuarioSelecionado.getRecoperarSenha().isAtivo()) {
						mv.addObject("mensagemExp",
								"Aviso: o endereço de recoperação de Senha já expirou. Por favor, Faça um novo pedido.");
					}

					mv.addObject("usuario", usuarioSelecionado);
				}

				// }

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;
	}

}
