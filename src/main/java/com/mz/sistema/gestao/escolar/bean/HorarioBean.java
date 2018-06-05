package com.mz.sistema.gestao.escolar.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.enumerado.Aula;
import com.mz.sistema.gestao.escolar.modelo.Calendario;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.modelo.Horario;
import com.mz.sistema.gestao.escolar.modelo.HorarioAula;
import com.mz.sistema.gestao.escolar.modelo.Matricula;
import com.mz.sistema.gestao.escolar.modelo.Turma;
import com.mz.sistema.gestao.escolar.modelo.Turno;
import com.mz.sistema.gestao.escolar.servico.GeradorDeRelatoriosServico;
import com.mz.sistema.gestao.escolar.servico.HorarioAulaServico;
import com.mz.sistema.gestao.escolar.servico.HorarioServico;
import com.mz.sistema.gestao.escolar.servico.TurnoServico;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named
@SessionScope
@Controller
public class HorarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Horario horario = new Horario();
	private Funcionario funcionario = new Funcionario();
	private Escola escola = new Escola();
	private HorarioAula horarioAula = new HorarioAula();
	private List<HorarioAula> horarioAulas = new ArrayList<>();
	private List<Horario> horarios = new ArrayList<>();
	private List<Horario> horariosProfessor = new ArrayList<Horario>();
	private List<Turno> turnos = new ArrayList<Turno>();
	private List<Aula> aulas = Arrays.asList(Aula.values());
	private Turma turma = new Turma();
	private Long idTurno;
	private boolean horarioMinhaTurmaBoolean = true;
	// private Map<Long, Answer[]> answers = new HashMap<Long, Answer[]>();
	@Autowired
	private GeradorDeRelatoriosServico geradorDeRelatoriosServico;

	@Autowired
	private HorarioServico horarioServico;

	@Autowired
	private TurnoServico turnoServico;
	@Autowired
	private HorarioAulaServico horarioAulaServico;
	@Autowired
	private AuthenticationContext authenticationContext;

	public void iniciarBeanHorarioAluno() {
		try {
			horarioMinhaTurmaBoolean = true;
			horarios = new ArrayList<>();
			Matricula matricula = authenticationContext.getMatriculaEscolaLogada();
			if (matricula != null) {
				if (matricula.getTurmaDestino() != null) {
					if (matricula.getTurmaDestino().getId() != null) {
						horarioMinhaTurmaBoolean = horarioServico
								.verificarHorarioMinhaTurma(matricula.getTurma().getId());
						turma = matricula.getTurmaDestino();
						if (horarioMinhaTurmaBoolean != true) {
							horarioAulas = horarioAulaServico.obterHorarioAulaPorIdEscolaPoridTurno(
									matricula.getTurma().getTurno().getId(), matricula.getEscola().getId());
						}
					}
				} else if (matricula.getTurma() != null) {
					if (matricula.getTurma().getId() != null) {
						turma = matricula.getTurma();
						horarioMinhaTurmaBoolean = horarioServico
								.verificarHorarioMinhaTurma(matricula.getTurma().getId());
						if (horarioMinhaTurmaBoolean != true) {
							horarioAulas = horarioAulaServico.obterHorarioAulaPorIdEscolaPoridTurno(
									matricula.getTurma().getTurno().getId(), matricula.getEscola().getId());
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void iniciarBeanHorarioProfessor() {
		try {
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			escola = funcionarioEscola.getEscola();
			funcionario = funcionarioEscola.getFuncionario();
			horarioAulas = new ArrayList<>();
			turnos = turnoServico.listarTodosTurnosDaEscola();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buscarHorario() {
		try {
			this.horarioAulas = horarioAulaServico.obterHorarioAulaPorIdEscolaPoridTurno(horarioAula.getTurno().getId(),
					escola.getId());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void salvar() {
		try {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Horário de Aula cadastrado com sucesso!", null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Horario> getHorariosTurma(HorarioAula horarioAula) {
		try {

			if (horarioAula != null) {

				if (horarioAula.getId() != 0) {
					if (turma != null)
						this.horarios = horarioServico.obterHorarioPorIdHorarioAulaPoridTurma(horarioAula.getId(),
								turma.getId());
					if (horarios != null)
						return horarios;
				}

			}
		} catch (Exception e) {

		}
		return new ArrayList<Horario>();
	}

	public void imprimirHorarioMinhaTurma() {
		try {

			String caminho = "/academico/relatorio/horario/aluno.jasper";
			Map<String, Object> parametros = new HashMap<>();
			parametros.put("distrito", turma.getEscola().getDistrital().getEndereco().getDistrito().getNome());
			parametros.put("provincia",
					turma.getEscola().getDistrital().getEndereco().getDistrito().getProvincia().toString());
			parametros.put("escola", turma.getEscola().getDescricao());
			List<Horario> horarios = horarioServico.obterHorarioPorIdTurma(turma.getId());

			parametros.put("classe", turma.getClasse().getDescricao());

			parametros.put("turma", turma.getDescricao() + "/" + horarios.get(0).getTurma().getTurno().getDescricao());
			for (int i = 0; i < horarioAulas.size(); i++) {

				for (int j = 0; j < horarios.size(); j++) {
					if (turma.getId() == horarios.get(j).getTurma().getId()
							&& horarios.get(j).getDiaSemana().getSigla().equals("SEG")
							&& horarios.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
						parametros.put("segunda", horarios.get(j).getDisciplina().getDescricao());
					}
					if (turma.getId() == horarios.get(j).getTurma().getId()
							&& horarios.get(j).getDiaSemana().getSigla().equals("TER")
							&& horarios.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
						parametros.put("terca", horarios.get(j).getDisciplina().getDescricao());
					}
					if (turma.getId() == horarios.get(j).getTurma().getId()
							&& horarios.get(j).getDiaSemana().getSigla().equals("QUA")
							&& horarios.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
						parametros.put("quarta", horarios.get(j).getDisciplina().getDescricao());
					}
					if (turma.getId() == horarios.get(j).getTurma().getId()
							&& horarios.get(j).getDiaSemana().getSigla().equals("QUI")
							&& horarios.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
						parametros.put("quinta", horarios.get(j).getDisciplina().getDescricao());
					}
					if (turma.getId() == horarios.get(j).getTurma().getId()
							&& horarios.get(j).getDiaSemana().getSigla().equals("SEX")
							&& horarios.get(j).getHorarioAula().getId() == horarioAulas.get(i).getId()) {
						parametros.put("sexta", horarios.get(j).getDisciplina().getDescricao());
					}
				}

			}
			geradorDeRelatoriosServico.geraPdfComConexaoDataSource(caminho, parametros, "Horario da minha turma.pdf",
					new JRBeanCollectionDataSource(horarioAulas));
			// JasperPrintManager.printReport(relatorio, true);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public List<Horario> getHorariosProfessor(HorarioAula horarioAula) {
		try {

			if (horarioAula != null) {

				if (horarioAula.getId() != 0) {
					if (funcionario != null) {
						Calendario calendario = authenticationContext.getCalendarioEscolar();
						if (calendario != null)
							this.horariosProfessor = horarioServico.obterHorarioPorIdHorarioAulaPoridProfessor(
									horarioAula.getId(), funcionario.getId(), calendario.getAno());

						if (horariosProfessor != null)
							return horariosProfessor;
					}
				}

			}
		} catch (Exception e) {

		}
		return new ArrayList<Horario>();
	}

	public List<Turno> getTurnos() {
		return turnos;
	}

	public void setTurnos(List<Turno> turnos) {
		this.turnos = turnos;
	}

	public List<HorarioAula> getHorarioAulas() {
		return horarioAulas;
	}

	public void setHorarioAulas(List<HorarioAula> horarioAulas) {
		this.horarioAulas = horarioAulas;
	}

	public List<Aula> getAulas() {
		return aulas;
	}

	public void setAulas(List<Aula> aulas) {
		this.aulas = aulas;
	}

	public Long getIdTurno() {
		return idTurno;
	}

	public void setIdTurno(Long idTurno) {
		this.idTurno = idTurno;
	}

	public List<Horario> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<Horario> horarios) {
		this.horarios = horarios;
	}

	public Horario getHorario() {
		return horario;
	}

	public void setHorario(Horario horario) {
		this.horario = horario;
	}

	public HorarioAula getHorarioAula() {
		return horarioAula;
	}

	public void setHorarioAula(HorarioAula horarioAula) {
		this.horarioAula = horarioAula;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public List<Horario> getHorariosProfessor() {
		return horariosProfessor;
	}

	public void setHorariosProfessor(List<Horario> horariosProfessor) {
		this.horariosProfessor = horariosProfessor;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public boolean isHorarioMinhaTurmaBoolean() {
		return horarioMinhaTurmaBoolean;
	}

	public void setHorarioMinhaTurmaBoolean(boolean horarioMinhaTurmaBoolean) {
		this.horarioMinhaTurmaBoolean = horarioMinhaTurmaBoolean;
	}
}
