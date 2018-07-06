
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

import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.enumerado.Provincia;
import com.mz.sistema.gestao.escolar.enumerado.RoleName;
import com.mz.sistema.gestao.escolar.modelo.Calendario;
import com.mz.sistema.gestao.escolar.modelo.Classe;
import com.mz.sistema.gestao.escolar.modelo.Distrital;
import com.mz.sistema.gestao.escolar.modelo.Distrito;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.modelo.Turno;
import com.mz.sistema.gestao.escolar.servico.ClasseServico;
import com.mz.sistema.gestao.escolar.servico.EscolaServico;
import com.mz.sistema.gestao.escolar.servico.FuncionarioEscolaServico;
import com.mz.sistema.gestao.escolar.servico.FuncionarioServico;
import com.mz.sistema.gestao.escolar.servico.MatriculaServico;
import com.mz.sistema.gestao.escolar.servico.TransferenciaServico;
import com.mz.sistema.gestao.escolar.servico.TurnoServico;
import com.mz.sistema.gestao.escolar.util.DataUtils;
import com.mz.sistema.gestao.escolar.util.Mensagem;
import com.mz.sistema.gestao.escolar.util.TipoLetra;

@Controller
@Named
@SessionScope
public class EscolaBean {

	private static final String FORMATO_DATA_PADRAO = "dd/MM/yyyy";

	private Escola escola;
	private Escola escolaSelecionada = new Escola();
	private Escola professorEscola;
	private List<Escola> escolas = new ArrayList<>();
	private List<FuncionarioEscola> funcionarioEscolas;
	private List<Provincia> provincias = Arrays.asList(Provincia.values());
	private Funcionario professorSelecionado = new Funcionario();
	private Funcionario professorSelecionadoParaEscola;
	private Funcionario professorPesquisado;
	private List<Funcionario> professoresFiltratrados;
	private List<Funcionario> professoresIncluidos;
	private List<Funcionario> professoresIncluidoNaLista;
	private List<Classe> classes = new ArrayList<>();
	private List<Turno> turnos = new ArrayList<>();
	private Turno[] turnosSelecionados;
	private Classe[] classesSelecionadas;
	private int quantidadeCaracteres = 0;

	public Long totalAlunosDaEscola;
	public Long totalAlunosAlocadoEmTurmasDaEscola;
	public Long totalTurmaDaEscola;
	public Long totalProfessoresDaEscola;
	private Long totalFuncionarioDaEscola;

	private String formataDataCadastro;
	private String formataDataAletacao;
	private List<Turno> turnosDaEscola;
	private List<Classe> classesDaEscola;
	private Funcionario directorDaEscolaLogada;
	private PieChartModel pieModel;
	private String pesquisa = new String();

	private boolean cadastroEscolaBoolean = false;
	private boolean novaEscolaBoolean = false;
	private boolean editarEscolaBoolean = false;
	private boolean activarEscolaBoolean = false;
	private boolean vizualizarODirectorEscolaBoolean = false;
	private Escola escolaSelecionadaExclusao;
	private Integer qtdEscolasEncontradas = 0;

	@Autowired
	private EscolaServico escolaServico;

	@Autowired
	private ClasseServico classeServico;
	@Autowired
	private TurnoServico turnoServico;
	@Autowired
	private FuncionarioServico professorServico;
	@Autowired
	private AuthenticationContext authenticacaoContext;

	@Autowired
	private MatriculaServico matriculaServico;
	@Autowired
	private TransferenciaServico transferenciaServico;

	@Autowired
	private FuncionarioEscolaServico funcionarioEscolaServico;
	@Autowired
	private FuncionarioBean funcionarioBean;

	public void iniciarBean() {
		try {
			Integer ano = authenticacaoContext.getCalendarioEscolar().getAno();
			FuncionarioEscola funcionarioEscola = authenticacaoContext.getFuncionarioEscolaLogada();
			Escola escola = new Escola();
			if (funcionarioEscola == null) {

			} else {

				escola = funcionarioEscola.getEscola();
			}
			this.totalAlunosDaEscola = escolaServico.totalEstudantesDaEscola(escola.getId(), ano);
			this.totalTurmaDaEscola = escolaServico.totalTurmaDaEscola(escola.getId());
			this.totalAlunosAlocadoEmTurmasDaEscola = escolaServico
					.totalEstudantesAlocadoEmTurmasDaEscola(escola.getId());
			this.totalProfessoresDaEscola = escolaServico.totalProfessoresDaEscola(escola.getId());
			createPieModel(escola, ano);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// prettyCofing
	public void iniciarIdntificacaoDaEscola() {

		try {
			escolaSelecionada = authenticacaoContext.getFuncionarioEscolaLogada().getEscola();

			turnosDaEscola = escolaServico.obterTurnosPorIdEscola(escolaSelecionada.getId());
			classesDaEscola = escolaServico.obterClassesPorIdEscola(escolaSelecionada.getId());
			classes = classeServico.obterTodasClasses();
			turnos = turnoServico.listarTodosTurnos();

			escolaSelecionada.setClasses(new ArrayList<Classe>(classesDaEscola));
			escolaSelecionada.setTurnos(new ArrayList<Turno>(turnosDaEscola));
			FuncionarioEscola directoresDasEscolas = funcionarioEscolaServico
					.obterDirectorEscolaPorPermicao(RoleName.ROLE_DIRECTOR, escolaSelecionada.getId());
			this.directorDaEscolaLogada = directoresDasEscolas.getFuncionario();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createPieModel(Escola escola, Integer ano) {
		try {
			// String situacao ="Transferido";
			Long qtdAlunosMatriculados = matriculaServico.obterMatriculadoPorSexo(escola.getId(), ano, true);
			Long qtdAlunasMatriculadas = matriculaServico.obterMatriculadoPorSexo(escola.getId(), ano, false);
			Long qtdAlunosdosQueTransferiramNestaEscola = transferenciaServico
					.obterAlunosdosQueTransferiramNestaEscola(escola.getId(), ano, true);
			Long qtdAlunosdosQueForamTransferidosParaEstaEscola = transferenciaServico
					.obterAlunosdosQueForamTransferidosParaEstaEscola(escola.getId(), ano, true);

			System.out.println("Escola: " + escola.getId());
			System.out.println("Sida dos Trnsferidos: " + qtdAlunosdosQueTransferiramNestaEscola);
			if (qtdAlunosMatriculados == null) {
				qtdAlunosMatriculados = 0L;
			}
			if (qtdAlunosdosQueTransferiramNestaEscola == null) {
				qtdAlunosdosQueTransferiramNestaEscola = 0L;
			}
			if (qtdAlunosdosQueForamTransferidosParaEstaEscola == null) {
				qtdAlunosdosQueForamTransferidosParaEstaEscola = 0L;
			}
			if (qtdAlunasMatriculadas == null) {
				qtdAlunasMatriculadas = 0L;
			}
			pieModel = new PieChartModel();
			if (qtdAlunosMatriculados > 0)
				pieModel.set("ALUNOS", qtdAlunosMatriculados);
			if (qtdAlunasMatriculadas > 0)
				pieModel.set("ALUNAS", qtdAlunasMatriculadas);
			if (qtdAlunosdosQueTransferiramNestaEscola > 0)
				pieModel.set("TRANSFERIDOS", qtdAlunosdosQueTransferiramNestaEscola);
			if (qtdAlunosdosQueForamTransferidosParaEstaEscola > 0)
				pieModel.set("VINDOS TRANSFERIDOS", qtdAlunosdosQueForamTransferidosParaEstaEscola);

			// pieModel.set("Anulação matrícula", 325);

			pieModel.setTitle("Estatística de " + ano);
			pieModel.setLegendPosition("w");
			pieModel.setShowDataLabels(true);
			pieModel.setDiameter(200);
			if (qtdAlunasMatriculadas == 0l && qtdAlunosdosQueTransferiramNestaEscola == 0L
					&& qtdAlunosMatriculados == 0l && qtdAlunosdosQueForamTransferidosParaEstaEscola == 0L) {
				pieModel = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void salvar() {
		try {
			Funcionario funcionario = authenticacaoContext.getFuncionarioLogado();
			Distrital direcaoDitrital = authenticacaoContext.getFuncionarioDirecaoDistritalLogado();
			String descricao = TipoLetra.capitalizeString(escola.getDescricao()).replace(" De ", " de ")
					.replace(" Da ", " da ").replace("cao", "ção").replace("Secundaria", "Secundária")
					.replace("sao", "são").replace(" Sao ", " são ").replace(" Do ", " do ");

			escola.setDescricao(descricao);

			escola.setDescricao(escola.getDescricao().trim());
			escola.setDistrital(direcaoDitrital);
			Escola escolaExistente = escolaServico.obterEscolaExistente(direcaoDitrital.getId(),
					escola.getDescricao().trim(), escola.getLocalidade().trim());
			if (escolaExistente != null && escola.getId() != escolaExistente.getId()) {
				Mensagem.mensagemInfo("AVISO:  Já existe a esta escola cadastrada no sistema!");
				return;
			}

			if (novaEscolaBoolean == true) {
				Mensagem.mensagemInfo("AVISO: Escola cadastrada com sucesso!");
				escola.setFuncCadastro(funcionario);

			}
			if (editarEscolaBoolean == true) {
				Mensagem.mensagemInfo("AVISO: a escola atualizada com sucesso!");
				editarEscolaBoolean = false;
				cadastroEscolaBoolean = false;
				escola.setFuncAlteracao(funcionario);
			}

			escolaServico.salvar(escola);
			escola = new Escola();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String cadastroEscola() {
		cadastroEscolaBoolean = false;
		novaEscolaBoolean = false;
		editarEscolaBoolean = false;
		activarEscolaBoolean = false;
		escolas = new ArrayList<>();
		pesquisa = null;
		qtdEscolasEncontradas = 0;
		vizualizarODirectorEscolaBoolean = false;
		return "/academico/director-ditrital/escola/cadastro?faces-redirect=true";
	}

	public void salvarEscolaSelecionada() {

		escolaServico.salvar(escolaSelecionada);
		Mensagem.mensagemInfo("AVISO: informação da escola foi atualizada com sucesso!");

	}

	public void limparCampos() {
		escola = new Escola();
	}

	public void alocarProfessorEscola(Escola escola) {

		try {
			Funcionario professorDaEscolaExistente = professorServico
					.obterFuncionariosDaEscola(professorSelecionado.getId(), escola.getId());

			if (professorDaEscolaExistente != null
					&& professorDaEscolaExistente.getId() == professorSelecionado.getId()) {
				Mensagem.mensagemInfo("O funcionário Já foi alocado nesta escola!");
				return;
			}
			// escola.getUsuarios().add(professorSelecionado);
			escolaServico.salvar(escola);
			Mensagem.mensagemInfo("funcionário alocado na escola com sucesso!");
			escola = new Escola();
			professorSelecionadoParaEscola = null;
			professoresIncluidos = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Distrito> getDistritosDaProvincia() {
		List<Distrito> distritos = null;
		// if (escola.getEnderenco().getDistrito().getProvincia() != null) {
		// distritos =
		// distritoServico.obterDistritosDaProvincia(escola.getEnderenco().getDistrito().getProvincia());
		// }
		return distritos;
	}

	public void procurarProfessor() {
		professoresFiltratrados = null;
		this.professorPesquisado = new Funcionario();
	}

	public void selecionarProfessor(Funcionario professor) {
		this.professorPesquisado = professor;
	}

	public void professorSelecionadoDaLista(Funcionario professor) {
		this.professorSelecionado = professor;
		this.professorPesquisado = null;
		this.professoresIncluidos = null;
	}

	public void selecionarProfessorNaEscola(Funcionario professor) {
		this.professorSelecionadoParaEscola = professor;
	}

	public void voltarDaPesquisa() {
		this.professorPesquisado = null;
		this.professorSelecionadoParaEscola = null;
	}

	public void buscarNomeDoProfessor() {
		try {
			if (professorPesquisado != null) {
				professoresFiltratrados = professorServico.obterPorNome(professorPesquisado.getNome());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscar() {
		try {
			if (pesquisa == null || pesquisa == "") {
				escolas = new ArrayList<>();
			} else if (pesquisa != null && pesquisa != "") {
				escolas = escolaServico.obterEscolasPorPesquisa(pesquisa);
			}
			if (escolas == null) {
				this.qtdEscolasEncontradas = 0;
			} else {
				this.qtdEscolasEncontradas = escolas.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void incluirProfessor() {
		try {
			// professorSelecionado=new Professor();
			System.out.println("Id do professor: " + professorSelecionado.getId());
			System.out.println("Nome do professor: " + professorSelecionado.getNome());
			System.out.println("Formacao do professor: " + professorSelecionado.getFormacao());
			professoresIncluidos = professorServico.obterPorId(professorSelecionado.getId());
			// professoresFiltratrados.add(professorPesquisado);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String limpar() {
		escola = new Escola();
		cadastroEscolaBoolean = true;
		escolas = null;
		return "/academico/director-ditrital/escola/cadastro?faces-redirect=true";
	}

	public void novaEscola() {
		this.cadastroEscolaBoolean = true;
		this.novaEscolaBoolean = true;
		editarEscolaBoolean = false;
		activarEscolaBoolean = false;
		// pesquisa = null;
		escola = new Escola();
		escolas = null;
		quantidadeCaracteres = 0;

	}

	public void voltarParaPequisa() {
		escola = null;
		editarEscolaBoolean = false;
		activarEscolaBoolean = false;
		vizualizarODirectorEscolaBoolean = false;
		this.cadastroEscolaBoolean = false;
		if (novaEscolaBoolean == true) {
			novaEscolaBoolean = false;
		}
		buscar();

	}

	public void pequisaEscola() {

	}

	public void selecionar(Escola escola) {
		this.escola = escola;
		contarQuantidadeCarateres();
		this.cadastroEscolaBoolean = true;
		editarEscolaBoolean = true;
		activarEscolaBoolean = false;
		setFormataDataCadastro(DataUtils.obterDataFormatoBanco(escola.getDataCadastro(), FORMATO_DATA_PADRAO));
		setFormataDataAletacao(DataUtils.obterDataFormatoBanco(escola.getDataAlteracao(), FORMATO_DATA_PADRAO));
		this.escola.setDataAlteracao(new Date());
		vizualizarODirectorEscolaBoolean = false;

	}

	public void selecionarParaVisualizarODirectorDessaEscola(Escola escola) {
		this.escola = escola;
		this.cadastroEscolaBoolean = true;
		editarEscolaBoolean = true;
		activarEscolaBoolean = false;
		setFormataDataCadastro(DataUtils.obterDataFormatoBanco(escola.getDataCadastro(), FORMATO_DATA_PADRAO));
		setFormataDataAletacao(DataUtils.obterDataFormatoBanco(escola.getDataAlteracao(), FORMATO_DATA_PADRAO));
		vizualizarODirectorEscolaBoolean = true;
		totalAlunosDaEscola = 0L;
		totalFuncionarioDaEscola = 0l;
		funcionarioEscolas = new ArrayList<>();
		try {
			// List<FuncionarioEscola>
			// funcionarioEscolas=funcionarioEscolaServico.obterDirigentesDaEscolaPorPermicoes("ROLE_DIRECTOR",
			// "ROLE_DIRECTOR_ADJUNTO", escola.getId());

			List<FuncionarioEscola> funcionarioEscolas = funcionarioEscolaServico
					.obterFuncionariosPorIdEscola(escola.getId());
			if (!funcionarioEscolas.isEmpty()) {
				for (int i = 0; i < funcionarioEscolas.size(); i++) {
					if (funcionarioEscolas.get(i).getPermissao().getDescricao().equals("ROLE_DIRECTOR")
							|| funcionarioEscolas.get(i).getPermissao().getDescricao()
									.equals("ROLE_DIRECTOR_ADJUNTO")) {
						this.funcionarioEscolas.add(funcionarioEscolas.get(i));
					}
					totalFuncionarioDaEscola = (long) funcionarioEscolas.size();
				}
			}

			turnos = turnoServico.listarTodosTurnos();
			classes = classeServico.obterTodasClasses();

			turnosDaEscola = escolaServico.obterTurnosPorIdEscola(escola.getId());
			classesDaEscola = escolaServico.obterClassesPorIdEscola(escola.getId());
			classes = classeServico.obterTodasClasses();
			turnos = turnoServico.listarTodosTurnos();

			this.escola.setClasses(new ArrayList<Classe>(classesDaEscola));
			this.escola.setTurnos(new ArrayList<Turno>(turnosDaEscola));
			Calendario calendario = authenticacaoContext.getCalendarioEscolar();

			if (calendario != null) {
				totalAlunosDaEscola = matriculaServico.obterTotalAlunosMatriculasPorEscolaPorAno(escola.getId(),
						calendario.getAno());
				if (totalAlunosDaEscola == null) {
					totalAlunosDaEscola = 0l;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void prepararExcluirCategoria(FuncionarioEscola funcionarioEscola) {
		funcionarioBean.prepararExcluirCategoria(funcionarioEscola);

	}

	public void contarQuantidadeCarateres() {
		quantidadeCaracteres = 0;
		try {
			if (escola.getObservacao() != null) {
				quantidadeCaracteres = escola.getObservacao().length();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<Funcionario> getObterIdProfessor() {
		List<Funcionario> professor = null;
		if (professorSelecionado != null) {
			professor = professorServico.obterPorId(professorSelecionado.getId());
		}
		return professor;
	}

	public void prepararParExcluir(Escola escola) {
		funcionarioBean.funcionarioEscolaExclusao = null;
		this.escolaSelecionadaExclusao = escola;

	}

	public void excluir() {
		try {
			escolaServico.excluir(this.escolaSelecionadaExclusao);
			buscar();
			Mensagem.mensagemInfo("AVISO: escola excluida com sucesso!");
			System.out.println("Chamou a funaco!");

		} catch (Exception e) {
			Mensagem.mensagemErro("ERRO: Não é possivel excluir essa escola pois tem dependência!");
		}

	}

	// funcao que retorna listas dev escolas
	public List<Escola> getObterTodasEscolas() {
		List<Escola> escolas = null;
		if (professorSelecionadoParaEscola != null) {
			escolas = escolaServico.listarTodas();
		}
		return escolas;
	}

	public Escola getEscola() {
		return escola;
	}

	public void setEscola(Escola escola) {
		this.escola = escola;
	}

	public List<Escola> getEscolas() {
		return escolas;
	}

	public void setEscolas(List<Escola> escolas) {
		this.escolas = escolas;
	}

	public List<Provincia> getProvincias() {
		return provincias;
	}

	public void setProvincias(List<Provincia> provincias) {
		this.provincias = provincias;
	}

	public Funcionario getProfessorSelecionado() {
		return professorSelecionado;
	}

	public void setProfessorSelecionado(Funcionario professorSelecionado) {
		this.professorSelecionado = professorSelecionado;
	}

	public List<Funcionario> getProfessoresFiltratrados() {
		return professoresFiltratrados;
	}

	public void setProfessoresFiltratrados(List<Funcionario> professoresFiltratrados) {
		this.professoresFiltratrados = professoresFiltratrados;
	}

	public Funcionario getProfessorPesquisado() {
		return professorPesquisado;
	}

	public void setProfessorPesquisado(Funcionario professorPesquisado) {
		this.professorPesquisado = professorPesquisado;
	}

	public Escola getProfessorEscola() {
		return professorEscola;
	}

	public void setProfessorEscola(Escola professorEscola) {
		this.professorEscola = professorEscola;
	}

	public List<Funcionario> getProfessoresIncluidos() {
		return professoresIncluidos;
	}

	public void setProfessoresIncluidos(List<Funcionario> professoresIncluidos) {
		this.professoresIncluidos = professoresIncluidos;
	}

	public Funcionario getProfessorSelecionadoParaEscola() {
		return professorSelecionadoParaEscola;
	}

	public void setProfessorSelecionadoParaEscola(Funcionario professorSelecionadoParaEscola) {
		this.professorSelecionadoParaEscola = professorSelecionadoParaEscola;
	}

	public List<Funcionario> getProfessoresIncluidoNaLista() {
		return professoresIncluidoNaLista;
	}

	public void setProfessoresIncluidoNaLista(List<Funcionario> professoresIncluidoNaLista) {
		this.professoresIncluidoNaLista = professoresIncluidoNaLista;
	}

	public Long getTotalAlunosDaEscola() {
		return totalAlunosDaEscola;
	}

	public void setTotalAlunosDaEscola(Long totalAlunosDaEscola) {
		this.totalAlunosDaEscola = totalAlunosDaEscola;
	}

	public Long getTotalAlunosAlocadoEmTurmasDaEscola() {
		return totalAlunosAlocadoEmTurmasDaEscola;
	}

	public void setTotalAlunosAlocadoEmTurmasDaEscola(Long totalAlunosAlocadoEmTurmasDaEscola) {
		this.totalAlunosAlocadoEmTurmasDaEscola = totalAlunosAlocadoEmTurmasDaEscola;
	}

	public Long getTotalProfessoresDaEscola() {
		return totalProfessoresDaEscola;
	}

	public void setTotalProfessoresDaEscola(Long totalProfessoresDaEscola) {
		this.totalProfessoresDaEscola = totalProfessoresDaEscola;
	}

	public Long getTotalTurmaDaEscola() {
		return totalTurmaDaEscola;
	}

	public void setTotalTurmaDaEscola(Long totalTurmaDaEscola) {
		this.totalTurmaDaEscola = totalTurmaDaEscola;
	}

	public boolean isCadastroEscolaBoolean() {
		return cadastroEscolaBoolean;
	}

	public void setCadastroEscolaBoolean(boolean cadastroEscolaBoolean) {
		this.cadastroEscolaBoolean = cadastroEscolaBoolean;
	}

	public boolean isNovaEscolaBoolean() {
		return novaEscolaBoolean;
	}

	public void setNovaEscolaBoolean(boolean novaEscolaBoolean) {
		this.novaEscolaBoolean = novaEscolaBoolean;
	}

	public boolean isEditarEscolaBoolean() {
		return editarEscolaBoolean;
	}

	public void setEditarEscolaBoolean(boolean editarEscolaBoolean) {
		this.editarEscolaBoolean = editarEscolaBoolean;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public Escola getEscolaSelecionadaExclusao() {
		return escolaSelecionadaExclusao;
	}

	public void setEscolaSelecionadaExclusao(Escola escolaSelecionadaExclusao) {
		this.escolaSelecionadaExclusao = escolaSelecionadaExclusao;
	}

	public boolean isActivarEscolaBoolean() {
		return activarEscolaBoolean;
	}

	public void setActivarEscolaBoolean(boolean activarEscolaBoolean) {
		this.activarEscolaBoolean = activarEscolaBoolean;
	}

	public String getFormataDataCadastro() {
		return formataDataCadastro;
	}

	public void setFormataDataCadastro(String formataDataCadastro) {
		this.formataDataCadastro = formataDataCadastro;
	}

	public String getFormataDataAletacao() {
		return formataDataAletacao;
	}

	public void setFormataDataAletacao(String formataDataAletacao) {
		this.formataDataAletacao = formataDataAletacao;
	}

	public Funcionario getDirectorDaEscolaLogada() {
		return directorDaEscolaLogada;
	}

	public void setDirectorDaEscolaLogada(Funcionario directorDaEscolaLogada) {
		this.directorDaEscolaLogada = directorDaEscolaLogada;
	}

	public Turno[] getTurnosSelecionados() {
		return turnosSelecionados;
	}

	public void setTurnosSelecionados(Turno[] turnosSelecionados) {
		this.turnosSelecionados = turnosSelecionados;
	}

	public Classe[] getClassesSelecionadas() {
		return classesSelecionadas;
	}

	public void setClassesSelecionadas(Classe[] classesSelecionadas) {
		this.classesSelecionadas = classesSelecionadas;
	}

	public List<Classe> getClasses() {
		return classes;
	}

	public void setClasses(List<Classe> classes) {
		this.classes = classes;
	}

	public List<Turno> getTurnos() {
		return turnos;
	}

	public void setTurnos(List<Turno> turnos) {
		this.turnos = turnos;
	}

	public Escola getEscolaSelecionada() {
		return escolaSelecionada;
	}

	public void setEscolaSelecionada(Escola escolaSelecionada) {
		this.escolaSelecionada = escolaSelecionada;
	}

	public List<Turno> getTurnosDaEscola() {
		return turnosDaEscola;
	}

	public void setTurnosDaEscola(List<Turno> turnosDaEscola) {
		this.turnosDaEscola = turnosDaEscola;
	}

	public TurnoServico getTurnoServico() {
		return turnoServico;
	}

	public void setTurnoServico(TurnoServico turnoServico) {
		this.turnoServico = turnoServico;
	}

	public PieChartModel getPieModel() {
		return pieModel;
	}

	public void setPieModel(PieChartModel pieModel) {
		this.pieModel = pieModel;
	}

	public Integer getQtdEscolasEncontradas() {
		return qtdEscolasEncontradas;
	}

	public void setQtdEscolasEncontradas(Integer qtdEscolasEncontradas) {
		this.qtdEscolasEncontradas = qtdEscolasEncontradas;
	}

	public boolean isVizualizarODirectorEscolaBoolean() {
		return vizualizarODirectorEscolaBoolean;
	}

	public void setVizualizarODirectorEscolaBoolean(boolean vizualizarODirectorEscolaBoolean) {
		this.vizualizarODirectorEscolaBoolean = vizualizarODirectorEscolaBoolean;
	}

	public List<FuncionarioEscola> getFuncionarioEscolas() {
		return funcionarioEscolas;
	}

	public void setFuncionarioEscolas(List<FuncionarioEscola> funcionarioEscolas) {
		this.funcionarioEscolas = funcionarioEscolas;
	}

	public Long getTotalFuncionarioDaEscola() {
		return totalFuncionarioDaEscola;
	}

	public void setTotalFuncionarioDaEscola(Long totalFuncionarioDaEscola) {
		this.totalFuncionarioDaEscola = totalFuncionarioDaEscola;
	}

	public int getQuantidadeCaracteres() {
		return quantidadeCaracteres;
	}

	public void setQuantidadeCaracteres(int quantidadeCaracteres) {
		this.quantidadeCaracteres = quantidadeCaracteres;
	}

}
