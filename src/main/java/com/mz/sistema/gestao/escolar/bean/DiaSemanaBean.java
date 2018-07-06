
package com.mz.sistema.gestao.escolar.bean;

import java.io.Serializable;
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

import com.mz.sistema.gestao.escolar.enumerado.Dia;
import com.mz.sistema.gestao.escolar.modelo.DiaSemana;
import com.mz.sistema.gestao.escolar.servico.DiaSemanaServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Named
@Controller
@SessionScope
public class DiaSemanaBean implements Serializable {

	private static final long serialVersionUID = -8077768006424832717L;

	private DiaSemana diaSemana = new DiaSemana();
	private List<DiaSemana> diaSemanas = new ArrayList<>();
	private DiaSemana diaSemanaSelecionado;
	private Dia dia;
	private List<Dia> dias = Arrays.asList(Dia.values());

	private Integer qtdDiaSemanasEncontrados;
	private String pesquisa;
	private boolean editarDiaSemanaBoolean;
	private boolean novoDiaSemanaBoolean;
	private boolean cadastroDiaSemanaBoolean;

	@Autowired
	private DiaSemanaServico diaSemanaServico;

	private int quantidadeCaracteres;

	// prettyCofing
	public void iniciarBean() {

		try {
			cadastroDiaSemanaBoolean = false;
			novoDiaSemanaBoolean = false;
			editarDiaSemanaBoolean = false;
			diaSemanas = new ArrayList<>();
			pesquisa = null;
			qtdDiaSemanasEncontrados = 0;
			quantidadeCaracteres = 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void salvar() {
		try {
			DiaSemana diaSemanaExistente = diaSemanaServico.obterDiaSemanaExistente(diaSemana.getSigla());
			if (diaSemanaExistente != null && diaSemanaExistente.getId() != diaSemana.getId()) {
				Mensagem.mensagemErro("ERRO: Já existe este dia da semana cadastrado no sistema!");
				return;
			}
			diaSemanaServico.salvar(diaSemana);
			if (diaSemana.getId() == null) {
				Mensagem.mensagemInfo("AVISO: o dia da semana foi cadastrado com sucesso!");
			} else {
				Mensagem.mensagemInfo("AVISO: o dia da semana foi atualizado com sucesso!");
			}
			if (editarDiaSemanaBoolean == true) {
				editarDiaSemanaBoolean = false;
				cadastroDiaSemanaBoolean = false;
			}
			diaSemana = new DiaSemana();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void contarQuantidadeCarateres() {
		quantidadeCaracteres = 0;
		try {
			if (diaSemana.getObservacao() != null) {
				quantidadeCaracteres = diaSemana.getObservacao().length();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buscar() {
		try {
			qtdDiaSemanasEncontrados = 0;
			diaSemanas = diaSemanaServico.obterDiaSemanaPorPesquisa(pesquisa);
			if (diaSemanas != null) {
				qtdDiaSemanasEncontrados = diaSemanas.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void novo() {
		cadastroDiaSemanaBoolean = true;
		novoDiaSemanaBoolean= true;
		diaSemana = new DiaSemana();
		diaSemana.setDataCadastro(new Date());
		quantidadeCaracteres = 0;
		editarDiaSemanaBoolean = false;
	}

	public void editar(DiaSemana diaSemana) {
		try {
			cadastroDiaSemanaBoolean = true;
			novoDiaSemanaBoolean = false;
			editarDiaSemanaBoolean = true;
			this.diaSemana = diaSemana;
			if (this.diaSemana.getDataCadastro() == null) {
				this.diaSemana.setDataCadastro(new Date());
			}
			if (diaSemana.getObservacao() != null) {
				quantidadeCaracteres = diaSemana.getObservacao().length();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void voltar() {
		cadastroDiaSemanaBoolean = false;
		novoDiaSemanaBoolean = false;
		diaSemana = null;
		editarDiaSemanaBoolean = false;

	}

	public void prepararParaExcluir(DiaSemana diaSemana) {
		this.diaSemanaSelecionado = diaSemana;
	}

	public void excluir() {
		try {
			diaSemanaServico.excluir(this.diaSemanaSelecionado);
			buscar();
			Mensagem.mensagemInfo("AVISO: dia da semana foi excluido com sucesso!");
			System.out.println("Chamou a funaco!");

		} catch (Exception e) {
			Mensagem.mensagemErro("ERRO: dia da semana  não foi excluido através da dependência.");
		}

	}

	public DiaSemana getDiaSemana() {
		if (diaSemana == null) {
		} else if (diaSemana.getDescricao() == null) {
		} else if (diaSemana.getDescricao().toUpperCase().equals(Dia.DOM.getLabel().toString().toUpperCase())) {
			diaSemana.setOrdem(1);
			diaSemana.setSigla(Dia.DOM.toString());
		} else if (diaSemana.getDescricao().toUpperCase().equals(Dia.SEG.getLabel().toString().toUpperCase())) {
			diaSemana.setOrdem(2);
			diaSemana.setSigla(Dia.SEG.toString());
		}
		else if (diaSemana.getDescricao().toUpperCase().equals( Dia.TER.getLabel().toString().toUpperCase())) {
			diaSemana.setOrdem(3);
			diaSemana.setSigla(Dia.TER.toString());
		}else if (diaSemana.getDescricao().toUpperCase().equals( Dia.QUA.getLabel().toString().toUpperCase())) {
			diaSemana.setOrdem(4);
			diaSemana.setSigla(Dia.QUA.toString());
		}else if (diaSemana.getDescricao().toUpperCase().equals( Dia.QUI.getLabel().toString().toUpperCase())) {
			diaSemana.setOrdem(5);
			diaSemana.setSigla(Dia.QUI.toString());
		}else if (diaSemana.getDescricao().toUpperCase().equals( Dia.SEX.getLabel().toString().toUpperCase())) {
			diaSemana.setOrdem(6);
			diaSemana.setSigla(Dia.SEX.toString());
		}else if (diaSemana.getDescricao().toUpperCase().equals( Dia.SAB.getLabel().toString().toUpperCase())) {
			diaSemana.setOrdem(7);
			diaSemana.setSigla(Dia.SAB.toString());
		}
		return diaSemana;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	public List<DiaSemana> getDiaSemanas() {
		return diaSemanas;
	}

	public void setDiaSemanas(List<DiaSemana> diaSemanas) {
		this.diaSemanas = diaSemanas;
	}

	public DiaSemana getDiaSemanaSelecionado() {
		return diaSemanaSelecionado;
	}

	public void setDiaSemanaSelecionado(DiaSemana diaSemanaSelecionado) {
		this.diaSemanaSelecionado = diaSemanaSelecionado;
	}

	public List<Dia> getDias() {
		return dias;
	}

	public void setDias(List<Dia> dias) {
		this.dias = dias;
	}

	public Integer getQtdDiaSemanasEncontrados() {
		return qtdDiaSemanasEncontrados;
	}

	public void setQtdDiaSemanasEncontrados(Integer qtdDiaSemanasEncontrados) {
		this.qtdDiaSemanasEncontrados = qtdDiaSemanasEncontrados;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public boolean isEditarDiaSemanaBoolean() {
		return editarDiaSemanaBoolean;
	}

	public void setEditarDiaSemanaBoolean(boolean editarDiaSemanaBoolean) {
		this.editarDiaSemanaBoolean = editarDiaSemanaBoolean;
	}

	public boolean isNovoDiaSemanaBoolean() {
		return novoDiaSemanaBoolean;
	}

	public void setNovoDiaSemanaBoolean(boolean novoDiaSemanaBoolean) {
		this.novoDiaSemanaBoolean = novoDiaSemanaBoolean;
	}

	public boolean isCadastroDiaSemanaBoolean() {
		return cadastroDiaSemanaBoolean;
	}

	public void setCadastroDiaSemanaBoolean(boolean cadastroDiaSemanaBoolean) {
		this.cadastroDiaSemanaBoolean = cadastroDiaSemanaBoolean;
	}

	public int getQuantidadeCaracteres() {
		return quantidadeCaracteres;
	}

	public void setQuantidadeCaracteres(int quantidadeCaracteres) {
		this.quantidadeCaracteres = quantidadeCaracteres;
	}

	public Dia getDia() {
		
		return dia;
	}

	public void setDia(Dia dia) {
		this.dia = dia;
	}

	
	
}
