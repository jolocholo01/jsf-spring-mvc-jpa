
package com.mz.sistema.gestao.escolar.bean;

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

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.enumerado.TipoCurso;
import com.mz.sistema.gestao.escolar.modelo.Calendario;
import com.mz.sistema.gestao.escolar.modelo.DisciplinaClasse;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.modelo.Matricula;
import com.mz.sistema.gestao.escolar.modelo.Matriz;
import com.mz.sistema.gestao.escolar.modelo.Nota;
import com.mz.sistema.gestao.escolar.modelo.ProfessorTurma;
import com.mz.sistema.gestao.escolar.modelo.Trimestre;
import com.mz.sistema.gestao.escolar.modelo.Turma;
import com.mz.sistema.gestao.escolar.servico.DisciplinaClasseServico;
import com.mz.sistema.gestao.escolar.servico.GeradorDeRelatoriosServico;
import com.mz.sistema.gestao.escolar.servico.MatriculaServico;
import com.mz.sistema.gestao.escolar.servico.MatrizServico;
import com.mz.sistema.gestao.escolar.servico.NotaServico;
import com.mz.sistema.gestao.escolar.servico.ProfessorTurmaServico;
import com.mz.sistema.gestao.escolar.servico.TrimestreServico;
import com.mz.sistema.gestao.escolar.servico.TurmaServico;
import com.mz.sistema.gestao.escolar.util.TipoLetra;

@Controller
@Named
@SessionScope
public class NotaBean {

	private Nota nota = new Nota();
	private Trimestre trimestre = new Trimestre();
	private Nota notaSelecionada;
	private ProfessorTurma professorTurma;
	private List<Nota> notas = new ArrayList<>();
	private List<Trimestre> trimestres = new ArrayList<>();
	private Trimestre trimestreSelecionado;
	private List<Matriz> matrizes;
	private Matriz matrizSelecionada;

	private String tipoTrimestre;
	private Double mediaAvaliacaoContinua;
	private Double mediaAvaliacaoSomativa;
	private Integer mediaTrimestral;
	private boolean mediaAvaliacaoC = false;
	private boolean mediaAvaliacaoS = false;
	private boolean mediaDoTrimestre = false;
	private boolean selecionarturma = false;

	// Teste
	private List<Matricula> matriculas = new ArrayList<>();
	private Turma turmaSelecionada;
	private Turma turma;
	private List<Turma> turmas;
	private DisciplinaClasse disciplinaClasse;
	private boolean disciplinaTurmaSelecionada = false;
	private List<TipoCurso> cursos = Arrays.asList(TipoCurso.values());

	@Autowired
	private DisciplinaClasseServico disciplinaClasseServico;

	@Autowired
	private ProfessorTurmaServico professorTurmaServico;

	@Autowired
	private NotaServico notaServico;
	@Autowired
	private TrimestreServico trimestreServico;

	@Autowired
	private MatriculaServico matriculaServico;
	@Autowired
	private TurmaServico turmaServico;
	@Autowired
	private MatrizServico matrizServico;
	@Autowired
	private AuthenticationContext authenticationContext;
	@Autowired
	private GeradorDeRelatoriosServico geradorDeRelatoriosServico;

	public void iniciarBean() {
		this.turmaSelecionada = null;
		this.turma = new Turma();
		turmas = new ArrayList<>();
		disciplinaClasse = new DisciplinaClasse();
		disciplinaTurmaSelecionada = false;
		try {
			Date data = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String dataFormatodo = sdf.format(data);
			Integer ano = Integer.valueOf(dataFormatodo);
			turma.setAno(ano);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void iniciarLancarNotaNumaTurma() {
		turma = new Turma();
		this.turmaSelecionada = null;
		turmas = new ArrayList<>();
		disciplinaClasse = new DisciplinaClasse();
		disciplinaTurmaSelecionada = false;

		try {
			// FuncionarioEscola funcionarioEscola =
			// authenticationContext.getFuncionarioEscolaLogada();
			// Escola escola = funcionarioEscola.getEscola();
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

	public void salvar(Nota nota) {
		try {
			double mediaAnual;

			this.nota = null;
			if (nota == null) {
			} else if (nota != null) {
				Trimestre trimestre = this.trimestreSelecionado;
				if (trimestre != null) {
					if (trimestre.getDescricao() != null) {
						if (trimestre.getDescricao().equals("1º Trimestre")) {
							trimetre1(nota);
							nota.setTrimestre(trimestre);
						} else if (trimestre.getDescricao().equals("2º Trimestre")) {
							trimetre2(nota);
							nota.setTrimestre2(trimestre);
						} else if (trimestre.getDescricao().equals("3º Trimestre")) {
							nota.setTrimestre3(trimestre);
							trimetre3(nota);

						}
					}
				}
				mediaAnual = ((double) (nota.getMediaTrimestral() + nota.getMediaTrimestral2()
						+ nota.getMediaTrimestral3())) / 3;
				System.out.println("Media Final:" + mediaAnual);
				nota.setMediaAnual((int) Math.round(mediaAnual));
				System.out.println("Media Final aredondado:" + nota.getMediaAnual());

				if (nota.getMatricula().getClasse().getCiclo().equals("2º CICLO")) {
					if (nota.getMatricula().getClasse().getDescricao().equals("12ª CLASSE")) {
						if (nota.getMediaAnual() >= 10 && nota.getMediaAnual() < 14) {
							nota.setSituacaoAnual("Admitido");
						} else if (nota.getMediaAnual() >= 14 && nota.getMediaAnual() < 21) {
							nota.setSituacaoAnual("Dispensado");
						} else if (nota.getMediaAnual() >= 0 && nota.getMediaAnual() < 10) {
							nota.setSituacaoAnual("Reprovado");
						}
					} else {
						if (nota.getMediaAnual() >= 10) {
							nota.setSituacaoAnual("Aprovado");
						} else {
							nota.setSituacaoAnual("Reprovado");
						}

					}
				}
		
				notaServico.salvar(nota);

			}
		} catch (Exception e) {

		}
	}

	public void trimetre1(Nota nota) {
		Double mediaAc = null;
		Double mediaAs = null;
		Double mediaTr = null;

		/* 1º Trimestre */
		if (nota.getAc1() == null) {
			nota.setMediaAc(null);
		} else if (nota.getAc1() != null) {
			mediaAc = nota.getAc1();
			nota.setMediaAc(mediaAc);
		}
		if (nota.getAc2() == null) {
		} else if (nota.getAc2() != null) {
			if (nota.getAc1() == null) {
				nota.setAc1(0D);
			}
			mediaAc = (nota.getAc1() + nota.getAc2());
			mediaAc = mediaAc / 2;
			nota.setMediaAc(mediaAc);
		}
		if (nota.getAc3() == null) {

		} else if (nota.getAc3() != null) {
			if (nota.getAc1() == null) {
				nota.setAc1(0D);
			}
			if (nota.getAc2() == null) {
				nota.setAc2(0D);
			}
			mediaAc = (nota.getAc1() + nota.getAc2() + nota.getAc3());
			mediaAc = mediaAc / 3;
			nota.setMediaAc(mediaAc);
		}

		if (nota.getAs1() == null) {
			nota.setMediaAs(null);
		} else if (nota.getAs1() != null) {
			if (nota.getMediaAc() == null) {
				nota.setMediaAc(0D);
			}
			mediaAs = (nota.getMediaAc() + nota.getAs1());
			mediaAs = mediaAs / 2;
			nota.setMediaAs(mediaAs);
		}
		if (nota.getAs2() == null) {
		} else if (nota.getAs2() != null) {
			if (nota.getMediaAc() == null) {
				nota.setMediaAc(0D);
			}
			if (nota.getAs1() == null) {
				nota.setAs1(0D);
			}
			mediaAs = (nota.getMediaAc() + nota.getAs1() + nota.getAs2());
			mediaAs = mediaAs / 3;
			nota.setMediaAs(mediaAs);
		}
		if (nota.getAvaliacaoFinal() == null) {
			nota.setMediaTrimestral(0);
			nota.setSituacao(null);
		} else if (nota.getAvaliacaoFinal() != null) {
			if (nota.getMediaAs() == null) {
				nota.setMediaAs(0D);
			}
			mediaTr = ((2 * nota.getMediaAs()) + nota.getAvaliacaoFinal());
			mediaTr = mediaTr / 3;
			mediaTrimestral = (int) Math.round(mediaTr);
			nota.setMediaTrimestral(mediaTrimestral);
			if (nota.getMatricula().getClasse().getCiclo().equals("2º CICLO")) {
				if (nota.getMatricula().getClasse().getDescricao().equals("12ª CLASSE")) {
					if (nota.getMediaTrimestral() >= 10 && nota.getMediaTrimestral() < 14) {
						nota.setSituacao("Admitido");
					} else if (nota.getMediaTrimestral() >= 14 && nota.getMediaTrimestral() < 21) {
						nota.setSituacao("Dispensado");
					} else if (nota.getMediaTrimestral() >= 0 && nota.getMediaTrimestral() < 10) {
						nota.setSituacao("Reprovado");
					}
				} else if (nota.getMatricula().getClasse().getDescricao().equals("11ª CLASSE")) {
					if (nota.getMediaTrimestral() >= 10) {
						nota.setSituacao("Aprovado");
					} else {
						nota.setSituacao("Reprovado");
					}
				}
			} else if (nota.getMediaTrimestral() >= 10) {
				nota.setSituacao("Aprovado");
			} else {
				nota.setSituacao("Reprovado");
			}

			if (professorTurma.getProfessor() == null) {

			} else if (professorTurma.getProfessor() != null) {
				nota.setProfessor(professorTurma.getProfessor());
			}

		}
	}

	public void trimetre2(Nota nota) {
		Double mediaAc2 = null;
		Double mediaAs2 = null;
		Double mediaTr2 = null;
		/* 1º Trimestre */
		if (nota.getAc12() == null) {
			nota.setMediaAc2(null);
		} else if (nota.getAc12() != null) {
			mediaAc2 = nota.getAc12();
			nota.setMediaAc2(mediaAc2);
		}
		if (nota.getAc22() == null) {
		} else if (nota.getAc22() != null) {
			if (nota.getAc12() == null) {
				nota.setAc12(0D);
			}
			mediaAc2 = (nota.getAc12() + nota.getAc22());
			mediaAc2 = mediaAc2 / 2;
			nota.setMediaAc2(mediaAc2);
		}
		if (nota.getAc32() == null) {

		} else if (nota.getAc32() != null) {
			if (nota.getAc12() == null) {
				nota.setAc12(0D);
			}
			if (nota.getAc22() == null) {
				nota.setAc22(0D);
			}
			mediaAc2 = (nota.getAc12() + nota.getAc22() + nota.getAc32());
			mediaAc2 = mediaAc2 / 3;
			nota.setMediaAc2(mediaAc2);
		}

		if (nota.getAs12() == null) {
			nota.setMediaAs2(null);
		} else if (nota.getAs12() != null) {
			if (nota.getMediaAc2() == null) {
				nota.setMediaAc2(0D);
			}
			mediaAs2 = (nota.getMediaAc2() + nota.getAs12());
			mediaAs2 = mediaAs2 / 2;
			nota.setMediaAs2(mediaAs2);
		}
		if (nota.getAs22() == null) {
		}
		if (nota.getAs22() != null) {
			if (nota.getMediaAc3() == null) {
				nota.setMediaAc3(0D);
			}
			if (nota.getAs12() == null) {
				nota.setAs12(0D);
			}
			mediaAs2 = (nota.getMediaAc2() + nota.getAs12() + nota.getAs22());
			mediaAs2 = mediaAs2 / 3;
			nota.setMediaAs2(mediaAs2);
		}
		if (nota.getAvaliacaoFinal2() == null) {
			nota.setMediaTrimestral2(0);
			nota.setSituacao2(null);
		} else if (nota.getAvaliacaoFinal2() != null) {
			if (nota.getMediaAs2() == null) {
				nota.setMediaAs2(0D);
			}
			mediaTr2 = ((2 * nota.getMediaAs2()) + nota.getAvaliacaoFinal2());
			mediaTr2 = mediaTr2 / 3;
			mediaTrimestral = (int) Math.round(mediaTr2);
			nota.setMediaTrimestral2(mediaTrimestral);

			if (nota.getMatricula().getClasse().getCiclo().equals("2º CICLO")) {
				if (nota.getMatricula().getClasse().getDescricao().equals("12ª CLASSE")) {
					if (nota.getMediaTrimestral2() >= 10 && nota.getMediaTrimestral2() < 14) {
						nota.setSituacao2("Admitido");
					} else if (nota.getMediaTrimestral2() >= 14 && nota.getMediaTrimestral2() < 21) {
						nota.setSituacao2("Dispensado");
					} else if (nota.getMediaTrimestral2() >= 0 && nota.getMediaTrimestral2() < 10) {
						nota.setSituacao2("Reprovado");
					}
				} else if (nota.getMatricula().getClasse().getDescricao().equals("11ª CLASSE")) {
					if (nota.getMediaTrimestral2() >= 10) {
						nota.setSituacao2("Aprovado");
					} else {
						nota.setSituacao2("Reprovado");
					}
				}
			}

			else if (nota.getMediaTrimestral2() >= 10) {
				nota.setSituacao2("Aprovado");
			} else {
				nota.setSituacao2("Reprovado");
			}
		}

		if (professorTurma.getProfessor() == null) {

		} else if (professorTurma.getProfessor() != null) {
			nota.setProfessor2(professorTurma.getProfessor());
		}
	}

	public void trimetre3(Nota nota) {
		Double mediaAc3 = null;
		Double mediaAs3 = null;
		Double mediaTr3 = null;
		/* 3º Trimestre */
		if (nota.getAc13() == null) {
			nota.setMediaAc3(null);
		} else if (nota.getAc13() != null) {
			mediaAc3 = nota.getAc13();
			nota.setMediaAc3(mediaAc3);
		}
		if (nota.getAc23() == null) {
		} else if (nota.getAc23() != null) {
			if (nota.getAc13() == null) {
				nota.setAc13(0D);
			}
			mediaAc3 = (nota.getAc13() + nota.getAc23());
			mediaAc3 = mediaAc3 / 2;
			nota.setMediaAc3(mediaAc3);
		}
		if (nota.getAc33() == null) {

		} else if (nota.getAc33() != null) {
			if (nota.getAc13() == null) {
				nota.setAc13(0D);
			}
			if (nota.getAc23() == null) {
				nota.setAc23(0D);
			}
			mediaAc3 = (nota.getAc13() + nota.getAc23() + nota.getAc33());
			mediaAc3 = mediaAc3 / 3;
			nota.setMediaAc3(mediaAc3);
		}

		if (nota.getAs13() == null) {
			nota.setMediaAs3(null);
		} else if (nota.getAs13() != null) {
			if (nota.getMediaAc3() == null) {
				nota.setMediaAc3(0D);
			}
			mediaAs3 = (nota.getMediaAc3() + nota.getAs13());
			mediaAs3 = mediaAs3 / 2;
			nota.setMediaAs3(mediaAs3);
		}
		if (nota.getAs23() == null) {
		} else if (nota.getAs23() != null) {
			if (nota.getMediaAc3() == null) {
				nota.setMediaAc3(0D);
			}
			if (nota.getAs13() == null) {
				nota.setAs13(0D);
			}
			mediaAs3 = (nota.getMediaAc3() + nota.getAs13() + nota.getAs23());
			mediaAs3 = mediaAs3 / 3;
			nota.setMediaAs3(mediaAs3);
		}
		if (nota.getAvaliacaoFinal3() == null) {
			nota.setMediaTrimestral3(0);
			nota.setSituacao3(null);
		} else if (nota.getAvaliacaoFinal3() != null) {
			if (nota.getMediaAs3() == null) {
				nota.setMediaAs3(0D);
			}
			mediaTr3 = ((2 * nota.getMediaAs3()) + nota.getAvaliacaoFinal3());
			mediaTr3 = mediaTr3 / 3;
			mediaTrimestral = (int) Math.round(mediaTr3);
			nota.setMediaTrimestral3(mediaTrimestral);
			if (nota.getMatricula().getClasse().getCiclo().equals("2º CICLO")) {
				if (nota.getMatricula().getClasse().getDescricao().equals("12ª CLASSE")) {
					if (nota.getMediaTrimestral3() >= 10 && nota.getMediaTrimestral3() < 14) {
						nota.setSituacao3("Admitido");
					} else if (nota.getMediaTrimestral3() >= 14 && nota.getMediaTrimestral3() < 21) {
						nota.setSituacao3("Dispensado");
					} else if (nota.getMediaTrimestral3() >= 0 && nota.getMediaTrimestral3() < 10) {
						nota.setSituacao3("Reprovado");
					}
				} else if (nota.getMatricula().getClasse().getDescricao().equals("11ª CLASSE")) {
					if (nota.getMediaTrimestral3() >= 10) {
						nota.setSituacao3("Aprovado");
					} else {
						nota.setSituacao3("Reprovado");
					}
				}
			} else if (nota.getMediaTrimestral3() >= 10) {
				nota.setSituacao3("Aprovado");
			} else {
				nota.setSituacao3("Reprovado");
			}
		}

		if (professorTurma.getProfessor() == null) {

		} else if (professorTurma.getProfessor() != null) {
			nota.setProfessor3(professorTurma.getProfessor());
		}
	}

	// funcao para al
	public void proximoPasso() {
		try {
			disciplinaTurmaSelecionada = true;
			selecionarturma = true;

			disciplinaClasse = disciplinaClasseServico.obterDisciplinasClassePorId(disciplinaClasse.getId());

			this.notas = notaServico.obterNotasPorIdTurmaEDisciplinaDoProfessor(
					disciplinaClasse.getDisciplina().getId(), turmaSelecionada.getId());
			this.professorTurma = professorTurmaServico.obterProfessorTurmaPorIdTurmarPorIdDisciplina(
					turmaSelecionada.getId(), disciplinaClasse.getDisciplina().getId());
			if (this.professorTurma == null) {
				this.professorTurma = new ProfessorTurma();
				this.professorTurma.setTurma(turmaSelecionada);
				this.professorTurma.setDisciplina(disciplinaClasse.getDisciplina());
			}

			this.mediaAvaliacaoContinua = null;
			this.mediaAvaliacaoSomativa = null;
			this.mediaTrimestral = null;

			trimestreSelecionado = trimestreServico.obterTrimestrePorId(trimestreSelecionado.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String listarTurmaParaLancarNotas() {
		this.selecionarturma = false;

		return "/academico/professor/nota/turma?faces-redirect=true";
	}

	public String lancarNotaDoAluno(ProfessorTurma professorTurma) {

		try {
			selecionarturma = true;
			this.professorTurma = professorTurma;
			buscarCadernetaProfessor();
			trimestreSelecionado = trimestreServico.obterTrimestreAtivo();
		} catch (Exception e) {

		}
		return "/academico/professor/nota/nota?faces-redirect=true";
	}

	public String relatorioNotas() {
		this.selecionarturma = false;
		return "/academico/professor/nota/relatorio?faces-redirect=true";
	}

	public String voltarLançarNotas() {
		this.selecionarturma = true;
		return "/academico/professor/index?faces-redirect=true";
	}

	// funcao serve para buscar os alunos e depois ira conseguir lancar as notas
	// de uma disciplina do professor
	public void selecionarTurmaComProfessorDisciplina(ProfessorTurma professorTurma) {
		this.professorTurma = professorTurma;
		buscarCadernetaProfessor();
		try {
			trimestreSelecionado = trimestreServico.obterTrimestreAtivo();
		} catch (Exception e) {

		}
	}

	public void buscarCadernetaProfessor() {
		try {

			this.notas = notaServico.obterNotasPorIdTurmaEDisciplinaDoProfessor(
					this.professorTurma.getDisciplina().getId(), this.professorTurma.getTurma().getId());

			this.mediaAvaliacaoContinua = null;
			this.mediaAvaliacaoSomativa = null;
			this.mediaTrimestral = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscarCadernetaProfessorPorTrimestre() {

		this.notas = notaServico.obterNotasPorIdTurmaPorDisciplinaDoProfessorPorTrimetres(
				this.professorTurma.getDisciplina().getId(), this.professorTurma.getTurma().getId(), trimestre.getId());

	}

	public void selecionarTurma(Turma turma) {
		this.turmaSelecionada = turma;
		disciplinaTurmaSelecionada = false;
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
			trimestreSelecionado = trimestreServico.obterTrimestreAtivo();
			trimestres = trimestreServico.obterTrimestresPorCalendarioVigente();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Trimestre getObterTrimestreAtivo() {
		try {
			return trimestreServico.obterTrimestreAtivo();
		} catch (Exception e) {
			return new Trimestre();
		}

	}

	public void imprimirNotasDeAlunosPorDisciplinaETurmaETrimetres(ProfessorTurma professorTurma) {

		try {
			String caminho = "/academico/relatorio/aluno/notas_alunos_anual_por_disciplina.jasper";
			String nomeProfessor = "Sem Professor", nomeDiciplina = null;
			Map<String, Object> parametro = new HashMap<>();
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
				parametro.put("idDisciplina", professorTurma.getDisciplina().getId());
			} else {
				parametro.put("idDisciplina", null);
			}
			if (professorTurma.getTurma() != null)
				if (professorTurma.getTurma().getId() != null) {
					parametro.put("idTurma", professorTurma.getTurma().getId());
				} else {
					parametro.put("idTurma", null);
				}

			parametro.put("professor", nomeProfessor);
			parametro.put("disciplina", nomeDiciplina);
			String nomeDoc = "CADERNETA_DE_" + nomeDiciplina.toUpperCase() + "_DA_"
					+ professorTurma.getTurma().getClasse().getSigla() + "ª"
					+ professorTurma.getTurma().getDescricao().toUpperCase() + "_DO_CURSO_"
					+ professorTurma.getTurma().getCurso() + ".pdf";
			geradorDeRelatoriosServico.geraPdf(caminho, parametro, nomeDoc);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void matriculaDaTurma() {
		this.matriculas = matriculaServico.listarTodas();
	}

	public void listarAlunosMatriculdoTurma() {
		this.notas = notaServico.obterNotaPorTurma(professorTurma.getTurma().getId());
	}

	public List<Nota> getObterListaAlunosMatriculdoDaTurma() {
		try {
			return notaServico.obterNotaPorTurma(professorTurma.getTurma().getId());
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	// public List<Nota> getObterNotasDoAluno() {
	// return notaServico.obterNotaPorIdAluno();
	// }

	public void voltar() {
		if (disciplinaTurmaSelecionada == false) {
			this.turmaSelecionada = null;
			this.matrizSelecionada = null;
		} else {
			disciplinaTurmaSelecionada = false;
		}

	}

	public void voltarSelecaoTurma() {
		disciplinaTurmaSelecionada = false;
		this.turmaSelecionada = null;
		this.matrizSelecionada = null;

	}

	public void setNota(Nota nota) {
		this.nota = nota;
	}

	public List<Nota> getNotas() {
		return notas;
	}

	public void setNotas(List<Nota> notas) {
		this.notas = notas;
	}

	public ProfessorTurma getProfessorTurma() {
		return professorTurma;
	}

	public void setProfessorTurma(ProfessorTurma professorTurma) {
		this.professorTurma = professorTurma;
	}

	public List<Matricula> getMatriculas() {
		return matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	public String getTipoTrimestre() {
		if (nota != null) {
			setTipoTrimestre("Media Trimestral");
		}
		return tipoTrimestre;
	}

	public void setTipoTrimestre(String tipoTrimestre) {
		this.tipoTrimestre = tipoTrimestre;
	}

	public Nota getNotaSelecionada() {
		return notaSelecionada;
	}

	public void setNotaSelecionada(Nota notaSelecionada) {
		this.notaSelecionada = notaSelecionada;
	}

	public Double getMediaAvaliacaoContinua() {
		return mediaAvaliacaoContinua;
	}

	public void setMediaAvaliacaoContinua(Double mediaAvaliacaoContinua) {
		this.mediaAvaliacaoContinua = mediaAvaliacaoContinua;
	}

	public Double getMediaAvaliacaoSomativa() {
		return mediaAvaliacaoSomativa;
	}

	public void setMediaAvaliacaoSomativa(Double mediaAvaliacaoSomativa) {
		this.mediaAvaliacaoSomativa = mediaAvaliacaoSomativa;
	}

	public Integer getMediaTrimestral() {
		return mediaTrimestral;
	}

	public void setMediaTrimestral(Integer mediaTrimestral) {
		this.mediaTrimestral = mediaTrimestral;
	}

	public boolean isMediaAvaliacaoC() {
		return mediaAvaliacaoC;
	}

	public void setMediaAvaliacaoC(boolean mediaAvaliacaoC) {
		this.mediaAvaliacaoC = mediaAvaliacaoC;
	}

	public boolean isMediaAvaliacaoS() {
		return mediaAvaliacaoS;
	}

	public void setMediaAvaliacaoS(boolean mediaAvaliacaoS) {
		this.mediaAvaliacaoS = mediaAvaliacaoS;
	}

	public boolean isMediaDoTrimestre() {
		return mediaDoTrimestre;
	}

	public void setMediaDoTrimestre(boolean mediaDoTrimestre) {
		this.mediaDoTrimestre = mediaDoTrimestre;
	}

	public List<Trimestre> getTrimestres() {
		return trimestres;
	}

	public void setTrimestres(List<Trimestre> trimestres) {
		this.trimestres = trimestres;
	}

	public Trimestre getTrimestre() {
		return trimestre;
	}

	public void setTrimestre(Trimestre trimestre) {
		this.trimestre = trimestre;
	}

	public Trimestre getTrimestreSelecionado() {
		return trimestreSelecionado;
	}

	public void setTrimestreSelecionado(Trimestre trimestreSelecionado) {
		this.trimestreSelecionado = trimestreSelecionado;
	}

	public boolean isSelecionarturma() {
		return selecionarturma;
	}

	public void setSelecionarturma(boolean selecionarturma) {
		this.selecionarturma = selecionarturma;
	}

	public Turma getTurmaSelecionada() {
		return turmaSelecionada;
	}

	public void setTurmaSelecionada(Turma turmaSelecionada) {
		this.turmaSelecionada = turmaSelecionada;
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

	public DisciplinaClasse getDisciplinaClasse() {
		return disciplinaClasse;
	}

	public void setDisciplinaClasse(DisciplinaClasse disciplinaClasse) {
		this.disciplinaClasse = disciplinaClasse;
	}

	public boolean isDisciplinaTurmaSelecionada() {
		return disciplinaTurmaSelecionada;
	}

	public void setDisciplinaTurmaSelecionada(boolean disciplinaTurmaSelecionada) {
		this.disciplinaTurmaSelecionada = disciplinaTurmaSelecionada;
	}

	public Nota getNota() {
		return nota;
	}

	public List<TipoCurso> getCursos() {
		return cursos;
	}

	public void setCursos(List<TipoCurso> cursos) {
		this.cursos = cursos;
	}

	public List<Matriz> getMatrizes() {
		return matrizes;
	}

	public void setMatrizes(List<Matriz> matrizes) {
		this.matrizes = matrizes;
	}

	public Matriz getMatrizSelecionada() {
		return matrizSelecionada;
	}

	public void setMatrizSelecionada(Matriz matrizSelecionada) {
		this.matrizSelecionada = matrizSelecionada;
	}

}
