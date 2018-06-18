// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.modelo.Calendario;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.modelo.Matricula;
import com.mz.sistema.gestao.escolar.modelo.Trimestre;
import com.mz.sistema.gestao.escolar.servico.CalendarioServico;
import com.mz.sistema.gestao.escolar.servico.MatriculaServico;
import com.mz.sistema.gestao.escolar.servico.TrimestreServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Named
@SessionScope
@Controller
public class CalendarioBean {
	private Calendario calendario = new Calendario();
	private List<Calendario> calendarios = new ArrayList<>();
	private Trimestre trimestre1 = new Trimestre();
	private Trimestre trimestre2 = new Trimestre();
	private Trimestre trimestre3 = new Trimestre();
	private List<Trimestre> trimestres;
	private String pesquisa;
	private Calendario calendarioSelecionado;
	private Calendario calendarioSelecionadoExclusao;
	private Integer qtdCalendariosEncontrados;
	private Integer anoEscolar;
	private boolean cadastroCalendarioBoolean = false;
	private boolean novoCalendarioBoolean = false;
	private boolean editarCalendarioBoolean = false;

	private Matricula alunosNovos;
	private Matricula alunosRenovados;
	private Matricula alunosTransferidos;
	private Integer qtdAlunosNovos;
	private Integer qtdAlunosNovosMatriculados;
	private Integer qtdAlunosRenovados;
	private Integer qtdAlunosRenovadosEfetuado;
	private Integer qtdAlunosTransferidos;
	private Integer qtdAlunosEscola;
	private Integer qtdAlunosEscolaEfetuado;

	@Autowired
	private CalendarioServico calendarioServico;
	@Autowired
	private AuthenticationContext authenticationContext;
	@Autowired
	private TrimestreServico trimestreServico;
	@Autowired
	private MatriculaServico matriculaServico;

	private Integer quantidadeCaracteres;

	

	// So esse metodao deve ser carregado por director da escola
		public void iniciarBean() {
			try {
				calendario = calendarioServico.obterCalendarioVigente();
				calendarios = calendarioServico.listarTodos();
				if (calendario == null) {
					calendario = new Calendario();
					this.trimestres = null;
				} else if (calendario != null) {
					this.trimestres = trimestreServico.obterTrimestrePorIdCalendario(calendario.getId());
				}
				caregarAcompanharMatricula();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	public void caregarAcompanharMatricula() {
		alunosNovos = new Matricula();
		alunosRenovados = new Matricula();
		alunosTransferidos = new Matricula();
		qtdAlunosNovos = 0;
		qtdAlunosRenovados = 0;
		qtdAlunosTransferidos = 0;
		qtdAlunosEscola = 0;
		qtdAlunosNovosMatriculados = 0;
		qtdAlunosRenovadosEfetuado = 0;
		qtdAlunosEscolaEfetuado = 0;
		try {
			if (calendario == null) {
			} else if (calendario != null) {
				FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
				if (funcionarioEscola != null) {
					Escola idEscolaOrigem = funcionarioEscola.getEscola();
					if (idEscolaOrigem != null) {
						List<Matricula> matriculas = matriculaServico.obterEstatistcaMatriculasPorEscolaPorAno(idEscolaOrigem.getId(),
								calendario.getAno());
						if (!matriculas.isEmpty())
							for (Matricula matricula : matriculas) {
								// if
								// (matricula.getAcomanhamentoMatricula().getDescricao().equals("NOVOS
								// ALUNOS")) {
								if (matricula.getAcompanhamento() == 1) {
									alunosNovos = matricula;
									qtdAlunosNovos++;
									qtdAlunosEscola++;
									if (matricula.isAtivo()
											&& (matricula.getTurma() != null || matricula.getTurmaDestino() != null)) {
										qtdAlunosNovosMatriculados++;
										qtdAlunosEscolaEfetuado++;

									}
								}

								if (matricula.getAcompanhamento()==2) {
									alunosRenovados = matricula;
									qtdAlunosRenovados++;
									qtdAlunosEscola++;
									if (matricula.isAtivo()
											&& (matricula.getTurma() != null || matricula.getTurmaDestino() != null)) {
										qtdAlunosRenovadosEfetuado++;
										qtdAlunosEscolaEfetuado++;
									}
								}

							}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void salvar() {

		try {
			Funcionario funcionario=(Funcionario) authenticationContext.getUsuarioLogado();
			Calendario IdCalendario = calendarioServico.obterCalendarioPorAno(calendario.getAno());
			if (IdCalendario != null && IdCalendario.getId() != calendario.getId()) {
				Mensagem.mensagemInfo("Já existe este calendário escolar cadastrado no sistema!");
				return;
			}
			calendarioVigente();
			if (calendario.isAtivo() == true) {

				Calendario calendarioVigente = calendarioServico.obterCalendarioVigente(calendario.isAtivo());
				if (calendarioVigente == null) {

				} else if (calendarioVigente != null && calendarioVigente.getId() != calendario.getId()) {
					Trimestre trimestre = trimestreServico.obterTrimestreAtivo();
					trimestre.setAtivo(false);
					trimestreServico.salvar(trimestre);
					calendarioVigente.setAtivo(false);
					calendarioServico.salvar(calendarioVigente);

				}
			} else {
				trimestre1.setAtivo(false);
				trimestre2.setAtivo(false);
				trimestre3.setAtivo(false);
			}
			calendario.setFuncionario(funcionario);
			calendario.setPesquisa(String.valueOf(calendario.getAno()));
			calendario = calendarioServico.salvarERetornarCalendario(calendario);

			trimestre1.setDescricao("1º Trimestre");
			trimestre1.setCalendario(calendario);
			trimestreServico.salvar(trimestre1);

			trimestre2.setDescricao("2º Trimestre");
			trimestre2.setCalendario(calendario);
			trimestreServico.salvar(trimestre2);

			trimestre3.setDescricao("3º Trimestre");
			trimestre3.setCalendario(calendario);
			trimestreServico.salvar(trimestre3);
			trimestre1 = new Trimestre();
			trimestre2 = new Trimestre();
			trimestre3 = new Trimestre();

			Mensagem.mensagemInfo("Calendário Escolar Inserido com sucesso!");
			calendarios = calendarioServico.obterCalendarioPorPesquisa(calendario.getPesquisa());
			/*
			 * Calendario calendarioEscolar =
			 * authenticationContext.getCalendarioEscolar(); if
			 * (calendarioEscolar == null) { this.trimestres = null; } else if
			 * (calendarioEscolar != null) { this.trimestres =
			 * trimestreServico.obterTrimestrePorIdCalendario(calendarioEscolar.
			 * getId()); }
			 */
			pesquisa = calendario.getPesquisa();
			voltarParaPequisa();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void calendarioVigente() {
		if (calendario.isAtivo() == true) {
			if (trimestre1.isAtivo() == false && trimestre2.isAtivo() == false && trimestre3.isAtivo() == false) {
				trimestre1.setAtivo(true);
			}
		}
	}

	public void trimestre1Vigente() {

		if (trimestre1.isAtivo() == true) {
			trimestre2.setAtivo(false);
			trimestre3.setAtivo(false);

		}
	}

	public void trimestre2Vigente() {

		if (trimestre2.isAtivo() == true) {
			trimestre1.setAtivo(false);
			trimestre3.setAtivo(false);

		}
	}

	public void trimestre3Vigente() {

		if (trimestre3.isAtivo() == true) {
			trimestre1.setAtivo(false);
			trimestre2.setAtivo(false);

		}
	}

	public String cadastroCalendario() {
		calendario = null;
		this.cadastroCalendarioBoolean = false;
		this.novoCalendarioBoolean = false;
		calendarios = new ArrayList<>();
		pesquisa = null;
		qtdCalendariosEncontrados = 0;
		calendarioSelecionado = null;
		return "/academico/director-ditrital/calendario/gerenciar?faces-redirect=true";
	}

	public void novoCalendario() {
		trimestre1 = new Trimestre();
		trimestre2 = new Trimestre();
		trimestre3 = new Trimestre();
		this.cadastroCalendarioBoolean = true;
		this.novoCalendarioBoolean = true;
		this.editarCalendarioBoolean = false;
		this.qtdCalendariosEncontrados = 0;
		calendario = new Calendario();
		calendario.setAtivo(false);
		calendario.setDataCadastro(new Date());

	}

	public void voltarParaPequisa() {
		calendario = null;
		this.cadastroCalendarioBoolean = false;
		this.editarCalendarioBoolean = false;
		if (novoCalendarioBoolean == true) {
			novoCalendarioBoolean = false;
		}
		pequisaCalendario();

	}

	public void pequisaCalendario() {
		calendarios = null;
		qtdCalendariosEncontrados = 0;
		try {
			if (!pesquisa.trim().equals(""))
				calendarios = calendarioServico.obterCalendarioPorPesquisa(pesquisa);

			if (calendarios != null) {
				qtdCalendariosEncontrados = calendarios.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void editar(Calendario calendario) {
		try {
			this.calendario = calendario;
			this.calendario.setDataAlteracao(new Date());
			this.cadastroCalendarioBoolean = true;
			this.editarCalendarioBoolean = true;
			if (this.calendario.getObservacao() != null)
				this.quantidadeCaracteres = this.calendario.getObservacao().length();
			trimestres = trimestreServico.obterTrimestrePorIdCalendario(calendario.getId());
			if (trimestres != null) {
				for (Trimestre trimestre : trimestres) {
					if (trimestre.getDescricao().equals("1º Trimestre")) {
						trimestre1.setAtivo(trimestre.isAtivo());
						trimestre1.setCalendario(trimestre.getCalendario());
						trimestre1.setDataInicio(trimestre.getDataInicio());
						trimestre1.setDataFinal(trimestre.getDataFinal());
						trimestre1.setSemana(trimestre.getSemana());
						trimestre1.setDescricao(trimestre.getDescricao());
						trimestre1.setId(trimestre.getId());
					} else if (trimestre.getDescricao().equals("2º Trimestre")) {
						trimestre2.setAtivo(trimestre.isAtivo());
						trimestre2.setCalendario(trimestre.getCalendario());
						trimestre2.setDataInicio(trimestre.getDataInicio());
						trimestre2.setDataFinal(trimestre.getDataFinal());
						trimestre2.setSemana(trimestre.getSemana());
						trimestre2.setDescricao(trimestre.getDescricao());
						trimestre2.setId(trimestre.getId());
					} else if (trimestre.getDescricao().equals("3º Trimestre")) {
						trimestre3.setAtivo(trimestre.isAtivo());
						trimestre3.setCalendario(trimestre.getCalendario());
						trimestre3.setDataInicio(trimestre.getDataInicio());
						trimestre3.setDataFinal(trimestre.getDataFinal());
						trimestre3.setSemana(trimestre.getSemana());
						trimestre3.setDescricao(trimestre.getDescricao());
						trimestre3.setId(trimestre.getId());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void prepararParaExcluir(Calendario calendario) {
		this.calendarioSelecionadoExclusao = calendario;

	}

	public void excluir() {
		try {
			if (this.calendarioSelecionadoExclusao.isAtivo()) {
				Mensagem.mensagemAlerta(
						"Aviso: O calendário escolar não foi excluido através de ser calendário vigente.");
			} else {
				trimestres = trimestreServico.obterTrimestrePorIdCalendario(this.calendarioSelecionadoExclusao.getId());
				if (trimestres != null) {
					for (Trimestre trimestre : trimestres) {
						trimestreServico.excluir(trimestre);
					}
				}
				calendarioServico.excluir(calendarioSelecionadoExclusao);
				Mensagem.mensagemInfo("Aviso: O calendário escolar foi excluido com sucesso!");
				pequisaCalendario();
			}
		} catch (Exception e) {
			Mensagem.mensagemAlerta(
					"ERRO: Não foi possível excluir esse registo pois exitem dependências a ele em outras tabelas!");
		}

	}

	public List<Trimestre> getTrimestrePorCalendario() {
		List<Trimestre> trimestres = null;
		Calendario calendario = authenticationContext.getCalendarioEscolar();
		if (calendario == null) {
			this.trimestres = null;
		} else if (calendario != null) {
			trimestres = trimestreServico.obterTrimestrePorIdCalendario(calendario.getId());
		}
		return trimestres;

	}

	public List<Calendario> getObterTodosCalendarios() {
		return calendarioServico.listarTodos();
	}

	public Calendario getCalendario() {
		return calendario;
	}

	public void setCalendario(Calendario calendario) {
		this.calendario = calendario;
	}

	public List<Calendario> getCalendarios() {
		return calendarios;
	}

	public void setCalendarios(List<Calendario> calendarios) {
		this.calendarios = calendarios;
	}

	public Trimestre getTrimestre1() {
		return trimestre1;
	}

	public void setTrimestre1(Trimestre trimestre1) {
		this.trimestre1 = trimestre1;
	}

	public Trimestre getTrimestre2() {
		return trimestre2;
	}

	public void setTrimestre2(Trimestre trimestre2) {
		this.trimestre2 = trimestre2;
	}

	public Trimestre getTrimestre3() {
		return trimestre3;
	}

	public void setTrimestre3(Trimestre trimestre3) {
		this.trimestre3 = trimestre3;
	}

	public List<Trimestre> getTrimestres() {
		return trimestres;
	}

	public void setTrimestres(List<Trimestre> trimestres) {
		this.trimestres = trimestres;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public boolean isNovoCalendarioBoolean() {
		return novoCalendarioBoolean;
	}

	public void setNovoCalendarioBoolean(boolean novoCalendarioBoolean) {
		this.novoCalendarioBoolean = novoCalendarioBoolean;
	}

	public boolean isEditarCalendarioBoolean() {
		return editarCalendarioBoolean;
	}

	public void setEditarCalendarioBoolean(boolean editarCalendarioBoolean) {
		this.editarCalendarioBoolean = editarCalendarioBoolean;
	}

	public boolean isCadastroCalendarioBoolean() {
		return cadastroCalendarioBoolean;
	}

	public void setCadastroCalendarioBoolean(boolean cadastroCalendarioBoolean) {
		this.cadastroCalendarioBoolean = cadastroCalendarioBoolean;
	}

	public Calendario getCalendarioSelecionado() {
		return calendarioSelecionado;
	}

	public void setCalendarioSelecionado(Calendario calendarioSelecionado) {
		this.calendarioSelecionado = calendarioSelecionado;
	}

	public Integer getQtdCalendariosEncontrados() {
		return qtdCalendariosEncontrados;
	}

	public void setQtdCalendariosEncontrados(Integer qtdCalendariosEncontrados) {
		this.qtdCalendariosEncontrados = qtdCalendariosEncontrados;
	}

	public Integer getQuantidadeCaracteres() {
		return quantidadeCaracteres;
	}

	public void setQuantidadeCaracteres(Integer quantidadeCaracteres) {
		this.quantidadeCaracteres = quantidadeCaracteres;
	}

	public Calendario getCalendarioSelecionadoExclusao() {
		return calendarioSelecionadoExclusao;
	}

	public void setCalendarioSelecionadoExclusao(Calendario calendarioSelecionadoExclusao) {
		this.calendarioSelecionadoExclusao = calendarioSelecionadoExclusao;
	}

	public Matricula getAlunosNovos() {
		return alunosNovos;
	}

	public void setAlunosNovos(Matricula alunosNovos) {
		this.alunosNovos = alunosNovos;
	}

	public Matricula getAlunosRenovados() {
		return alunosRenovados;
	}

	public void setAlunosRenovados(Matricula alunosRenovados) {
		this.alunosRenovados = alunosRenovados;
	}

	public Matricula getAlunosTransferidos() {
		return alunosTransferidos;
	}

	public void setAlunosTransferidos(Matricula alunosTransferidos) {
		this.alunosTransferidos = alunosTransferidos;
	}

	public Integer getQtdAlunosNovos() {
		return qtdAlunosNovos;
	}

	public void setQtdAlunosNovos(Integer qtdAlunosNovos) {
		this.qtdAlunosNovos = qtdAlunosNovos;
	}

	public Integer getQtdAlunosRenovados() {
		return qtdAlunosRenovados;
	}

	public void setQtdAlunosRenovados(Integer qtdAlunosRenovados) {
		this.qtdAlunosRenovados = qtdAlunosRenovados;
	}

	public Integer getQtdAlunosTransferidos() {
		return qtdAlunosTransferidos;
	}

	public void setQtdAlunosTransferidos(Integer qtdAlunosTransferidos) {
		this.qtdAlunosTransferidos = qtdAlunosTransferidos;
	}

	public Integer getQtdAlunosEscola() {
		return qtdAlunosEscola;
	}

	public void setQtdAlunosEscola(Integer qtdAlunosEscola) {
		this.qtdAlunosEscola = qtdAlunosEscola;
	}

	public Integer getQtdAlunosNovosMatriculados() {
		return qtdAlunosNovosMatriculados;
	}

	public void setQtdAlunosNovosMatriculados(Integer qtdAlunosNovosMatriculados) {
		this.qtdAlunosNovosMatriculados = qtdAlunosNovosMatriculados;
	}

	public Integer getQtdAlunosRenovadosEfetuado() {
		return qtdAlunosRenovadosEfetuado;
	}

	public void setQtdAlunosRenovadosEfetuado(Integer qtdAlunosRenovadosEfetuado) {
		this.qtdAlunosRenovadosEfetuado = qtdAlunosRenovadosEfetuado;
	}

	public Integer getQtdAlunosEscolaEfetuado() {
		return qtdAlunosEscolaEfetuado;
	}

	public void setQtdAlunosEscolaEfetuado(Integer qtdAlunosEscolaEfetuado) {
		this.qtdAlunosEscolaEfetuado = qtdAlunosEscolaEfetuado;
	}

	@SuppressWarnings("deprecation")
	public Integer getAnoEscolar() {
		try {
			
			Date date=new Date();
			anoEscolar=date.getYear();
		} catch (Exception e) {
					}
		return anoEscolar;
	}

	public void setAnoEscolar(Integer anoEscolar) {
		this.anoEscolar = anoEscolar;
	}

}
