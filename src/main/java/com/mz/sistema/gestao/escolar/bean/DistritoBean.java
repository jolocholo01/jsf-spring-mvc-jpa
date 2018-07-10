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

import com.mz.sistema.gestao.escolar.enumerado.Provincia;
import com.mz.sistema.gestao.escolar.modelo.Distrito;
import com.mz.sistema.gestao.escolar.modelo.Permissao;
import com.mz.sistema.gestao.escolar.servico.DistritoServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Named
@Controller("distritoBean")
@SessionScope
public class DistritoBean implements Serializable {

	private static final long serialVersionUID = -8077768006424832717L;

	private Distrito distrito = new Distrito();
	private List<Distrito> distritos = new ArrayList<Distrito>();
	private Distrito ditritoselecionado;
	private List<Provincia> provincias = Arrays.asList(Provincia.values());

	private Integer qtdDistritosEncontrados;
	private String pesquisa;
	private boolean editarDistritoBoolean;
	private boolean novoDistritoBoolean;
	private boolean cadastroDistritoBoolean;

	@Autowired
	private DistritoServico distritoServico;

	private int quantidadeCaracteres;

	// prettyCofing
	public void iniciarBean() {

		try {
			cadastroDistritoBoolean = false;
			novoDistritoBoolean = false;
			editarDistritoBoolean = false;
			distritos = new ArrayList<>();
			pesquisa = null;
			qtdDistritosEncontrados = 0;
			quantidadeCaracteres = 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void salvar() {
		try {
			Distrito distritoExistente = distritoServico.obterDistritoExistente(distrito.getProvincia(),
					distrito.getNome());
			if (distritoExistente != null && distritoExistente.getId() != distrito.getId()) {
				Mensagem.mensagemErro("ERRO: Já existe um distrito cadastrado no sistema!");
				return;
			}
			distritoServico.salvar(distrito);
			if (distrito.getId() == null) {
				Mensagem.mensagemInfo("AVISO: Distrito foi cadastrado com sucesso!");
			} else {
				Mensagem.mensagemInfo("AVISO: Distrito foi atualizado com sucesso!");
			}
			if (editarDistritoBoolean == true) {
				editarDistritoBoolean = false;
				cadastroDistritoBoolean = false;
			}
			distrito = new Distrito();
			distrito.setDataCadastro(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void contarQuantidadeCarateres() {
		quantidadeCaracteres = 0;
		try {
			if (distrito.getObservacao() != null) {
				quantidadeCaracteres = distrito.getObservacao().length();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buscar() {
		try {
			qtdDistritosEncontrados = 0;
			distritos = distritoServico.obterDistritosDaProvincia(pesquisa);
			if (distritos != null) {
				qtdDistritosEncontrados = distritos.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void novo() {
		cadastroDistritoBoolean = true;
		novoDistritoBoolean = true;
		distrito = new Distrito();
		distrito.setDataCadastro(new Date());
		quantidadeCaracteres = 0;
		editarDistritoBoolean = false;
	}

	public void editar(Distrito distrito) {
		cadastroDistritoBoolean = true;
		novoDistritoBoolean = false;
		editarDistritoBoolean = true;
		this.distrito = distrito;
		if (this.distrito.getDataCadastro() == null) {
			this.distrito.setDataCadastro(new Date());
		}
		if (distrito.getObservacao() != null) {
			quantidadeCaracteres = distrito.getObservacao().length();
		}
	}

	public void voltar() {
		cadastroDistritoBoolean = false;
		novoDistritoBoolean = false;
		distrito = null;
		editarDistritoBoolean = false;

	}

	public void cancelar() {
		distrito = new Distrito();
		ditritoselecionado = null;
	}

	public void prepararParExcluir(Distrito distrito) {
		this.ditritoselecionado = distrito;
	}

	public void excluir() {
		try {
			distritoServico.excluir(this.ditritoselecionado);
			buscar();
			Mensagem.mensagemInfo("AVISO: distrito excluido com sucesso!");
			System.out.println("Chamou a funaco!");

		} catch (Exception e) {
			Mensagem.mensagemAlerta("ATENÇÃO: distrito não foi excluido através da dependência.");
		}

	}

	public List<Provincia> getEstados() {
		return Arrays.asList(Provincia.values());
	}

	public Distrito getDitritoselecionado() {
		return ditritoselecionado;
	}

	public void setDitritoselecionado(Distrito ditritoselecionado) {
		this.ditritoselecionado = ditritoselecionado;
	}

	public Distrito getDistrito() {
		return distrito;
	}

	public void setDistrito(Distrito distrito) {
		this.distrito = distrito;
	}

	public List<Distrito> getDistritos() {
		return distritos;
	}

	public void setDistritos(List<Distrito> distritos) {
		this.distritos = distritos;
	}

	public Distrito getditritoselecionado() {
		return ditritoselecionado;
	}

	public void setditritoselecionado(Distrito ditritoselecionado) {
		this.ditritoselecionado = ditritoselecionado;
	}

	public List<Provincia> getProvincias() {
		return provincias;
	}

	public void setProvincias(List<Provincia> provincias) {
		this.provincias = provincias;
	}

	public Integer getQtdDistritosEncontrados() {
		return qtdDistritosEncontrados;
	}

	public void setQtdDistritosEncontrados(Integer qtdDistritosEncontrados) {
		this.qtdDistritosEncontrados = qtdDistritosEncontrados;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public boolean isEditarDistritoBoolean() {
		return editarDistritoBoolean;
	}

	public void setEditarDistritoBoolean(boolean editarDistritoBoolean) {
		this.editarDistritoBoolean = editarDistritoBoolean;
	}

	public boolean isNovoDistritoBoolean() {
		return novoDistritoBoolean;
	}

	public void setNovoDistritoBoolean(boolean novoDistritoBoolean) {
		this.novoDistritoBoolean = novoDistritoBoolean;
	}

	public boolean isCadastroDistritoBoolean() {
		return cadastroDistritoBoolean;
	}

	public void setCadastroDistritoBoolean(boolean cadastroDistritoBoolean) {
		this.cadastroDistritoBoolean = cadastroDistritoBoolean;
	}

	public int getQuantidadeCaracteres() {
		return quantidadeCaracteres;
	}

	public void setQuantidadeCaracteres(int quantidadeCaracteres) {
		this.quantidadeCaracteres = quantidadeCaracteres;
	}

}
