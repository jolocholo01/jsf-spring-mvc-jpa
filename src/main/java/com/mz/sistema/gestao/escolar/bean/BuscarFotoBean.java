// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.enumerado.EstadoCivil;
import com.mz.sistema.gestao.escolar.enumerado.RoleName;
import com.mz.sistema.gestao.escolar.modelo.Foto;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.modelo.Permissao;
import com.mz.sistema.gestao.escolar.modelo.Usuario;
import com.mz.sistema.gestao.escolar.servico.FotoServico;
import com.mz.sistema.gestao.escolar.servico.FuncionarioServico;
import com.mz.sistema.gestao.escolar.servico.PermissaoServico;
import com.mz.sistema.gestao.escolar.servico.UsuarioServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Named
@Controller
@SessionScoped
public class BuscarFotoBean {
	public boolean atualizarFotoBoolean = false;
	private Usuario usuario;
	private Funcionario funcionario;
	private Foto foto;
	private String autenticarSenha;
	private List<EstadoCivil> estadoCivils;
	public boolean alterarSenhaUsuarioBoolean = false;
	public boolean verificarFuncaoAtualizarBoolean = false;
	private boolean verificarLinkAtualizarPerfilBoolean = false;
	private boolean verificarLinkAtualizarDadosBoolean = false;
	private boolean programadorBoolean = false;
	@Autowired
	private PermissaoServico permissaoServico;
	@Autowired
	private AuthenticationContext autenticacao;
	@Autowired
	private UsuarioServico usuarioServico;
	@Autowired
	private FotoServico fotoServico;
	@Autowired
	private FuncionarioServico funcionarioServico;

	private Part fotografia;

	public void inicirBean() {
		alterarSenhaUsuarioBoolean = false;
		verificarFuncaoAtualizarBoolean = false;
		verificarLinkAtualizarDadosBoolean = false;
		verificarLinkAtualizarPerfilBoolean = false;
	}

	public void iniciarMeusPerfil() {
		verificarLinkAtualizarPerfilBoolean = true;
		verificarLinkAtualizarDadosBoolean = false;
		atualizarFotoBoolean = true;
		alterarSenhaUsuarioBoolean = false;
		verificarFuncaoAtualizarBoolean = false;
		try {
			usuario = autenticacao.getUsuarioLogado();
			funcionario = funcionarioServico.obterFuncionarioPorIdPorPermissoes(usuario.getId());
			if (funcionario == null) {
				funcionario = new Funcionario();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void iniciarAtualizacaoDados() {
		verificarLinkAtualizarDadosBoolean = true;
		verificarLinkAtualizarPerfilBoolean = false;
		atualizarFotoBoolean = true;
		alterarSenhaUsuarioBoolean = false;
		verificarFuncaoAtualizarBoolean = false;
	}

	public String getRetornarFoto() {
		try {
			foto = new Foto();
			if (atualizarFotoBoolean == false) {
				usuario = autenticacao.getUsuarioLogado();
				if (usuario == null) {
					usuario = new Usuario();
				}
				foto = fotoServico.obterFotoPorIdUsuario(usuario.getId());
				if (foto == null) {
					foto = new Foto();
				}
			}

			if (foto.getFotografia() != null) {
				return "";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/resources/images/user.png";

	}

	public void atualizar() {

		if (fotografia != null) {

			try {
				InputStream is = fotografia.getInputStream();
				byte[] bytes = IOUtils.toByteArray(is);
				foto.setFotografia(bytes);
				foto.setUsuario(usuario);
				this.foto = fotoServico.salvar(foto);
				System.out.println("Nome do Arquivo :" + fotografia.getInputStream());
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
		try {
			usuarioServico.salvar(usuario);
			usuario = usuarioServico.obterUsuarioPeloId(autenticacao.getUsuarioLogado().getId());
			if (funcionario != null) {
				if (funcionario.getId() != null)
					funcionarioServico.salvar(funcionario);
			}

			Mensagem.mensagemInfo("O seu perfil foi atualizado com sucesso");

			verificarFuncaoAtualizarBoolean = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void atualizarDados() {

		try {
			usuario.setEmail(usuario.getEmail().toLowerCase());
			if (programadorBoolean == true) {
				boolean verficador = false;
				if (usuario.getId() == null) {
					funcionario = new Funcionario();

					usuario.setPermissoes(new ArrayList<>());

					// String descricao = "ROLE_PROGRAMADOR";
					Permissao permissao = permissaoServico.obterPermissaoPorDscricao(RoleName.ROLE_PROGRAMADOR);
					if (permissao == null) {
						permissao = new Permissao();
						permissao.setDescricao(RoleName.ROLE_PROGRAMADOR);
						permissao.setNome(RoleName.ROLE_PROGRAMADOR.getLabel().toString());
						permissaoServico.salvar(permissao);
						permissao = permissaoServico.obterPermissaoPorDscricao(RoleName.ROLE_PROGRAMADOR);
					}
					if (!usuario.getPermissoes().contains(permissao)) {
						usuario.getPermissoes().add(permissao);
						usuario.setAtivo(true);
					}
					usuario = usuarioServico.salvarUsuario(usuario);
					usuario = usuarioServico.obterUsuarioPeloId(usuario.getId());
					funcionario.setId(usuario.getId());
					funcionarioServico.salvar(funcionario);
					verficador = true;
				}
				if (verficador == false)
					usuario = usuarioServico.salvarUsuario(usuario);

			} else
				usuario = usuarioServico.salvarUsuario(usuario);

			Mensagem.mensagemInfo("Os seus dados foram atualizado com sucesso");

			verificarFuncaoAtualizarBoolean = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void alterarSenha() {
		// String senhaCriptografada =
		// passwordEncoder.encodePassword(autenticarSenha, null);
		// if (senhaCriptografada.equals(usuario.getSenha())) {
		if (usuario.getNovaSenha().equals(usuario.getConfirmaSenha()) && usuario.getNovaSenha().length() >= 4) {
			String novaSenhaCriptografada = usuarioServico.criptografarSenha(usuario.getNovaSenha());
			usuario.setSenha(novaSenhaCriptografada);
			usuario.setSenhaAlterada(true);
			usuarioServico.salvar(usuario);
			usuario = usuarioServico.obterUsuarioPeloId(autenticacao.getUsuarioLogado().getId());
			Mensagem.mensagemInfo("Aviso: a sua senha foi alterada com sucesso!");
		} else {
			Mensagem.mensagemErro("AVISO Imposivel executar o pedido pois:".toUpperCase());
			if (!usuario.getNovaSenha().equals(usuario.getConfirmaSenha())) {
				Mensagem.mensagemErro("   SENHA deve ser igual a CONFIRMAÇÃO!");
			}
			if (usuario.getNovaSenha().length() < 4) {
				Mensagem.mensagemErro("   SENHA tem que possuir no mínimo 4 caracteres!");
			}
		}
		// }

	}

	public void salvarNovaSenha() {
		usuarioServico.salvar(usuario);
		usuario = usuarioServico.obterUsuarioPeloId(autenticacao.getUsuarioLogado().getId());

	}

	public void autenticar() {

		// String senhaCriptografada =
		// usuarioServico.criptografarSenha(autenticarSenha);
		if (usuarioServico.verificarSenhaDigitada(autenticarSenha, usuario.getPassword())) {
			alterarSenhaUsuarioBoolean = true;
		} else {
			Mensagem.mensagemErro("A senha digitada não confere.");
		}

	}

	public String voltar() {
		
		
		try {
			FacesContext context = FacesContext.getCurrentInstance();

			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			/// academico/director-ditrital/index.xhtml
			alterarSenhaUsuarioBoolean = false;
			String url = request.getRequestURI() + "";
			// URL:/sistema-escolar/academico/director/index.jsf
			
			System.out.println("URL:" + url);
			String vals[] = url.split("/academico/");
			String link = vals[1];
			String nomeArquivo = link.split("/")[0];			
			System.out.println("URL com uma pasta:" + "/academico/" + nomeArquivo);
			System.out.println("URL desejado:" + "/academico/" + nomeArquivo + "/index");
			
			return "/academico/" + nomeArquivo + "/index?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void enviarFoto() {
		if (fotografia != null) {
			try {
				InputStream is = fotografia.getInputStream();
				byte[] bytes = IOUtils.toByteArray(is);
				foto.setFotografia(bytes);
				foto.setUsuario(usuario);
				this.foto = fotoServico.salvar(foto);
			} catch (IOException e) {
				//
				e.printStackTrace();
			}
		}
		System.out.println("Chamou a funcao\n\n\n");
	}

	public String getRetornarImagesEscola() {
		return "/resources/images/escola.png";
	}

	public String getRetornarImagesFun() {
		return "/resources/images/funcionario.png";
	}

	public String getRetornarImagesFuns() {
		return "/resources/images/funcionarios.png";
	}

	public String getRetornarImagesprofessor() {
		return "/resources/images/professor.png";
	}

	public String getRetornarImagesFunc() {
		return "/resources/images/func.png";
	}

	public String getRetornarImagesDados() {
		return "/resources/images/dados.png";
	}

	public String getRetornarImagesPassword() {
		return "/resources/images/password.png";
	}

	public String getRetornarImagesRelatorio() {
		return "/resources/images/relatorio.png";
	}

	public String getRetornarImagesCalendario() {
		return "/resources/images/calendario.png";
	}

	public String getRetornarImagesNota() {
		return "/resources/images/nota.png";
	}

	public String getRetornarImagesNota1() {
		return "/resources/images/nota1.png";
	}

	public String getRetornarImagesDiario() {
		return "/resources/images/diario.png";
	}

	public String getRetornarImagesLivro() {
		return "/resources/images/books.png";
	}

	public String getRetornarImagesEstudante() {
		return "/resources/images/estudante.png";
	}

	public String getRetornarImagesMatricula() {
		return "/resources/images/matricula.png";
	}

	public String getRetornarImagesTurma() {
		return "/resources/images/turma.png";
	}

	public String getRetornarImagesTurma1() {
		return "/resources/images/turma1.png";
	}

	public String getRetornarImagesCorreto() {
		return "/resources/images/correto.gif";
	}

	public String getRetornarImagesNext() {
		return "/resources/images/next.png";
	}

	public String getRetornarImagesSeach() {
		return "/resources/images/proc.gif";
	}

	public String getRetornarImagesProximo() {
		return "/resources/images/proximo.png";
	}

	public String getRetornarImagesCadastro() {
		return "/resources/images/cadastro.png";
	}

	public String getRetornarImagesEditar() {
		return "/resources/images/editar.png";
	}

	public String getRetornarImagesProcurar() {
		return "/resources/images/procurar.png";
	}

	public String getRetornarImagesExcluir() {
		return "/resources/images/excluir.png";
	}

	public String getRetornarImagesAluno() {
		return "/resources/images/aluno1.png";
	}

	public String getRetornarImagesLivros() {
		return "/resources/images/livro.png";
	}

	public String getRetornarImageseditarProfe() {
		return "/resources/images/icon.png";
	}

	public String getRetornarImageseditar1() {
		return "/resources/images/editar1.png";
	}

	public String getRetornarImagesBuscarDados() {
		return "/resources/images/buscarDado.png";
	}

	public String getRetornarImagesBook() {
		return "/resources/images/book.png";
	}

	public String getRetornarImagesInativo() {
		return "/resources/images/inativo.png";
	}

	public String getRetornarImagesAtivo() {
		return "/resources/images/ativo.png";
	}

	public String getRetornarImagesBtn() {
		return "/resources/images/btn.png";
	}

	public String getRetornarImagesAtivar() {
		return "/resources/images/ativar.png";
	}

	public String getRetornarImagesPrint() {
		return "/resources/images/printer.png";
	}

	public String getRetornarImagesEmitir() {
		return "/resources/images/emitir.png";
	}

	public String getRetornarImagesMatriculaAluno() {
		return "/resources/images/ma.png";
	}

	public String getRetornarImagesAluno2() {
		return "/resources/images/aluno2.png";
	}

	public String getRetornarImagesExportarPDF() {
		return "/resources/images/exportar_pdf.png";
	}

	public String getRetornarImagesTransferencia() {
		return "/resources/images/if_transfer.png";
	}

	public String getRetornarImagesPrinter() {
		return "/resources/images/printer1.png";
	}

	public boolean isAtualizarFotoBoolean() {
		return atualizarFotoBoolean;
	}

	public void setAtualizarFotoBoolean(boolean atualizarFotoBoolean) {
		this.atualizarFotoBoolean = atualizarFotoBoolean;
	}

	public Usuario getUsuario() {

		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<EstadoCivil> getEstadoCivils() {
		if (usuario != null) {
			if (usuario.isSexo() == true) {
				estadoCivils = Arrays.asList(EstadoCivil.CASADO, EstadoCivil.SOLTEIRO, EstadoCivil.OUTRO);
			} else if (usuario.isSexo() == false) {
				estadoCivils = Arrays.asList(EstadoCivil.CASADA, EstadoCivil.SOLTEIRA, EstadoCivil.OUTRO);
			}
		}
		return estadoCivils;
	}

	public void setEstadoCivils(List<EstadoCivil> estadoCivils) {
		this.estadoCivils = estadoCivils;
	}

	public String getAutenticarSenha() {
		return autenticarSenha;
	}

	public void setAutenticarSenha(String autenticarSenha) {
		this.autenticarSenha = autenticarSenha;
	}

	public boolean isAlterarSenhaUsuarioBoolean() {
		return alterarSenhaUsuarioBoolean;
	}

	public void setAlterarSenhaUsuarioBoolean(boolean alterarSenhaUsuarioBoolean) {
		this.alterarSenhaUsuarioBoolean = alterarSenhaUsuarioBoolean;
	}

	public Part getFotografia() {

		return fotografia;
	}

	public void setFotografia(Part fotografia) {
		this.fotografia = fotografia;
	}

	public Foto getFoto() {
		return foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}

	public boolean isVerificarFuncaoAtualizarBoolean() {
		return verificarFuncaoAtualizarBoolean;
	}

	public void setVerificarFuncaoAtualizarBoolean(boolean verificarFuncaoAtualizarBoolean) {
		this.verificarFuncaoAtualizarBoolean = verificarFuncaoAtualizarBoolean;
	}

	public boolean isVerificarLinkAtualizarPerfilBoolean() {
		return verificarLinkAtualizarPerfilBoolean;
	}

	public void setVerificarLinkAtualizarPerfilBoolean(boolean verificarLinkAtualizarPerfilBoolean) {
		this.verificarLinkAtualizarPerfilBoolean = verificarLinkAtualizarPerfilBoolean;
	}

	public boolean isVerificarLinkAtualizarDadosBoolean() {
		return verificarLinkAtualizarDadosBoolean;
	}

	public void setVerificarLinkAtualizarDadosBoolean(boolean verificarLinkAtualizarDadosBoolean) {
		this.verificarLinkAtualizarDadosBoolean = verificarLinkAtualizarDadosBoolean;
	}

	public boolean isProgramadorBoolean() {
		return programadorBoolean;
	}

	public void setProgramadorBoolean(boolean programadorBoolean) {
		this.programadorBoolean = programadorBoolean;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
}
