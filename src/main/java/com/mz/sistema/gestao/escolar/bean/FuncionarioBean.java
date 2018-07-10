package com.mz.sistema.gestao.escolar.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */

import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.enumerado.EstadoCivil;
import com.mz.sistema.gestao.escolar.enumerado.Provincia;
import com.mz.sistema.gestao.escolar.enumerado.RoleName;
import com.mz.sistema.gestao.escolar.enumerado.TipoFuncao;
import com.mz.sistema.gestao.escolar.modelo.Distrital;
import com.mz.sistema.gestao.escolar.modelo.Distrito;
import com.mz.sistema.gestao.escolar.modelo.Endereco;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.modelo.Pais;
import com.mz.sistema.gestao.escolar.modelo.Permissao;
import com.mz.sistema.gestao.escolar.modelo.Usuario;
import com.mz.sistema.gestao.escolar.servico.DirecaoDistritalServico;
import com.mz.sistema.gestao.escolar.servico.DistritoServico;
import com.mz.sistema.gestao.escolar.servico.EscolaServico;
import com.mz.sistema.gestao.escolar.servico.FuncionarioEscolaServico;
import com.mz.sistema.gestao.escolar.servico.FuncionarioServico;
import com.mz.sistema.gestao.escolar.servico.GeradorDeRelatoriosServico;
import com.mz.sistema.gestao.escolar.servico.PaisServico;
import com.mz.sistema.gestao.escolar.servico.PermissaoServico;
import com.mz.sistema.gestao.escolar.servico.RecuperarSenhaServico;
import com.mz.sistema.gestao.escolar.servico.UsuarioServico;
import com.mz.sistema.gestao.escolar.util.DataUtils;
import com.mz.sistema.gestao.escolar.util.GeradorCodigo;
import com.mz.sistema.gestao.escolar.util.Mensagem;
import com.mz.sistema.gestao.escolar.util.Replace;
import com.mz.sistema.gestao.escolar.util.StringUtil;
import com.mz.sistema.gestao.escolar.util.TipoLetra;

@Named
@Controller
@SessionScope
public class FuncionarioBean {
	private Funcionario funcionario = new Funcionario();
	private List<Funcionario> funcionarios = new ArrayList<>();
	private boolean virificarNomeFuncionarioBoolean = false;
	private boolean virificarUsuarioFuncionarioBoolean = false;
	
	
	private Integer funcionarioEncontrado = 0;
	private Funcionario funcionarioExclusao;
	private Funcionario funcionarioSelecionado;
	private Funcionario funcionarioCadastro;
	private Double cargaHoraria;

	private boolean estadoCadastro = false;
	private boolean programadorBoolean = false;
	private boolean efectivoBoolean = false;
	private boolean estadoAvancar = false;

	private List<EstadoCivil> estadoCivils;
	private List<Provincia> provincias = Arrays.asList(Provincia.values());
	private List<TipoFuncao> tipoFuncoes = Arrays.asList(TipoFuncao.values());

	private Distrito distrito = new Distrito();
	private List<Distrito> distritos = new ArrayList<>();
	private List<Pais> paises = new ArrayList<>();
	private List<Distrital> servicosdistritais = new ArrayList<>();

	// alocacao do funcionario na escola
	private boolean adicionarFuncionarioEscolaBoolean = false;
	private List<Escola> escolas = new ArrayList<>();
	private List<Permissao> permissoes = new ArrayList<>();
	private List<FuncionarioEscola> funcionarioEscolas = new ArrayList<>();
	private FuncionarioEscola funcionarioEscola;
	public FuncionarioEscola funcionarioEscolaExclusao;

	private static final String FORMATA_SENHA_PADRAO = "ddMMyyyy";

	private String pesquisa;
	private int quantidadeCaracteres;
	private Integer qtdCategoriaEscontrada;

	@Autowired
	private FuncionarioServico funcionarioServico;
	@Autowired
	private FuncionarioEscolaServico funcionarioEscolaServico;
	@Autowired
	private PaisServico paisServico;
	@Autowired
	private DistritoServico distritoServico;
	@Autowired
	private DirecaoDistritalServico servicoDistritalServico;

	@Autowired
	private PermissaoServico permissaoServico;
	@Autowired
	private UsuarioServico usuarioServico;
	@Autowired
	private EscolaServico escolaServico;
	@Autowired
	private AuthenticationContext authenticationContext;

	@Autowired
	private GeradorDeRelatoriosServico geradorDeRelatoriosServico;
	@Autowired
	private RecuperarSenhaServico recoperarSenhaServico;

	// funcao que esta ser chamado no pretty config para director distrital
	public void iniciarBean() {
		funcionarioSelecionado = null;
		estadoAvancar = false;
		this.funcionarioSelecionado = null;
		programadorBoolean = false;
		carregarDadosFunc();
	}

	// funcao que esta ser chamado no pretty config so para programador
	public void iniciarBeanProgramador() {
		funcionarioSelecionado = null;
		estadoAvancar = false;
		this.funcionarioSelecionado = null;
		programadorBoolean = true;
		carregarDadosFunc();

	}

	private void carregarDadosFunc() {
		funcionario = new Funcionario();
		virificarNomeFuncionarioBoolean = false;
		virificarUsuarioFuncionarioBoolean = false;
		this.funcionarioCadastro = null;
		distrito = new Distrito();
		estadoCadastro = false;
		funcionarios = new ArrayList<>();
		pesquisa = new String();
		funcionarioEncontrado = 0;
		this.funcionarioSelecionado = null;
		estadoCadastro = false;
		adicionarFuncionarioEscolaBoolean = false;
		cargaHoraria = 0D;
	}

	public void impromirRecibo(Funcionario funcionario) {
		try {
			// http://localhost:8080/sistema-escolar/login
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

			String ulrTrocaSenha = request.getRequestURL() + "";
			String acessoAOSistema = ulrTrocaSenha
					.replace("academico/director-ditrital/funcionario/cadastro.jsf", "login.jsp")
					.replace("academico/programador/funcionario/cadastro.jsf", "login.jsp")
					.replace("academico/director/funcionario/cadastro", "login.jsp");
			System.out.println("Link:" + acessoAOSistema);
			String caminho;

			if (programadorBoolean == true) {
				caminho = "/academico/programador/funcionario/relatorio/cadastro.jasper";
			} else {
				caminho = "/academico/relatorio/funcionario/cadastro.jasper";
			}

			Map<String, Object> parametro = new HashMap<>();
			parametro.put("link", acessoAOSistema);
			if (funcionario.isSenhaAlterada() == false) {
				String senha = DataUtils.obterDataFormatoBanco(funcionario.getDataNascimento(), FORMATA_SENHA_PADRAO);
				parametro.put("senha", "Senha: ");
				parametro.put("dataSenha", senha);
			} else {
				parametro.put("senha", null);
				parametro.put("dataSenha", null);
			}
			if (funcionario.getNascionalidade() != null) {
				if (funcionario.getNascionalidade().equals("Estrangeira")) {
					parametro.put("pais", "País: ");
					parametro.put("paisValue", funcionario.getPais().getNome());
					parametro.put("localNasci", null);
					// funcionario de mocambique
				} else if (!funcionario.getNascionalidade().equals("Estrangeira")) {
					parametro.put("pais", null);
					parametro.put("paisValue", null);
					parametro.put("localNasci", "LN");
					if (funcionario.getEnderenco() == null) {

						parametro.put("provinciaFuncionario", "");
					} else {
						parametro.put("provinciaFuncionario", funcionario.getEnderenco().getDistrito().getProvincia());
					}
				}
			}
			parametro.put("idFuncionario", funcionario.getId());
			parametro.put("idDirecaoDitrital", funcionario.getDirecaoDistrital().getId());

			geradorDeRelatoriosServico.geraPdf(caminho, parametro, "doc.pdf");
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public String gerenciarFuncionario() {
		return "/academico/director-ditrital/escola/funcionario?faces-redirect=true";
	}



	public void transformar() {
		if (funcionario.getObservacao() == null) {
		} else
			this.quantidadeCaracteres = funcionario.getObservacao().length();
	}

	public void salvar() {

		try {
			Funcionario funcionarioExistente = funcionarioServico.obterFuncionarioExistente(
					funcionario.getNome().toUpperCase(), funcionario.getDataNascimento(), funcionario.isSexo(),
					funcionario.getNascionalidade(), funcionario.getPai().toUpperCase(),
					funcionario.getMae().toUpperCase());

			if (funcionarioExistente != null && funcionarioExistente.getId() != funcionario.getId()) {
				Mensagem.mensagemInfo("AVISO: Já existe um Funcionário Cadastrado no sistema!");
				return;
			}
			String numeroMatricula = null;
			if (funcionario.getId() == null) {
				Long numeroUltimaMatricula = funcionarioServico.obterNumeroUltimoFuncionario();
				if (numeroUltimaMatricula == null) {
					numeroUltimaMatricula = 1l;
				} else {
					numeroUltimaMatricula++;
				}
				numeroMatricula = numeroUltimaMatricula.toString();
			} else {
				numeroMatricula = funcionario.getNumero();
			}

			funcionario.setNome(funcionario.getNome().toUpperCase());
			funcionario.setPai(funcionario.getPai().toUpperCase());
			funcionario.setMae(funcionario.getMae().toUpperCase());
			if (funcionario.getEmail() != null) {
				funcionario.setEmail(funcionario.getEmail().toLowerCase());
			}
			if (funcionario.getNascionalidade().equals("Estrangeira")) {
				funcionario.getEnderenco().setDistrito(null);
				funcionario.setLocalNascimento(null);
			} else if (!funcionario.getNascionalidade().equals("Estrangeira")) {
				funcionario.setPais(null);

			}
			funcionario.setHoraCadastro(new Date());
			// geracao da senha e a criptografia da mesma
			if (funcionario.getSenha() == null) {
				String senha = DataUtils.obterDataFormatoBanco(funcionario.getDataNascimento(), FORMATA_SENHA_PADRAO);
				String senhaCriptgrafada = usuarioServico.criptografarSenha(senha);
				funcionario.setSenha(senhaCriptgrafada);
				System.out.println("Senha de  : " + funcionario.getNome() + " é :" + senha);
			}
			if (numeroMatricula != null) {
				numeroMatricula = StringUtil.preencherZerosAEsquerda(numeroMatricula, 20);
			}
			funcionario.setNumero(numeroMatricula);
			funcionarioServico.salvar(funcionario);
			if (funcionario.getId() == null) {
				Mensagem.mensagemInfo("AVISO: Funcionário salvo com sucesso!");
			} else {
				Mensagem.mensagemInfo("AVISO: Funcionário foi atualizado com sucesso!");
			}
		
				pesquisa = funcionario.getLogin();

			buscarFuncionarioPorNomePorUsuarioPorTelefone();
			voltarPesquisa();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void voltarAvancar() {
		estadoAvancar = false;

	}

	public void avancar() {
		try {
			Distrital distrital = new Distrital();
			if (programadorBoolean == true) {
				distrital = servicoDistritalServico
						.obterDirecaoDistritalPorId(funcionario.getDirecaoDistrital().getId());
				if (funcionario.getLogin() != null && funcionario.getId() == null) {
					String login = funcionario.getLogin().substring(0, 3);
					Long numero = Long.valueOf(login);
					if (numero != distrital.getEndereco().getDistrito().getId()) {
						funcionario.setLogin(null);
					}
				}
				if (funcionario.getPermissoes().isEmpty()) {
					// String descricao = "ROLE_DIRECTOR_DISTRITO";
					Permissao permissao = permissaoServico.obterPermissaoPorDscricao(RoleName.ROLE_DIRECTOR_DISTRITO);
					if (permissao == null) {
						permissao = new Permissao();
						permissao.setDescricao(RoleName.ROLE_DIRECTOR_DISTRITO);
						permissao.setNome(RoleName.ROLE_DIRECTOR_DISTRITO.getLabel().toString());
						permissaoServico.salvar(permissao);
						permissao = permissaoServico.obterPermissaoPorDscricao(RoleName.ROLE_DIRECTOR_DISTRITO);
					}
					if (!funcionario.getPermissoes().contains(permissao)) {
						funcionario.getPermissoes().add(permissao);
						funcionario.setAtivo(true);
					}
				}
			}
			if (programadorBoolean == false) {
				distrital = authenticationContext.getFuncionarioDirecaoDistritalLogado();
			}
			if (funcionario.getLogin() == null) {

				if (distrital != null) {
					if (distrital.getEndereco().getDistrito().getId() != null) {
						String login = null;
						login = GeradorCodigo
								.gerarCodigoAleatorioSemRepeticao(distrital.getEndereco().getDistrito().getId());
						Usuario usuario = usuarioServico.obterUsuarioPeloLogin(login);
						while (usuario != null) {
							login = null;
							login = GeradorCodigo
									.gerarCodigoAleatorioSemRepeticao(distrital.getEndereco().getDistrito().getId());
							usuario = usuarioServico.obterUsuarioPeloLogin(login);
						}
						funcionario.setLogin(login);
					}
					funcionario.setDirecaoDistrital(distrital);
				}
				funcionario.setDataCadastro(new Date());
			}
			System.out.println("Data de Nascimento:"
					+ DataUtils.obterDataFormatoBanco(funcionario.getDataNascimento(), "dd/MM/yyyy"));
			estadoAvancar = true;
			transformar();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buscarFuncionario() {
		try {
			Distrital funcionarioLogado = authenticationContext.getFuncionarioDirecaoDistritalLogado();
			Distrital funcionarioDeDirecaoDitritalLogado = authenticationContext.getFuncionarioDirecaoDistritalLogado();

			if (virificarNomeFuncionarioBoolean == false && virificarUsuarioFuncionarioBoolean == false) {
				Mensagem.mensagemAlerta("AVISO: Informe os critérios de buscas");
				return;
			} else if (virificarUsuarioFuncionarioBoolean == true) {
				if (funcionario.getLogin().trim().equals("")) {
					Mensagem.mensagemAlerta("AVISO: Informe o usuário do funcionário");
				} else
					funcionarios = funcionarioServico.obterFuncionariosPorNomeOuUsuaio(funcionario.getLogin(),
							funcionarioLogado.getId(), funcionarioDeDirecaoDitritalLogado.getId());
			} else if (virificarNomeFuncionarioBoolean == true) {
				if (funcionario.getNome().trim().equals("")) {
					Mensagem.mensagemAlerta("AVISO: Informe o nome do funcionário");
				} else
					funcionarios = funcionarioServico.obterFuncionariosPorNomeOuUsuaio(funcionario.getNome(),
							funcionarioLogado.getId(), funcionarioDeDirecaoDitritalLogado.getId());
			}
			if (virificarNomeFuncionarioBoolean == true && virificarUsuarioFuncionarioBoolean == true) {
				if (funcionario.getNome().trim().equals("") && funcionario.getLogin().trim().equals("")) {
					Mensagem.mensagemAlerta("AVISO: Informe o usuário e nome do funcionário");
				} else
					funcionarios = funcionarioServico.obterFuncionariosPorNomePorUsuaio(funcionario.getNome(),
							funcionario.getLogin(), funcionarioDeDirecaoDitritalLogado.getId());
			}
			if (funcionarios == null) {
				funcionarioEncontrado = 0;
			} else if (funcionarios != null) {
				funcionarioEncontrado = funcionarios.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscarFuncionarioPorNomePorUsuarioPorTelefone() {
		try {
			if (programadorBoolean == true) {
				funcionarios = funcionarioServico.obterFuncionariosPorNomeOuUsuaio(pesquisa.trim());
			} else {
				Usuario funcionarioLogado = authenticationContext.getUsuarioLogado();
				Distrital funcionarioDeDirecaoDitritalLogado = authenticationContext
						.getFuncionarioDirecaoDistritalLogado();

				funcionarios = funcionarioServico.obterFuncionariosPorNomeOuUsuaio(pesquisa.trim(),
						funcionarioLogado.getId(), funcionarioDeDirecaoDitritalLogado.getId());
			}
			if (funcionarios != null) {
				funcionarioEncontrado = funcionarios.size();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void prepararExclusao(Funcionario funcionario) {
		this.funcionarioExclusao = funcionario;
		String nomeFuncionario = TipoLetra.capitalizeString(this.funcionarioExclusao.getNome())
				.replace(" Dos ", " dos ").replace(" Das ", "das").replace(" De ", " de ").replace(" À ", " à ");
		this.funcionarioExclusao.setNome(nomeFuncionario);
	}

	public void buscarDadosFuncionaroSelecionado(Funcionario funcionario) {
		this.funcionarioSelecionado = funcionario;
		buscarFuncionarioSelecionadoComListaEscolaCategoria();
	}

	private void buscarFuncionarioSelecionadoComListaEscolaCategoria() {
		funcionarioEscolas = funcionarioEscolaServico
				.obterFuncionarioEscolaPorIdFuncionario(this.funcionarioSelecionado.getId());
		if (funcionarioEscolas != null)
			for (FuncionarioEscola funcionarioEscola : funcionarioEscolas) {
				String nomePermissao = TipoLetra.capitalizeString(funcionarioEscola.getPermissao().getNome())
						.replace(" Dos ", " dos ").replace(" Das ", "das").replace(" De ", " de ")
						.replace(" Da ", " da ").replace(" À ", " à ");
				funcionarioEscola.getPermissao().setNome(nomePermissao);
				if (funcionarioEscola.isActivo()) {
					efectivoBoolean = true;
				}

			}
		if (funcionarioEscolas == null) {
			qtdCategoriaEscontrada = 0;
		} else {
			qtdCategoriaEscontrada = funcionarioEscolas.size();
		}
	}

	public void prepararExcluirCategoria(FuncionarioEscola funcionarioEscola) {
		this.funcionarioEscolaExclusao = funcionarioEscola;

		this.funcionarioEscolaExclusao.getPermissao()
				.setNome(Replace.rescreverTexto(this.funcionarioEscolaExclusao.getPermissao().getNome()));
		String nomeFuncionario = TipoLetra.capitalizeString(this.funcionarioEscolaExclusao.getFuncionario().getNome())
				.replace(" Dos ", " dos ").replace(" Das ", "das").replace(" De ", " de ").replace(" À ", " à ");
		this.funcionarioEscolaExclusao.getFuncionario().setNome(nomeFuncionario);

	}

	public void excluirCategoria() {

		try {
			removerUsuarioPermissao(funcionarioEscolaExclusao);
			if (funcionarioEscolaExclusao.isActivo()) {
				efectivoBoolean = false;
			}
			funcionarioEscolaServico.excluir(funcionarioEscolaExclusao);
			buscarFuncionarioSelecionadoComListaEscolaCategoria();
			Mensagem.mensagemInfo("AVISO: a categoria do funcionário foi removida com sucesso");
		} catch (Exception e) {
			Mensagem.mensagemErro("AVISO: a categoria do funcionário não foi removida através da dependência!");
			e.printStackTrace();
		}

	}

	public void editar(Funcionario funcionario) {

		this.funcionario = funcionario;
		if (funcionario.getEnderenco() == null) {
			funcionario.setEnderenco(new Endereco());
		} else if (funcionario.getEnderenco().getDistrito() != null) {
			distrito.setProvincia(funcionario.getEnderenco().getDistrito().getProvincia());
			distritos = distritoServico.obterDistritosDaProvincia(distrito.getProvincia());
			distrito.setNome(funcionario.getEnderenco().getDistrito().getNome());
		}
		if (funcionario.getPais() == null) {
		} else {
			paises = paisServico.obterTodosPaises();
		}

		this.estadoCadastro = true;
		this.estadoAvancar = false;
		try {
			if (programadorBoolean == true) {
				servicosdistritais = servicoDistritalServico.obterTodosServicosDistritais();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void adicionarFuncionarioEscola() {
		try {
			permissoes = permissaoServico.listarPermissoesPorDistrito();
			adicionarFuncionarioEscolaBoolean = true;
			escolas = escolaServico.obterTodasEsolasPorDirecaoDistrital();
			funcionarioEscola = new FuncionarioEscola();
			funcionarioEscola.setDataCadastro(new Date());
			efectivoBoolean = false;
			if (funcionarioEscolas != null)
				for (FuncionarioEscola funcionarioEscola : funcionarioEscolas) {
					if (funcionarioEscola.isActivo()) {
						efectivoBoolean = true;
					}

				}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void alocar() {
		try {
			FuncionarioEscola funcionarioEscolaExistente = funcionarioEscolaServico.obterFuncionarioEscolaExistente(
					funcionarioEscola.getEscola().getId(), funcionarioSelecionado.getId(),
					funcionarioEscola.getPermissao().getId());
			if (funcionarioEscolaExistente != null && funcionarioEscolaExistente.getId() != funcionarioEscola.getId()) {
				Mensagem.mensagemErro("ERRO: o funcionário já foi alocado nessa escola com essa categoria!");
				return;
			}
			cargaHoraria = funcionarioSelecionado.getCargaHoraria();
			funcionarioEscola.setFuncionario(funcionarioSelecionado);

			Funcionario funcionario = funcionarioServico
					.obterFuncionarioPorIdPorPermissoes(funcionarioSelecionado.getId());

			funcionarioEscola.setHoraCadastro(new Date());

			if (funcionarioEscola.isActivo()) {
				funcionarioEscola.setDescricao(funcionarioEscola.getPermissao().getNome());
			} else {
				if (funcionarioEscola.getPermissao().getNome().equals(RoleName.ROLE_SECRETARIO.getLabel().toString())) {
					funcionarioEscola.setDescricao("AUXILAR DA SECRETARIA");
				} else if (funcionarioEscola.getPermissao().getNome()
						.equals(RoleName.ROLE_DIRECTOR.getLabel().toString())) {
					funcionarioEscola.setDescricao("AUXILAR DO DIRECTOR DA ESCOLA");
				} else if (funcionarioEscola.getPermissao().getNome()
						.equals(RoleName.ROLE_PROFESSOR.getLabel().toString())) {
					funcionarioEscola.setDescricao("AUXILAR DO PROFESSOR");
				}
				else if (funcionarioEscola.getPermissao().getNome()
						.equals(RoleName.ROLE_DIRECTOR_ADJUNTO.getLabel().toString())) {
					funcionarioEscola.setDescricao("AUXILAR DO DIRECTOR ADJUNTO DA ESCOLA");
				}

			}

			funcionarioEscola = funcionarioEscolaServico.salvar(funcionarioEscola);
			if (funcionario == null) {
				funcionarioSelecionado = funcionarioServico.obterFuncionarioPorId(funcionarioSelecionado.getId());
				funcionarioSelecionado.setPermissoes(new ArrayList<>());
			} else {
				funcionarioSelecionado = funcionario;
			}
			if (funcionarioEscola.isActivo()) {
				if (funcionarioSelecionado.isAtivo() == false)
					funcionarioSelecionado.setAtivo(true);

				if (!funcionarioSelecionado.getPermissoes().contains(funcionarioEscola.getPermissao())) {
					funcionarioSelecionado.getPermissoes().add(funcionarioEscola.getPermissao());
				}
			}
			if (funcionarioEscola.isActivo() == false) {
				Funcionario funcionarioRemovido = funcionarioServico
						.obterFuncionarioPorIdPorPermissoes(funcionarioSelecionado.getId());

				if (funcionario != null) {
					if (!funcionarioRemovido.getPermissoes().contains(funcionarioEscola.getPermissao())) {
						funcionarioSelecionado.getPermissoes().remove(funcionarioEscola.getPermissao());
					}
					if (funcionarioRemovido.getPermissoes().contains(funcionarioEscola.getPermissao())) {
						if (funcionarioRemovido.getPermissoes().size() == 1) {
							funcionarioSelecionado.setAtivo(false);
						} else
							funcionarioSelecionado.getPermissoes().remove(funcionarioEscola.getPermissao());
					}

				}

			}
			funcionarioSelecionado.setCargaHoraria(cargaHoraria);
			funcionarioServico.salvar(funcionarioSelecionado);

			Mensagem.mensagemInfo("AVISO: funcionário alocado com sucesso!");
			voltarNaAlocacao();
			buscarFuncionarioSelecionadoComListaEscolaCategoria();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void editar(FuncionarioEscola funcionarioEscola) {
		this.funcionarioEscola = funcionarioEscola;
		try {
			if (this.funcionarioEscola.isActivo()) {
				efectivoBoolean = false;
			}
			permissoes = permissaoServico.listarPermissoesPorDistrito();
			adicionarFuncionarioEscolaBoolean = true;
			escolas = escolaServico.obterTodasEsolasPorDirecaoDistrital();
			funcionarioEscola.setDataCadastro(new Date());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// falta verificar em quantas direcoes o funcionario esta alocado
	public void removerUsuarioPermissao(FuncionarioEscola funcionarioEscola) {
		try {
			List<FuncionarioEscola> funcionarioEscolas = funcionarioEscolaServico
					.obterFuncionarioEscolaPorIdFuncionario(funcionarioEscola.getFuncionario().getId());

			Usuario usuarioSelecionado = usuarioServico.obterUsuarioPeloId(funcionarioSelecionado.getId());
			if (funcionarioEscolas == null) {
			} else {
				if (funcionarioEscolas.size() == 1) {
					if (usuarioSelecionado.getPermissoes().size() == 1) {
						if (usuarioSelecionado.getPermissoes().contains(funcionarioEscola.getPermissao())) {
							usuarioSelecionado.getPermissoes().remove(funcionarioEscola.getPermissao());
							usuarioSelecionado.setAtivo(false);
							usuarioServico.salvar(usuarioSelecionado);
						}

					} else
						verificarPermissao(funcionarioEscola, usuarioSelecionado);
				} else if (funcionarioEscolas.size() > 1) {
					int contador = 0;
					for (int i = 0; i < funcionarioEscolas.size(); i++) {

						if (funcionarioEscolas.get(i).getPermissao().equals(funcionarioEscola.getPermissao())) {
							contador++;

						}
					}
					System.out.println("Quantidade de categoria: " + contador);
					if (contador == 1) {
						verificarPermissao(funcionarioEscola, usuarioSelecionado);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void verificarPermissao(FuncionarioEscola funcionarioEscola, Usuario usuarioSelecionado) {
		if (usuarioSelecionado.getPermissoes().contains(funcionarioEscola.getPermissao())) {
			usuarioSelecionado.getPermissoes().remove(funcionarioEscola.getPermissao());
			usuarioServico.salvar(usuarioSelecionado);
		}
	}

	public void bucarPaises() {
		if (funcionario.getNascionalidade() != null)
			if (funcionario.getNascionalidade().equals("Estrangeira"))
				paises = paisServico.obterTodosPaises();
	}

	public void bucarDistritoDaProvincia() {
		try {
			if (!distrito.equals(null))
				if (distrito.getProvincia() != null)
					distritos = distritoServico.obterDistritosDaProvincia(distrito.getProvincia());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void voltar() {
		this.funcionarioSelecionado = null;
		this.adicionarFuncionarioEscolaBoolean = false;
		funcionarioEscola = null;

		buscarFuncionarioPorNomePorUsuarioPorTelefone();

	}

	public void voltarNaAlocacao() {
		this.adicionarFuncionarioEscolaBoolean = false;
		funcionarioEscola = null;

	}

	public void novoFuncionario() {
		this.funcionario = new Funcionario();
		this.estadoCadastro = true;
		estadoAvancar = false;
		this.quantidadeCaracteres = 0;
		servicosdistritais = new ArrayList<>();

		try {
			if (programadorBoolean == true) {
				funcionario.setPermissoes(new ArrayList<>());
				servicosdistritais = servicoDistritalServico.obterTodosServicosDistritais();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void voltarPesquisa() {
		this.funcionario = null;
		this.estadoCadastro = false;

	}

	public void excluir() {
		try {
			this.funcionarioExclusao.setNome(this.funcionarioExclusao.getNome().toUpperCase());
			Usuario usuario = usuarioServico.obterUsuarioPeloId(funcionarioExclusao.getId());
			if (usuario == null) {

			} else {
				if (usuario.getRecuperarSenha() == null) {
				} else {
					if (usuario.getRecuperarSenha().getId() != null)
						recoperarSenhaServico.excluir(usuario.getRecuperarSenha());
				}
			}
			funcionarioServico.excluir(this.funcionarioExclusao);
			Mensagem.mensagemInfo("AVISO: Funcionário excluido com sucesso");
			buscarFuncionarioPorNomePorUsuarioPorTelefone();
		} catch (Exception e) {
			Mensagem.mensagemErro("ERRO: O funcionário não foi excluido através da dependência");
		}

	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public Integer getFuncionarioEncontrado() {
		return funcionarioEncontrado;
	}

	public void setFuncionarioEncontrado(Integer funcionarioEncontrado) {
		this.funcionarioEncontrado = funcionarioEncontrado;
	}

	public boolean isVirificarNomeFuncionarioBoolean() {
		return virificarNomeFuncionarioBoolean;
	}

	public void setVirificarNomeFuncionarioBoolean(boolean virificarNomeFuncionarioBoolean) {
		this.virificarNomeFuncionarioBoolean = virificarNomeFuncionarioBoolean;
	}

	public boolean isVirificarUsuarioFuncionarioBoolean() {
		return virificarUsuarioFuncionarioBoolean;
	}

	public void setVirificarUsuarioFuncionarioBoolean(boolean virificarUsuarioFuncionarioBoolean) {
		this.virificarUsuarioFuncionarioBoolean = virificarUsuarioFuncionarioBoolean;
	}

	public Funcionario getFuncionarioExclusao() {
		return funcionarioExclusao;
	}

	public void setFuncionarioExclusao(Funcionario funcionarioExclusao) {
		this.funcionarioExclusao = funcionarioExclusao;
	}

	public Funcionario getFuncionarioCadastro() {
		return funcionarioCadastro;
	}

	public void setFuncionarioCadastro(Funcionario funcionarioCadastro) {
		this.funcionarioCadastro = funcionarioCadastro;
	}

	public List<Pais> getPaises() {
		return paises;
	}

	public void setPaises(List<Pais> paises) {
		this.paises = paises;
	}

	public List<EstadoCivil> getEstadoCivils() {
		if (funcionario != null) {
			if (funcionario.isSexo() == true) {
				estadoCivils = Arrays.asList(EstadoCivil.CASADO, EstadoCivil.SOLTEIRO);
			} else if (funcionario.isSexo() == false) {
				estadoCivils = Arrays.asList(EstadoCivil.CASADA, EstadoCivil.SOLTEIRA);
			}
		}
		return estadoCivils;
	}

	public void setEstadoCivils(List<EstadoCivil> estadoCivils) {
		this.estadoCivils = estadoCivils;
	}

	public List<Provincia> getProvincias() {
		return provincias;
	}

	public void setProvincias(List<Provincia> provincias) {
		this.provincias = provincias;
	}

	public Distrito getDistrito() {
		return distrito;
	}

	public void setDistrito(Distrito distrito) {
		this.distrito = distrito;
	}

	public List<Distrito> getDistritos() {
		return distritos;
	}

	public void setDistritos(List<Distrito> distritos) {
		this.distritos = distritos;
	}

	public boolean isEstadoCadastro() {
		return estadoCadastro;
	}

	public void setEstadoCadastro(boolean estadoCadastro) {
		this.estadoCadastro = estadoCadastro;
	}

	public List<FuncionarioEscola> getFuncionarioEscolas() {
		return funcionarioEscolas;
	}

	public void setFuncionarioEscolas(List<FuncionarioEscola> funcionarioEscolas) {
		this.funcionarioEscolas = funcionarioEscolas;
	}

	public Funcionario getFuncionarioSelecionado() {
		return funcionarioSelecionado;
	}

	public void setFuncionarioSelecionado(Funcionario funcionarioSelecionado) {
		this.funcionarioSelecionado = funcionarioSelecionado;
	}

	public List<Escola> getEscolas() {
		return escolas;
	}

	public void setEscolas(List<Escola> escolas) {
		this.escolas = escolas;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public boolean isEstadoAvancar() {
		return estadoAvancar;
	}

	public void setEstadoAvancar(boolean estadoAvancar) {
		this.estadoAvancar = estadoAvancar;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public int getQuantidadeCaracteres() {
		return quantidadeCaracteres;
	}

	public void setQuantidadeCaracteres(int quantidadeCaracteres) {
		this.quantidadeCaracteres = quantidadeCaracteres;
	}

	public boolean isAdicionarFuncionarioEscolaBoolean() {
		return adicionarFuncionarioEscolaBoolean;
	}

	public void setAdicionarFuncionarioEscolaBoolean(boolean adicionarFuncionarioEscolaBoolean) {
		this.adicionarFuncionarioEscolaBoolean = adicionarFuncionarioEscolaBoolean;
	}

	public FuncionarioEscola getFuncionarioEscola() {
		return funcionarioEscola;
	}

	public void setFuncionarioEscola(FuncionarioEscola funcionarioEscola) {
		this.funcionarioEscola = funcionarioEscola;
	}

	public Integer getQtdCategoriaEscontrada() {
		return qtdCategoriaEscontrada;
	}

	public void setQtdCategoriaEscontrada(Integer qtdCategoriaEscontrada) {
		this.qtdCategoriaEscontrada = qtdCategoriaEscontrada;
	}

	public FuncionarioEscola getFuncionarioEscolaExclusao() {
		return funcionarioEscolaExclusao;
	}

	public void setFuncionarioEscolaExclusao(FuncionarioEscola funcionarioEscolaExclusao) {
		this.funcionarioEscolaExclusao = funcionarioEscolaExclusao;
	}

	public List<TipoFuncao> getTipoFuncoes() {
		return tipoFuncoes;
	}

	public void setTipoFuncoes(List<TipoFuncao> tipoFuncoes) {
		this.tipoFuncoes = tipoFuncoes;
	}

	public boolean isProgramadorBoolean() {
		return programadorBoolean;
	}

	public void setProgramadorBoolean(boolean programadorBoolean) {
		this.programadorBoolean = programadorBoolean;
	}

	public List<Distrital> getServicosdistritais() {
		return servicosdistritais;
	}

	public void setServicosdistritais(List<Distrital> servicosdistritais) {
		this.servicosdistritais = servicosdistritais;
	}

	public boolean isEfectivoBoolean() {
		return efectivoBoolean;
	}

	public void setEfectivoBoolean(boolean efectivoBoolean) {
		this.efectivoBoolean = efectivoBoolean;
	}

	public Double getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(Double cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

}
