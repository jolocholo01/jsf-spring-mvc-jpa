package com.mz.sistema.gestao.escolar.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.mz.sistema.gestao.escolar.enumerado.EstadoTransferencia;
import com.mz.sistema.gestao.escolar.modelo.Calendario;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.modelo.Transferencia;
import com.mz.sistema.gestao.escolar.modelo.Turma;
import com.mz.sistema.gestao.escolar.servico.MatriculaServico;
import com.mz.sistema.gestao.escolar.servico.TransferenciaServico;
import com.mz.sistema.gestao.escolar.servico.TurmaServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;
import com.mz.sistema.gestao.escolar.util.ValorExtenso;

@Named
@Controller
@SessionScope
public class TransferenciaBean {
	private Transferencia transferencia;
	private Transferencia transferenciaSelacionada;
	private Transferencia transferenciaSelacionadaExclusao;
	private List<Transferencia> transferencias = new ArrayList<>();
	private List<Transferencia> pedidosTransferencia = new ArrayList<>();
	private List<Turma> turmas = new ArrayList<>();
	private boolean estadoMatriculaSelacionada = false;
	private String pesquisa;

	private int quantidadeCaracteres;
	private Integer quantidadePedidoTransferencia;
	private String quantidadePedidoTransferenciaAlgarismo;
	private Integer quantidadeAlunos;

	@Autowired
	private TurmaServico turmaServico;
	@Autowired
	private TransferenciaServico transferenciaServico;
	@Autowired
	private AuthenticationContext authenticationContext;
	@Autowired
	private MatriculaServico matriculaServico;

	public void iniciarBean() {

		try {
			pesquisa = new String();
			quantidadePedidoTransferencia = 0;
			estadoMatriculaSelacionada = false;
			transferencias = new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void iniciarPedidoTransferenciaBean() {

		try {
			pesquisa = new String();
			quantidadePedidoTransferencia = 0;
			estadoMatriculaSelacionada = false;
			transferencias = new ArrayList<>();
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();
			Calendario calendario = authenticationContext.getCalendarioEscolar();
			transferencias = transferenciaServico.receberTransferenciasPorIdEscolaPorAnoComEstadoFalse(escola.getId(),
					calendario.getAno());
			if (transferencias != null) {
				quantidadeAlunos = transferencias.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void pedidoscaregadoPorPoll() {

		try {

			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();
			Calendario calendario = authenticationContext.getCalendarioEscolar();
			pedidosTransferencia = transferenciaServico
					.receberTransferenciasPorIdEscolaPorAnoComEstadoFalse(escola.getId(), calendario.getAno());
			if (pedidosTransferencia == null) {
				quantidadePedidoTransferencia = 0;
			} else {
				quantidadePedidoTransferencia = pedidosTransferencia.size();
				ValorExtenso valorExtenso = new ValorExtenso();
				quantidadePedidoTransferenciaAlgarismo = valorExtenso
						.write(BigDecimal.valueOf(quantidadePedidoTransferencia)).toLowerCase()
						.replace("um mil ", "mil ").replace(" metical", "").replace(" meticais", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void receberTransferenciasPesquisado() {

		try {
			// quantidadePedidoTransferencia = 0;
			estadoMatriculaSelacionada = false;
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();
			Calendario calendario = authenticationContext.getCalendarioEscolar();
			transferencias = transferenciaServico.receberTransferenciasPorIdEscolaPorAno(pesquisa, escola.getId(),
					calendario.getAno());
			if (transferencias != null) {
				quantidadeAlunos = transferencias.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buscarTransferenciasPesquisado() {

		try {
			// quantidadePedidoTransferencia = 0;
			estadoMatriculaSelacionada = false;
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();
			Calendario calendario = authenticationContext.getCalendarioEscolar();
			transferencias = transferenciaServico.obterTransferenciasPorIdEscolaPorAno(pesquisa, escola.getId(),
					calendario.getAno());
			if (transferencias != null) {
				quantidadeAlunos = transferencias.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void salvar() {

		try {
			Turma turma = turmaServico
					.obterTurmaPorId(transferenciaSelacionada.getMatricula().getTurmaDestino().getId());
			Integer inscritos = turma.getInscrito() + 1;
			turma.setInscrito(inscritos);
			turmaServico.salvar(turma);
			Integer numeroUltimoAlunoNaTurma = matriculaServico.obterNumeroUltimoAlunoNaTurma(turma.getId());
			if (numeroUltimoAlunoNaTurma == null) {
				numeroUltimoAlunoNaTurma = 0;
			}
			numeroUltimoAlunoNaTurma++;
			transferenciaSelacionada.getMatricula().setNumeroAlunoTurma(numeroUltimoAlunoNaTurma);
			transferenciaSelacionada.getMatricula().setTurmaDestino(transferenciaSelacionada.getMatricula().getTurma());
			transferenciaSelacionada.getMatricula().setTurma(turma);
			transferenciaSelacionada.getMatricula().setSituacao(EstadoTransferencia.TRANSFERIDO.getLabel());
			transferenciaSelacionada.setSituacao(EstadoTransferencia.TRANSFERIDO.getLabel());
			transferenciaSelacionada.setFinalizada(true);
			transferenciaSelacionada.getMatricula().setEscola(transferenciaSelacionada.getEscolaDestino());
			transferenciaSelacionada.getMatricula().getAluno().setEscola(transferenciaSelacionada.getEscolaDestino());
			matriculaServico.salvar(transferenciaSelacionada.getMatricula());
			transferenciaServico.salvar(transferenciaSelacionada);
			Mensagem.mensagemInfo("AVISO: Transferência de aluno realizada com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void aceitarASolicitacaoTransferencai(Transferencia transferencia) {
		estadoMatriculaSelacionada = true;
		this.transferenciaSelacionada = transferencia;
		try {
			if (this.transferenciaSelacionada.getDataMatricula() == null)
				this.transferenciaSelacionada.setDataMatricula(new Date());
			turmas = new ArrayList<>();

			if (transferencia.getMatricula().getClasse().getCiclo().equals("2º CICLO")) {
				turmas = turmaServico.obterTurmasPorClasseAreaCurso(transferencia.getClasse().getId(),
						transferencia.getMatricula().getTipoArea(), transferencia.getMatricula().getCurso(),
						transferencia.getMatricula().getAno(), transferencia.getEscolaDestino().getId());

			} else if (transferencia.getMatricula().getClasse().getCiclo().equals("1º CICLO")) {
				turmas = turmaServico.obterTurmasPorClasseCurso(transferencia.getClasse().getId(),
						transferencia.getMatricula().getCurso(), transferencia.getMatricula().getAno(),
						transferencia.getEscolaDestino().getId());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void prepararExclusao(Transferencia transferencia) {
		this.transferenciaSelacionadaExclusao = transferencia;

	}

	public void finalizar() {

		try {
			transferenciaSelacionadaExclusao.getMatricula().setSituacao(EstadoTransferencia.FINALIZAR.getLabel());
			transferenciaSelacionadaExclusao.setSituacao(EstadoTransferencia.FINALIZAR.getLabel());
			transferenciaSelacionadaExclusao.setFinalizada(true);
			matriculaServico.salvar(transferenciaSelacionadaExclusao.getMatricula());
			transferenciaServico.salvar(transferenciaSelacionadaExclusao);
			Mensagem.mensagemInfo("AVISO: Transferência de aluno foi finalizada com sucesso!");
			receberTransferenciasPesquisado();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cancelar() {

		try {
			transferenciaSelacionadaExclusao.getMatricula().setSituacao(EstadoTransferencia.CANCELADO.getLabel());
			transferenciaSelacionadaExclusao.setSituacao(EstadoTransferencia.CANCELADO.getLabel());
			transferenciaSelacionadaExclusao.setFinalizada(true);
			matriculaServico.salvar(transferenciaSelacionadaExclusao.getMatricula());
			transferenciaServico.salvar(transferenciaSelacionadaExclusao);
			Mensagem.mensagemInfo("AVISO: Transferência de aluno foi cancelada com sucesso!");
			receberTransferenciasPesquisado();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void excluir() {

		try {

			transferenciaSelacionadaExclusao.getMatricula().setSituacao(EstadoTransferencia.EXCLUIDO.getLabel());
			matriculaServico.salvar(transferenciaSelacionadaExclusao.getMatricula());
			transferenciaServico.excluir(transferenciaSelacionadaExclusao);
			Mensagem.mensagemInfo("AVISO: Transferência de aluno excluida com sucesso!");
			buscarTransferenciasPesquisado();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void voltar() {
		estadoMatriculaSelacionada = false;
	}

	public Transferencia getTransferencia() {
		return transferencia;
	}

	public void setTransferencia(Transferencia transferencia) {
		this.transferencia = transferencia;
	}

	public List<Transferencia> getTransferencias() {
		return transferencias;
	}

	public void setTransferencias(List<Transferencia> transferencias) {
		this.transferencias = transferencias;
	}

	public boolean isEstadoMatriculaSelacionada() {
		return estadoMatriculaSelacionada;
	}

	public void setEstadoMatriculaSelacionada(boolean estadoMatriculaSelacionada) {
		this.estadoMatriculaSelacionada = estadoMatriculaSelacionada;
	}

	public int getQuantidadeCaracteres() {
		return quantidadeCaracteres;
	}

	public void setQuantidadeCaracteres(int quantidadeCaracteres) {
		this.quantidadeCaracteres = quantidadeCaracteres;
	}

	public Integer getQuantidadePedidoTransferencia() {
		return quantidadePedidoTransferencia;
	}

	public void setQuantidadePedidoTransferencia(Integer quantidadePedidoTransferencia) {
		this.quantidadePedidoTransferencia = quantidadePedidoTransferencia;
	}

	public Transferencia getTransferenciaSelacionada() {
		return transferenciaSelacionada;
	}

	public void setTransferenciaSelacionada(Transferencia transferenciaSelacionada) {
		this.transferenciaSelacionada = transferenciaSelacionada;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public Transferencia getTransferenciaSelacionadaExclusao() {
		return transferenciaSelacionadaExclusao;
	}

	public void setTransferenciaSelacionadaExclusao(Transferencia transferenciaSelacionadaExclusao) {
		this.transferenciaSelacionadaExclusao = transferenciaSelacionadaExclusao;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public List<Transferencia> getPedidosTransferencia() {
		return pedidosTransferencia;
	}

	public void setPedidosTransferencia(List<Transferencia> pedidosTransferencia) {
		this.pedidosTransferencia = pedidosTransferencia;
	}

	public Integer getQuantidadeAlunos() {
		return quantidadeAlunos;
	}

	public void setQuantidadeAlunos(Integer quantidadeAlunos) {
		this.quantidadeAlunos = quantidadeAlunos;
	}

	public String getQuantidadePedidoTransferenciaAlgarismo() {
		return quantidadePedidoTransferenciaAlgarismo;
	}

	public void setQuantidadePedidoTransferenciaAlgarismo(String quantidadePedidoTransferenciaAlgarismo) {
		this.quantidadePedidoTransferenciaAlgarismo = quantidadePedidoTransferenciaAlgarismo;
	}

}
