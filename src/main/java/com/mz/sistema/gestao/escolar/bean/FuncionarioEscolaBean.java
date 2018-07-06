package com.mz.sistema.gestao.escolar.bean;

import java.util.ArrayList;
import java.util.Arrays;
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

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.enumerado.EstadoCivil;
import com.mz.sistema.gestao.escolar.enumerado.Provincia;
import com.mz.sistema.gestao.escolar.modelo.Distrito;
import com.mz.sistema.gestao.escolar.modelo.Endereco;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.modelo.Pais;
import com.mz.sistema.gestao.escolar.modelo.Permissao;
import com.mz.sistema.gestao.escolar.modelo.Usuario;
import com.mz.sistema.gestao.escolar.servico.DistritoServico;
import com.mz.sistema.gestao.escolar.servico.FuncionarioEscolaServico;
import com.mz.sistema.gestao.escolar.servico.FuncionarioServico;
import com.mz.sistema.gestao.escolar.servico.PaisServico;
import com.mz.sistema.gestao.escolar.servico.PermissaoServico;
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
public class FuncionarioEscolaBean {
	private Funcionario funcionario = new Funcionario();
	private FuncionarioEscola funcionarioEscolaSelecionado;
	private FuncionarioEscola funcionarioEscola;

	private List<FuncionarioEscola> funcionarioEscolas = new ArrayList<>();
	private List<Funcionario> funcionarios = new ArrayList<>();
	private Escola escola = new Escola();
	private String pesquisa;
	private boolean editarFuncionarioBoolean;
	private boolean adicionarFuncionarioEscolaBoolean;
	private boolean alocarFuncionarioBoolean;
	private boolean novoFuncionarioBoolean;
	private int qtdFuncionariosEncontrados;
	private boolean proximaPrimeiraFaseBoolean;
	private boolean proximaSegundaFaseBoolean;
	private boolean proximaTerceiraFaseBoolean;
	private static final String FORMATA_SENHA_PADRAO = "ddMMyyyy";
	private List<EstadoCivil> estadoCivils;
	private List<Permissao> permissoes = new ArrayList<>();

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	@Autowired
	private FuncionarioServico funcionarioServico;
	@Autowired
	private FuncionarioEscolaServico funcionarioEscolaServico;
	@Autowired
	private PaisServico paisServico;
	@Autowired
	private DistritoServico distritoServico;


	@Autowired
	private PermissaoServico permissaoServico;
	@Autowired
	private UsuarioServico usuarioServico;

	@Autowired
	private AuthenticationContext authenticationContext;
	private Integer funcionarioEncontrado;
	private Funcionario funcionarioSelecionado;

	private List<Distrito> distritos = new ArrayList<>();
	private Distrito distrito = new Distrito();
	private List<Provincia> provincias = Arrays.asList(Provincia.values());

	private List<Pais> paises;
	private FuncionarioEscola funcionarioEscolaExclusao;

	public void iniciarBean() {
		funcionario = null;
		funcionarioEscolas = new ArrayList<>();
		funcionarios = new ArrayList<>();
		distritos = new ArrayList<>();
		editarFuncionarioBoolean = false;
		novoFuncionarioBoolean = false;
		qtdFuncionariosEncontrados = 0;
		proximaPrimeiraFaseBoolean = true;
		proximaSegundaFaseBoolean = false;
		proximaTerceiraFaseBoolean = false;
		alocarFuncionarioBoolean = false;
		adicionarFuncionarioEscolaBoolean = false;

	}

	public void buscarFuncionarioPorEscola() {
		try {

			FuncionarioEscola escolaLogado = authenticationContext.getFuncionarioEscolaLogada();
			System.out.println("id Escola:" + escolaLogado.getEscola().getId());
			funcionarios = funcionarioEscolaServico.obterFuncionariosPorIdEscola(escolaLogado.getEscola().getId(),
					pesquisa);

			if (funcionarios != null) {
				setFuncionarioEncontrado(funcionarios.size());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

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
			if (pesquisa == null) {
				pesquisa = funcionario.getLogin();
			}
			buscarFuncionarioPorEscola();
			voltarParaPequisa();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void novo() {
		novoFuncionarioBoolean = true;
		funcionario = new Funcionario();
		editarFuncionarioBoolean = false;
		proximaPrimeiraFaseBoolean = true;
		alocarFuncionarioBoolean = false;
	}

	public void voltarParaPequisa() {
		novoFuncionarioBoolean = false;
		editarFuncionarioBoolean = false;
		funcionario = null;
		proximaPrimeiraFaseBoolean = false;
		proximaSegundaFaseBoolean = false;
		proximaTerceiraFaseBoolean = false;
		alocarFuncionarioBoolean = false;
		buscarFuncionarioPorEscola();

	}

	public void bucarDistritoDaProvincia() {
		try {

			if (!distrito.equals(null)) {
				if (distrito.getProvincia() != null) {
					System.out.println("provincia de " + distrito.getProvincia());
					distritos = distritoServico.obterDistritosDaProvincia(distrito.getProvincia());

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void primeiraFase() {
		proximaPrimeiraFaseBoolean = false;
		proximaSegundaFaseBoolean = true;
		proximaTerceiraFaseBoolean = false;
		try {
			if (funcionario.getLogin() == null) {
				Funcionario funcionario = authenticationContext.getFuncionarioLogado();
				if (funcionario != null) {
					if (funcionario.getDirecaoDistrital().getEndereco().getDistrito().getId() != null) {
						String login = GeradorCodigo.gerarCodigoAleatorioSemRepeticao(
								funcionario.getDirecaoDistrital().getEndereco().getDistrito().getId());
						Usuario usuario = usuarioServico.obterUsuarioPeloLogin(login);
						while (usuario != null) {
							login = null;
							login = GeradorCodigo.gerarCodigoAleatorioSemRepeticao(
									funcionario.getDirecaoDistrital().getEndereco().getDistrito().getId());
							usuario = usuarioServico.obterUsuarioPeloLogin(login);
						}
						funcionario.setLogin(login);
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.mensagemFatal("AVISO: Deu erro inexperado!");
		}

	}

	public void voltarPrimeiraFase() {
		proximaPrimeiraFaseBoolean = true;
		proximaSegundaFaseBoolean = false;
		proximaTerceiraFaseBoolean = false;
	}

	public void segundaFase() {
		try {
			proximaPrimeiraFaseBoolean = false;
			proximaSegundaFaseBoolean = false;
			proximaTerceiraFaseBoolean = true;
			if (funcionario.getDataCadastro() == null) {
				funcionario.setDataCadastro(new Date());
			}

			if (funcionario.getSenha() == null) {
				String senha = DataUtils.obterDataFormatoBanco(funcionario.getDataNascimento(), FORMATA_SENHA_PADRAO);
				String senhaCriptgrafada = usuarioServico.criptografarSenha(senha);
				funcionario.setSenha(senhaCriptgrafada);

			}
			if (funcionario.getNascionalidade().equals("Estrangeira")) {
				funcionario.setEnderenco(new Endereco());
				funcionario.getEnderenco().setDistrito(null);
				funcionario.setLocalNascimento(null);
			} else if (!funcionario.getNascionalidade().equals("Estrangeira")) {
				funcionario.setPais(null);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void voltarSegundaFase() {
		proximaPrimeiraFaseBoolean = false;
		proximaSegundaFaseBoolean = true;
		proximaTerceiraFaseBoolean = false;
	}

	public void editar(Funcionario funcionario) {
		try {
			distrito = new Distrito();
			this.funcionario = funcionario;
			novoFuncionarioBoolean = true;
			editarFuncionarioBoolean = true;
			proximaPrimeiraFaseBoolean = true;
			alocarFuncionarioBoolean = false;
			if (funcionario.getEnderenco() == null) {
				funcionario.setEnderenco(new Endereco());
			} else if (this.funcionario.getEnderenco().getDistrito() != null) {
				System.out.println("Provincia:" + funcionario.getEnderenco().getDistrito().getProvincia());
				distrito.setProvincia(funcionario.getEnderenco().getDistrito().getProvincia());
				distritos = distritoServico.obterDistritosDaProvincia(distrito.getProvincia());
				distrito.setNome(funcionario.getEnderenco().getDistrito().getNome());
			}
			if (this.funcionario.getPais() == null) {
			} else {
				setPaises(paisServico.obterTodosPaises());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void editar(FuncionarioEscola funcionarioEscola) {
		try {

			this.funcionarioEscola = funcionarioEscola;
			adicionarFuncionarioEscolaBoolean = true;
			permissoes = permissaoServico.obterPermissoesPorEscola();
			escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();
			if (this.funcionarioEscola.getDataCadastro() == null) {
				this.funcionarioEscola.setDataCadastro(new Date());
			}
			if (!this.funcionarioEscola.isActivo()) {
				funcionarioEscola.setEstado(true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<Pais> getObterPaises() {
		try {
			if (funcionario.getNascionalidade() == null)
				paises = new ArrayList<>();
			else if (funcionario.getNascionalidade().equals("Estrangeira")) {
				paises = paisServico.obterTodosPaises();

			}
			return paises;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public void adicionarFuncionarioEscola() {
		try {
			permissoes = permissaoServico.obterPermissoesPorEscola();
			adicionarFuncionarioEscolaBoolean = true;
			escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();
			funcionarioEscola = new FuncionarioEscola();
			funcionarioEscola.setDataCadastro(new Date());
			funcionarioEscola.setEstado(true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void alocar() {
		try {
			funcionarioEscola.setEscola(escola);

			FuncionarioEscola funcionarioEscolaExistente = funcionarioEscolaServico.obterFuncionarioEscolaExistente(
					funcionarioEscola.getEscola().getId(), funcionarioSelecionado.getId(),
					funcionarioEscola.getPermissao().getId());
			if (funcionarioEscolaExistente != null && funcionarioEscolaExistente.getId() != funcionarioEscola.getId()) {
				Mensagem.mensagemErro("ERRO: o funcionário já foi adicionado com essa categoria!");
				return;
			}

			funcionarioEscola.setFuncionario(funcionarioSelecionado);

			Funcionario funcionario = funcionarioServico
					.obterFuncionarioPorIdPorPermissoes(funcionarioSelecionado.getId());

			funcionarioEscola.setHoraCadastro(new Date());
			FuncionarioEscola funcionarioEscola2 = funcionarioEscola;
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

				if (!funcionario.getPermissoes().contains(funcionarioEscola.getPermissao())) {
					funcionarioSelecionado.getPermissoes().add(funcionarioEscola.getPermissao());
				}
			}
			if (funcionarioEscola.isActivo() == false) {
				List<FuncionarioEscola> funcionarioEscolas = funcionarioEscolaServico
						.obterFuncionarioEscolaPorIdFuncionario(funcionarioEscola.getFuncionario().getId());
				if (funcionarioEscolas.size() == 1) {
					funcionarioSelecionado.getPermissoes().remove(funcionarioEscola.getPermissao());

				} else if (funcionarioEscolas.size() > 1) {
					int contador = 0;
					for (int i = 0; i < funcionarioEscolas.size(); i++) {

						if (funcionarioEscolas.get(i).getPermissao().equals(funcionarioEscola.getPermissao())) {
							contador++;

						}
					}
					if(contador==1){
						funcionarioSelecionado.getPermissoes().remove(funcionarioEscola.getPermissao());
					}
				}
			}
			
			funcionarioEscolas = funcionarioEscolaServico
					.obterFuncionarioEscolaPorIdEscolaPorIdFuncionario(escola.getId(), funcionario.getId());
			adicionarFuncionarioEscolaBoolean = false;
			funcionarioServico.salvar(funcionarioSelecionado);
			if (funcionarioEscola2.getId() == null)
				Mensagem.mensagemInfo("AVISO: a categoria do funcionário foi cadastrada com sucesso!");
			else
				Mensagem.mensagemInfo("AVISO: a categoria do funcionário foi atualizada com sucesso!");

		} catch (Exception e) {
			e.printStackTrace();
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

	public FuncionarioEscola getFuncionarioEscolaExclusao() {
		return funcionarioEscolaExclusao;
	}

	public void setFuncionarioEscolaExclusao(FuncionarioEscola funcionarioEscolaExclusao) {
		this.funcionarioEscolaExclusao = funcionarioEscolaExclusao;
	}

	public void excluirCategoria() {

		try {
			if(funcionarioEscolaExclusao.isEstado()==false){
				Mensagem.mensagemErro("ERRO: não tem competência de remover essa categoria!");
				return;
			}
			removerUsuarioPermissao(funcionarioEscolaExclusao);
			funcionarioEscolaServico.excluir(funcionarioEscolaExclusao);
			funcionarioEscolas = funcionarioEscolaServico
					.obterFuncionarioEscolaPorIdEscolaPorIdFuncionario(escola.getId(), funcionarioSelecionado.getId());
			Mensagem.mensagemInfo("AVISO: a categoria do funcionário foi removida com sucesso!");
		} catch (Exception e) {
			Mensagem.mensagemErro("ERRO: A categoria do funcionário não foi removida através da dependência!");
			e.printStackTrace();
		}

	}

	// falta verificar em quantas direcoes o funcionario esta alocado
	private void removerUsuarioPermissao(FuncionarioEscola funcionarioEscola) {
	
			List<FuncionarioEscola> funcionarioEscolas = funcionarioEscolaServico
					.obterFuncionarioEscolaPorIdFuncionario(funcionarioEscola.getFuncionario().getId());

			Funcionario usuarioSelecionado = funcionarioServico.obterFuncionarioPorIdPorPermissoes(funcionarioSelecionado.getId());
			if (funcionarioEscolas == null) {
			} else {
				if (funcionarioEscolas.size() == 1) {
					if (usuarioSelecionado.getPermissoes().size() == 1) {
						if (usuarioSelecionado.getPermissoes().contains(funcionarioEscola.getPermissao())) {
							usuarioSelecionado.getPermissoes().remove(funcionarioEscola.getPermissao());
							usuarioSelecionado.setAtivo(false);
							funcionarioServico.salvar(usuarioSelecionado);
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
		

	}

	private void verificarPermissao(FuncionarioEscola funcionarioEscola, Funcionario usuarioSelecionado) {
		if (usuarioSelecionado.getPermissoes().contains(funcionarioEscola.getPermissao())) {
			usuarioSelecionado.getPermissoes().remove(funcionarioEscola.getPermissao());
			funcionarioServico.salvar(usuarioSelecionado);
		}
	}

	public List<Distrito> getDistritos() {
		return distritos;
	}

	public void setDistritos(List<Distrito> distritos) {
		this.distritos = distritos;
	}

	public Distrito getDistrito() {
		return distrito;
	}

	public void setDistrito(Distrito distrito) {
		this.distrito = distrito;
	}

	public List<Provincia> getProvincias() {
		return provincias;
	}

	public void setProvincias(List<Provincia> provincias) {
		this.provincias = provincias;
	}

	public void funcionaroSelecionado(Funcionario funcionario) {
		adicionarFuncionarioEscolaBoolean = false;
		novoFuncionarioBoolean = false;
		editarFuncionarioBoolean = false;
		alocarFuncionarioBoolean = true;

		try {
			this.funcionarioSelecionado = funcionario;

			funcionarioEscolas = new ArrayList<>();
			escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();
			funcionarioEscolas = funcionarioEscolaServico
					.obterFuncionarioEscolaPorIdEscolaPorIdFuncionario(escola.getId(), funcionario.getId());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void voltar() {
		adicionarFuncionarioEscolaBoolean = false;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public List<FuncionarioEscola> getFuncionarioEscolas() {
		return funcionarioEscolas;
	}

	public void setFuncionarioEscolas(List<FuncionarioEscola> funcionarioEscolas) {
		this.funcionarioEscolas = funcionarioEscolas;
	}

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public boolean isEditarFuncionarioBoolean() {
		return editarFuncionarioBoolean;
	}

	public void setEditarFuncionarioBoolean(boolean editarFuncionarioBoolean) {
		this.editarFuncionarioBoolean = editarFuncionarioBoolean;
	}

	public boolean isNovoFuncionarioBoolean() {
		return novoFuncionarioBoolean;
	}

	public void setNovoFuncionarioBoolean(boolean novoFuncionarioBoolean) {
		this.novoFuncionarioBoolean = novoFuncionarioBoolean;
	}

	public int getQtdFuncionariosEncontrados() {
		return qtdFuncionariosEncontrados;
	}

	public void setQtdFuncionariosEncontrados(int qtdFuncionariosEncontrados) {
		this.qtdFuncionariosEncontrados = qtdFuncionariosEncontrados;
	}

	public boolean isProximaPrimeiraFaseBoolean() {
		return proximaPrimeiraFaseBoolean;
	}

	public void setProximaPrimeiraFaseBoolean(boolean proximaPrimeiraFaseBoolean) {
		this.proximaPrimeiraFaseBoolean = proximaPrimeiraFaseBoolean;
	}

	public boolean isProximaSegundaFaseBoolean() {
		return proximaSegundaFaseBoolean;
	}

	public void setProximaSegundaFaseBoolean(boolean proximaSegundaFaseBoolean) {
		this.proximaSegundaFaseBoolean = proximaSegundaFaseBoolean;
	}

	public boolean isProximaTerceiraFaseBoolean() {
		return proximaTerceiraFaseBoolean;
	}

	public void setProximaTerceiraFaseBoolean(boolean proximaTerceiraFaseBoolean) {
		this.proximaTerceiraFaseBoolean = proximaTerceiraFaseBoolean;
	}

	public FuncionarioEscola getFuncionarioEscolaSelecionado() {
		return funcionarioEscolaSelecionado;
	}

	public void setFuncionarioEscolaSelecionado(FuncionarioEscola funcionarioEscolaSelecionado) {
		this.funcionarioEscolaSelecionado = funcionarioEscolaSelecionado;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public Integer getFuncionarioEncontrado() {
		return funcionarioEncontrado;
	}

	public void setFuncionarioEncontrado(Integer funcionarioEncontrado) {
		this.funcionarioEncontrado = funcionarioEncontrado;
	}

	public Funcionario getFuncionarioSelecionado() {
		return funcionarioSelecionado;
	}

	public void setFuncionarioSelecionado(Funcionario funcionarioSelecionado) {
		this.funcionarioSelecionado = funcionarioSelecionado;
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

	public List<Pais> getPaises() {
		return paises;
	}

	public void setPaises(List<Pais> paises) {
		this.paises = paises;
	}

	public boolean isAlocarFuncionarioBoolean() {
		return alocarFuncionarioBoolean;
	}

	public void setAlocarFuncionarioBoolean(boolean alocarFuncionarioBoolean) {
		this.alocarFuncionarioBoolean = alocarFuncionarioBoolean;
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

}
