// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.bean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.chave.composta.ProfessorTurmaId;
import com.mz.sistema.gestao.escolar.modelo.Calendario;
import com.mz.sistema.gestao.escolar.modelo.DiaSemana;
import com.mz.sistema.gestao.escolar.modelo.DisciplinaClasse;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.modelo.Horario;
import com.mz.sistema.gestao.escolar.modelo.HorarioAula;
import com.mz.sistema.gestao.escolar.modelo.Matriz;
import com.mz.sistema.gestao.escolar.modelo.ProfessorTurma;
import com.mz.sistema.gestao.escolar.modelo.Turma;
import com.mz.sistema.gestao.escolar.modelo.Turno;
import com.mz.sistema.gestao.escolar.servico.DiaSemanaServico;
import com.mz.sistema.gestao.escolar.servico.DisciplinaClasseServico;
import com.mz.sistema.gestao.escolar.servico.FuncionarioEscolaServico;
import com.mz.sistema.gestao.escolar.servico.GeradorDeRelatoriosServico;
import com.mz.sistema.gestao.escolar.servico.HorarioAulaServico;
import com.mz.sistema.gestao.escolar.servico.HorarioServico;
import com.mz.sistema.gestao.escolar.servico.MatrizServico;
import com.mz.sistema.gestao.escolar.servico.ProfessorTurmaServico;
import com.mz.sistema.gestao.escolar.servico.TurmaServico;
import com.mz.sistema.gestao.escolar.servico.TurnoServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;
import com.mz.sistema.gestao.escolar.util.TipoLetra;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrintManager;

@Controller
@Named
@SessionScope
public class ProfessorTurmaBean {
	private ProfessorTurma professorTurma = new ProfessorTurma();
	private Funcionario funcionario = new Funcionario();
	private List<Funcionario> funcionarios = new ArrayList<>();
	private List<FuncionarioEscola> funcionariosEscola = new ArrayList<>();
	private List<ProfessorTurma> professorTurmas = new ArrayList<>();
	private List<HorarioAula> horarioAulas = new ArrayList<>();
	private List<Horario> horariosProfessor = new ArrayList<>();
	private List<ProfessorTurma> turmasProfessor;

	public ProfessorTurma professorTurmaSelecionada;
	public boolean disciplinaTurmaSelecionada = false;
	private boolean buscarProfessorBoolean = false;
	private boolean virificarDisciplinaProfessorBoolean = false;
	private boolean elecionadaTurnoExtra = false;
	private Turma turma;
	private String pesquisa;
	private String professor;
	private List<Matriz> matrizes;
	private List<Turma> turmas;
	private Matriz matriz;
	private Matriz matrizSelecionada;
	private Turma turmaSelecionada;
	private Turno turnoSelecionado;
	private DisciplinaClasse disciplinaClasse;

	@Autowired
	private MatrizServico matrizServico;
	@Autowired
	private DiaSemanaServico diaSemanaServico;

	@Autowired
	private HorarioServico horarioServico;
	@Autowired
	private HorarioAulaServico horarioAulaServico;
	@Autowired
	private DisciplinaClasseServico disciplinaClasseServico;
	@Autowired
	private TurmaServico turmaServico;
	@Autowired
	private TurnoServico turnoServico;
	@Autowired
	private ProfessorTurmaServico professorTurmaServico;
	@Autowired
	private FuncionarioEscolaServico funcionarioEscolaServico;
	@Autowired
	private AuthenticationContext authenticationContext;
	@Autowired
	private NotaBean notaBean;

	@Autowired
	private GeradorDeRelatoriosServico geradorDeRelatoriosServico;

	public void iniciarBean() {
		try {
			this.turmasProfessor = null;
			notaBean.turmaSelecionadaParaCadastroDeNotas = null;
			Calendario calendario = authenticationContext.getCalendarioEscolar();
			Escola escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();
			if (calendario == null || escola == null) {
				this.turmasProfessor = null;
			} else if (calendario != null && escola != null) {
				this.turmasProfessor = professorTurmaServico.obterTurmasDoProfessorPorEscolaAno(escola.getId(),
						calendario.getAno());
			}
			// System.out.println("Chamou o botao");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void iniciarAlocacao() {
		turma = new Turma();
		this.turmaSelecionada = null;
		funcionariosEscola = new ArrayList<>();
		turmas = new ArrayList<>();
		disciplinaClasse = new DisciplinaClasse();
		disciplinaTurmaSelecionada = false;
		buscarProfessorBoolean = false;
		try {
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();
			funcionariosEscola = funcionarioEscolaServico.obterFuncionariosPorEscola(escola.getId());
			Calendario calendario = authenticationContext.getCalendarioEscolar();
			turma.setAno(calendario.getAno());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// funcao que busca as turmas de uma classse por turno
	public void buscarTurmas() {

		setTurmas(new ArrayList<>());

		try {
			Escola escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();

			if (turma.getClasse().getCiclo().equals("2º CICLO")) {
				setTurmas(turmaServico.obterTurmasPorClasseAreaCurso(turma.getClasse().getId(), turma.getArea(),
						turma.getCurso(), turma.getAno(), escola.getId()));
			} else if (turma.getClasse().getCiclo().equals("1º CICLO")) {
				setTurmas(turmaServico.obterTurmasPorClasseCurso(turma.getClasse().getId(), turma.getCurso(),
						turma.getAno(), escola.getId()));
			}

		} catch (Exception e) {

		}

	}
// Metodo de salvar Horario, primeiro quando e chamado esse metodo se existir um horario va eliminar e depois salvar
	public void salvar() {

		try {
			List<Horario> horarios = horarioServico.obterHorarioPorIdTurmaPorIdDiciplina(turmaSelecionada.getId(),
					disciplinaClasse.getDisciplina().getId());
			if (horarios != null && funcionario.getId() != null) {
				if (horarios.get(0) != null)
					horarioServico.excluir(horarios.get(0));

				professorTurma = professorTurmaServico.obterProfessorTurmaPorIdTurmarPorIdDisciplina(
						turmaSelecionada.getId(), disciplinaClasse.getDisciplina().getId());
				if (professorTurma == null) {
					professorTurma = new ProfessorTurma();
				} else {
					professorTurmaServico.excluir(professorTurma);
					professorTurma = new ProfessorTurma();
				}
			}

			List<String> horariosSeg = new ArrayList<>(), horariosTer = new ArrayList<>(),
					horariosQua = new ArrayList<>(), horariosQui = new ArrayList<>(), horariosSex = new ArrayList<>();
			for (HorarioAula horarioAula : horarioAulas) {
				Horario horario = new Horario();

				if (horarioAula.isHorarioProfNestaTurmaSeg() == true) {
					DiaSemana diaSemana = diaSemanaServico.obterDiaSemanaPorSigla("SEG");
					
				
						horario = new Horario();
					
					horario.setDiaSemana(diaSemana);
					horario.setDisciplina(disciplinaClasse.getDisciplina());
					horario.setTurma(turmaSelecionada);
					if (funcionario != null)
						horario.setProfessor(funcionario);
					horario.setHorarioAula(horarioAula);
					horario.setAno(turmaSelecionada.getAno());
					horarioServico.salvar(horario);
					horariosSeg.add(diaSemana.getSigla() + horarioAula.getOrdem());

				}
				if (horarioAula.isHorarioProfNestaTurmaTerc() == true) {
					DiaSemana diaSemana = diaSemanaServico.obterDiaSemanaPorSigla("TER");
					
						horario = new Horario();
					
					horario.setDiaSemana(diaSemana);
					horario.setDisciplina(disciplinaClasse.getDisciplina());
					horario.setTurma(turmaSelecionada);
					horario.setProfessor(funcionario);
					horario.setHorarioAula(horarioAula);
					horario.setAno(turmaSelecionada.getAno());
					horarioServico.salvar(horario);

					horariosTer.add(diaSemana.getSigla() + horarioAula.getOrdem() + "");

					horario = new Horario();
				}
				if (horarioAula.isHorarioProfNestaTurmaQua() == true) {
					DiaSemana diaSemana = diaSemanaServico.obterDiaSemanaPorSigla("QUA");
					
						horario = new Horario();
					
					horario.setDiaSemana(diaSemana);
					horario.setDisciplina(disciplinaClasse.getDisciplina());
					horario.setTurma(turmaSelecionada);
					if (funcionario != null)
						horario.setProfessor(funcionario);
					horario.setHorarioAula(horarioAula);
					horario.setAno(turmaSelecionada.getAno());
					horarioServico.salvar(horario);
					horariosQua.add(diaSemana.getSigla() + horarioAula.getOrdem() + "");
				}
				if (horarioAula.isHorarioProfNestaTurmaQui() == true) {
					DiaSemana diaSemana = diaSemanaServico.obterDiaSemanaPorSigla("QUI");

					
						horario = new Horario();
					
					horario.setDiaSemana(diaSemana);
					horario.setDisciplina(disciplinaClasse.getDisciplina());
					horario.setTurma(turmaSelecionada);
					horario.setAno(turmaSelecionada.getAno());
					if (funcionario != null)
						horario.setProfessor(funcionario);
					horario.setHorarioAula(horarioAula);
					horarioServico.salvar(horario);
					horariosQui.add(diaSemana.getSigla() + horarioAula.getOrdem() + "");

				}
				if (horarioAula.isHorarioProfNestaTurmaSex() == true) {
					DiaSemana diaSemana = diaSemanaServico.obterDiaSemanaPorSigla("SEX");
						horario = new Horario();
					

					horario.setDiaSemana(diaSemana);
					horario.setDisciplina(disciplinaClasse.getDisciplina());
					horario.setTurma(turmaSelecionada);
					if (funcionario != null)
						horario.setProfessor(funcionario);
					horario.setHorarioAula(horarioAula);
					horario.setAno(turmaSelecionada.getAno());
					horarioServico.salvar(horario);

					horariosSex.add(diaSemana.getSigla() + horarioAula.getOrdem() + "");

				}

			}
			// Horario do professor
			String siglaTurno;
			if (elecionadaTurnoExtra == false) {
				siglaTurno = turmaSelecionada.getTurno().getSigla();
				professorTurma.setElecionadaTurnoExtra("Nao");

			} else {
				siglaTurno = turnoSelecionado.getSigla();
				professorTurma.setElecionadaTurnoExtra("Sim");
			}
			String labelSeg = horariosSeg.toString().replace("[", "").replace("]", "").replace(", SEG", "")
					.replace("SEG", "2" + siglaTurno);

			String labelTer = horariosTer.toString().replace("[", "").replace("]", "").replace(", TER", "")
					.replace("TER", "3" + siglaTurno);
			String labelQua = horariosQua.toString().replace("[", "").replace("]", "").replace(", QUA", "")
					.replace("QUA", "4" + siglaTurno);
			String labelQui = horariosQui.toString().replace("[", "").replace("]", "").replace(", QUI", "")
					.replace("QUI", "5" + siglaTurno);
			String labelSex = horariosSex.toString().replace("[", "").replace("]", "").replace(", SEX", "")
					.replace("SEX", "6" + siglaTurno);
			String label1 = labelSeg == null ? "" : labelSeg + " ";
			String label2 = labelTer == null ? "" : labelTer + " ";
			String label3 = labelQua == null ? "" : labelQua + " ";
			String label4 = labelQui == null ? "" : labelQui + " ";
			String label5 = labelSex == null ? "" : labelSex + " ";

			String label = label1 + label2 + label3 + label4 + label5;
			label = label.replace("     ", " ").replace("    ", " ").replace("   ", " ").replace("  ", " ");

			professorTurma.setHorario(label.toString());

			System.out.println("Horario do professor:" + professorTurma.getHorario());

			ProfessorTurmaId professorTurmaId = new ProfessorTurmaId();
			professorTurmaId.setId_disciplina(disciplinaClasse.getDisciplina().getId());
			professorTurmaId.setId_escola(turmaSelecionada.getEscola().getId());
			professorTurmaId.setId_turma(turmaSelecionada.getId());
			professorTurma.setCredito(disciplinaClasse.getCredito());
			professorTurma.setId(professorTurmaId);
			if (funcionario != null)
				professorTurma.setProfessor(funcionario);
			professorTurmaServico.salvar(professorTurma);
			Mensagem.mensagemInfo("AVISO: Horario de Professor foi cadastrado com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buscarMatrizesCurriculares() {
		matrizes = new ArrayList<>();
		try {
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();

			if (turma.getCurso() != null && turma.getClasse().getId() != 0 && escola.getId() != null) {
				if (turma.getClasse().getCiclo().equals("2º CICLO")) {
					matrizes = matrizServico.obterMatrizPorClasseCursoAtivo(turma.getClasse().getId(), turma.getCurso(),
							escola.getId());

				}
			}

		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	public void selecionarTurma(Turma turma) {
		elecionadaTurnoExtra = false;
		this.turmaSelecionada = turma;
		buscarProfessorBoolean = false;
		horariosProfessor = new ArrayList<>();
		horarioAulas = new ArrayList<>();
		try {
			if (turma.getCurso() != null && turma.getClasse().getId() != 0 && turma.getEscola().getId() != null) {
				if (turma.getClasse().getCiclo().equals("2º CICLO")) {
					matrizSelecionada = matrizServico.obterMatrizPorSegundoCiclo(turma.getClasse().getId(),
							turma.getCurso(), turma.getArea(), turma.getEscola().getId());
				} else if (turma.getClasse().getCiclo().equals("1º CICLO")) {
					matrizSelecionada = matrizServico.obterMatrizPorPrimeiroCiclo(turma.getClasse().getId(),
							turma.getCurso(), turma.getEscola().getId());

				}
			}
			horariosProfessor = horarioServico.obterHorarioPorIdTurno(this.turmaSelecionada.getTurno().getId(),
					this.turmaSelecionada.getEscola().getId(), this.turmaSelecionada.getAno());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void procurarProfessor() {
		pesquisa = new String();
		buscarProfessorBoolean = true;
		funcionarios = new ArrayList<>();

		try {
			horariosProfessor = horarioServico.obterHorarioPorIdTurno(this.turmaSelecionada.getTurno().getId(),
					this.turmaSelecionada.getEscola().getId(), this.turmaSelecionada.getAno());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void selecionarFuncionario(Funcionario funcionario) {

		this.funcionario = funcionario;
		professor = funcionario.getId() + " - " + funcionario.getNome();
		virificarDisciplinaProfessorBoolean = false;
		try {

			buscarProfessorBoolean = false;
			if (elecionadaTurnoExtra == false)
				horarioAulas = horarioAulaServico.obterHorarioAulaPorEscolaTurno(turmaSelecionada.getTurno().getId());
			else if (elecionadaTurnoExtra == true) {
				horarioAulas = horarioAulaServico.obterHorarioAulaPorEscolaTurno(turnoSelecionado.getId());

			}

			verificaProfessor(horarioAulas);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void verificaProfessor(List<HorarioAula> horarioAulas) {

		try {
			if (horarioAulas != null) {
				for (int i = 0; i < horarioAulas.size(); i++) {
					verificaHorario(horarioAulas, i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Turno getObterTurno() {

		try {
			if (elecionadaTurnoExtra) {
				turnoSelecionado = turnoServico.obterTurnoCursoDiurno(turmaSelecionada.getTurno().getCurso(),
						turmaSelecionada.getTurno().getDescricao());
				return turnoSelecionado;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Turno();
	}

	private void verificaHorario(List<HorarioAula> horarioAulas, int i) {
		if (horariosProfessor != null) {
			for (int j = 0; j < horariosProfessor.size(); j++) {
				// Horario(s) ocupado(s) pelo professor escolhido, nesta turma
				if (funcionario.getId() == horariosProfessor.get(j).getProfessor().getId()
						&& turmaSelecionada.getId() == horariosProfessor.get(j).getTurma().getId()
						&& disciplinaClasse.getDisciplina().getId() == horariosProfessor.get(j).getDisciplina().getId()
						|| disciplinaClasse.getDisciplina().getId() == horariosProfessor.get(j).getDisciplina().getId()
								&& turmaSelecionada.getId() == horariosProfessor.get(j).getTurma().getId()) {
					verificaHorarioOcupadoPeloProfessorNestaTurma(horarioAulas, i, j);
					if (virificarDisciplinaProfessorBoolean == true) {
						if (horariosProfessor.get(j).getProfessor().getId() != null) {
							funcionario = horariosProfessor.get(j).getProfessor();
							professor = funcionario.getId() + " - " + funcionario.getNome();
						}
					}

				}
				// Horario(s) ocupado(s) pelo professor escolhido, em outra
				// turma
				if (funcionario.getId() == horariosProfessor.get(j).getProfessor().getId()
						&& !this.turmaSelecionada.getId().equals(horariosProfessor.get(j).getTurma().getId())) {
					verificaHorarioOcupadoPeloProfessorOutraTurma(horarioAulas, i, j);
				}
				// Horario(s) ocupado(s) por outro(s) professores(as) nesta
				// turma
				if (disciplinaClasse.getDisciplina().getId() != horariosProfessor.get(j).getDisciplina().getId()
						&& turmaSelecionada.getId() == horariosProfessor.get(j).getTurma().getId()) {
					verificaHorarioOcupadoPorOutrosProfessoresNestaTurma(horarioAulas, i, j);
				}

			}
		}
	}

	private void verificaHorarioOcupadoPorOutrosProfessoresNestaTurma(List<HorarioAula> horarioAulas, int i, int j) {
		if (horariosProfessor.get(j).getDiaSemana().getSigla().equals("SEG")
				&& horariosProfessor.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
			horarioAulas.get(i).setHorariosOutrosProfNestaTurmaSeg(true);
		}
		if (horariosProfessor.get(j).getDiaSemana().getSigla().equals("TER")
				&& horariosProfessor.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
			horarioAulas.get(i).setHorariosOutrosProfNestaTurmaTerc(true);
		}
		if (horariosProfessor.get(j).getDiaSemana().getSigla().equals("QUA")
				&& horariosProfessor.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
			horarioAulas.get(i).setHorariosOutrosProfNestaTurmaQua(true);
		}
		if (horariosProfessor.get(j).getDiaSemana().getSigla().equals("QUI")
				&& horariosProfessor.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
			horarioAulas.get(i).setHorariosOutrosProfNestaTurmaQui(true);
		}
		if (horariosProfessor.get(j).getDiaSemana().getSigla().equals("SEX")
				&& horariosProfessor.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
			horarioAulas.get(i).setHorariosOutrosProfNestaTurmaSex(true);
		}

	}

	private void verificaHorarioOcupadoPeloProfessorOutraTurma(List<HorarioAula> horarioAulas, int i, int j) {
		if (horariosProfessor.get(j).getDiaSemana().getSigla().equals("SEG")
				&& horariosProfessor.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
			horarioAulas.get(i).setHorarioProfOutraTurmaSeg(true);
		}
		if (horariosProfessor.get(j).getDiaSemana().getSigla().equals("TER")
				&& horariosProfessor.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
			horarioAulas.get(i).setHorarioProfOutraTurmaTerc(true);
		}
		if (horariosProfessor.get(j).getDiaSemana().getSigla().equals("QUA")
				&& horariosProfessor.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
			horarioAulas.get(i).setHorarioProfOutraTurmaQua(true);
		}
		if (horariosProfessor.get(j).getDiaSemana().getSigla().equals("QUI")
				&& horariosProfessor.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
			horarioAulas.get(i).setHorarioProfOutraTurmaQui(true);
		}
		if (horariosProfessor.get(j).getDiaSemana().getSigla().equals("SEX")
				&& horariosProfessor.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
			horarioAulas.get(i).setHorarioProfOutraTurmaSex(true);
		}

	}

	private void verificaHorarioOcupadoPeloProfessorNestaTurma(List<HorarioAula> horarioAulas, int i, int j) {
		if (horariosProfessor.get(j).getDiaSemana().getSigla().equals("SEG")
				&& horariosProfessor.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
			horarioAulas.get(i).setHorarioProfNestaTurmaSeg(true);
		}
		if (horariosProfessor.get(j).getDiaSemana().getSigla().equals("TER")
				&& horariosProfessor.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
			horarioAulas.get(i).setHorarioProfNestaTurmaTerc(true);
		}
		if (horariosProfessor.get(j).getDiaSemana().getSigla().equals("QUA")
				&& horariosProfessor.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
			horarioAulas.get(i).setHorarioProfNestaTurmaQua(true);
		}
		if (horariosProfessor.get(j).getDiaSemana().getSigla().equals("QUI")
				&& horariosProfessor.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
			horarioAulas.get(i).setHorarioProfNestaTurmaQui(true);
		}
		if (horariosProfessor.get(j).getDiaSemana().getSigla().equals("SEX")
				&& horariosProfessor.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
			horarioAulas.get(i).setHorarioProfNestaTurmaSex(true);
		}
	}

	public void voltarProcuraProfessor() {
		buscarProfessorBoolean = false;
	}

	public void buscarProfessor() {
		try {
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();
			funcionarios = funcionarioEscolaServico.obterFuncionariosPorIdEscola(escola.getId(),
					pesquisa.trim().toUpperCase());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void voltar() {
		if (disciplinaTurmaSelecionada == false) {
			this.turmaSelecionada = null;
			this.matrizSelecionada = null;
			buscarProfessorBoolean = false;
		} else {
			disciplinaTurmaSelecionada = false;
		}
	}

	public void proximoAlocacao() {
		try {
			professor = new String();
			funcionario = new Funcionario();
			disciplinaTurmaSelecionada = true;
			virificarDisciplinaProfessorBoolean = true;
			if (elecionadaTurnoExtra == false)
				horarioAulas = horarioAulaServico.obterHorarioAulaPorEscolaTurno(turmaSelecionada.getTurno().getId());
			else if (elecionadaTurnoExtra == true) {
				turnoSelecionado = getObterTurno();
				horariosProfessor = horarioServico.obterHorarioPorIdTurno(turnoSelecionado.getId(),
						this.turmaSelecionada.getEscola().getId(), this.turmaSelecionada.getAno());
				horarioAulas = horarioAulaServico.obterHorarioAulaPorEscolaTurno(turnoSelecionado.getId());

			}

			disciplinaClasse = disciplinaClasseServico.obterDisciplinasClassePorId(disciplinaClasse.getId());

			verificaProfessor(horarioAulas);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void gerarRelatorioDeAlunosDaTurma(ProfessorTurma professorTurma) {

		try {
			String caminho = "/academico/relatorio/aluno/listar_alunos_por_disciplina.jasper", filename = "doc.pdf";
			Map<String, Object> parametro = new HashMap<>();
			String nomeDiciplina = null, nomeProfessor = null;
			Integer idTurma = null, idDisciplina = null;
			if (professorTurma.getProfessor() != null) {
				if (professorTurma.getProfessor().getNome() != null)
					nomeProfessor = TipoLetra.capitalizeString(professorTurma.getProfessor().getNome())
							.replace(" Dos ", " dos ").replace(" Das ", "das").replace(" De ", " de ")
							.replace(" À ", " à ");
			}
			if (professorTurma.getDisciplina() != null) {
				if (professorTurma.getDisciplina().getDescricao() != null)
					nomeDiciplina = TipoLetra.capitalizeString(professorTurma.getDisciplina().getDescricao())
							.replace(" Dos ", " dos ").replace(" Das ", "das").replace(" De ", " de ")
							.replace(" À ", " à ");
				if (professorTurma.getDisciplina().getId() != null) {
					idDisciplina = professorTurma.getDisciplina().getId();
				}
			}
			if (professorTurma.getTurma() != null)
				if (professorTurma.getTurma().getId() != null) {
					idTurma = professorTurma.getTurma().getId();
				}

			parametro.put("idTurma", idTurma);
			parametro.put("idDisciplina", idDisciplina);
			parametro.put("professor", nomeProfessor);
			parametro.put("disciplina", nomeDiciplina);

			geradorDeRelatoriosServico.geraPdf(caminho, parametro, filename);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void imprimirDisciplinaMinistradaPorProfessor() throws SQLException {
		try {
			// Faces.getViewRoot().
			String caminho = "/academico/relatorio/horario/turma.jasper";
			Map<String, Object> parametro = new HashMap<>();
			Funcionario funcionario = authenticationContext.getFuncionarioLogado();
			if (funcionario != null)
				parametro.put("idProfessor", funcionario.getId());
			String relatorio = geradorDeRelatoriosServico.gerarRelatorio(caminho, parametro);
			JasperPrintManager.printReport(relatorio, true);

		} catch (JRException e) {
			e.printStackTrace();
		}

	}

	public String voltarDaMinhasTurmas() {
		return "/academico/professor/professor?faces-redirect=true";
	}

	public ProfessorTurma getProfessorTurma() {
		return professorTurma;
	}

	public void setProfessorTurma(ProfessorTurma professorTurma) {
		this.professorTurma = professorTurma;
	}

	public List<ProfessorTurma> getProfessorTurmas() {
		return professorTurmas;
	}

	public void setProfessorTurmas(List<ProfessorTurma> professorTurmas) {
		this.professorTurmas = professorTurmas;
	}

	public List<ProfessorTurma> getTurmasProfessor() {
		return turmasProfessor;
	}

	public void setTurmasProfessor(List<ProfessorTurma> turmasProfessor) {
		this.turmasProfessor = turmasProfessor;
	}

	public ProfessorTurma getProfessorTurmaSelecionada() {
		return professorTurmaSelecionada;
	}

	public void setProfessorTurmaSelecionada(ProfessorTurma professorTurmaSelecionada) {
		this.professorTurmaSelecionada = professorTurmaSelecionada;
	}

	public boolean isDisciplinaTurmaSelecionada() {
		return disciplinaTurmaSelecionada;
	}

	public void setDisciplinaTurmaSelecionada(boolean disciplinaTurmaSelecionada) {
		this.disciplinaTurmaSelecionada = disciplinaTurmaSelecionada;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
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

	public List<FuncionarioEscola> getFuncionariosEscola() {
		return funcionariosEscola;
	}

	public void setFuncionariosEscola(List<FuncionarioEscola> funcionariosEscola) {
		this.funcionariosEscola = funcionariosEscola;
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

	public Matriz getMatrizSelecionada() {
		return matrizSelecionada;
	}

	public void setMatrizSelecionada(Matriz matrizSelecionada) {
		this.matrizSelecionada = matrizSelecionada;
	}

	public DisciplinaClasse getDisciplinaClasse() {
		return disciplinaClasse;
	}

	public void setDisciplinaClasse(DisciplinaClasse disciplinaClasse) {
		this.disciplinaClasse = disciplinaClasse;
	}

	public boolean isBuscarProfessorBoolean() {
		return buscarProfessorBoolean;
	}

	public void setBuscarProfessorBoolean(boolean buscarProfessorBoolean) {
		this.buscarProfessorBoolean = buscarProfessorBoolean;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
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

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public List<HorarioAula> getHorarioAulas() {
		return horarioAulas;
	}

	public void setHorarioAulas(List<HorarioAula> horarioAulas) {
		this.horarioAulas = horarioAulas;
	}

	public List<Horario> getHorariosProfessor() {
		return horariosProfessor;
	}

	public void setHorariosProfessor(List<Horario> horariosProfessor) {
		this.horariosProfessor = horariosProfessor;
	}

	public boolean isVirificarDisciplinaProfessorBoolean() {
		return virificarDisciplinaProfessorBoolean;
	}

	public void setVirificarDisciplinaProfessorBoolean(boolean virificarDisciplinaProfessorBoolean) {
		this.virificarDisciplinaProfessorBoolean = virificarDisciplinaProfessorBoolean;
	}

	public boolean isElecionadaTurnoExtra() {
		return elecionadaTurnoExtra;
	}

	public void setElecionadaTurnoExtra(boolean elecionadaTurnoExtra) {
		this.elecionadaTurnoExtra = elecionadaTurnoExtra;
	}

	public Turno getTurnoSelecionado() {
		return turnoSelecionado;
	}

	public void setTurnoSelecionado(Turno turnoSelecionado) {
		this.turnoSelecionado = turnoSelecionado;
	}

}
