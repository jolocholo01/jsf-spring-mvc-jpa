
package com.mz.sistema.gestao.escolar.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import com.mz.sistema.gestao.escolar.enumerado.Ciclo;
import com.mz.sistema.gestao.escolar.enumerado.TipoArea;
import com.mz.sistema.gestao.escolar.enumerado.TipoCurso;
import com.mz.sistema.gestao.escolar.modelo.Area;
import com.mz.sistema.gestao.escolar.modelo.Calendario;
import com.mz.sistema.gestao.escolar.modelo.Classe;
import com.mz.sistema.gestao.escolar.modelo.DisciplinaClasse;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.modelo.Matriz;
import com.mz.sistema.gestao.escolar.modelo.Usuario;
import com.mz.sistema.gestao.escolar.servico.AreaServico;
import com.mz.sistema.gestao.escolar.servico.CalendarioServico;
import com.mz.sistema.gestao.escolar.servico.DisciplinaClasseServico;
import com.mz.sistema.gestao.escolar.servico.EscolaServico;
import com.mz.sistema.gestao.escolar.servico.GeradorDeRelatoriosServico;
import com.mz.sistema.gestao.escolar.servico.MatrizServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;
import com.mz.sistema.gestao.escolar.util.TipoLetra;

@Named
@SessionScope
@Controller
public class MatrizBean {
	private Matriz matriz;
	private Matriz matrizSelecionada;
	private Matriz matrizSelecionadaPraExclusao;
	private Matriz matrizSelecionadaPraFinalizar;
	private Matriz alterarMatriz = new Matriz();
	private List<Matriz> matrizes;
	private List<Matriz> matrizesCurriculares;
	private List<Classe> classes;
	private List<Area> areas;
	private List<TipoCurso> tipoCursos;

	private List<DisciplinaClasse> disciplinaClasses = new ArrayList<>();
	private List<DisciplinaClasse> disciplinaClassesSelecionadaSegundoCiclo = new ArrayList<>();
	private List<DisciplinaClasse> disciplinaClassesArea = new ArrayList<>();
	private List<DisciplinaClasse> disciplinaClassesArea1 = new ArrayList<>();
	private List<DisciplinaClasse> disciplinaClassesAreaEdicao = new ArrayList<>();
	private List<TipoArea> tipoAreas;
	private Classe classeSelecionada;
	private Classe classe;
	private Escola escola;
	private Calendario calendario;
	private List<Ciclo> ciclos;
	private String tipoArea = "TRONCO COMUM";
	private boolean selecionarDisciplinaBoolean = false;
	private boolean proximoBotaoParaEdicaoBoolean = false;
	private boolean finalizarCadastroMatrizBoolean = false;
	private boolean excluirCadastroMatrizBoolean = false;

	private Integer qtdMatrizesEncontradas;

	@Autowired
	private MatrizServico matrizServico;
	@Autowired
	private DisciplinaClasseServico disciplinaClasseServico;

	@Autowired
	private EscolaServico escolaServico;
	@Autowired
	private AreaServico areaServico;
	@Autowired
	private AuthenticationContext authenticationContext;
	@Autowired
	private GeradorDeRelatoriosServico geradorDeRelatoriosServico;

	@Autowired
	private CalendarioServico calendarioServico;

	public void iniciarClasseBean() {
		try {
			this.matrizSelecionada = null;
			matrizesCurriculares = new ArrayList<>();
			classes = new ArrayList<>();
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			escola = funcionarioEscola.getEscola();
			classes = escolaServico.obterClassesPorIdEscola(escola.getId());

			classe = new Classe();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void iniciarBean() {
		try {
			this.matrizSelecionada = null;
			matrizes = new ArrayList<>();
			escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();
			calendario = authenticationContext.getCalendarioEscolar();
			ciclos = Arrays.asList(Ciclo.PRIMEIRO_CICLO, Ciclo.SEGUNDO_CICLO);
			this.classeSelecionada = null;

			classes = escolaServico.obterClassesPorIdEscola(escola.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void busarMatrizes() {
		try {
			qtdMatrizesEncontradas = 0;
			matrizesCurriculares = new ArrayList<>();
			List<Matriz> matrizes = matrizServico.obterMatrizesPorIdClasse(escola.getId(), classe.getId());
			int count = 0, count2 = 0;
			if(matrizes==null){}else
			for (Matriz matriz : matrizes) {
				if (matriz.getCurso().equals("DIURNO")) {
					count++;
					if (count == 1) {
						matrizesCurriculares.add(matriz);
					}
				} else {
					count2++;
					if (count2 == 1) {
						matrizesCurriculares.add(matriz);
					}
				}

			}
			if (matrizesCurriculares != null) {
				qtdMatrizesEncontradas = matrizesCurriculares.size();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void salvar() {
		try {
			if (calendario == null) {
				Mensagem.mensagemAlerta(
						"AVISO:Não pode cadastrar matriz curricular pois, não existe Calendário Escolar no sistema");
				return;
			}
			matriz.setAno(calendario.getAno());
			if (this.classeSelecionada.getTipoEnsino().equals("ENSINO SECUNDÁRIO")) {

				if (classeSelecionada.getCiclo().equals("1º CICLO")) {
					if (matriz.getDisciplinaClasses().isEmpty()) {
						Mensagem.mensagemAlerta("AVISO: selecione disciplinas para cadastrar matriz curricular!");
						return;
					}
					Matriz matrizExistente = matrizServico.disciplinaClasseExistente(classeSelecionada.getId(),
							matriz.getCurso(), escola.getId());
					if (matrizExistente == null) {

					} else if (matrizExistente != null && matriz.getId() != matrizExistente.getId()) {
						Mensagem.mensagemAlerta("AVISO: ja existe disciplinas distribuida nesta classe.");
						return;
					}

				} else if (classeSelecionada.getCiclo().equals("2º CICLO")) {
					matriz.setTipoArea(getLabelDisciplinas());
					Matriz matrizExistente = matrizServico.disciplinaClasseExistenteSegundoCiclo(
							classeSelecionada.getId(), matriz.getCurso(), matriz.getTipoArea(), escola.getId());
					if (matrizExistente == null) {

					} else if (matrizExistente != null && matriz.getId() != matrizExistente.getId()) {
						Mensagem.mensagemAlerta("AVISO: ja existe disciplinas distribuida nesta classe.");
						return;
					}
					if (disciplinaClassesArea.isEmpty()) {
						Mensagem.mensagemAlerta("AVISO: selecione disciplinas para cadastrar matriz curricular!");
						return;
					}

					matriz.setDisciplinaClasses(new ArrayList<>(disciplinaClassesArea1));
					matriz.getDisciplinaClasses().addAll(disciplinaClassesArea);
				}
			}
			matriz.setDataCadastro(new Date());
			matriz.setClasse(classeSelecionada);
			matriz.setEscola(escola);
			Funcionario funcionario = authenticationContext.getFuncionarioLogado();
			matriz.setFuncCadastro(funcionario);

			matriz.setCiclo(classeSelecionada.getCiclo());
			matrizServico.salvar(matriz);
			matriz = new Matriz();

			selecionarDisciplinaBoolean = false;

			Mensagem.mensagemInfo("Matriz curricular incluida com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getLabelDisciplinas() {
		StringBuilder label = new StringBuilder("");
		if (disciplinaClassesArea.isEmpty()) {
			return label.toString();
		}
		List<String> areas = new ArrayList<>();

		for (DisciplinaClasse disciplinaClasse : disciplinaClassesArea) {
			areas.add(disciplinaClasse.getDisciplina().getDescricao());
		}
		Collections.sort(areas);
		System.out.println("Disciplinas : " + areas);
		label.append(areas);
		return label.toString().replace("[", "(").replace("]", ")");
	}

	public void salvarEdicao() {

		try {
			if (matrizSelecionada.getClasse().getCiclo().equals("2º CICLO")) {
				if (disciplinaClassesArea.isEmpty()) {
					Mensagem.mensagemAlerta("AVISO: selecione disciplinas para cadastrar matriz curricular!");
					return;
				}
				matrizSelecionada.setTipoArea(getLabelDisciplinas());
				disciplinaClassesAreaEdicao.removeAll(disciplinaClassesArea);
				disciplinaClassesAreaEdicao.addAll(disciplinaClassesArea);
				matrizSelecionada.setDisciplinaClasses(new ArrayList<>());
				matrizServico.salvar(matrizSelecionada);
				matrizSelecionada.setDisciplinaClasses(new ArrayList<>(disciplinaClassesAreaEdicao));
			}
			matrizSelecionada.setAtiva(false);
			Funcionario funcionario = authenticationContext.getFuncionarioLogado();
			matrizSelecionada.setFuncAlteracao(funcionario);
			matrizSelecionada.setDataAlteracao(new Date());

			matrizServico.salvar(matrizSelecionada);
			if (matrizSelecionada.getClasse().getCiclo().equals("1º CICLO"))
				Mensagem.mensagemInfo("AVISO: Matriz curricular da " + matrizSelecionada.getClasse().getDescricao()
						+ " do curso " + matrizSelecionada.getCurso() + " foi atualizada com sucesso!");
			else
				Mensagem.mensagemInfo("AVISO: Matriz curricular da " + matrizSelecionada.getClasse().getDescricao()
						+ " " + matrizSelecionada.getTipoArea() + " do curso " + matrizSelecionada.getCurso()
						+ " foi atualizada com sucesso!");

			voltarEdicao();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void salvarAfinalizacao() {
		try {
			matrizSelecionadaPraFinalizar.setAtiva(true);
			Funcionario funcionario = authenticationContext.getFuncionarioLogado();
			matrizSelecionadaPraFinalizar.setFuncFinalizar(funcionario);
			matrizSelecionadaPraFinalizar.setDataFinalizacao(new Date());
			matrizServico.salvar(matrizSelecionadaPraFinalizar);
			Mensagem.mensagemInfo("AVISO: A Matriz curricular foi finalizada com sucesso");
			buscarMatrizesPorCiclo();
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.mensagemFatal("AVISO: Hove erro ao finalizar a matriz curricular!");
		}
	}

	public void selecionarClasse(Classe classe) {
		this.matriz = new Matriz();
		tipoCursos = Arrays.asList(TipoCurso.values());
		this.classeSelecionada = classe;
		disciplinaClasses = null;
		disciplinaClassesArea = new ArrayList<>();
		selecionarDisciplinaBoolean = false;
		try {
			if (this.classeSelecionada.getTipoEnsino().equals("ENSINO SECUNDÁRIO")) {
				if (this.classeSelecionada.getCiclo().equals("1º CICLO")) {
					disciplinaClasses = disciplinaClasseServico.obterDisciplinasPorClasse(classeSelecionada.getId());
				} else if (this.classeSelecionada.getCiclo().equals("2º CICLO")) {
					disciplinaClasses = disciplinaClasseServico
							.obterDisciplinasPorClassePorArea(classeSelecionada.getId(), tipoArea);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void imprimirListaAlunosPorClassePorCurso(Matriz matriz) {
		try {
			calendario = calendarioServico.obterCalendarioVigente();
			if (calendario == null) {
				Mensagem.mensagemErro("ERRO: Não exite calendário escolar em vigor!");
				return;
			}
			String filename = "ALUNOS DA " + matriz.getClasse().getDescricao() + " DO CURSO " + matriz.getCurso()+".pdf";
			String caminho = "/academico/relatorio/aluno/listar_alunos_na_classe_por_curso.jasper";
			Map<String, Object> parametro = new HashMap<>();

			parametro.put("idEscola", matriz.getEscola().getId());
			parametro.put("Ano", calendario.getAno());
			parametro.put("idClasse", matriz.getClasse().getId());
			parametro.put("Curso", matriz.getCurso());
			geradorDeRelatoriosServico.geraPdf(caminho, parametro, filename);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void seguinte() {

		try {
			disciplinaClassesArea1 = new ArrayList<>(matriz.getDisciplinaClasses());
			if (disciplinaClassesArea1.isEmpty()) {
				Mensagem.mensagemAlerta("AVISO: selecione disciplinas para cadastrar matriz curricular!");
				return;
			}
			selecionarDisciplinaBoolean = true;
			areas = areaServico.obterAreasPorCicloOrdenarPorOrdemDecrescenteDiferenteDaArea(
					this.classeSelecionada.getCiclo(), tipoArea);
			disciplinaClassesSelecionadaSegundoCiclo = new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void proximo() {

		try {
			disciplinaClassesAreaEdicao = new ArrayList<>(matrizSelecionada.getDisciplinaClasses());
			if (disciplinaClassesAreaEdicao.isEmpty()) {
				Mensagem.mensagemAlerta("AVISO: selecione disciplinas para cadastrar matriz curricular!");
				return;
			}
			proximoBotaoParaEdicaoBoolean = true;
			areas = areaServico.obterAreasPorCicloOrdenarPorOrdemDecrescenteDiferenteDaArea(
					this.matrizSelecionada.getClasse().getCiclo(), tipoArea);
			disciplinaClassesSelecionadaSegundoCiclo = disciplinaClasseServico.obterDisciplinasPorClasseEArea(
					this.matrizSelecionada.getClasse().getId(), this.matrizSelecionada.getArea().getId());
			// matrizServico.salvar(this.matrizSelecionada);
			matrizSelecionada = matrizServico.obterMatrizPorIdELeftJoin(matrizSelecionada.getId());
			disciplinaClassesArea = new ArrayList<>(matrizSelecionada.getDisciplinaClasses());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void prepararMatrizParaExcluir(Matriz matriz) {
		this.matrizSelecionadaPraExclusao = matriz;
		String curso = TipoLetra.capitalizeString(this.matrizSelecionadaPraExclusao.getCurso());
		this.matrizSelecionadaPraExclusao.setCurso(curso);
		finalizarCadastroMatrizBoolean = false;
		excluirCadastroMatrizBoolean = true;

	}

	public void excluir() {
		try {
			matrizServico.exclui(this.matrizSelecionadaPraExclusao);
			Mensagem.mensagemInfo("Matriz curricular removida com sucesso");
			buscarMatrizesPorCiclo();
		} catch (Exception e) {
			Mensagem.mensagemInfo("AVISO: não foi possivel remover a Matriz curricular através da dependência.");
		}
	}

	public void selecionarMatriz(Matriz matriz) {
		disciplinaClassesArea = new ArrayList<>();
		tipoCursos = Arrays.asList(TipoCurso.values());
		proximoBotaoParaEdicaoBoolean = false;
		finalizarCadastroMatrizBoolean = false;

		try {
			this.matrizSelecionada = matrizServico.obterMatrizPorIdELeftJoin(matriz.getId());
			if (matriz.getClasse().getTipoEnsino().equals("ENSINO SECUNDÁRIO")) {

				if (matriz.getClasse().getCiclo().equals("1º CICLO")) {
					disciplinaClasses = disciplinaClasseServico.obterDisciplinasPorClasse(matriz.getClasse().getId());
				} else if (matriz.getClasse().getCiclo().equals("2º CICLO")) {
					disciplinaClasses = disciplinaClasseServico
							.obterDisciplinasPorClassePorArea(matriz.getClasse().getId(), tipoArea);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void selecionarMatrizParaFinalizar(Matriz matriz) {
		disciplinaClassesArea = new ArrayList<>();
		tipoCursos = Arrays.asList(TipoCurso.values());
		proximoBotaoParaEdicaoBoolean = false;
		finalizarCadastroMatrizBoolean = true;
		excluirCadastroMatrizBoolean = false;
		this.matrizSelecionadaPraFinalizar = new Matriz();

		try {
			this.matrizSelecionadaPraFinalizar = matrizServico.obterMatrizPorIdELeftJoin(matriz.getId());
			if (matriz.getClasse().getTipoEnsino().equals("ENSINO SECUNDÁRIO")) {

				if (matriz.getClasse().getCiclo().equals("1º CICLO")) {
					disciplinaClasses = disciplinaClasseServico.obterDisciplinasPorClasse(matriz.getClasse().getId());
				} else if (matriz.getClasse().getCiclo().equals("2º CICLO")) {
					disciplinaClasses = disciplinaClasseServico
							.obterDisciplinasPorClassePorArea(matriz.getClasse().getId(), tipoArea);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void voltar() {
		if (selecionarDisciplinaBoolean == true) {
			selecionarDisciplinaBoolean = false;
			disciplinaClassesSelecionadaSegundoCiclo = new ArrayList<>();
		} else if (selecionarDisciplinaBoolean != true) {
			this.matrizSelecionada = null;
			this.classeSelecionada = null;
			disciplinaClasses = null;
		}

	}

	public void voltarEdicao() {

		try {
			buscarMatrizesPorCiclo();
			if (proximoBotaoParaEdicaoBoolean == true) {
				proximoBotaoParaEdicaoBoolean = false;
				this.matrizSelecionada = matrizServico.obterMatrizPorIdELeftJoinAtiva(
						this.matrizSelecionada.getClasse().getId(), this.matrizSelecionada.getCiclo(),
						this.matrizSelecionada.getEscola().getId());
				;
				disciplinaClassesArea = new ArrayList<>();
			} else if (proximoBotaoParaEdicaoBoolean != true) {
				this.matrizSelecionada = null;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscarDisciplinaDaClasse() {
		if (classeSelecionada == null) {
		} else if (classeSelecionada != null) {
			disciplinaClasses = disciplinaClasseServico.obterDisciplinasPorClasse(classeSelecionada.getId());

		}
	}

	public void buscarDisciplinaDaClassePorArea() {

		if (classeSelecionada == null) {
		} else if (classeSelecionada != null) {
			if (selecionarDisciplinaBoolean == false) {
				disciplinaClasses = disciplinaClasseServico.obterDisciplinasPorClassePorArea(classeSelecionada.getId(),
						tipoArea);
			} else if (selecionarDisciplinaBoolean != false) {

				disciplinaClassesSelecionadaSegundoCiclo = disciplinaClasseServico
						.obterDisciplinasPorClasseEArea(classeSelecionada.getId(), matriz.getArea().getId());

				System.out.println("\n\\n Id= " + matriz.getArea().getId());
			}

		}
		System.out.println("Chamou a funcao\n\n\n\n");
		System.out.println("Id= " + matriz.getArea().getId());
	}

	public void buscarDisciplinaDaClassePorAreaEdicao() {

		if (matrizSelecionada == null) {
		} else if (matrizSelecionada != null) {
			if (proximoBotaoParaEdicaoBoolean == false) {
				disciplinaClasses = disciplinaClasseServico
						.obterDisciplinasPorClassePorArea(matrizSelecionada.getClasse().getId(), tipoArea);
			} else if (proximoBotaoParaEdicaoBoolean != false) {

				disciplinaClassesSelecionadaSegundoCiclo = disciplinaClasseServico.obterDisciplinasPorClasseEArea(
						matrizSelecionada.getClasse().getId(), matrizSelecionada.getArea().getId());
			}

		}
		System.out.println("Chamou a funcao\n\n\n\n");
	}

	public void buscarMatrizesPorCiclo() {
		finalizarCadastroMatrizBoolean = false;
		try {
			matrizes = new ArrayList<>();
			if (calendario == null) {
			} else if (escola == null) {
			} else if (calendario != null && escola != null) {
				List<Matriz> matrizes = matrizServico.obterMatrizPorEscolaPorCiclo(alterarMatriz.getCiclo(),
						escola.getId());
				int count = 0;
				if (!matrizes.isEmpty()) {
					for (Matriz matriz : matrizes) {
						count++;
						matriz.setOrdem(count);
						this.matrizes.add(matriz);

					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Matriz getMatriz() {
		return matriz;
	}

	public void setMatriz(Matriz matriz) {
		this.matriz = matriz;
	}

	public List<Matriz> getMatrizes() {
		return matrizes;
	}

	public void setMatrizes(List<Matriz> matrizes) {
		this.matrizes = matrizes;
	}

	public List<DisciplinaClasse> getDisciplinaClasses() {
		return disciplinaClasses;
	}

	public void setDisciplinaClasses(List<DisciplinaClasse> disciplinaClasses) {
		this.disciplinaClasses = disciplinaClasses;
	}

	public Classe getClasseSelecionada() {
		return classeSelecionada;
	}

	public void setClasseSelecionada(Classe classeSelecionada) {
		this.classeSelecionada = classeSelecionada;
	}

	public List<Classe> getClasses() {
		return classes;
	}

	public void setClasses(List<Classe> classes) {
		this.classes = classes;
	}

	public List<TipoCurso> getTipoCursos() {
		return tipoCursos;
	}

	public void setTipoCursos(List<TipoCurso> tipoCursos) {
		this.tipoCursos = tipoCursos;
	}

	public Escola getEscola() {
		return escola;
	}

	public void setEscola(Escola escola) {
		this.escola = escola;
	}

	public Calendario getCalendario() {
		return calendario;
	}

	public void setCalendario(Calendario calendario) {
		this.calendario = calendario;
	}

	public Matriz getAlterarMatriz() {
		return alterarMatriz;
	}

	public void setAlterarMatriz(Matriz alterarMatriz) {
		this.alterarMatriz = alterarMatriz;
	}

	public List<Ciclo> getCiclos() {
		return ciclos;
	}

	public void setCiclos(List<Ciclo> ciclos) {
		this.ciclos = ciclos;
	}

	public Matriz getMatrizSelecionada() {
		return matrizSelecionada;
	}

	public void setMatrizSelecionada(Matriz matrizSelecionada) {
		this.matrizSelecionada = matrizSelecionada;
	}

	public Matriz getMatrizSelecionadaPraExclusao() {
		return matrizSelecionadaPraExclusao;
	}

	public void setMatrizSelecionadaPraExclusao(Matriz matrizSelecionadaPraExclusao) {
		this.matrizSelecionadaPraExclusao = matrizSelecionadaPraExclusao;
	}

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

	public String getTipoArea() {
		return tipoArea;
	}

	public void setTipoArea(String tipoArea) {
		this.tipoArea = tipoArea;
	}

	public boolean isSelecionarDisciplinaBoolean() {
		return selecionarDisciplinaBoolean;
	}

	public void setSelecionarDisciplinaBoolean(boolean selecionarDisciplinaBoolean) {
		this.selecionarDisciplinaBoolean = selecionarDisciplinaBoolean;
	}

	public List<DisciplinaClasse> getDisciplinaClassesArea() {
		return disciplinaClassesArea;
	}

	public void setDisciplinaClassesArea(List<DisciplinaClasse> disciplinaClassesArea) {
		this.disciplinaClassesArea = disciplinaClassesArea;
	}

	public List<DisciplinaClasse> getDisciplinaClassesArea1() {
		return disciplinaClassesArea1;
	}

	public void setDisciplinaClassesArea1(List<DisciplinaClasse> disciplinaClassesArea1) {
		this.disciplinaClassesArea1 = disciplinaClassesArea1;
	}

	public List<TipoArea> getTipoAreas() {
		return tipoAreas;
	}

	public void setTipoAreas(List<TipoArea> tipoAreas) {
		this.tipoAreas = tipoAreas;
	}

	public List<DisciplinaClasse> getDisciplinaClassesSelecionadaSegundoCiclo() {
		return disciplinaClassesSelecionadaSegundoCiclo;
	}

	public void setDisciplinaClassesSelecionadaSegundoCiclo(
			List<DisciplinaClasse> disciplinaClassesSelecionadaSegundoCiclo) {
		this.disciplinaClassesSelecionadaSegundoCiclo = disciplinaClassesSelecionadaSegundoCiclo;
	}

	public boolean isProximoBotaoParaEdicaoBoolean() {
		return proximoBotaoParaEdicaoBoolean;
	}

	public void setProximoBotaoParaEdicaoBoolean(boolean proximoBotaoParaEdicaoBoolean) {
		this.proximoBotaoParaEdicaoBoolean = proximoBotaoParaEdicaoBoolean;
	}

	public List<DisciplinaClasse> getDisciplinaClassesAreaEdicao() {
		return disciplinaClassesAreaEdicao;
	}

	public void setDisciplinaClassesAreaEdicao(List<DisciplinaClasse> disciplinaClassesAreaEdicao) {
		this.disciplinaClassesAreaEdicao = disciplinaClassesAreaEdicao;
	}

	public boolean isFinalizarCadastroMatrizBoolean() {
		return finalizarCadastroMatrizBoolean;
	}

	public void setFinalizarCadastroMatrizBoolean(boolean finalizarCadastroMatrizBoolean) {
		this.finalizarCadastroMatrizBoolean = finalizarCadastroMatrizBoolean;
	}

	public List<Matriz> getMatrizesCurriculares() {
		return matrizesCurriculares;
	}

	public void setMatrizesCurriculares(List<Matriz> matrizesCurriculares) {
		this.matrizesCurriculares = matrizesCurriculares;
	}

	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	public Integer getQtdMatrizesEncontradas() {
		return qtdMatrizesEncontradas;
	}

	public void setQtdMatrizesEncontradas(Integer qtdMatrizesEncontradas) {
		this.qtdMatrizesEncontradas = qtdMatrizesEncontradas;
	}

	public Matriz getMatrizSelecionadaPraFinalizar() {
		return matrizSelecionadaPraFinalizar;
	}

	public void setMatrizSelecionadaPraFinalizar(Matriz matrizSelecionadaPraFinalizar) {
		this.matrizSelecionadaPraFinalizar = matrizSelecionadaPraFinalizar;
	}

	public boolean isExcluirCadastroMatrizBoolean() {
		return excluirCadastroMatrizBoolean;
	}

	public void setExcluirCadastroMatrizBoolean(boolean excluirCadastroMatrizBoolean) {
		this.excluirCadastroMatrizBoolean = excluirCadastroMatrizBoolean;
	}

}
