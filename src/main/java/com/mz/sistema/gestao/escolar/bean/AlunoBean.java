
package com.mz.sistema.gestao.escolar.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import com.mz.sistema.gestao.escolar.chave.composta.NotaId;
import com.mz.sistema.gestao.escolar.enumerado.EstadoCivil;
import com.mz.sistema.gestao.escolar.enumerado.EstadoTransferencia;
import com.mz.sistema.gestao.escolar.enumerado.Provincia;
import com.mz.sistema.gestao.escolar.enumerado.TipoCurso;
import com.mz.sistema.gestao.escolar.enumerado.Trimestres;
import com.mz.sistema.gestao.escolar.modelo.Aluno;
import com.mz.sistema.gestao.escolar.modelo.Calendario;
import com.mz.sistema.gestao.escolar.modelo.Classe;
import com.mz.sistema.gestao.escolar.modelo.DisciplinaClasse;
import com.mz.sistema.gestao.escolar.modelo.Distrito;
import com.mz.sistema.gestao.escolar.modelo.Endereco;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.modelo.Matricula;
import com.mz.sistema.gestao.escolar.modelo.Matriz;
import com.mz.sistema.gestao.escolar.modelo.Nota;
import com.mz.sistema.gestao.escolar.modelo.Pais;
import com.mz.sistema.gestao.escolar.modelo.Permissao;
import com.mz.sistema.gestao.escolar.modelo.Transferencia;
import com.mz.sistema.gestao.escolar.modelo.Trimestre;
import com.mz.sistema.gestao.escolar.modelo.Turma;
import com.mz.sistema.gestao.escolar.modelo.Usuario;
import com.mz.sistema.gestao.escolar.servico.AlunoServico;
import com.mz.sistema.gestao.escolar.servico.ClasseServico;
import com.mz.sistema.gestao.escolar.servico.DisciplinaClasseServico;
import com.mz.sistema.gestao.escolar.servico.DistritoServico;
import com.mz.sistema.gestao.escolar.servico.EscolaServico;
import com.mz.sistema.gestao.escolar.servico.GeradorDeRelatoriosServico;
import com.mz.sistema.gestao.escolar.servico.MatriculaServico;
import com.mz.sistema.gestao.escolar.servico.MatrizServico;
import com.mz.sistema.gestao.escolar.servico.NotaServico;
import com.mz.sistema.gestao.escolar.servico.PaisServico;
import com.mz.sistema.gestao.escolar.servico.PermissaoServico;
import com.mz.sistema.gestao.escolar.servico.RecuperarSenhaServico;
import com.mz.sistema.gestao.escolar.servico.TransferenciaServico;
import com.mz.sistema.gestao.escolar.servico.TrimestreServico;
import com.mz.sistema.gestao.escolar.servico.TurmaServico;
import com.mz.sistema.gestao.escolar.servico.UsuarioServico;
import com.mz.sistema.gestao.escolar.util.DataUtils;
import com.mz.sistema.gestao.escolar.util.GeradorCodigo;
import com.mz.sistema.gestao.escolar.util.Mensagem;
import com.mz.sistema.gestao.escolar.util.StringUtil;
import com.mz.sistema.gestao.escolar.util.TipoLetra;
import com.mz.sistema.gestao.escolar.util.ValorExtenso;

@Named
@Controller
@SessionScope
public class AlunoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2003824937712714535L;
	private Aluno aluno = new Aluno();
	private Aluno alunoSelecionado;
	private Aluno alunoSelecionadoExclusao;
	private Nota nota;
	private Nota notaSelecionda;

	private Transferencia transferencia = new Transferencia();
	private List<Aluno> alunos = new ArrayList<>();
	private List<Matricula> matriculas = new ArrayList<>();
	private List<Matricula> matriculaSSelecionadas = new ArrayList<>();

	private List<Nota> notas;
	private List<Matriz> matrizes;
	private Integer mediaAlunoTrimetral;
	private String mediaAlunoTrimetralPorExtenso;
	private List<Provincia> provincias = Arrays.asList(Provincia.values());
	private Escola escola = new Escola();
	private Escola escolaSelecionada = new Escola();
	private Trimestre trimestreAtivo;
	private Matricula matricula;
	private Matricula matriculaSelecionadaExclusao;
	private Matricula matriculaSelecionada;
	private Matricula matriculaClasseDestinho;
	private Matricula matriculaRenovacao;
	private String pesquisa;
	private String tipoArea;
	private Integer listarMatricula;
	private Integer idadeInicial;
	private Integer idadeFinal;

	private boolean cadastroAlunoBoolean = false;
	private boolean matriculaAlunoBoolean = false;
	private boolean novoAlunoBoolean = false;
	private boolean proximaPrimeiraFaseBoolean = false;
	private boolean proximaSegundaFaseBoolean = false;
	private boolean proximaTerceiraFaseBoolean = false;
	private boolean escolherSegundoCriterioBoolean = false;
	private boolean escolherTerceiroCriterioBoolean = false;
	private boolean renovarMatriculaBoolean = false;
	private boolean renovarMatriculaSelecionadaBoolean = false;
	private boolean novoAlunoSelecionadoBoolean = false;
	private boolean confirmarsalvarMatricula = false;
	private boolean matriculaSelecionadaParaAlocacaoTurmaBoolean = false;
	private boolean matriculaAvancarBoolean = false;
	private boolean editarMatricula = false;
	private boolean marcarTodosBoolean = false;
	private boolean alocadoComSucessoBoolean = false;
	private boolean proximoAlocarAlunoBoolean = false;

	private Integer qtdAlunosEncontrados;
	private Double valorMatricula;

	private List<Pais> paises = new ArrayList<>();
	private List<Classe> classes = new ArrayList<>();
	private List<EstadoCivil> estadoCivils;
	private List<Trimestres> tipoTrimestres = Arrays.asList(Trimestres.values());
	private List<TipoCurso> cursos = Arrays.asList(TipoCurso.values());
	private List<EstadoTransferencia> estadoTransferencias = Arrays.asList(EstadoTransferencia.values());

	public List<EstadoTransferencia> getEstadoTransferencias() {
		return estadoTransferencias;
	}

	public void setEstadoTransferencias(List<EstadoTransferencia> estadoTransferencias) {
		this.estadoTransferencias = estadoTransferencias;
	}

	private Distrito distrito = new Distrito();
	private List<Distrito> distritos = new ArrayList<>();
	private List<Turma> turmas = new ArrayList<>();
	private Matriz matriz = new Matriz();
	private List<DisciplinaClasse> disciplinaSelecionadas = new ArrayList<>();
	// private List<DisciplinaClasse> disciplinasClasse = new ArrayList<>();

	private static final String FORMATA_SENHA_PADRAO = "ddMMyyyy";

	@Autowired
	private AlunoServico alunoServico;
	@Autowired
	private AuthenticationContext authenticationContext;

	@Autowired
	private UsuarioServico usuarioServico;
	@Autowired
	private EscolaServico escolaServico;
	@Autowired
	private NotaServico notaServico;
	@Autowired
	private DistritoServico distritoServico;
	@Autowired
	private MatriculaServico matriculaServico;
	@Autowired
	private MatrizServico matrizServico;
	@Autowired
	private TransferenciaServico transferenciaServico;

	@Autowired
	private TrimestreServico trimestreServico;
	@Autowired
	private PermissaoServico permissaoServico;
	@Autowired
	private TurmaServico turmaServico;
	@Autowired
	private PaisServico paisServico;

	@Autowired
	private RecuperarSenhaServico recoperarSenhaServico;
	@Autowired
	private DisciplinaClasseServico disciplinaClasseServico;

	@Autowired
	private ClasseServico classeServico;

	@Autowired
	private GeradorDeRelatoriosServico geradorDeRelatoriosServico;

	private Integer qtdAlunoMatriculado = 0;
	private Classe classe;
	private int existemMatriculas;

	private static final String FORMATA_DATA_NESCIMENTO_PARA_SENHA_PADRAO = "ddMMyyyy";

	public void iniciarBean() {
		aluno = null;
		alunos = new ArrayList<>();
		this.notas = null;
		cadastroAlunoBoolean = false;
		novoAlunoBoolean = false;
		qtdAlunosEncontrados = 0;
		proximaPrimeiraFaseBoolean = true;
		proximaSegundaFaseBoolean = false;
		proximaTerceiraFaseBoolean = false;
		matriculaAlunoBoolean = false;
		proximoAlocarAlunoBoolean = false;
		trimestreAtivo = new Trimestre();
		try {
			if (valorMatricula != null)
				matricula.setValor(valorMatricula);
			System.out.println("Valor da matrcula: " + valorMatricula);
			trimestreAtivo = trimestreServico.obterTrimestreAtivo();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void iniciarQuadroMatriculas() {
		transferencia = new Transferencia();
		editarMatricula = false;
		renovarMatriculaBoolean = false;
		escolherSegundoCriterioBoolean = false;
		escolherTerceiroCriterioBoolean = false;
		renovarMatriculaSelecionadaBoolean = false;
		matriculaAlunoBoolean = false;
		confirmarsalvarMatricula = false;
		matriculas = new ArrayList<>();
		qtdAlunoMatriculado = 0;
		pesquisa = new String();
		alunos = new ArrayList<>();
		proximoAlocarAlunoBoolean = false;
		matricula = new Matricula();
		notaSelecionda = new Nota();
		try {

			if (valorMatricula != null)
				matricula.setValor(valorMatricula);
			System.out.println("Valor da matrcula: " + valorMatricula);
			aluno = new Aluno();
			Calendario calendario = authenticationContext.getCalendarioEscolar();
			if (calendario == null) {

			} else {
				matricula.setAno(calendario.getAno());
			}
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();
			matricula.setEscola(escola);
		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	public void iniciarEntumacao() {
		renovarMatriculaBoolean = false;
		escolherSegundoCriterioBoolean = true;
		escolherTerceiroCriterioBoolean = true;
		this.turmas = new ArrayList<>();
	}

	public void buscarAluno() {
		alunos = new ArrayList<>();
		try {
			if (pesquisa.trim().equals(""))
				alunos = new ArrayList<>();
			else
				alunos = alunoServico.obterAlunosPorPesquisa(pesquisa);
			if (alunos != null) {
				qtdAlunosEncontrados = alunos.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void salvar() {

		try {
			Aluno alunoExistente = alunoServico.obterAlunoExistente(aluno.getNome().toUpperCase(),
					aluno.getDataNascimento(), aluno.isSexo(), aluno.getNascionalidade(), aluno.getPai().toUpperCase(),
					aluno.getMae().toUpperCase());

			if (alunoExistente != null && alunoExistente.getId() != aluno.getId()) {
				Mensagem.mensagemInfo("AVISO: Já existe um Aluno cadastrado no sistema com esses dados!");
				return;
			}
			if (aluno.getDataNascimento() != null) {
			} else if (aluno.getDataNascimento() != null) {
				String senha = DataUtils.obterDataFormatoBanco(aluno.getDataNascimento(),
						FORMATA_DATA_NESCIMENTO_PARA_SENHA_PADRAO);
				System.out.println("Senha.............:  " + senha);
				String senhaCriptografada = usuarioServico.criptografarSenha(senha);
				System.out.println("Senha Criptgrafada.: '" + senhaCriptografada + "'");
				aluno.setSenha(senhaCriptografada);

			}

			aluno.setPai(aluno.getPai().toUpperCase());
			aluno.setMae(aluno.getMae().toUpperCase());

			aluno.setNome(aluno.getNome().toUpperCase());
			if (aluno.getEmail() != null) {
				aluno.setEmail(aluno.getEmail().toLowerCase());
			}
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();
			aluno.setEscola(escola);
			if (aluno.getId() == null) {
				Funcionario funcionario = (Funcionario) authenticationContext.getUsuarioLogado();
				aluno.setFuncionario(funcionario);
			}

			alunoServico.salvar(aluno);
			if (aluno.getId() == null) {
				Mensagem.mensagemInfo("AVISO: Aluno cadastrado com sucesso!");
			} else {
				Mensagem.mensagemInfo("AVISO: Os dados de aluno foram atualizados com sucesso!");
			}
			pesquisa = aluno.getLogin();
			buscar();
			voltarParaPequisa();
		} catch (Exception e) {

			Mensagem.mensagemErro("ERRO: Ocoreu erro inexperado ao atualizar dados de aluno.");

			e.printStackTrace();

		}
	}

	public void salvarTransferencia() {

		try {

			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();
			Transferencia transferenciaExistente = transferenciaServico.obterTransferenciasExistente(escola.getId(),
					escolaSelecionada.getId(), matriculaSelecionada.getId());
			if (transferenciaExistente != null && transferenciaExistente.getId() != transferencia.getId()) {
				Mensagem.mensagemErro("ERRO: Já existe uma solicitação de transferência deste aluno para esta escola!");
				return;
			}

			transferencia.setEscolaDestino(escolaSelecionada);
			transferencia.setEscolaOrigem(escola);
			matriculaSelecionada.setSituacao(EstadoTransferencia.EM_ANALIZE.getLabel());
			transferencia.setSituacao(EstadoTransferencia.EM_ANALIZE.getLabel());
			if (matriculaSelecionada.getTurma() != null) {
				if (matriculaSelecionada.getTurma().getId() != 0) {
					Integer inscritos = matriculaSelecionada.getTurma().getInscrito() - 1;
					matriculaSelecionada.getTurma().setInscrito(inscritos);
					turmaServico.salvar(matriculaSelecionada.getTurma());
					transferencia.setTurmaOrigem(matriculaSelecionada.getTurma());
				}
			}
			matriculaSelecionada.setEscola(escola);
			transferencia.setClasse(matriculaSelecionada.getClasse());

			matriculaServico.salvar(matriculaSelecionada);
			transferencia.setMatricula(matriculaSelecionada);

			transferenciaServico.salvar(transferencia);
			Mensagem.mensagemInfo("AVISO: a solicitação de transferência foi efectuado com sucesso!");
		} catch (Exception e) {

			Mensagem.mensagemErro("ERRO: Ocoreu erro inexperado ao trnsfaerir aluno.");

			e.printStackTrace();

		}
	}

	public void salvarMatricula() {

		try {
			Matricula matriculaExistente = matriculaServico.obterMatriculaExistente(matricula.getAno(),
					alunoSelecionado.getId());

			if (matriculaExistente != null && matriculaExistente.getId() != matricula.getId()) {
				if (matriculaExistente.getClasse() != null) {
					classe = matriculaExistente.getClasse();
				}
				matricula = matriculaExistente;
				alocadoComSucessoBoolean = true;
				Mensagem.mensagemAlerta("ATENÇÃO: O aluno já foi matriculado neste ano!");
				return;
			}

			String numeroReciboMatricula = null, numeroMatricula = null;
			if (matricula.getId() == null) {
				Long numeroReciboUltimaMatricula = matriculaServico.obterNumeroReciboUltimaMatricula();
				if (numeroReciboUltimaMatricula == null) {
					numeroReciboUltimaMatricula = 0L;
				}
				numeroReciboUltimaMatricula++;
				numeroReciboMatricula = numeroReciboUltimaMatricula.toString();

				Long numeroUltimaMatricula = matriculaServico.obterNumeroUltimaMatricula(matricula.getAno());
				if (numeroUltimaMatricula == null) {
					numeroUltimaMatricula = 0L;
				}

				String numeroDeMatricula = String.valueOf(numeroUltimaMatricula);
				numeroUltimaMatricula = 0L;
				numeroDeMatricula = numeroDeMatricula.replace(matricula.getAno().toString(), "");
				numeroUltimaMatricula = Long.valueOf(numeroDeMatricula);
				numeroUltimaMatricula++;
				numeroMatricula = numeroUltimaMatricula.toString();
			} else {
				numeroMatricula = matricula.getNumero();
				numeroReciboMatricula = matricula.getNumeroRecibo();
			}

			numeroReciboMatricula = StringUtil.preencherZerosAEsquerda(numeroReciboMatricula, 20);
			matricula.setNumeroRecibo(numeroReciboMatricula);

			numeroMatricula = StringUtil.preencherZerosAEsquerda(numeroMatricula + "" + matricula.getAno().toString(),
					9);
			matricula.setNumero(numeroMatricula);
			if (novoAlunoSelecionadoBoolean == true) {

				matricula.setAcompanhamento(1);
			} else {

				matricula.setAcompanhamento(2);
			}

			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();
			matricula.setEscola(escola);
			matricula.setEscolaOrigem(escola);
			matricula.setAluno(alunoSelecionado);
			matricula.getAluno().setAtivo(true);
			matricula.getAluno().setAtivo(true);
			matricula.setAtivo(true);
			matricula.setDataMatricula(new Date());

			///

			if (matriz.getDisciplinaClasses().isEmpty()) {
				Mensagem.mensagemErro(
						"ERRO: Não pode matricular aluno nesta classe pois não existe componente curricular!");
				return;
			}
			Nota nota = new Nota();
			NotaId notaId = null;
			if (classe.getCiclo().equals("2º CICLO") || classe.getDescricao().equals("10ª CLASSE")) {

				Integer count = 0;
				for (DisciplinaClasse disciplinaClasse : matriz.getDisciplinaClasses()) {
					if (!disciplinaClasse.isDisciplinaSeleconadaBoolean()) {
						count++;
					}
				}
				if (count == matriz.getDisciplinaClasses().size()) {
					Mensagem.mensagemErro("ERRO: selecione disciplinas para matricular este aluno!");
					return;
				}
				matricula = matriculaServico.salvarRetornarMatricula(matricula);
				for (DisciplinaClasse disciplinaClasse : matriz.getDisciplinaClasses()) {

					if (notaId == null) {
						notaId = new NotaId();
					}
					if (disciplinaClasse.isDisciplinaSeleconadaBoolean()) {
						if (!disciplinaClasse.getDisciplina().getDescricao().toUpperCase().equals("REUNIÃO DE TURMA")) {
							nota = notaServico.obterNotasPorIdMatriculaPorDisciplina(matricula.getId(),
									disciplinaClasse.getId());
							if (nota == null) {
								nota = new Nota();

							}
							notaId.setId_disciplina_classe(disciplinaClasse.getId());
							notaId.setId_matricula(matricula.getId());
							nota.setId(notaId);
							notaServico.salvar(nota);

						}

					}
				}

			} else if (classe.getCiclo().equals("1º CICLO") || !classe.getDescricao().equals("10ª CLASSE")) {
				matricula = matriculaServico.salvarRetornarMatricula(matricula);
				for (DisciplinaClasse disciplinaClasse : matriz.getDisciplinaClasses()) {
					if (notaId == null) {
						notaId = new NotaId();
					}
					if (!disciplinaClasse.getDisciplina().getDescricao().toUpperCase().equals("REUNIÃO DE TURMA")) {
						nota = notaServico.obterNotasPorIdMatriculaPorDisciplina(matricula.getId(),
								disciplinaClasse.getId());
						if (nota == null) {
							nota = new Nota();

						}
						notaId.setId_disciplina_classe(disciplinaClasse.getId());
						notaId.setId_matricula(matricula.getId());
						nota.setId(notaId);
						notaServico.salvar(nota);
					}

				}
			}

			confirmarsalvarMatricula = true;

			Mensagem.mensagemInfo("AVISO: Aluno matriculado com sucesso!");
			// matricula =
			// matriculaServico.obterMatriculaPorId(matricula.getId());
			alocadoComSucessoBoolean = true;

		} catch (

		Exception e) {
			Mensagem.mensagemErro("ERRO: Ocorreu erro inexperado ao matricular este aluno!");
			e.printStackTrace();

		}
	}

	public void adicionarDisciplina() {
		try {
			if (nota.getDisciplinaClasse() == null) {
				Mensagem.mensagemErro("O campo disciplina é obrigatório");
				return;
			}
			NotaId notaId = new NotaId();

			notaId.setId_disciplina_classe(nota.getDisciplinaClasse().getId());
			notaId.setId_matricula(matriculaSelecionada.getId());

			nota = notaServico.obterNotasPorIdMatriculaPorDisciplina(matriculaSelecionada.getId(),
					nota.getDisciplinaClasse().getId());

			if (nota == null) {
				nota = new Nota();
			}
			nota.setId(notaId);
			notaServico.salvar(nota);
			buscarNotas();
			Mensagem.mensagemInfo("AVISO: A disciplina adicionada om sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void atualizarMatricula() {
		try {
			NotaId notaId = new NotaId();
			matricula = matriculaServico.salvarRetornarMatricula(matricula);
			for (Nota nota : notas) {
				if (notaId == null) {
					notaId = new NotaId();
				}

				Nota nota2 = notaServico.obterNotasPorIdMatriculaPorDisciplina(nota.getMatricula().getId(),
						nota.getDisciplinaClasse().getId());
				if (nota2 == null) {
					nota2 = new Nota();

				}
				notaId.setId_disciplina_classe(nota.getDisciplinaClasse().getId());
				notaId.setId_matricula(nota.getMatricula().getId());
				nota2.setId(notaId);
				notaServico.salvar(nota);

			}

			Mensagem.mensagemInfo("AVISO: A matrícula  de aluno foi atualizado com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void eliminarDisiplina(Nota nota) {
		try {
			boolean notasCadastrada = false;
			Nota notaExistente = notaServico.obterNotasPorIdMatriculaPorDisciplina(nota.getMatricula().getId(),
					nota.getDisciplinaClasse().getId());
			if (notaExistente == null) {
				notas.remove(nota);
			} else {
				notasCadastrada = verificarNotasDeAluno(notasCadastrada, nota);
				if (notasCadastrada) {
					Mensagem.mensagemErro(
							"ERRO: Não é possível excluir uma disciplina que já tem nota cadastrada no sistema!");
					return;
				}
				notaServico.excluir(nota);
				Mensagem.mensagemInfo("AVISO: O vínculo entre o aluno e esta disciplina foi excluido com sucesso!");
				buscarNotas();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void buscarNotas() {
		notas = new ArrayList<>();
		List<Nota> notas = notaServico.obterNotasPorIdMatricula(this.matriculaSelecionada.getId());
		if (notas != null) {
			int count = 0;
			for (Nota nota : notas) {
				count++;
				nota.setOrdem(count);
				this.notas.add(nota);
			}
		}

	}

	public void perepararParaEliminarDisiplina(Nota nota) {
		this.notaSelecionda = nota;

	}

	public void salvarAlocacaoTurma() {

		try {

			Integer quantidadeMatriculado = 0;
			if (matriculas != null) {
				for (Matricula matricula : matriculas) {
					if (matricula.isMatriculaSelacionada() == true) {
						quantidadeMatriculado++;
						matricula.setTurma(this.matricula.getTurma());
						matricula.setDataEnturmacao(this.matricula.getDataEnturmacao());
						matricula.setTemTurma(true);
						matriculaServico.salvar(matricula);
					}
				}
			}
			if (quantidadeMatriculado == 0) {
				Mensagem.mensagemAlerta("ATENÇÃO: selecione pelo meno um aluno para alocar na turma de destino!");
				return;
			}
			if (turmas == null) {
				Mensagem.mensagemAlerta(
						"ATENÇÃO: Não foi alocado nenhum aluno pois não esxite nenhuma turma da classe!");
				return;
			}
			if (matricula.getTurma() == null) {
				Mensagem.mensagemErro("O campos turma é obrigatório");
				return;
			}
			Turma turma = turmaServico.obterTurmaPorId(matricula.getTurma().getId());
			Integer quantidadeAlunosMatriculado = turma.getInscrito() + quantidadeMatriculado;
			if (turma.getRestanteVaga() == null) {
				turma.setRestanteVaga(0);
			}
			Integer restanteVagas = turma.getRestanteVaga() - quantidadeMatriculado;
			turma.setRestanteVaga(restanteVagas);
			turma.setInscrito(quantidadeAlunosMatriculado);
			Turma turmaPk = turmaServico.salvarRetornarTurma(turma);
			this.matricula.setTurma(turmaPk);

			Mensagem.mensagemInfo("AVISO: Fora(m) alocado(s) " + quantidadeMatriculado + " aluno(s) na turma "
					+ turmaPk.getClasse().getOrdem() + "ª" + turmaPk.getDescricao() + "!");
			List<Matricula> matriculas = matriculaServico.obterMatriculaPorTurmaAtivas(turmaPk.getId());
			if (matriculas != null) {
				Integer numeroAlunoNaTurma = 0;
				for (Matricula matricula : matriculas) {
					numeroAlunoNaTurma++;
					matricula.setNumeroAlunoTurma(numeroAlunoNaTurma);
					matriculaServico.salvar(matricula);

				}
			}

		} catch (Exception e) {
			Mensagem.mensagemErro("ERRO: Ocoreu erro inexperado ao matricular aluno.");
			e.printStackTrace();

		}
	}

	public void prepararExclusao(Aluno aluno) {
		this.alunoSelecionadoExclusao = aluno;
	}

	public void prepararExcluirMatricula(Matricula matricula) {
		this.matriculaSelecionadaExclusao = matricula;
		String nomeAluno = TipoLetra.capitalizeString(this.matriculaSelecionadaExclusao.getAluno().getNome());
		this.matriculaSelecionadaExclusao.getAluno().setNome(nomeAluno);
	}

	public void excluir() {
		try {
			Aluno alunoExclusao = alunoServico.obterAlunoPorId(this.alunoSelecionadoExclusao.getId());
			if (alunoExclusao == null) {

			} else {
				if (alunoExclusao.getRecuperarSenha() == null) {
				} else {
					if (alunoExclusao.getRecuperarSenha().getId() != null)
						recoperarSenhaServico.excluir(alunoExclusao.getRecuperarSenha());
				}
				alunoServico.excluir(alunoExclusao);
				// usuarioServico.e
				buscar();
				Mensagem.mensagemInfo("AVISO: aluno foi excluido com sucesso!");
			}

		} catch (Exception e) {
			Mensagem.mensagemErro(
					"ERRO: Não foi possível excluir esse registo pois existe dependência a ele em outras tabelas!");
		}
	}

	public void excluirMatricula() {
		try {
			if (matriculaSelecionadaExclusao.getTurma() == null) {
				if (matriculaSelecionadaExclusao.getTurma() != null) {
					Mensagem.mensagemErro("ERRO: Não é possível excluir um aluno que está alocado em uma turma!");
					return;
				}

				List<Nota> notas = notaServico.obterNotasPorIdMatricula(matriculaSelecionadaExclusao.getId());
				if (notas == null) {

				} else if (notas != null) {
					boolean notasCadastrada = false;
					for (Nota nota : notas) {

						notasCadastrada = verificarNotasDeAluno(notasCadastrada, nota);
					}
					if (notasCadastrada) {
						Mensagem.mensagemErro(
								"ERRO: Não é possível excluir um aluno que já tem notas associadas a ele!");
						return;
					}

					matriculaSelecionadaExclusao.setNotas(new ArrayList<>(notas));
					if (matriculaSelecionadaExclusao.getAluno() != null)
						matriculaSelecionadaExclusao.getAluno()
								.setNome(matriculaSelecionadaExclusao.getAluno().getNome().toUpperCase());
					matriculaServico.excluir(matriculaSelecionadaExclusao);

					Mensagem.mensagemInfo("AVISO: matrícula foi excluida com sucesso!");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.mensagemErro(
					"ERRO: Não foi possível excluir esse registo pois exitem dependência a ele em outras tabelas!");
		}
	}

	private boolean verificarNotasDeAluno(boolean notasCadastrada, Nota nota) {
		if (nota.getAc1() != null) {
			notasCadastrada = true;
		}
		if (nota.getAc2() != null) {
			notasCadastrada = true;
		}
		if (nota.getAc3() != null) {
			notasCadastrada = true;
		}

		if (nota.getAc12() != null) {
			notasCadastrada = true;
		}
		if (nota.getAc22() != null) {
			notasCadastrada = true;
		}
		if (nota.getAc32() != null) {
			notasCadastrada = true;
		}

		if (nota.getAc13() != null) {
			notasCadastrada = true;
		}
		if (nota.getAc23() != null) {
			notasCadastrada = true;
		}
		if (nota.getAc33() != null) {
			notasCadastrada = true;
		}

		if (nota.getAs1() != null) {
			notasCadastrada = true;
		}
		if (nota.getAs2() != null) {
			notasCadastrada = true;
		}

		if (nota.getAs12() != null) {
			notasCadastrada = true;
		}
		if (nota.getAs22() != null) {
			notasCadastrada = true;
		}

		if (nota.getAs13() != null) {
			notasCadastrada = true;
		}
		if (nota.getAs23() != null) {
			notasCadastrada = true;
		}
		if (nota.getAvaliacaoFinal() != null) {
			notasCadastrada = true;
		}
		if (nota.getAvaliacaoFinal2() != null) {
			notasCadastrada = true;
		}
		if (nota.getAvaliacaoFinal3() != null) {
			notasCadastrada = true;
		}
		return notasCadastrada;
	}

	public void excluirEnturmacao() {
		try {
			if (matriculaSelecionadaExclusao.getTurma().isAtivo() != false
					|| matriculaSelecionadaExclusao.getTurmaDestino().isAtivo() != false) {
				if (matriculaSelecionadaExclusao.getTurma() != null) {
					Integer qtdAlunosMatriculados = matriculaSelecionadaExclusao.getTurma().getInscrito() - 1;
					if (matriculaSelecionadaExclusao.getTurma().getRestanteVaga() == null) {
						matriculaSelecionadaExclusao.getTurma().setRestanteVaga(0);
					}
					Integer restanteVagas = matriculaSelecionadaExclusao.getTurma().getRestanteVaga() + 1;
					matriculaSelecionadaExclusao.getTurma().setRestanteVaga(restanteVagas);
					matriculaSelecionadaExclusao.getTurma().setInscrito(qtdAlunosMatriculados);
					turmaServico.salvar(matriculaSelecionadaExclusao.getTurma());
					matriculaSelecionadaExclusao.setTurma(null);
				}
				if (matriculaSelecionadaExclusao.getTurmaDestino() != null) {
					Integer qtdAlunosMatriculados = matriculaSelecionadaExclusao.getTurmaDestino().getInscrito() - 1;
					if (matriculaSelecionadaExclusao.getTurmaDestino().getRestanteVaga() == null) {
						matriculaSelecionadaExclusao.getTurmaDestino().setRestanteVaga(0);
					}
					Integer restanteVagas = matriculaSelecionadaExclusao.getTurmaDestino().getRestanteVaga() + 1;
					matriculaSelecionadaExclusao.getTurmaDestino().setRestanteVaga(restanteVagas);
					matriculaSelecionadaExclusao.getTurmaDestino().setInscrito(qtdAlunosMatriculados);
					turmaServico.salvar(matriculaSelecionadaExclusao.getTurmaDestino());
					matriculaSelecionadaExclusao.setTurmaDestino(null);

				}
				matriculaSelecionadaExclusao.setTemTurma(false);
				matriculaServico.salvar(matriculaSelecionadaExclusao);

				Mensagem.mensagemInfo("AVISO: o aluno foi excluido na turma com sucesso!");
			} else {
				Mensagem.mensagemErro("ERRO: Não foi possível excluir o aluno na turma pois a turma esta fachada!");
			}

		} catch (

		Exception e) {
			e.printStackTrace();
			Mensagem.mensagemErro(
					"ERRO: Não foi possível excluir esse registo pois existe dependência a ele em outras tabelas!");
		}
	}

	public void selecionarMatriculaAcolacaoTurma(Matricula matricula) {
		if (matriculaSelecionadaParaAlocacaoTurmaBoolean == true) {
			matriculaSSelecionadas.add(matricula);
		} else {
			matriculaSSelecionadas.remove(matricula);
		}
		if (!matriculaSSelecionadas.isEmpty()) {
			System.out.println("Quantidade de aluno Selecionadao :" + matriculaSSelecionadas.size());
		}
	}

	public List<Classe> getObterTodasClasses() {
		try {
			List<Classe> classes = null;
			Long idEscola = authenticationContext.getFuncionarioEscolaLogada().getEscola().getId();
			classes = escolaServico.obterClassesPorIdEscola(idEscola);
			return classes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	public List<Escola> getObterTodasEscolas() {
		try {
			List<Escola> escolas = null;
			Long idEscola = authenticationContext.getFuncionarioEscolaLogada().getEscola().getId();
			escolas = escolaServico.obterEscolasPorId(idEscola);
			return escolas;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	public void buscarTurmas() {
		try {
			turmas = new ArrayList<>();

			if (matricula.getClasse().getCiclo().equals("2º CICLO")) {
				turmas = turmaServico.obterTurmasPorClasseAreaCurso(matricula.getClasse().getId(),
						matricula.getTipoArea(), matricula.getCurso(), matricula.getAno(), escola.getId());

			} else if (matricula.getClasse().getCiclo().equals("1º CICLO")) {
				turmas = turmaServico.obterTurmasPorClasseCurso(matricula.getClasse().getId(), matricula.getCurso(),
						matricula.getAno(), escola.getId());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void selecionarMatricula(Matricula matricula) {
		matrizes = new ArrayList<>();
		escolaSelecionada = new Escola();
		try {
			this.matriculaSelecionada = matricula;
			this.matricula = matricula;

			matriculaClasseDestinho = new Matricula();
			tipoArea = matriculaSelecionada.getClasse().getDescricao() + " " + matriculaSelecionada.getTipoArea();
			renovarMatriculaSelecionadaBoolean = true;
			this.matriculaRenovacao = new Matricula();
			this.matriculaRenovacao.setTipoArea(matricula.getTipoArea());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buscarClasseDestino() {
		matrizes = new ArrayList<>();
		try {
			if (escolaSelecionada.getId() != null && matriculaSelecionada.getClasse().getId() != 0) {
				classes = escolaServico.obterClassesPorIdEscolaPorIdClasse(escolaSelecionada.getId(),
						matriculaSelecionada.getClasse().getId());
				System.out.println("Curso:" + matriculaSelecionada.getCurso());
				System.out.println("Escola:" + escolaSelecionada.getId());

				matrizes = matrizServico.obterMatriz2Ciclo(matriculaSelecionada.getClasse().getId(),
						matriculaSelecionada.getCurso(), matriculaSelecionada.getTipoArea(), escolaSelecionada.getId());
				// System.out.println("Área Curricular de destino:" +
				// matrizes.get(0).getTipoArea());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void voltarListaMatricula() {
		escolaSelecionada = new Escola();
		renovarMatriculaSelecionadaBoolean = false;

	}

	public void buscarMatriculas() {
		try {
			qtdAlunoMatriculado = 0;
			matriculas = new ArrayList<>();

			if (matricula.getAno().equals(null)) {
				Mensagem.mensagemErro("O campo período lectivo é obrigatório");
			}

			if (escolherSegundoCriterioBoolean == true && escolherTerceiroCriterioBoolean == true) {

				if (matricula.getClasse() == null) {
					Mensagem.mensagemErro("O campos classe é obrigatório");
				}
				if (matricula.getCurso().trim().equals("")) {
					Mensagem.mensagemErro("O campo curso é obrigatório");
				}
				if (matricula.getTurma() == null) {
					Mensagem.mensagemErro("O campos turma é obrigatório");
				} else

					matriculas = matriculaServico.obterMatriculaPorTurma(matricula.getTurma().getId());
			} else if (escolherSegundoCriterioBoolean == true) {
				if (matricula.getClasse() == null) {
					Mensagem.mensagemErro("O campos classe é obrigatório");
				}
				if (matricula.getCurso().trim().equals("")) {
					Mensagem.mensagemErro("O campo curso é obrigatório");
				} else if (matricula.getClasse().getCiclo().equals("2º CICLO")) {

					matriculas = matriculaServico.obterMatriculasPorClassePorCursoPorAreaPorTurma(
							matricula.getClasse().getId(), matricula.getEscola().getId(), matricula.getCurso(),
							matricula.getAno(), matricula.getTipoArea(), listarMatricula);

				} else if (matricula.getClasse().getCiclo().equals("1º CICLO")) {

					matriculas = matriculaServico.obterMatriculasPorClassePorCursoPorTurma(
							matricula.getClasse().getId(), matricula.getEscola().getId(), matricula.getCurso(),
							matricula.getAno(), listarMatricula);

				}
			} else if (escolherSegundoCriterioBoolean == true && escolherTerceiroCriterioBoolean == true) {
				if (matricula.getClasse() == null) {
					Mensagem.mensagemErro("O campos classe é obrigatório");
				}
				if (matricula.getCurso().trim().equals("")) {
					Mensagem.mensagemErro("O campo curso é obrigatório");
				}
				if (matricula.getTurma() == null) {
					Mensagem.mensagemErro("O campos turma é obrigatório");
				} else
					matriculas = matriculaServico.obterMatriculaPorTurma(matricula.getTurma().getId());
			} else if (escolherTerceiroCriterioBoolean == true) {

				if (matricula.getTurma() == null) {
					Mensagem.mensagemErro("O campos turma é obrigatório");
				} else
					matriculas = matriculaServico.obterMatriculaPorTurma(matricula.getTurma().getId());
			} else if (escolherSegundoCriterioBoolean == true) {
				if (matricula.getCurso().trim().equals("")) {
					Mensagem.mensagemErro("O campo curso é obrigatório");
				}
				if (matricula.getClasse() == null) {
					Mensagem.mensagemErro("O campos classe é obrigatório");
				}
			} else {
				if (listarMatricula == 1) {
					matriculas = matriculaServico.obterMatriculasPorEscolaPorAno(matricula.getEscola().getId(),
							matricula.getAno());
				} else if (listarMatricula == 2) {
					matriculas = matriculaServico.obterMatriculasPorEscolaPorAnoTemTurmaPergunta(
							matricula.getEscola().getId(), matricula.getAno(), Boolean.FALSE);
				} else if (listarMatricula == 3) {
					matriculas = matriculaServico.obterMatriculasPorEscolaPorAnoTemTurmaPergunta(
							matricula.getEscola().getId(), matricula.getAno(), Boolean.TRUE);
				}

			}
			if (matriculas != null) {
				qtdAlunoMatriculado = matriculas.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buscarMatriculasAlocarNaTurma() {
		try {
			qtdAlunoMatriculado = 0;
			matriculas = new ArrayList<>();

			if (matricula.getAno().equals(null)) {
				Mensagem.mensagemErro("O campo ano lectivo é obrigatório");
			}

			if (matricula.getClasse() == null) {
				Mensagem.mensagemErro("O campos classe é obrigatório");
			}
			if (matricula.getCurso().trim().equals("")) {
				Mensagem.mensagemErro("O campo curso é obrigatório");

			} else if (matricula.getClasse().getCiclo().equals("2º CICLO")) {

				matriculas = matriculaServico.obterMatriculasPorClassePorCursoPorAreaPorTurma(
						matricula.getClasse().getId(), matricula.getEscola().getId(), matricula.getCurso(),
						matricula.getAno(), matricula.getTipoArea(), listarMatricula);

			} else if (matricula.getClasse().getCiclo().equals("1º CICLO")) {

				matriculas = matriculaServico.obterMatriculasPorClassePorCursoPorTurma(matricula.getClasse().getId(),
						matricula.getEscola().getId(), matricula.getCurso(), matricula.getAno(), listarMatricula);

			}

			if (!matriculas.isEmpty()) {
				qtdAlunoMatriculado = matriculas.size();

			}

		} catch (

		Exception e) {
			e.printStackTrace();
		}

	}

	public void listarAlunos() {
		try {
			qtdAlunoMatriculado = 0;
			matriculas = new ArrayList<>();
			List<Matricula> matriculas = new ArrayList<>();
			if (idadeInicial == null && idadeFinal != null) {
				Mensagem.mensagemErro("O campo idade inicial é obrigatório");
				return;
			}
			if (idadeInicial != null && idadeFinal == null) {
				Mensagem.mensagemErro("O campo idade final é obrigatório");
				return;
			}
			Integer anoInicio = null, anoFim = null;
			if (idadeInicial != null && idadeFinal != null) {
				Date data = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				String dataFormatado = sdf.format(data);
				Integer anoAtual = Integer.valueOf(dataFormatado);
				anoFim = anoAtual - idadeInicial;
				anoInicio = anoAtual - idadeFinal;

			}

			if (matricula.getClasse().getCiclo().equals("2º CICLO")) {

				matriculas = matriculaServico.obterMatriculasPorClassePorCursoPorAreaPorTurma(
						matricula.getEscola().getId(), matricula.getClasse().getId(), matricula.getCurso(),
						matricula.getAno(), matricula.getTipoArea(), listarMatricula, anoInicio, anoFim);

			} else if (matricula.getClasse().getCiclo().equals("1º CICLO")) {

				matriculas = matriculaServico.obterMatriculasPorClassePorCursoPorTurmaSemArea(
						matricula.getEscola().getId(), matricula.getClasse().getId(), matricula.getCurso(),
						matricula.getAno(), listarMatricula, anoInicio, anoFim);

			}

			if (!matriculas.isEmpty()) {
				qtdAlunoMatriculado = matriculas.size();
				Integer contador = 0;
				for (Matricula matricula : matriculas) {
					contador++;
					matricula.setOrdem(contador);
					this.matriculas.add(matricula);
				}
				setExistemMatriculas(0);
			} else {
				setExistemMatriculas(1);
			}

		} catch (

		Exception e) {
			e.printStackTrace();
		}

	}

	public void buscar() {
		try {

			alunos = alunoServico.obterAlunosPorPesquisa(pesquisa);
			if (alunos == null) {
				qtdAlunosEncontrados = 0;
			} else {
				qtdAlunosEncontrados = alunos.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void bucarDistritoDaProvincia() {
		try {
			if (!distrito.equals(null))
				if (distrito.getProvincia() != null)
					distritos = distritoServico.obterDistritosDaProvincia(distrito.getProvincia());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void primeiraFase() {
		proximaPrimeiraFaseBoolean = false;
		proximaSegundaFaseBoolean = true;
		proximaTerceiraFaseBoolean = false;
		try {
			if (aluno.getLogin() == null) {
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
						aluno.setLogin(login);
					}

				}
				aluno.setPermissoes(new ArrayList<>());
				Permissao permissao = usuarioServico.obterPermicaoPolaDescricao("ROLE_ALUNO");
				while (permissao == null) {
					permissao = new Permissao();
					permissao.setNome("ROLE_ALUNO");
					permissao = permissaoServico.salvarRetornarPermissao(permissao);
				}
				aluno.getPermissoes().add(permissao);
			}

			System.out.println(
					"Data de Nascimento:" + DataUtils.obterDataFormatoBanco(aluno.getDataNascimento(), "dd/MM/yyyy"));
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.mensagemErro("ERRO: hove um erro ao atribuir a permissão do aluno!");
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
			if (aluno.getDataCadastro() == null) {
				aluno.setDataCadastro(new Date());
			}

			if (aluno.getSenha() == null) {
				String senha = DataUtils.obterDataFormatoBanco(aluno.getDataNascimento(), FORMATA_SENHA_PADRAO);
				String senhaCriptgrafada = usuarioServico.criptografarSenha(senha);
				aluno.setSenha(senhaCriptgrafada);
				System.out.println("Senha de  : " + aluno.getNome() + " é :" + senha);
			}
			if (aluno.getNascionalidade().equals("Estrangeira")) {
				aluno.setEnderenco(new Endereco());
				aluno.getEnderenco().setDistrito(null);
				aluno.setLocalNascimento(null);
			} else if (!aluno.getNascionalidade().equals("Estrangeira")) {
				aluno.setPais(null);

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

	public List<Pais> getObterPaises() {
		try {
			if (aluno.getNascionalidade() == null)
				paises = new ArrayList<>();
			else if (aluno.getNascionalidade().equals("Estrangeira")) {
				paises = paisServico.obterTodosPaises();

			}
			return paises;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	// inicializar notas de aluno e chamado no pritty config
	public void buscarMinhasNotas() {
		String tipoMediatrimestre = "";
		mediaAlunoTrimetral = null;
		mediaAlunoTrimetralPorExtenso = null;
		try {
			this.notas = null;
			matricula = authenticationContext.getMatriculaEscolaLogada();
			if (matricula == null) {
			} else {

				if (trimestreAtivo == null) {

				} else if (trimestreAtivo != null) {
					this.notas = notaServico.obterNotaPorIdAluno(matricula.getAluno().getId(),
							matricula.getClasse().getId(), matricula.getAno());
					if (trimestreAtivo.getDescricao().equals("1º Trimestre")) {
						tipoMediatrimestre = "mediaTrimestral";
					} else if (trimestreAtivo.getDescricao().equals("2º Trimestre")) {
						tipoMediatrimestre = "mediaTrimestral2";
					} else {
						tipoMediatrimestre = "mediaTrimestral3";
					}
					Double mediaTrimestral = notaServico.obterMediaTrimestralOuAnulDoAluno(matricula.getAluno().getId(),
							matricula.getClasse().getId(), tipoMediatrimestre);
					if (mediaTrimestral == null) {
					} else if (mediaTrimestral != 0) {
						mediaAlunoTrimetral = (int) Math.round(mediaTrimestral);
						ValorExtenso valorExtenso = new ValorExtenso();
						mediaAlunoTrimetralPorExtenso = valorExtenso.write(BigDecimal.valueOf(mediaAlunoTrimetral))
								.toLowerCase().replace("um mil ", "mil ").toLowerCase().replace("meticais", "")
								.replace("metical", "");

					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void editar(Aluno aluno) {
		this.aluno = aluno;
		if (aluno != null)
			if (aluno.getEnderenco() == null)
				aluno.setEnderenco(new Endereco());
			else if (aluno.getEnderenco() != null)
				if (aluno.getEnderenco().getDistrito() != null) {
					distrito = aluno.getEnderenco().getDistrito();
					bucarDistritoDaProvincia();
				}
		novoAlunoBoolean = false;
		cadastroAlunoBoolean = true;
	}

	public void avancarParaSelecionarDisciplina() {
		matriculaAvancarBoolean = true;
		alocadoComSucessoBoolean = false;
		try {
			matriz = new Matriz();
			marcarTodosBoolean = false;
			classe = new Classe();
			valorMatricula = matricula.getValor();

			classe = classeServico.obterClassePorId(matricula.getClasse().getId());
			if (classe != null)
				if (classe.getCiclo().equals("2º CICLO")) {
					matriz = matrizServico.obterMatriz2CicloPorIdELeftJoinAtiva(classe.getId(), matricula.getCurso(),
							matricula.getTipoArea(), escola.getId());
				} else if (classe.getCiclo().equals("1º CICLO")) {
					matriz = matrizServico.obterMatrizPorIdELeftJoinAtiva(classe.getId(), matricula.getCurso(),
							escola.getId());

				}
			System.out.println("Valor da matrcula: " + valorMatricula);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void voltaraVancarParaSelecionarDisciplina() {
		matriculaAvancarBoolean = false;
		alocadoComSucessoBoolean = false;

	}

	public void selecionarAluno(Aluno aluno) {
		this.alunoSelecionado = aluno;
		matriculaAvancarBoolean = false;
		matriculaAlunoBoolean = true;
		novoAlunoBoolean = false;
		cadastroAlunoBoolean = true;
		matricula = new Matricula();
		matriz = new Matriz();
		alocadoComSucessoBoolean = false;
		try {
			if (valorMatricula != null)
				matricula.setValor(valorMatricula);
			novoAlunoSelecionadoBoolean = false;
			Matricula novaMatricula = matriculaServico.obterMatriculaPorIdAluno(aluno.getId());
			if (novaMatricula == null) {
				novoAlunoSelecionadoBoolean = true;
			} else {
				novoAlunoSelecionadoBoolean = false;
			}

			Calendario calendario = authenticationContext.getCalendarioEscolar();
			matricula.setAno(calendario.getAno());
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();
			classes = escolaServico.obterClassesPorIdEscola(escola.getId());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void imprimir(Aluno aluno) {
		this.alunoSelecionado = aluno;
		matriculaAlunoBoolean = true;
		novoAlunoBoolean = false;
		cadastroAlunoBoolean = true;
		matricula = new Matricula();
		matriz = new Matriz();
		try {
			Calendario calendario = authenticationContext.getCalendarioEscolar();
			matricula.setAno(calendario.getAno());
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();
			classes = escolaServico.obterClassesPorIdEscola(escola.getId());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscarMatrizesCurriculares() {
		matrizes = new ArrayList<>();
		try {
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			escola = funcionarioEscola.getEscola();
			if (matriculaSelecionada != null) {
				matricula = matriculaSelecionada;
			}

			if (renovarMatriculaSelecionadaBoolean == false) {
				if (matricula.getCurso() != null && matricula.getClasse().getId() != 0 && escola.getId() != null) {
					if (matricula.getClasse().getCiclo().equals("2º CICLO")) {
						matrizes = matrizServico.obterMatrizPorClasseCursoAtivo(matricula.getClasse().getId(),
								matricula.getCurso(), escola.getId());

					}
				}
			} else {
				if (matricula.getCurso() != null && matriculaRenovacao.getClasse().getId() != 0
						&& escola.getId() != null) {
					if (matriculaRenovacao.getClasse().getCiclo().equals("2º CICLO")) {
						matrizes = matrizServico.obterMatrizPorClasseCursoAtivo(matriculaRenovacao.getClasse().getId(),
								matricula.getCurso(), escola.getId());

					} else if (matriculaRenovacao.getClasse().getCiclo().equals("1º CICLO")) {
						matriz = matrizServico.obterMatrizPorIdELeftJoinAtiva(matriculaRenovacao.getClasse().getId(),
								matricula.getCurso(), escola.getId());

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscarMatrizCurricular() {
		matrizes = new ArrayList<>();
		try {
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			escola = funcionarioEscola.getEscola();
			if (matriculaSelecionada.getClasse().getCiclo().equals("1º CICLO")) {
				matriz = matrizServico.obterMatrizPorIdELeftJoinAtiva(matriculaSelecionada.getClasse().getId(),
						matriculaSelecionada.getCurso(), escola.getId());

			} else {

				matriz = matrizServico.obterMatriz2CicloPorIdELeftJoinAtiva(matriculaSelecionada.getClasse().getId(),
						matriculaSelecionada.getCurso(), matriculaSelecionada.getTipoArea(), escola.getId());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscarMatrizesPorAreaCurricular() {

		try {
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();
			if (matricula.getCurso() != null && matricula.getClasse().getId() != 0 && escola.getId() != null) {
				if (matricula.getClasse().getCiclo().equals("2º CICLO")) {
					matriz = matrizServico.obterMatriz2CicloPorIdELeftJoinAtiva(matricula.getClasse().getId(),
							matricula.getCurso(), matricula.getTipoArea(), escola.getId());

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Matriz> getObterMatrizesCurriculares() {

		try {
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();
			// if(matricula.getCurso() !=null && matricula.getClasse() !=null &&
			// escola !=null){
			matrizes = matrizServico.obterMatrizPorClasseCursoAtivo(matricula.getClasse().getId(), matricula.getCurso(),
					escola.getId());
			if (matrizes != null) {
				for (Matriz matriz : matrizes) {
					System.out.println("id:" + matriz.getId());
					System.out.println("Classe:" + matriz.getClasse().getDescricao());
					System.out.println("Curso:" + matriz.getCurso());
					System.out.println("Area:" + matriz.getTipoArea());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matrizes;
	}

	public void novoAluno() {
		aluno = new Aluno();
		aluno.setEnderenco(new Endereco());
		novoAlunoBoolean = true;
		cadastroAlunoBoolean = true;

	}

	public void voltarParaPequisa() {
		aluno = null;
		cadastroAlunoBoolean = false;
		if (novoAlunoBoolean == true) {
			novoAlunoBoolean = false;
		}
		if (matriculaAlunoBoolean == true) {
			matriculaAlunoBoolean = false;
		}
		proximaPrimeiraFaseBoolean = true;
		proximaSegundaFaseBoolean = false;
		proximaTerceiraFaseBoolean = false;
		matriculaAlunoBoolean = false;
		// buscar();

	}

	public void voltarPequisarAlunoParaMatricular() {
		aluno = null;
		confirmarsalvarMatricula = false;
		if (matriculaAlunoBoolean == true) {
			matriculaAlunoBoolean = false;
		}

	}

	public void voltarParaEscolherClasse() {
		proximoAlocarAlunoBoolean = false;
		try {
			matricula.setTurma(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void proxomoParaListarAlunos() {
		proximoAlocarAlunoBoolean = true;
		matricula.setDataEnturmacao(new Date());
		idadeFinal = null;
		idadeInicial = null;
		matriculas = new ArrayList<>();
		setExistemMatriculas(0);
		buscarTurmas();

	}

	public List<Distrito> getDistritosDaProvincia() {
		List<Distrito> distritos = null;
		if (aluno != null)
			if (aluno.getEnderenco() != null) {
				if (aluno.getEnderenco().getDistrito() != null) {
					if (aluno.getEnderenco().getDistrito().getProvincia() != null) {
						distritos = distritoServico
								.obterDistritosDaProvincia(aluno.getEnderenco().getDistrito().getProvincia());
					}
				}
			}
		return distritos;
	}

	public Aluno getAluno() {
		if (aluno == null) {
			distrito = new Distrito();
			provincias = new ArrayList<>();
			paises = new ArrayList<>();
		} else if (aluno.getNascionalidade() == null) {
			distrito = new Distrito();
			provincias = new ArrayList<>();
			paises = new ArrayList<>();
		} else if (!aluno.getNascionalidade().equals("Estrangeira")) {
			provincias = Arrays.asList(Provincia.values());
		}
		return aluno;
	}

	public void editar(Matricula matricula) {
		this.matriculaSelecionada = matricula;
		confirmarsalvarMatricula = false;
		renovarMatriculaSelecionadaBoolean = false;
		try {
			buscarMatrizesCurriculares();
			if (matricula.getClasse().getCiclo().equals("2º CICLO")) {
				matriz = matrizServico.obterMatriz2CicloPorIdELeftJoinAtiva(matricula.getClasse().getId(),
						matricula.getCurso(), matricula.getTipoArea(), matricula.getEscola().getId());

			} else if (matricula.getClasse().getCiclo().equals("1º CICLO")) {
				matriz = matrizServico.obterMatrizPorIdELeftJoinAtiva(matricula.getClasse().getId(),
						matricula.getCurso(), matricula.getEscola().getId());

			}
			nota = new Nota();
			notas = new ArrayList<>();
			buscarNotas();

			editarMatricula = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void salvarAlunoTurma() {

		try {

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void editarAlunoTurma(Matricula matricula) {
		this.matriculaSelecionada = matricula;
		editarMatricula = true;
		turmas = new ArrayList<>();
		try {
			if (matricula.getTurmaDestino() == null) {

			} else {
				this.matriculaSelecionada.setTurma(matricula.getTurmaDestino());
				this.matriculaSelecionada.setTurmaDestino(matricula.getTurmaDestino());
			}

			if (matricula.getClasse().getCiclo().equals("2º CICLO")) {
				turmas = turmaServico.obterTurmasPorClasseAreaCursoDiferenteIdTurma(matricula.getClasse().getId(),
						matricula.getTipoArea(), matricula.getCurso(), matricula.getAno(),
						matricula.getEscola().getId(), matricula.getTurma().getId());

			} else if (matricula.getClasse().getCiclo().equals("1º CICLO")) {
				turmas = turmaServico.obterTurmasPorClasseCursoDiferenteIdTurma(matricula.getClasse().getId(),
						matricula.getCurso(), matricula.getAno(), matricula.getEscola().getId(),
						matricula.getTurma().getId());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String marcarTodos() {
		if (marcarTodosBoolean == true) {
			for (DisciplinaClasse disciplinaClasse : matriz.getDisciplinaClasses()) {
				disciplinaClasse.setDisciplinaSeleconadaBoolean(true);
			}
		} else {
			for (DisciplinaClasse disciplinaClasse : matriz.getDisciplinaClasses()) {
				disciplinaClasse.setDisciplinaSeleconadaBoolean(false);
			}
		}
		return null;
	}

	public String desmarcarOtop(DisciplinaClasse disciplinaClasse) {

		if (disciplinaClasse.isDisciplinaSeleconadaBoolean() == false) {
			marcarTodosBoolean = false;
		} else {
			int count = 0;
			for (DisciplinaClasse disciplinaClasse1 : matriz.getDisciplinaClasses()) {
				if (disciplinaClasse1.isDisciplinaSeleconadaBoolean()) {
					count++;
				}
				if (count == matriz.getDisciplinaClasses().size())
					marcarTodosBoolean = true;
			}
		}
		return null;
	}

	public void voltar() {

		if (editarMatricula == true)
			buscarMatriculas();

		editarMatricula = false;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}

	public void exportarParaPDF(Matricula matricula) {
		String mediafinal = "mediaAnual", mediaAnualPorExtenso = null, situacao = null;
		Integer mediaFinalConvertido = null;
		String caminho = "/academico/relatorio/aluno/listar_notas_por_aluno.jasper", filename = "doc.pdf";
		Map<String, Object> parametro = new HashMap<>();
		ValorExtenso valorExtenso = new ValorExtenso();

		try {
			if (matricula.getClasse().getCiclo().equals("1º CICLO")) {
				Double mediaFinal = notaServico.obterMediaTrimestralOuAnulDoAluno(matricula.getAluno().getId(),
						matricula.getClasse().getId(), mediafinal);
				mediaFinalConvertido = (int) Math.round(mediaFinal);
				mediaAnualPorExtenso = valorExtenso.write(BigDecimal.valueOf(mediaFinalConvertido)).toLowerCase()
						.replace("um mil ", "mil ").toLowerCase().replace("meticais", "").replace("metical", "");
				if (matricula.getClasse().getDescricao().equals("10ª CLASSE")) {
					if (mediaFinalConvertido >= 10 && mediaFinalConvertido < 14) {
						situacao = "Admitido";
					} else if (mediaFinalConvertido >= 14 && mediaFinalConvertido < 21) {
						situacao = "Dispensado";
					} else if (mediaFinalConvertido >= 0 && mediaFinalConvertido < 10) {
						situacao = "Reprovado";
					}
				} else {
					if (mediaFinalConvertido >= 10 && mediaFinalConvertido < 21) {
						situacao = "Aprovado";
					} else if (mediaFinalConvertido >= 0 && mediaFinalConvertido < 10) {
						situacao = "Reprovado";
					}
				}
			}
			parametro.put("idMatricula", matricula.getId());
			parametro.put("mediaAnual", mediaFinalConvertido);
			parametro.put("mediaAnualPorExtenso", mediaAnualPorExtenso);
			parametro.put("situacao", situacao);

			geradorDeRelatoriosServico.geraPdf(caminho, parametro, filename);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void imprimirReciboInscricaoDoAluno(Matricula matricula) {

		try {
			// http://localhost:8080/sistema-escolar/login
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

			String url = request.getRequestURI() + "";

			System.out.println("URL:" + url);
			String vals[] = url.split("/academico/");
			String link = vals[0];

			link = link + "/login.jsp";
			String caminho = "/academico/relatorio/aluno/dados_aluno.jasper";
			Map<String, Object> parametro = new HashMap<>();
			parametro.put("link", link);
			if (matricula.getAluno().isSenhaAlterada() == false) {
				String senha = DataUtils.obterDataFormatoBanco(matricula.getAluno().getDataNascimento(),
						FORMATA_SENHA_PADRAO);
				parametro.put("password", senha);
			}
			if (matricula.getTurma() != null) {
				parametro.put("turma", matricula.getClasse().getSigla() + "ª-" + matricula.getTurma().getDescricao()
						+ "-" + matricula.getTurma().getTurno().getSigla());
			}
			parametro.put("idMatricula", matricula.getId());
			ValorExtenso e = new ValorExtenso();
			if (matricula.getValor() == null) {
				parametro.put("valorMatriculaExtenso", null);
			} else if (matricula.getValor() != null) {
				String valorExtenso = e.write(BigDecimal.valueOf(matricula.getValor()));
				parametro.put("valorMatriculaExtenso", valorExtenso.toLowerCase().replace("um mil ", "mil "));
			}

			geradorDeRelatoriosServico.geraPdf(caminho, parametro, "doc.pdf");
			// JasperPrintManager.printReport(relatorio, true);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public List<Provincia> getProvincias() {
		return provincias;
	}

	public void setProvincias(List<Provincia> provincias) {
		this.provincias = provincias;
	}

	public Escola getEscola() {
		return escola;
	}

	public void setEscola(Escola escola) {
		this.escola = escola;
	}

	public List<Nota> getNotas() {
		return notas;
	}

	public void setNotas(List<Nota> notas) {
		this.notas = notas;
	}

	public Integer getMediaAlunoTrimetral() {
		return mediaAlunoTrimetral;
	}

	public void setMediaAlunoTrimetral(Integer mediaAlunoTrimetral) {
		this.mediaAlunoTrimetral = mediaAlunoTrimetral;
	}

	public Trimestre getTrimestreAtivo() {
		return trimestreAtivo;
	}

	public void setTrimestreAtivo(Trimestre trimestreAtivo) {
		this.trimestreAtivo = trimestreAtivo;
	}

	public Matricula getMatricula() {
		return matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public List<Matricula> getMatriculas() {
		return matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	public boolean isCadastroAlunoBoolean() {
		return cadastroAlunoBoolean;
	}

	public void setCadastroAlunoBoolean(boolean cadastroAlunoBoolean) {
		this.cadastroAlunoBoolean = cadastroAlunoBoolean;
	}

	public boolean isNovoAlunoBoolean() {
		return novoAlunoBoolean;
	}

	public void setNovoAlunoBoolean(boolean novoAlunoBoolean) {
		this.novoAlunoBoolean = novoAlunoBoolean;
	}

	public String getMediaAlunoTrimetralPorExtenso() {
		return mediaAlunoTrimetralPorExtenso;
	}

	public void setMediaAlunoTrimetralPorExtenso(String mediaAlunoTrimetralPorExtenso) {
		this.mediaAlunoTrimetralPorExtenso = mediaAlunoTrimetralPorExtenso;
	}

	public Integer getQtdAlunosEncontrados() {
		return qtdAlunosEncontrados;
	}

	public void setQtdAlunosEncontrados(Integer qtdAlunosEncontrados) {
		this.qtdAlunosEncontrados = qtdAlunosEncontrados;
	}

	public List<Pais> getPaises() {
		return paises;
	}

	public void setPaises(List<Pais> paises) {
		this.paises = paises;
	}

	public List<EstadoCivil> getEstadoCivils() {

		if (aluno != null) {
			if (aluno.isSexo() == true) {
				estadoCivils = Arrays.asList(EstadoCivil.CASADO, EstadoCivil.SOLTEIRO);
			} else if (aluno.isSexo() == false) {
				estadoCivils = Arrays.asList(EstadoCivil.CASADA, EstadoCivil.SOLTEIRA);
			}
		}
		return estadoCivils;
	}

	public void setEstadoCivils(List<EstadoCivil> estadoCivils) {
		this.estadoCivils = estadoCivils;
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

	public Aluno getAlunoSelecionado() {
		return alunoSelecionado;
	}

	public void setAlunoSelecionado(Aluno alunoSelecionado) {
		this.alunoSelecionado = alunoSelecionado;
	}

	public boolean isMatriculaAlunoBoolean() {
		return matriculaAlunoBoolean;
	}

	public void setMatriculaAlunoBoolean(boolean matriculaAlunoBoolean) {
		this.matriculaAlunoBoolean = matriculaAlunoBoolean;
	}

	public List<TipoCurso> getCursos() {
		return cursos;
	}

	public void setCursos(List<TipoCurso> cursos) {
		this.cursos = cursos;
	}

	public List<Classe> getClasses() {
		return classes;
	}

	public void setClasses(List<Classe> classes) {
		this.classes = classes;
	}

	public List<Matriz> getMatrizes() {
		return matrizes;
	}

	public void setMatrizes(List<Matriz> matrizes) {
		this.matrizes = matrizes;
	}

	public Matriz getMatriz() {
		return matriz;
	}

	public void setMatriz(Matriz matriz) {
		this.matriz = matriz;
	}

	public Aluno getAlunoSelecionadoExclusao() {
		return alunoSelecionadoExclusao;
	}

	public void setAlunoSelecionadoExclusao(Aluno alunoSelecionadoExclusao) {
		this.alunoSelecionadoExclusao = alunoSelecionadoExclusao;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public boolean isEscolherTerceiroCriterioBoolean() {
		return escolherTerceiroCriterioBoolean;
	}

	public void setEscolherTerceiroCriterioBoolean(boolean escolherTerceiroCriterioBoolean) {
		this.escolherTerceiroCriterioBoolean = escolherTerceiroCriterioBoolean;
	}

	public boolean isEscolherSegundoCriterioBoolean() {
		return escolherSegundoCriterioBoolean;
	}

	public void setEscolherSegundoCriterioBoolean(boolean escolherSegundoCriterioBoolean) {
		this.escolherSegundoCriterioBoolean = escolherSegundoCriterioBoolean;
	}

	public Integer getQtdAlunoMatriculado() {
		return qtdAlunoMatriculado;
	}

	public void setQtdAlunoMatriculado(Integer qtdAlunoMatriculado) {
		this.qtdAlunoMatriculado = qtdAlunoMatriculado;
	}

	public boolean isRenovarMatriculaBoolean() {
		return renovarMatriculaBoolean;
	}

	public void setRenovarMatriculaBoolean(boolean renovarMatriculaBoolean) {
		this.renovarMatriculaBoolean = renovarMatriculaBoolean;
	}

	public List<Trimestres> getTipoTrimestres() {
		return tipoTrimestres;
	}

	public void setTipoTrimestres(List<Trimestres> tipoTrimestres) {
		this.tipoTrimestres = tipoTrimestres;
	}

	public boolean isRenovarMatriculaSelecionadaBoolean() {
		return renovarMatriculaSelecionadaBoolean;
	}

	public void setRenovarMatriculaSelecionadaBoolean(boolean renovarMatriculaSelecionadaBoolean) {
		this.renovarMatriculaSelecionadaBoolean = renovarMatriculaSelecionadaBoolean;
	}

	public Matricula getMatriculaRenovacao() {
		return matriculaRenovacao;
	}

	public void setMatriculaRenovacao(Matricula matriculaRenovacao) {
		this.matriculaRenovacao = matriculaRenovacao;
	}

	public boolean isNovoAlunoSelecionadoBoolean() {
		return novoAlunoSelecionadoBoolean;
	}

	public void setNovoAlunoSelecionadoBoolean(boolean novoAlunoSelecionadoBoolean) {
		this.novoAlunoSelecionadoBoolean = novoAlunoSelecionadoBoolean;
	}

	public Matricula getMatriculaSelecionadaExclusao() {
		return matriculaSelecionadaExclusao;
	}

	public void setMatriculaSelecionadaExclusao(Matricula matriculaSelecionadaExclusao) {
		this.matriculaSelecionadaExclusao = matriculaSelecionadaExclusao;
	}

	public boolean isMatriculaSelecionadaParaAlocacaoTurmaBoolean() {
		return matriculaSelecionadaParaAlocacaoTurmaBoolean;
	}

	public void setMatriculaSelecionadaParaAlocacaoTurmaBoolean(boolean matriculaSelecionadaParaAlocacaoTurmaBoolean) {
		this.matriculaSelecionadaParaAlocacaoTurmaBoolean = matriculaSelecionadaParaAlocacaoTurmaBoolean;
	}

	public List<Matricula> getMatriculaSSelecionadas() {
		return matriculaSSelecionadas;
	}

	public void setMatriculaSSelecionadas(List<Matricula> matriculaSSelecionadas) {
		this.matriculaSSelecionadas = matriculaSSelecionadas;
	}

	public boolean isConfirmarsalvarMatricula() {
		return confirmarsalvarMatricula;
	}

	public void setConfirmarsalvarMatricula(boolean confirmarsalvarMatricula) {
		this.confirmarsalvarMatricula = confirmarsalvarMatricula;
	}

	public Integer getListarMatricula() {
		return listarMatricula;
	}

	public void setListarMatricula(Integer listarMatricula) {
		this.listarMatricula = listarMatricula;
	}

	public List<DisciplinaClasse> getDisciplinaSelecionadas() {
		return disciplinaSelecionadas;
	}

	public void setDisciplinaSelecionadas(List<DisciplinaClasse> disciplinaSelecionadas) {
		this.disciplinaSelecionadas = disciplinaSelecionadas;
	}

	public Matricula getMatriculaSelecionada() {
		return matriculaSelecionada;
	}

	public void setMatriculaSelecionada(Matricula matriculaSelecionada) {
		this.matriculaSelecionada = matriculaSelecionada;
	}

	public Matricula getMatriculaClasseDestinho() {
		return matriculaClasseDestinho;
	}

	public void setMatriculaClasseDestinho(Matricula matriculaClasseDestinho) {
		this.matriculaClasseDestinho = matriculaClasseDestinho;
	}

	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	public String getTipoArea() {
		return tipoArea;
	}

	public void setTipoArea(String tipoArea) {
		this.tipoArea = tipoArea;
	}

	public Transferencia getTransferencia() {
		return transferencia;
	}

	public void setTransferencia(Transferencia transferencia) {
		this.transferencia = transferencia;
	}

	public boolean isEditarMatricula() {
		return editarMatricula;
	}

	public void setEditarMatricula(boolean editarMatricula) {
		this.editarMatricula = editarMatricula;
	}

	public Escola getEscolaSelecionada() {
		return escolaSelecionada;
	}

	public void setEscolaSelecionada(Escola escolaSelecionada) {
		this.escolaSelecionada = escolaSelecionada;
	}

	public boolean isMatriculaAvancarBoolean() {
		return matriculaAvancarBoolean;
	}

	public void setMatriculaAvancarBoolean(boolean matriculaAvancarBoolean) {
		this.matriculaAvancarBoolean = matriculaAvancarBoolean;
	}

	public boolean isMarcarTodosBoolean() {
		return marcarTodosBoolean;
	}

	public void setMarcarTodosBoolean(boolean marcarTodosBoolean) {
		this.marcarTodosBoolean = marcarTodosBoolean;
	}

	public boolean isAlocadoComSucessoBoolean() {
		return alocadoComSucessoBoolean;
	}

	public void setAlocadoComSucessoBoolean(boolean alocadoComSucessoBoolean) {
		this.alocadoComSucessoBoolean = alocadoComSucessoBoolean;
	}

	public boolean isProximoAlocarAlunoBoolean() {
		return proximoAlocarAlunoBoolean;
	}

	public void setProximoAlocarAlunoBoolean(boolean proximoAlocarAlunoBoolean) {
		this.proximoAlocarAlunoBoolean = proximoAlocarAlunoBoolean;
	}

	public Integer getIdadeInicial() {
		return idadeInicial;
	}

	public void setIdadeInicial(Integer idadeInicial) {
		this.idadeInicial = idadeInicial;
	}

	public Integer getIdadeFinal() {
		return idadeFinal;
	}

	public void setIdadeFinal(Integer idadeFinal) {
		this.idadeFinal = idadeFinal;
	}

	public int getExistemMatriculas() {
		return existemMatriculas;
	}

	public void setExistemMatriculas(int existemMatriculas) {
		this.existemMatriculas = existemMatriculas;
	}

	public Double getValorMatricula() {
		return valorMatricula;
	}

	public void setValorMatricula(Double valorMatricula) {
		this.valorMatricula = valorMatricula;
	}

	public Nota getNota() {
		return nota;
	}

	public void setNota(Nota nota) {
		this.nota = nota;
	}

	public Nota getNotaSelecionda() {
		return notaSelecionda;
	}

	public void setNotaSelecionda(Nota notaSelecionda) {
		this.notaSelecionda = notaSelecionda;
	}

}
