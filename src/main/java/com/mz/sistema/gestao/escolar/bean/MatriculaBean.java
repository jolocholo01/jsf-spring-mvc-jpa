// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.bean;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.enumerado.EstadoCivil;
import com.mz.sistema.gestao.escolar.modelo.Aluno;
import com.mz.sistema.gestao.escolar.modelo.Calendario;
import com.mz.sistema.gestao.escolar.modelo.Classe;
import com.mz.sistema.gestao.escolar.modelo.Disciplina;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.Matricula;
import com.mz.sistema.gestao.escolar.modelo.Matriz;
import com.mz.sistema.gestao.escolar.modelo.Nota;
import com.mz.sistema.gestao.escolar.modelo.Trimestre;
import com.mz.sistema.gestao.escolar.modelo.Turma;
import com.mz.sistema.gestao.escolar.modelo.Turno;
import com.mz.sistema.gestao.escolar.servico.AlunoServico;
import com.mz.sistema.gestao.escolar.servico.ClasseServico;
import com.mz.sistema.gestao.escolar.servico.EscolaServico;
import com.mz.sistema.gestao.escolar.servico.GeradorDeRelatoriosServico;
import com.mz.sistema.gestao.escolar.servico.MatriculaServico;
import com.mz.sistema.gestao.escolar.servico.MatrizServico;
import com.mz.sistema.gestao.escolar.servico.NotaServico;
import com.mz.sistema.gestao.escolar.servico.TrimestreServico;
import com.mz.sistema.gestao.escolar.servico.TurmaServico;
import com.mz.sistema.gestao.escolar.servico.TurnoServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;
import com.mz.sistema.gestao.escolar.util.ValorExtenso;

@Named
@SessionScope
@Controller
public class MatriculaBean {
	private Matricula matricula = new Matricula();
	private Matricula matriculaPorAluno = new Matricula();

	/* Consulta de alunos */
	private Aluno consultaPorAluno = new Aluno();
	private String consultarNomeAluno;
	private String pesquisa;
	private boolean consultarNomeAlunoBoolean = false;
	private boolean pesquisarAlunoBoolean = false;
	private List<Aluno> listarAlunos = new ArrayList<>();
	private List<Nota> disciplinasAluno = new ArrayList<>();
	private Long idAluno;

	private Turma turma = new Turma();
	private Nota nota = new Nota();
	private Turma turmaSelecionada;
	private List<Turma> turmas;
	private List<Matricula> matriculas = new ArrayList<>();

	private List<Matricula> adicionarMatriculasLista;
	private List<Matricula> listaEstudantesMatriculado;
	private List<Matricula> procuararEstudantesMatriculadoParaAltearcao;

	private List<Classe> classes;
	private List<Turno> turnos;

	private boolean inscricaoMatricula = false;
	private boolean inscricaoDeMatriculaPorTurma = false;
	private boolean inscricaoMatriculaPorTurma = false;
	private boolean visualizarNotasAlunoBoolean = false;

	private Trimestre trimestre;
	private List<Trimestre> trimestres = new ArrayList<>();
	private List<Nota> notas = new ArrayList<>();
	private Matricula matriculaSelecionada;
	private Matricula matriculaSelecionadaParaInscricao;
	private List<EstadoCivil> estadoCivils = Arrays.asList(EstadoCivil.values());
	private Matriz disciplinaDaClassse;
	private Disciplina[] disciplinaSelecionadas;
	private Integer qtdAlunoMatriculado = 0;

	private String nomeAluno;
	private String nomeAlunoDeMatriculaPorTurma;

	private boolean criterioBuscaNoAno;
	private boolean criterioBuscaNoNumeroMatricula;
	private boolean criterioBuscaNoNome;

	private Integer ano;

	@Autowired
	private MatriculaServico matriculaServico;

	@Autowired
	private AlunoServico alunoServico;
	@Autowired
	private AuthenticationContext authenticationContext;
	@Autowired
	private ClasseServico classeServico;
	@Autowired
	private TurmaServico turmaServico;
	@Autowired
	private TurnoServico turnoServico;
	@Autowired
	private TrimestreServico trimestreServico;
	@Autowired
	private MatrizServico matrizServico;
	@Autowired
	private EscolaServico escolaServico;
	@Autowired
	private NotaServico notaServico;
	@Autowired
	private GeradorDeRelatoriosServico geradorDeRelatoriosServico;
	private Integer mediaAlunoTrimetral;
	private String mediaAlunoTrimetralPorExtenso;

	public void iniciarBean() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
		Date date = Calendar.getInstance().getTime();
		ano = Integer.valueOf(dateFormat.format(date));
		matriculas = new ArrayList<>();
		pesquisa = new String();
		qtdAlunoMatriculado = 0;
		visualizarNotasAlunoBoolean = false;
	}

	// deve ser chamado no portal de aluno atraves de pritty config
	public void iniciarBeanDoc() {

		matriculas = new ArrayList<>();
		try {
			Aluno aluno = (Aluno) authenticationContext.getUsuarioLogado();
			matriculas = matriculaServico.obterMatriculasPorIdAluno(aluno.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void salvar() {
		try {
			Escola escola=authenticationContext.getFuncionarioEscolaLogada().getEscola();
			matricula.setEscola(escola);
			matricula.setEscola(escola);
			matriculaServico.salvar(matricula);
			Mensagem.mensagemInfo("Estudante alterado com sucesso");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// matricular aluno por turma
	public void salvarAlunoTurma() {
		if (matriculaPorAluno.getTurma() != null && matriculaPorAluno.isAtivo() == true) {
			Mensagem.mensagemInfo("Aviso: não pode matrícular este aluno pois já está matriculado!");
			return;
		}
		Integer inscritos = turmaSelecionada.getInscrito() + 1;
		turmaSelecionada.setInscrito(inscritos);
		turmaServico.salvar(turmaSelecionada);
		matriculaPorAluno.setAtivo(true);
		matriculaPorAluno.setTurma(turmaSelecionada);
		matriculaServico.salvar(matriculaPorAluno);
		Mensagem.mensagemAlerta("Aluno " + matriculaPorAluno.getAluno().getId() + " – "
				+ matriculaPorAluno.getAluno().getNome() + " foi matriculado com sucesso");
	}

	// matricula de alunos por turma
	public void salvarMatriculaDeAlunosPorTurma() {
		if (this.adicionarMatriculasLista == null) {
			Mensagem.mensagemInfo("Aviso: selecione um aluno para matricular!");
		} else if (this.adicionarMatriculasLista != null) {
			this.qtdAlunoMatriculado = 0;
			for (Matricula matricula : adicionarMatriculasLista) {
				matricula.setAtivo(true);
				matricula.setTurma(turmaSelecionada);
				matriculaServico.salvar(matricula);
				Mensagem.mensagemInfo("Aluno " + matricula.getAluno().getId() + " " + matricula.getAluno().getNome()
						+ ", foi matriculado com sucesso");
				this.qtdAlunoMatriculado++;
			}
			Integer inscritos = turmaSelecionada.getInscrito() + this.qtdAlunoMatriculado;
			turmaSelecionada.setInscrito(inscritos);
			turmaServico.salvar(turmaSelecionada);
			this.adicionarMatriculasLista = null;
		}

	}

	// buscar notas de aluno
	public void buscarNotasDeAluno() {
		String tipoMediatrimestre = "";
		try {

			if (matricula == null) {
			} else {
				if (trimestre == null) {

				} else if (trimestre != null) {
					this.setNotas(notaServico.obterNotaPorIdAluno(matricula.getAluno().getId(),
							matricula.getClasse().getId(), ano));
					if (trimestre.getDescricao().equals("1º Trimestre")) {
						tipoMediatrimestre = "mediaTrimestral";
					} else if (trimestre.getDescricao().equals("2º Trimestre")) {
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

	public void buscarNotasPorAluno(Matricula matricula) {
		this.visualizarNotasAlunoBoolean = true;
		this.matricula = matricula;
		notas = new ArrayList<>();
		mediaAlunoTrimetral = null;
		trimestre = new Trimestre();
		try {
			trimestres = trimestreServico.obterTodosTrimestresPorAno(matricula.getAno());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void voltarNotasPorAluno() {
		this.visualizarNotasAlunoBoolean = false;

	}

	public void buscarAlunoParaMatricula() {
		this.inscricaoMatricula = true;
		this.matriculaSelecionada = new Matricula();
		this.procuararEstudantesMatriculadoParaAltearcao = null;
		this.setNomeAluno("");
	}

	public void procurarAlunoParaMatricula() {
		this.inscricaoMatriculaPorTurma = true;
		this.matriculaSelecionada = new Matricula();
		this.procuararEstudantesMatriculadoParaAltearcao = null;
	}

	public void voltarProcurarAlunoParaMatricula() {
		this.inscricaoMatriculaPorTurma = false;
		this.procuararEstudantesMatriculadoParaAltearcao = null;
	}

	public void voltarConsulta() {
		this.inscricaoMatricula = false;
		// this.turmaSelecionada = new Turma();

	}

	public String retornarMatriculaPorAluno() {
		this.inscricaoMatricula = false;
		this.turmaSelecionada = null;
		this.turmas = null;
		this.setNomeAlunoDeMatriculaPorTurma("");
		idAluno = null;
		this.matriculaPorAluno = new Matricula();
		return "/academico/adjunto/matricula/aluno?faces-redirect=true";
	}

	public String retornarMatriculaPorTurma() {
		this.inscricaoDeMatriculaPorTurma = false;
		// this.turma=new Turma();
		this.turmas = null;

		Long idEscola = authenticationContext.getFuncionarioEscolaLogada().getEscola().getId();
		classes = escolaServico.obterClassesPorIdEscola(idEscola);
		turnos = turnoServico.listarTodosTurnosDaEscola();

		return "/academico/adjunto/matricula/turma?faces-redirect=true";
	}

	public void removerAlunoDaLista(Matricula matricula) {
		adicionarMatriculasLista.remove(matricula);

	}

	public void voltarMatricula() {
		this.matricula = new Matricula();
		this.turmaSelecionada = null;
		// Calendario calendario = authenticationContext.getCalendarioEscolar();
		// this.turmas =
		// turmaServico.obterTurmaPorClasseTurno(matricula.getClasse().getId(),
		// turma.getTurno().getId(),
		// calendario.getAno());
	}

	public void buscarAlunoSelecionado(Matricula matricula) {
		this.matricula = matricula;
		if (inscricaoMatricula != false) {
			this.matriculaPorAluno = matricula;
		}
		idAluno = this.matricula.getAluno().getId();
		this.inscricaoMatricula = false;
		this.matriculaSelecionada = null;
		this.procuararEstudantesMatriculadoParaAltearcao = null;
		this.procuararEstudantesMatriculadoParaAltearcao = null;
	}

	public void buscarAlunoPorCodigo(Long idAluno) {
		try {
			if (idAluno != null) {
				consultaPorAluno = alunoServico.obterAlunoPorId(idAluno);
				if (consultaPorAluno == null) {
					consultaPorAluno = new Aluno();
					consultaPorAluno.setNome("NÃO FOI ENCONTRADO!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscarAlunoSelecionadoDaClasseETurno(Matricula matricula) {
		this.matricula = matricula;
		this.inscricaoMatriculaPorTurma = false;
		this.matriculaSelecionada = null;
		this.procuararEstudantesMatriculadoParaAltearcao = null;
		this.setNomeAlunoDeMatriculaPorTurma("");
	}

	public String consultarMatriculasPorAluno() {

		this.consultarNomeAlunoBoolean = false;
		consultaPorAluno = new Aluno();
		listarAlunos = new ArrayList<>();
		this.pesquisarAlunoBoolean = false;
		disciplinasAluno = new ArrayList<>();
		matriculaPorAluno = null;
		// this.calendarios = null;
		return "/academico/secretario/matricula/consultar?faces-redirect=true";
	}

	public void buscarDiciplinaDoAluno() {
		this.pesquisarAlunoBoolean = true;
		matriculaPorAluno = matriculaServico.obterAlunoMatriculado(consultaPorAluno.getId(), ano);
		disciplinasAluno = notaServico.obterDisciplinaDoAlunoPorAnoAndId(consultaPorAluno.getId(), ano);
	}

	public void buscarAluno() {
		try {
			qtdAlunoMatriculado = 0;
			matriculas = matriculaServico.obterMatriculaPorAluno(pesquisa, ano);
			if (matriculas != null) {
				qtdAlunoMatriculado = matriculas.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void adicionarAlunoTurma() {

		List<Matricula> matriculas = new ArrayList<>();
		if (this.matricula.getId() == null) {
			Mensagem.mensagemAlerta("Aviso: selecione um aluno para matricular!");
		} else {
			matriculas = matriculaServico.obterMatriculasPorId(this.matricula.getId());
			if (this.adicionarMatriculasLista == null) {
				this.adicionarMatriculasLista = new ArrayList<>();
			}

			if (!this.adicionarMatriculasLista.containsAll(matriculas)) {
				this.adicionarMatriculasLista.addAll(matriculas);

			}
		}
	}

	public void buscarTurmaSelecionada(Turma turma) {
		this.turmaSelecionada = turma;
		this.nota = new Nota();
		this.matricula = new Matricula();

		disciplinaDaClassse = matrizServico.obterMatrizPorClasseCursoAno(turmaSelecionada.getClasse().getId(),
				turmaSelecionada.getTurno().getCurso(), turmaSelecionada.getAno());

	}

	public void buscarAlunoPorId() {
		Calendario calendario = authenticationContext.getCalendarioEscolar();
		if (calendario.getAno() != null) {
			if (idAluno != null)
				this.matriculaPorAluno = matriculaServico.obterAlunoMatriculado(idAluno, calendario.getAno());
		}
	}

	// Funcao que serve para selecionar turma para inscrever alunos
	public void buscarTurmaSelecionadaParaMatriculaPorTurma(Turma turma) {
		this.turmaSelecionada = turma;
		this.nota = new Nota();
		this.matricula = new Matricula();
		this.inscricaoDeMatriculaPorTurma = true;
		this.adicionarMatriculasLista = new ArrayList<>();
	}

	// Funcao que serve para sair da turma selecionada para inscrever alunos
	public void voltarTurmaSelecionadaParaMatriculaPorTurma() {
		this.turmaSelecionada = null;
		this.nota = null;
		this.inscricaoDeMatriculaPorTurma = false;
	}

	public void consultarTurma() {
		this.turmas = new ArrayList<>();
		Calendario calendario = authenticationContext.getCalendarioEscolar();
		this.turmas = turmaServico.obterTurmaPorClasseTurno(matricula.getClasse().getId(), turma.getTurno().getId(),
				calendario.getAno());
	}

	public void consultarTurnoClasse() {
		this.turmas = new ArrayList<>();
		Calendario calendario = authenticationContext.getCalendarioEscolar();
		turmas = turmaServico.obterTurmaPorClasseTurno(turma.getClasse().getId(), turma.getTurno().getId(),
				calendario.getAno());
	}

	public void buscarAlunoDaConsulta() {

		this.procuararEstudantesMatriculadoParaAltearcao = null;
		if (this.getNomeAluno() != null && this.getNomeAluno().length() < 3) {
			Mensagem.mensagemAlerta("Aviso: digite pelo meno 3 letras no campo nome");
		} else if (this.getNomeAluno().length() >= 3) {
			this.procuararEstudantesMatriculadoParaAltearcao = matriculaServico
					.obterAlunoDaEscolaPorNome(this.getNomeAluno().trim());
		}
	}

	public void buscarAlunoConsultaParaMatriculaPorTurma() {
		this.procuararEstudantesMatriculadoParaAltearcao = null;
		if (this.getNomeAlunoDeMatriculaPorTurma() != null && this.getNomeAlunoDeMatriculaPorTurma().length() < 3) {
			Mensagem.mensagemAlerta("Aviso: digite pelo meno 3 letras no campo nome");
		} else if (this.getNomeAlunoDeMatriculaPorTurma().length() >= 3) {
			this.procuararEstudantesMatriculadoParaAltearcao = matriculaServico.obterMatriculasDaClasseTrurnoAnoNome(
					turmaSelecionada.getClasse().getId(), turmaSelecionada.getTurno().getCurso(),
					this.getNomeAlunoDeMatriculaPorTurma().trim());
		}
	}

	public List<Matricula> getObterMatriculadaPorEscola() {
		return matriculaServico
				.obtereMatriculadoPorEscola(authenticationContext.getFuncionarioEscolaLogada().getEscola().getId());
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

	public List<Turno> getObterTodosTurnosDaEscola() {
		return turnoServico.listarTodosTurnosDaEscola();
	}

	public Matricula getAlunoMatriculado() {
		return matriculaServico.obterAlunoMatriculado(authenticationContext.getUsuarioLogado().getId(), 2017);
	}

	public List<Classe> getObterClasseDaEScola() {
		return classeServico.obterTodasClassesAtivas();
	}

	public List<Turma> getObterTurmasPorClasseDaEScola() {
		Calendario calendario = authenticationContext.getCalendarioEscolar();
		return turmaServico.obterTurmaPorIdClasse(1L, calendario.getAno());
	}

	public void listarEstudanteMatriculado() {
		listaEstudantesMatriculado = matriculaServico.obterEstudanteMatriculado(
				authenticationContext.getFuncionarioEscolaLogada().getEscola().getId(), 2017,
				matricula.getClasse().getId());

	}

	public void selecionarEstudanteParaInscricao(Matricula matricula) {
		this.inscricaoMatricula = true;
		this.matriculaSelecionada = matricula;
		this.matriculaSelecionadaParaInscricao = new Matricula();
	}

	public void buscarAlunoSelecionado(Aluno aluno) {
		this.consultaPorAluno = aluno;
		this.consultarNomeAlunoBoolean = false;
		disciplinasAluno = new ArrayList<>();
		matriculaPorAluno = null;
	}

	public void buscarAlunosPorNome() {
		this.listarAlunos = alunoServico.obterAlunosPorNome(consultarNomeAluno.trim().toUpperCase());
	}

	public void buscarFormPesquisaAluno() {
		this.consultarNomeAlunoBoolean = true;
		listarAlunos = new ArrayList<>();
		consultarNomeAluno = new String();
	}

	public String cancelar() {
		this.listaEstudantesMatriculado = null;
		return "/academico/director/director.jsf";
	}

	public String cancelarDoAdjunto() {
		this.listaEstudantesMatriculado = null;
		return "/academico/adjunto/index.jsf";
	}

	public void salvarInscricao() {
		matriculaSelecionadaParaInscricao.setAluno(matriculaSelecionada.getAluno());
		matriculaSelecionadaParaInscricao.setClasse(matriculaSelecionadaParaInscricao.getClasse());
		matriculaSelecionadaParaInscricao.setAno(2017);
		matriculaSelecionadaParaInscricao.setNumero("715729261");
		matriculaSelecionadaParaInscricao.setEscola(authenticationContext.getFuncionarioEscolaLogada().getEscola());

		matriculaServico.salvar(matriculaSelecionadaParaInscricao);
		Mensagem.mensagemInfo("Estudante Inscrito com sucesso");
	}

	public void cancelarInscricao() {
		// this.inscricaoMatricula = false;
		this.matriculaSelecionada = null;
		// listaEstudantesMatriculado =new
		// this.matriculaSelecionadaParaInscricao = new Matricula();
		this.inscricaoMatricula = false;
	}

	public void voltar() {
		this.inscricaoMatricula = false;
		this.consultarNomeAlunoBoolean = false;
		this.matriculaSelecionadaParaInscricao = null;
	}

	public void buscarEstudantesParaAlteracao() {

		// System.out.println("\n\n\nid Da Escola:"
		// +authenticationContext.getUsuarioEscolaLogada().getId());
		procuararEstudantesMatriculadoParaAltearcao = matriculaServico.obterEstudanteMatriculado(
				authenticationContext.getFuncionarioEscolaLogada().getEscola().getId(), matricula.getAno(),
				matricula.getClasse().getId());
		// System.out.println("\n\n\nid Da Escola:"
		// +procuararEstudantesMatriculadoParaAltearcao.get(0).getAluno().getNome());

	}

	public void emitirRelatórioDeAlunosPorSituacao(Turma turma) {
		String caminho = "/academico/relatorio/aluno/lista_alunos_por_situacao.jasper", filename = "doc.pdf";
		Map<String, Object> parametro = new HashMap<>();
		parametro.put("idTurma", turma.getId());
		try {
			geradorDeRelatoriosServico.geraPdf(caminho, parametro, filename);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void exportarParaPDF() {
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

	public List<Matricula> getObterListamatricula() {
		return matriculaServico.listarTodas();
	}

	public Matricula getMatricula() {
		return matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

	public List<Matricula> getMatriculas() {
		return matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	public List<Matricula> getListaEstudantesMatriculado() {
		return listaEstudantesMatriculado;
	}

	public void setListaEstudantesMatriculado(List<Matricula> listaEstudantesMatriculado) {
		this.listaEstudantesMatriculado = listaEstudantesMatriculado;
	}

	public boolean isInscricaoMatricula() {
		return inscricaoMatricula;
	}

	public void setInscricaoMatricula(boolean inscricaoMatricula) {
		this.inscricaoMatricula = inscricaoMatricula;
	}

	public Matricula getMatriculaSelecionada() {
		return matriculaSelecionada;
	}

	public void setMatriculaSelecionada(Matricula matriculaSelecionada) {
		this.matriculaSelecionada = matriculaSelecionada;
	}

	public Matricula getMatriculaSelecionadaParaInscricao() {
		return matriculaSelecionadaParaInscricao;
	}

	public void setMatriculaSelecionadaParaInscricao(Matricula matriculaSelecionadaParaInscricao) {
		this.matriculaSelecionadaParaInscricao = matriculaSelecionadaParaInscricao;
	}

	public boolean isCriterioBuscaNoAno() {
		return criterioBuscaNoAno;
	}

	public void setCriterioBuscaNoAno(boolean criterioBuscaNoAno) {
		this.criterioBuscaNoAno = criterioBuscaNoAno;
	}

	public boolean isCriterioBuscaNoNumeroMatricula() {
		return criterioBuscaNoNumeroMatricula;
	}

	public void setCriterioBuscaNoNumeroMatricula(boolean criterioBuscaNoNumeroMatricula) {
		this.criterioBuscaNoNumeroMatricula = criterioBuscaNoNumeroMatricula;
	}

	public boolean isCriterioBuscaNoNome() {
		return criterioBuscaNoNome;
	}

	public void setCriterioBuscaNoNome(boolean criterioBuscaNoNome) {
		this.criterioBuscaNoNome = criterioBuscaNoNome;
	}

	public List<Matricula> getProcuararEstudantesMatriculadoParaAltearcao() {
		return procuararEstudantesMatriculadoParaAltearcao;
	}

	public void setProcuararEstudantesMatriculadoParaAltearcao(
			List<Matricula> procuararEstudantesMatriculadoParaAltearcao) {
		this.procuararEstudantesMatriculadoParaAltearcao = procuararEstudantesMatriculadoParaAltearcao;
	}

	public List<EstadoCivil> getEstadoCivils() {
		return estadoCivils;
	}

	public void setEstadoCivils(List<EstadoCivil> estadoCivils) {
		this.estadoCivils = estadoCivils;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public Turma getTurmaSelecionada() {
		return turmaSelecionada;
	}

	public void setTurmaSelecionada(Turma turmaSelecionada) {
		this.turmaSelecionada = turmaSelecionada;
	}

	public Matriz getDisciplinaDaClassse() {
		return disciplinaDaClassse;
	}

	public void setDisciplinaDaClassse(Matriz disciplinaDaClassse) {
		this.disciplinaDaClassse = disciplinaDaClassse;
	}

	public Disciplina[] getDisciplinaSelecionadas() {
		return disciplinaSelecionadas;
	}

	public void setDisciplinaSelecionadas(Disciplina[] disciplinaSelecionadas) {
		this.disciplinaSelecionadas = disciplinaSelecionadas;
	}

	public Nota getNota() {
		return nota;
	}

	public void setNota(Nota nota) {
		this.nota = nota;
	}

	public boolean isInscricaoMatriculaPorTurma() {
		return inscricaoMatriculaPorTurma;
	}

	public void setInscricaoMatriculaPorTurma(boolean inscricaoMatriculaPorTurma) {
		this.inscricaoMatriculaPorTurma = inscricaoMatriculaPorTurma;
	}

	public List<Matricula> getAdicionarMatriculasLista() {
		return adicionarMatriculasLista;
	}

	public void setAdicionarMatriculasLista(List<Matricula> adicionarMatriculasLista) {
		this.adicionarMatriculasLista = adicionarMatriculasLista;
	}

	public Integer getQtdAlunoMatriculado() {
		return qtdAlunoMatriculado;
	}

	public void setQtdAlunoMatriculado(Integer qtdAlunoMatriculado) {
		this.qtdAlunoMatriculado = qtdAlunoMatriculado;
	}

	public boolean isInscricaoDeMatriculaPorTurma() {
		return inscricaoDeMatriculaPorTurma;
	}

	public void setInscricaoDeMatriculaPorTurma(boolean inscricaoDeMatriculaPorTurma) {
		this.inscricaoDeMatriculaPorTurma = inscricaoDeMatriculaPorTurma;
	}

	public Matricula getMatriculaPorAluno() {
		return matriculaPorAluno;
	}

	public void setMatriculaPorAluno(Matricula matriculaPorAluno) {
		this.matriculaPorAluno = matriculaPorAluno;
	}

	public String getNomeAluno() {
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public String getNomeAlunoDeMatriculaPorTurma() {
		return nomeAlunoDeMatriculaPorTurma;
	}

	public void setNomeAlunoDeMatriculaPorTurma(String nomeAlunoDeMatriculaPorTurma) {
		this.nomeAlunoDeMatriculaPorTurma = nomeAlunoDeMatriculaPorTurma;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public boolean isConsultarNomeAlunoBoolean() {
		return consultarNomeAlunoBoolean;
	}

	public void setConsultarNomeAlunoBoolean(boolean consultarNomeAlunoBoolean) {
		this.consultarNomeAlunoBoolean = consultarNomeAlunoBoolean;
	}

	public String getConsultarNomeAluno() {
		return consultarNomeAluno;
	}

	public void setConsultarNomeAluno(String consultarNomeAluno) {
		this.consultarNomeAluno = consultarNomeAluno;
	}

	public Aluno getConsultaPorAluno() {
		return consultaPorAluno;
	}

	public void setConsultaPorAluno(Aluno consultaPorAluno) {
		this.consultaPorAluno = consultaPorAluno;
	}

	public List<Aluno> getListarAlunos() {
		return listarAlunos;
	}

	public void setListarAlunos(List<Aluno> listarAlunos) {
		this.listarAlunos = listarAlunos;
	}

	public boolean isPesquisarAlunoBoolean() {
		return pesquisarAlunoBoolean;
	}

	public void setPesquisarAlunoBoolean(boolean pesquisarAlunoBoolean) {
		this.pesquisarAlunoBoolean = pesquisarAlunoBoolean;
	}

	public List<Nota> getDisciplinasAluno() {
		return disciplinasAluno;
	}

	public void setDisciplinasAluno(List<Nota> disciplinasAluno) {
		this.disciplinasAluno = disciplinasAluno;
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

	public Long getIdAluno() {
		return idAluno;
	}

	public void setIdAluno(Long idAluno) {
		this.idAluno = idAluno;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public boolean isVisualizarNotasAlunoBoolean() {
		return visualizarNotasAlunoBoolean;
	}

	public void setVisualizarNotasAlunoBoolean(boolean visualizarNotasAlunoBoolean) {
		this.visualizarNotasAlunoBoolean = visualizarNotasAlunoBoolean;
	}

	public Trimestre getTrimestre() {
		return trimestre;
	}

	public void setTrimestre(Trimestre trimestre) {
		this.trimestre = trimestre;
	}

	public List<Trimestre> getTrimestres() {
		return trimestres;
	}

	public void setTrimestres(List<Trimestre> trimestres) {
		this.trimestres = trimestres;
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

	public String getMediaAlunoTrimetralPorExtenso() {
		return mediaAlunoTrimetralPorExtenso;
	}

	public void setMediaAlunoTrimetralPorExtenso(String mediaAlunoTrimetralPorExtenso) {
		this.mediaAlunoTrimetralPorExtenso = mediaAlunoTrimetralPorExtenso;
	}

}
