// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.enumerado.TipoCurso;
import com.mz.sistema.gestao.escolar.enumerado.TipoTurno;
import com.mz.sistema.gestao.escolar.modelo.Calendario;
import com.mz.sistema.gestao.escolar.modelo.Classe;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.modelo.HorarioAula;
import com.mz.sistema.gestao.escolar.modelo.Matricula;
import com.mz.sistema.gestao.escolar.modelo.Matriz;
import com.mz.sistema.gestao.escolar.modelo.Sala;
import com.mz.sistema.gestao.escolar.modelo.Turma;
import com.mz.sistema.gestao.escolar.modelo.Turno;
import com.mz.sistema.gestao.escolar.modelo.Usuario;
import com.mz.sistema.gestao.escolar.servico.CalendarioServico;
import com.mz.sistema.gestao.escolar.servico.ClasseServico;
import com.mz.sistema.gestao.escolar.servico.EscolaServico;
import com.mz.sistema.gestao.escolar.servico.GeradorDeRelatoriosServico;
import com.mz.sistema.gestao.escolar.servico.HorarioAulaServico;
import com.mz.sistema.gestao.escolar.servico.MatriculaServico;
import com.mz.sistema.gestao.escolar.servico.MatrizServico;
import com.mz.sistema.gestao.escolar.servico.SalaServico;
import com.mz.sistema.gestao.escolar.servico.TurmaServico;
import com.mz.sistema.gestao.escolar.servico.TurnoServico;
import com.mz.sistema.gestao.escolar.util.DataUtils;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Named
@SessionScope
@Controller
public class TurmaBean {
	private Turma turma = new Turma();
	private Sala sala = new Sala();
	private Turma turmaSelecionada;
	private List<Turma> turmas = new ArrayList<>();
	private List<Turno> turnos = new ArrayList<>();
	private Turma turmaSelecionadaExclusao;
	private List<Matricula> matriculas = new ArrayList<>();

	public Turma getTurmaSelecionadaExclusao() {
		return turmaSelecionadaExclusao;
	}

	public void setTurmaSelecionadaExclusao(Turma turmaSelecionadaExclusao) {
		this.turmaSelecionadaExclusao = turmaSelecionadaExclusao;
	}

	private List<Sala> salas = new ArrayList<>();
	private List<TipoTurno> tipoTurnos = Arrays.asList(TipoTurno.values());
	private List<TipoCurso> cursos = Arrays.asList(TipoCurso.DIURNO, TipoCurso.NOTURNO);
	private Turma turmaSalva;
	private boolean procurarPorClasse;
	private boolean procurarPorTurno;
	private boolean procurarPorNomeTurma;
	private boolean turmaBoolean = false;
	private boolean procurarPorAno = true;
	private boolean procurarSalaParaCadastrarNaTurma = false;
	private List<HorarioAula> horarioAulas;
	private String salaPesquisada;
	private String codigoSala;
	private Integer quantidadeTurmas;
	private String prefixoDaTurma;

	// So pode ser matriculada o aluno na turma, aquele que esta matriculado na
	// classe
	// Matricular aluno na turma

	private List<Matricula> matriculasBanco;
	private List<Matricula> matriculasTabela;

	public List<Matriz> getMatrizes() {
		return matrizes;
	}

	public void setMatrizes(List<Matriz> matrizes) {
		this.matrizes = matrizes;
	}

	private List<Matriz> matrizes;
	@Autowired
	private TurmaServico turmaServico;
	@Autowired
	private ClasseServico classeServico;
	@Autowired
	private AuthenticationContext authenticationContext;
	@Autowired
	private MatriculaServico matriculaServico;
	@Autowired
	private TurnoServico turnoServico;
	@Autowired
	private HorarioAulaServico horarioAulaServico;

	@Autowired
	private SalaServico salaServico;
	@Autowired
	private EscolaServico escolaServico;
	@Autowired
	private MatrizServico matrizServico;
	@Autowired
	private GeradorDeRelatoriosServico geradorDeRelatoriosServico;
	
	@Autowired
	private CalendarioServico calendarioServico;

	public void iniciarBean() {
		turma = new Turma();
		turmas = new ArrayList<>();
		this.turmaSelecionada = null;
		quantidadeTurmas = 0;
		turmaBoolean = false;
		buscarEscolaECalendario();
	}

	private void buscarEscolaECalendario() {
		try {
			Calendario calendario = authenticationContext.getCalendarioEscolar();
			if (calendario != null) {
				turma.setAno(calendario.getAno());
				if (turmaSelecionada != null) {
					turmaSelecionada.setAno(calendario.getAno());
				}
			}
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();
			if (turmaSelecionada != null)
				turmaSelecionada.setEscola(escola);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void salvar(Turma turma) {
		horarioAulas = horarioAulaServico.obterHorarioAulaPorEscolaTurno(turma.getTurno().getId());
		if (horarioAulas == null) {
			Mensagem.mensagemInfo("Não pode Cadastar a Turma, pois existe dependencia com horório de Aula!");
			return;
		}
		if (this.getAlunoMatriculadoNaClasse() == 0) {
			Mensagem.mensagemInfo("Não pode Cadastar a Turma, pois existe alunos matriculado nesta classe!");
			return;
		}
		this.turmaSalva = turmaServico.salvarRetornarTurma(turma);
		this.turma = null;
		matriculasBanco = new ArrayList<>();
		matriculasBanco = matriculaServico.obterMatriculasDaclasse(turmaSalva.getClasse().getId(),
				turmaSalva.getTurno().getCurso());
		matriculasTabela = new ArrayList<>();

	}

	public void salvarTurma() {

		try {

			Turma turmaExistente = turmaServico.turmaExistente(turmaSelecionada.getAno(),
					turmaSelecionada.getTurno().getId(), turmaSelecionada.getSala().getId(),
					turmaSelecionada.getEscola().getId());
			if (turmaExistente != null && turmaExistente.getId() != turmaSelecionada.getId()) {
				Mensagem.mensagemErro("ERRO: Não pode cadastrar essa turma nesta sala pois, a turma "
						+ turmaExistente.getClasse().getSigla() + "ª" + turmaExistente.getDescricao()
						+ " de período da " + turmaExistente.getTurno().getDescricao().getLabel().toLowerCase() + " de sala nº "
						+ turmaExistente.getSala().getNumero() + " é que está disponível.");
				return;
			}
			Turma turmaExistente2 = turmaServico.obterTurmaExistentePorDscricao(turmaSelecionada.getDescricao(),
					turmaSelecionada.getClasse().getId(), turmaSelecionada.getTurno().getCurso(),
					turmaSelecionada.getAno(), turmaSelecionada.getEscola().getId());
			if (turmaExistente2 != null && turmaExistente2.getId() != turmaSelecionada.getId()) {
				Mensagem.mensagemErro("ERRO: Já existe uma turma com esses dados cadastrada no sistema!");
				return;
			}
			if (turmaSelecionada.getRestanteVaga() == null) {
				turmaSelecionada.setRestanteVaga(turmaSelecionada.getCapacidade());
			}
			turma=turmaSelecionada;
			turmaServico.salvar(turmaSelecionada);

			Mensagem.mensagemInfo("AVISO: Turma cadastrada com sucesso!");
			turmaSelecionada = null;
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void prepararExclusao(Turma turma) {
		this.turmaSelecionadaExclusao = turma;
	}

	public void excluir() {
		try {

			turmaServico.excluir(this.turmaSelecionadaExclusao);
			listarTurmas();
			Mensagem.mensagemInfo("Aviso: turma foi excluida com sucesso!");

		} catch (Exception e) {
			Mensagem.mensagemErro("ERRO: não foi excluida esta turma pois exite uma dependência!");
			e.printStackTrace();
		}
	}

	public void buscarMatrizesCurriculares() {

		try {
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();
			if (turmaSelecionada == null) {
				if (turma.getCurso() != null && turma.getClasse().getId() != 0 && escola.getId() != null) {
					if (turma.getClasse().getCiclo().equals("2º CICLO")) {
						matrizes = matrizServico.obterMatrizPorClasseCursoAtivo(turma.getClasse().getId(),
								turma.getCurso(), escola.getId());

					}
				}
			} else if (turmaSelecionada != null)
				if (turmaSelecionada.getCurso() != null && turmaSelecionada.getClasse().getId() != 0
						&& escola.getId() != null) {
					if (turmaSelecionada.getClasse().getCiclo().equals("2º CICLO")) {
						matrizes = matrizServico.obterMatrizPorClasseCursoAtivo(turmaSelecionada.getClasse().getId(),
								turmaSelecionada.getCurso(), escola.getId());
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscarTurnos() {

		try {

			if (turmaSelecionada.getCurso() != null) {

				// devo pesquisar a busca de turnos numa escola
				turnos = turnoServico.obterTurnoPorCurso(turmaSelecionada.getCurso());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void salvarMatricula() {

		if (matriculasTabela.isEmpty()) {
			Mensagem.mensagemInfo("Selecione Estudante para matracular");
			return;
		}
		Integer quntidadeMatriculado = 0;
		this.turmaSalva.setAtivo(true);
		Matricula m = new Matricula();
		// Nota nota=new Nota();
		for (Matricula matricula : matriculasTabela) {
			m = matriculaServico.obterMatriculaPorId(matricula.getId());
			m.setAtivo(true);
			m.setTurma(this.turmaSalva);
			quntidadeMatriculado++;
			this.turmaSalva.setInscrito(quntidadeMatriculado);
			matriculaServico.salvar(m);
		}
		turmaServico.salvar(turmaSalva);
		Mensagem.mensagemInfo("Alunos Alocado com sucesso");

	}

	public void buscarTurmaSelecionada(Turma turma) {
		this.turmaSelecionada = turma;
		turmaBoolean = true;
		buscarTurnos();

	}

	// nova turma
	public void novaTurma() {
		this.turmaSelecionada = new Turma();
		buscarEscolaECalendario();
		turmaBoolean = true;
	}

	public void buscarTurmaPorClasseOuTurno() {
		turmas = new ArrayList<>();
		if (procurarPorClasse == true && procurarPorTurno == true && procurarPorNomeTurma == true) {
			if (turma.getTurno() == null) {
				Mensagem.mensagemErro("Selecione o turno!");
			} else if (turma.getDescricao() == null) {
				Mensagem.mensagemErro("Preenche o campo nome da turma!");
			} else if (turma.getClasse() == null) {
				Mensagem.mensagemErro("Selecione a classe!");
			} else if (authenticationContext.getCalendarioEscolar() == null) {
			} else {
				turmas = turmaServico.obterTurmaPorClasseTurnoDescricao(turma.getClasse().getId(),
						turma.getTurno().getId(), turma.getDescricao(),
						authenticationContext.getCalendarioEscolar().getAno());
			}

		} else if (procurarPorClasse == true && procurarPorTurno == true) {
			if (turma.getTurno() == null) {
				Mensagem.mensagemErro("Selecione o turno!");
			} else if (turma.getClasse() == null) {
				Mensagem.mensagemErro("Selecione a classe!");
			} else if (authenticationContext.getCalendarioEscolar() == null) {
			} else {
				turmas = turmaServico.obterTurmaPorClasseTurno(turma.getClasse().getId(), turma.getTurno().getId(),
						authenticationContext.getCalendarioEscolar().getAno());
			}

		} else if (procurarPorClasse == true && procurarPorNomeTurma == true) {
			if (turma.getDescricao() == null) {
				Mensagem.mensagemErro("Preenche o campo nome da turma!");
			} else if (turma.getClasse() == null) {
				Mensagem.mensagemErro("Selecione a classe!");
			} else if (authenticationContext.getCalendarioEscolar() == null) {
			} else {
				turmas = turmaServico.obterTurmaPorClasseDescricao(turma.getClasse().getId(), turma.getDescricao(),
						authenticationContext.getCalendarioEscolar().getAno());
			}

		} else if (procurarPorTurno == true && procurarPorNomeTurma == true) {
			if (turma.getTurno() == null) {
				Mensagem.mensagemErro("Selecione o turno!");
			} else if (turma.getDescricao() == null) {
				Mensagem.mensagemErro("Preenche o campo nome da turma!");
			} else if (authenticationContext.getCalendarioEscolar() == null) {
			} else {
				turmas = turmaServico.obterTurmaPorDescricaoTurno(turma.getDescricao(), turma.getTurno().getId(),
						authenticationContext.getCalendarioEscolar().getAno());
			}

		} else if (procurarPorClasse == true) {
			if (turma.getClasse() == null) {
				Mensagem.mensagemErro("Selecione a classe!");
			} else if (authenticationContext.getCalendarioEscolar() == null) {
			} else {
				turmas = turmaServico.obterTurmaPorIdClasse(turma.getClasse().getId(),
						authenticationContext.getCalendarioEscolar().getAno());
			}

		} else if (procurarPorTurno == true) {
			if (turma.getTurno() == null) {
				Mensagem.mensagemErro("Selecione o  turno!");
			} else if (authenticationContext.getCalendarioEscolar() == null) {
			} else {
				turmas = turmaServico.obterTurmaPorIdTurno(turma.getTurno().getId(),
						authenticationContext.getCalendarioEscolar().getAno());
			}
		} else if (procurarPorNomeTurma == true) {
			if (turma.getDescricao() == null) {
				Mensagem.mensagemErro("preenche o campo nome da turma!");
			} else if (authenticationContext.getCalendarioEscolar() == null) {
			} else {
				turmas = turmaServico.obterTurmaPorDescricao(turma.getDescricao(),
						authenticationContext.getCalendarioEscolar().getAno());
			}
		}

	}

	public void listarTurmas() {
		turmas = new ArrayList<>();
		quantidadeTurmas = 0;
		try {
			Escola escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();

			if (turma.getClasse().getCiclo().equals("2º CICLO")) {
				turmas = turmaServico.obterTurmasPorClasseAreaCurso(turma.getClasse().getId(), turma.getArea(),
						turma.getCurso(), turma.getAno(), escola.getId());
			} else if (turma.getClasse().getCiclo().equals("1º CICLO")) {
				turmas = turmaServico.obterTurmasPorClasseCurso(turma.getClasse().getId(), turma.getCurso(),
						turma.getAno(), escola.getId());
			}

			if (turmas != null) {
				quantidadeTurmas = turmas.size();
			}
		} catch (Exception e) {

		}
	}

	public void buscarSala() {
		this.procurarSalaParaCadastrarNaTurma = true;
	}

	public void emitirDisciplinasLecionadasPeloProfessor() {
		try {

			String caminho = "/academico/relatorio/horario/minhas_turmas.jasper";
			Usuario professor = authenticationContext.getUsuarioLogado();
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();
			Calendario calendario = calendarioServico.obterCalendarioVigente();
			
			Map<String, Object> parametro = new HashMap<>();
			
			parametro.put("idProfessor", professor.getId());
			parametro.put("idEscola", escola.getId());
			parametro.put("ano", calendario.getAno());
			geradorDeRelatoriosServico.geraPdf(caminho, parametro, "Minhas turmas.pdf");
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void procularaSala() {
		try {
			Escola escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();
			this.salas = salaServico.obterSalaPorEscolaPorPesquisa(salaPesquisada.trim().toUpperCase(), escola.getId());
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void procularaSalaPorCodingo() {
		String codigo = new String();
		if (this.sala.getCodigo().equals(null)) {
			sala.setCodigo(null);
		} else if (!this.sala.getCodigo().equals(null)) {
			codigo = this.sala.getCodigo();
			this.sala = salaServico.obterSalaPorEscolaPorCodigoANDSala(this.sala.getCodigo().trim().toUpperCase());

			if (this.sala == null) {
				this.sala = new Sala();
				this.sala.setCodigo(codigo);
				codigo = null;
				this.sala.setDescricao(null);
			} else if (this.sala != null) {
				this.sala.setCodigo(codigo);
				codigo = null;

			}
		}
	}

	public void selecionarSala(Sala sala) {
		this.sala = sala;
		this.procurarSalaParaCadastrarNaTurma = false;
	}

	public void voltar(Turma turma) {
		this.turma = turma;
		this.turmaSalva = null;
		this.matriculas = null;
	}

	public void voltar() {
		this.procurarSalaParaCadastrarNaTurma = false;
		this.turmaSelecionada = null;
		turmaBoolean = false;
		listarTurmas();
	}

	public String cancelar() {
		this.turma = null;
		this.turma = new Turma();
		this.turmaSalva = null;
		this.matriculas = null;
		this.turmas = null;
		return "/academico/director/index?faces-redirect=true";
	}

	public List<Classe> getObterClasseDaEScola() {
		try {
			List<Classe> classes = null;
			Long idEscola = authenticationContext.getFuncionarioEscolaLogada().getEscola().getId();
			classes = escolaServico.obterClassesPorIdEscola(idEscola);
			return classes;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new ArrayList<>();
	}

	public List<Sala> getObterSalaDaEScola() {
		try {
			return salaServico.obterSalaPorEscolaAtivas();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new ArrayList<>();
	}

	public Classe getObterClasseDaEscolaPorId() {
		Classe classe = null;
		if (turmaSalva.getClasse() == null) {
		} else
			classe = classeServico.obterClassePorId(turmaSalva.getClasse().getId());
		return classe;
	}

	public Integer getObterQtdAlunosSelecionado() {

		Integer quantidade = 0;
		if (!matriculasTabela.isEmpty())
			for (Matricula matricula : matriculasTabela) {
				quantidade++;
				matricula.isRepetente();
			}
		return quantidade;
	}

	public Integer getObterQtdAlunosBanco() {

		Integer quantidade = 0;
		for (Matricula matricula : matriculasBanco) {
			quantidade++;
			matricula.isRepetente();
		}
		return quantidade;
	}

	public List<Matricula> getObterMatriculasDaclasse() {
		// buscarTodosAlunosTabelaEsquerda=
		return matriculaServico.obterMatriculasDaclasse(turmaSalva.getClasse().getId(),
				turmaSalva.getTurno().getCurso());
	}

	public Long getAlunoMatriculadoNaClasse() {
		Long qtdMatricula = 0L;
		if (turma != null) {
			if (turma.getClasse() != null) {
				qtdMatricula = matriculaServico.totalEstudanteMatriculadoClasse(
						authenticationContext.getFuncionarioEscolaLogada().getEscola().getId(),
						turma.getClasse().getId());
			}
		} else if ((turmaSalva != null)) {
			if (turmaSalva.getClasse() != null) {
				qtdMatricula = matriculaServico.totalEstudanteMatriculadoClasse(
						authenticationContext.getFuncionarioEscolaLogada().getEscola().getId(),
						turmaSalva.getClasse().getId());
			}
		}
		if (!qtdMatricula.equals(0)) {
			return qtdMatricula;
		}
		return 0l;
	}

	public List<Turno> getListaTurnoEscola() {
		List<Turno> turnosEscola = turnoServico.listarTodosTurnosDaEscola();
		return turnosEscola;
	}

	public List<Turno> getObterTurnoPorCurso() {
		try {
			List<Turno> turnos = null;
			if (turma.getTurno() != null) {

				if (turma.getTurno().getCurso() == null) {
				} else if (turma.getTurno().getCurso() != null) {
					turnos = turnoServico.obterTurnoPorCurso(turma.getTurno().getCurso());
				} else if (turmaSelecionada.getTurno().getCurso() == null) {
				} else if (turmaSelecionada.getTurno().getCurso() != null) {
					if (turmaSelecionada.getTurno() != null) {
						turnos = turnoServico.obterTurnoPorCurso(turmaSelecionada.getTurno().getCurso());
					}
				}
			}

			return turnos;
		} catch (Exception e) {

		}
		return new ArrayList<>();
	}

	public Turno getObterTurnoPorId() {
		Turno turno = null;
		if (turma != null) {
			if (turma.getTurno() != null) {
				turno = turnoServico.obterTurnoPorId(turma.getTurno().getId());
			}
		} else if (turmaSalva != null) {
			if (turmaSalva.getTurno() != null) {
				turno = turnoServico.obterTurnoPorId(turmaSalva.getTurno().getId());
			}
		} else if (turmaSelecionada != null) {
			if (turmaSelecionada.getTurno() != null) {
				turno = turnoServico.obterTurnoPorId(turmaSelecionada.getTurno().getId());
			}
		}
		return turno;
	}

	public void buscartodasMatriculasDireito() {
		matriculasBanco = matriculaServico.obterMatriculasDaclasse(turmaSalva.getClasse().getId(),
				turmaSalva.getTurno().getCurso());
		matriculasTabela.clear();
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

	public List<TipoTurno> getTipoTurnos() {
		return tipoTurnos;
	}

	public void setTipoTurnos(List<TipoTurno> tipoTurnos) {
		this.tipoTurnos = tipoTurnos;
	}

	public List<TipoCurso> getCursos() {
		return cursos;
	}

	public void setCursos(List<TipoCurso> cursos) {
		this.cursos = cursos;
	}

	public Turma getTurmaSalva() {
		return turmaSalva;
	}

	public void setTurmaSalva(Turma turmaSalva) {
		this.turmaSalva = turmaSalva;
	}

	public List<Matricula> getMatriculasBanco() {
		return matriculasBanco;
	}

	public void setMatriculasBanco(List<Matricula> matriculasBanco) {
		this.matriculasBanco = matriculasBanco;
	}

	public List<Matricula> getMatriculasTabela() {
		return matriculasTabela;
	}

	public void setMatriculasTabela(List<Matricula> matriculasTabela) {
		this.matriculasTabela = matriculasTabela;
	}

	public MatriculaServico getMatriculaServico() {
		return matriculaServico;
	}

	public void setMatriculaServico(MatriculaServico matriculaServico) {
		this.matriculaServico = matriculaServico;
	}

	public List<Matricula> getMatriculas() {
		return matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	public boolean isProcurarPorClasse() {
		return procurarPorClasse;
	}

	public void setProcurarPorClasse(boolean procurarPorClasse) {
		this.procurarPorClasse = procurarPorClasse;
	}

	public boolean isProcurarPorTurno() {
		return procurarPorTurno;
	}

	public void setProcurarPorTurno(boolean procurarPorTurno) {
		this.procurarPorTurno = procurarPorTurno;
	}

	public boolean isProcurarPorNomeTurma() {
		return procurarPorNomeTurma;
	}

	public void setProcurarPorNomeTurma(boolean procurarPorNomeTurma) {
		this.procurarPorNomeTurma = procurarPorNomeTurma;
	}

	public boolean isProcurarPorAno() {
		return procurarPorAno;
	}

	public void setProcurarPorAno(boolean procurarPorAno) {
		this.procurarPorAno = procurarPorAno;
	}

	public Turma getTurmaSelecionada() {
		return turmaSelecionada;
	}

	public void setTurmaSelecionada(Turma turmaSelecionada) {
		this.turmaSelecionada = turmaSelecionada;
	}

	public List<HorarioAula> getHorarioAulas() {
		return horarioAulas;
	}

	public void setHorarioAulas(List<HorarioAula> horarioAulas) {
		this.horarioAulas = horarioAulas;
	}

	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	public boolean isProcurarSalaParaCadastrarNaTurma() {
		return procurarSalaParaCadastrarNaTurma;
	}

	public void setProcurarSalaParaCadastrarNaTurma(boolean procurarSalaParaCadastrarNaTurma) {
		this.procurarSalaParaCadastrarNaTurma = procurarSalaParaCadastrarNaTurma;
	}

	public List<Sala> getSalas() {
		return salas;
	}

	public void setSalas(List<Sala> salas) {
		this.salas = salas;
	}

	public String getSalaPesquisada() {
		return salaPesquisada;
	}

	public void setSalaPesquisada(String salaPesquisada) {
		this.salaPesquisada = salaPesquisada;
	}

	public String getCodigoSala() {
		return codigoSala;
	}

	public void setCodigoSala(String codigoSala) {
		this.codigoSala = codigoSala;
	}

	public Integer getQuantidadeTurmas() {
		return quantidadeTurmas;
	}

	public void setQuantidadeTurmas(Integer quantidadeTurmas) {
		this.quantidadeTurmas = quantidadeTurmas;
	}

	public String getPrefixoDaTurma() {
		return prefixoDaTurma;
	}

	public void setPrefixoDaTurma(String prefixoDaTurma) {
		this.prefixoDaTurma = prefixoDaTurma;
	}

	public List<Turno> getTurnos() {
		return turnos;
	}

	public void setTurnos(List<Turno> turnos) {
		this.turnos = turnos;
	}

	public boolean isTurmaBoolean() {
		return turmaBoolean;
	}

	public void setTurmaBoolean(boolean turmaBoolean) {
		this.turmaBoolean = turmaBoolean;
	}

}
