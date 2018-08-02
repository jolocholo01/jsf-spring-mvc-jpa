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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.enumerado.Aula;
import com.mz.sistema.gestao.escolar.enumerado.TipoCurso;
import com.mz.sistema.gestao.escolar.enumerado.TipoTurno;
import com.mz.sistema.gestao.escolar.modelo.Turno;
import com.mz.sistema.gestao.escolar.servico.TurnoServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Controller
@Named
@SessionScope
public class TurnoBean {
	private List<Aula> aulas = Arrays.asList(Aula.values());
	private List<TipoCurso> cursos = Arrays.asList(TipoCurso.values());
	private List<TipoTurno> tipoTurnos = Arrays.asList(TipoTurno.values());
	private List<Turno> turnos = new ArrayList<>();
	private Turno turno;
	private Turno turnoSelecionado;

	private Integer qtdTurnosEncontrados;
	private String pesquisa;
	private boolean editarTurnoBoolean;
	private boolean novoTurnoBoolean;
	private boolean cadastroTurnoBoolean;
	
	private int quantidadeCaracteres;
	@Autowired
	private TurnoServico turnoServico;
	public void iniciarBean() {

		try {
			cadastroTurnoBoolean = false;
			novoTurnoBoolean = false;
			editarTurnoBoolean = false;
			turnos = new ArrayList<>();
			pesquisa = null;
			qtdTurnosEncontrados = 0;
			quantidadeCaracteres = 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void salvar() {
		try {
			Turno turnoExistente = turnoServico.obterTurnoExistente(turno.getDescricao());
			if (turnoExistente != null && turnoExistente.getId() != turno.getId()) {
				Mensagem.mensagemErro("ERRO: Já existe um turno cadastrado no sistema!");
				return;
			}
			if(turno.getDescricao().toString().equals(TipoTurno.MANHÃ.toString())){
				turno.setSigla("M");
				turno.setOrdem(1);
			}
			if(turno.getDescricao().toString().equals(TipoTurno.TARDE.toString())){
				turno.setSigla("T");
				turno.setOrdem(2);
			}
			if(turno.getDescricao().toString().equals(TipoTurno.NOITE.toString())){
				turno.setSigla("N");
				turno.setOrdem(3);
			}

			turnoServico.salvar(turno);
			if (turno.getId() == null) {
				Mensagem.mensagemInfo("AVISO: O turno foi cadastrado com sucesso!");
			} else {
				Mensagem.mensagemInfo("AVISO: O turno foi atualizado com sucesso!");
			}
			if(editarTurnoBoolean == true){
				editarTurnoBoolean = false;
				cadastroTurnoBoolean=false;
			}
			turno = new Turno();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void contarQuantidadeCarateres() {
		quantidadeCaracteres = 0;
		try {
			if (turno.getObservacao() != null) {
				quantidadeCaracteres = turno.getObservacao().length();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void buscar() {
		try {
			qtdTurnosEncontrados = 0;
			turnos = turnoServico.obterTurnoPorPesquisa(pesquisa);
			if (turnos != null) {
				qtdTurnosEncontrados = turnos.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void novo() {
		cadastroTurnoBoolean = true;
		novoTurnoBoolean= true;
		turno = new Turno();
		quantidadeCaracteres = 0;
		editarTurnoBoolean = false;
	}

	public void editar(Turno turno) {
		cadastroTurnoBoolean = true;
		novoTurnoBoolean = false;
		editarTurnoBoolean = true;
		this.turno = turno;
		if(this.turno.getDataCadastro()==null){
			this.turno.setDataCadastro(new Date());
		}
		if (turno.getObservacao() != null) {
			quantidadeCaracteres = turno.getObservacao().length();
		}
	}

	public void voltar() {
		cadastroTurnoBoolean = false;
		novoTurnoBoolean = false;
		turno = null;
		editarTurnoBoolean = false;

	}

	
	public void prepararParExcluir(Turno turno) {
		this.turnoSelecionado = turno;
	}

	public void excluir() {
		try {
			turnoServico.excluir(this.turnoSelecionado);
			buscar();
			Mensagem.mensagemInfo("AVISO: o turno foi excluido com sucesso!");
			System.out.println("Chamou a funaco!");

		} catch (Exception e) {
			Mensagem.mensagemErro("ERRO: o turno não foi excluido através da dependência em outras tabelas.");
		}

	}
	
	
	public List<Turno> getObterTodosTurnos() {
		return turnoServico.listarTodosTurnos();
	}

	public List<Turno> getObterTurnoDaEscola() {
		return turnoServico.listarTodosTurnosDaEscola();
	}

	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

	public List<Aula> getAulas() {
		return aulas;
	}

	public void setAulas(List<Aula> aulas) {
		this.aulas = aulas;
	}

	
	public List<Turno> getTurnos() {
		return turnos;
	}

	public void setTurnos(List<Turno> turnos) {
		this.turnos = turnos;
	}

	public Integer getQtdTurnosEncontrados() {
		return qtdTurnosEncontrados;
	}

	public void setQtdTurnosEncontrados(Integer qtdTurnosEncontrados) {
		this.qtdTurnosEncontrados = qtdTurnosEncontrados;
	}

	

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public boolean isEditarTurnoBoolean() {
		return editarTurnoBoolean;
	}

	public void setEditarTurnoBoolean(boolean editarTurnoBoolean) {
		this.editarTurnoBoolean = editarTurnoBoolean;
	}

	public boolean isNovoTurnoBoolean() {
		return novoTurnoBoolean;
	}

	public void setNovoTurnoBoolean(boolean novoTurnoBoolean) {
		this.novoTurnoBoolean = novoTurnoBoolean;
	}

	public boolean isCadastroTurnoBoolean() {
		return cadastroTurnoBoolean;
	}

	public void setCadastroTurnoBoolean(boolean cadastroTurnoBoolean) {
		this.cadastroTurnoBoolean = cadastroTurnoBoolean;
	}

	public int getQuantidadeCaracteres() {
		return quantidadeCaracteres;
	}

	public void setQuantidadeCaracteres(int quantidadeCaracteres) {
		this.quantidadeCaracteres = quantidadeCaracteres;
	}

	public Turno getTurnoSelecionado() {
		return turnoSelecionado;
	}

	public void setTurnoSelecionado(Turno turnoSelecionado) {
		this.turnoSelecionado = turnoSelecionado;
	}

	public List<TipoCurso> getCursos() {
		return cursos;
	}

	public void setCursos(List<TipoCurso> cursos) {
		this.cursos = cursos;
	}

	public List<TipoTurno> getTipoTurnos() {
		return tipoTurnos;
	}

	public void setTipoTurnos(List<TipoTurno> tipoTurnos) {
		this.tipoTurnos = tipoTurnos;
	}

}
