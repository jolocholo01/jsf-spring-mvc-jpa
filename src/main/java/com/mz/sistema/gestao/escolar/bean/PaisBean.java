
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

import com.mz.sistema.gestao.escolar.enumerado.Continente;
import com.mz.sistema.gestao.escolar.modelo.Pais;
import com.mz.sistema.gestao.escolar.servico.PaisServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Named
@Controller("paisBean")
@SessionScope
public class PaisBean implements Serializable {

	private static final long serialVersionUID = -8077768006424832717L;

	private Pais pais = new Pais();
	private List<Pais> paises = new ArrayList<Pais>();
	private Pais paisSelecionado;
	private List<Continente> continentes = Arrays.asList(Continente.values());

	private Integer qtdPaisesEncontrados;
	private String pesquisa;
	private boolean editarPaisBoolean;
	private boolean novoPaisBoolean;
	private boolean cadastroPaisBoolean;

	@Autowired
	private PaisServico paisServico;

	private int quantidadeCaracteres;

	// prettyCofing
	public void iniciarBean() {

		try {
			cadastroPaisBoolean = false;
			novoPaisBoolean = false;
			editarPaisBoolean = false;
			paises = new ArrayList<>();
			pesquisa = null;
			qtdPaisesEncontrados = 0;
			quantidadeCaracteres = 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void salvar() {
		try {
			Pais paisExistente = paisServico.obterPaisExistente(pais.getContinente(), pais.getNome());
			if (paisExistente != null && paisExistente.getId() != paisExistente.getId()) {
				Mensagem.mensagemAlerta("ATENÇÃO: Já existe um país cadastrado no sistema!");
				return;
			}
			Continente continente=pais.getContinente();
			paisServico.salvar(pais);
			if (pais.getId() == null) {
				Mensagem.mensagemInfo("AVISO: País foi cadastrado com sucesso!");
			} else if (pais.getId() != null) {
				Mensagem.mensagemInfo("AVISO: País foi atualizado com sucesso!");
			}
			if (editarPaisBoolean == true) {
				editarPaisBoolean = false;
				cadastroPaisBoolean = false;
			}
			pais = new Pais();
			pais.setContinente(continente);
			pais.setDataCadastro(new Date());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void contarQuantidadeCarateres() {
		quantidadeCaracteres = 0;
		try {
			if (pais.getObservacao() != null) {
				quantidadeCaracteres = pais.getObservacao().length();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buscar() {
		try {
			qtdPaisesEncontrados = 0;
			paises = paisServico.obterPaisPorPesquisa(pesquisa);
			if (paises != null) {
				qtdPaisesEncontrados = paises.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void novo() {
		cadastroPaisBoolean = true;
		novoPaisBoolean = true;
		pais = new Pais();
		quantidadeCaracteres = 0;
		editarPaisBoolean = false;
		pais.setDataCadastro(new Date());
	}

	public void editar(Pais pais) {
		cadastroPaisBoolean = true;
		novoPaisBoolean = false;
		editarPaisBoolean = true;
		this.pais = pais;
		if (this.pais.getDataCadastro() == null) {
			this.pais.setDataCadastro(new Date());
		}
		if (this.pais.getObservacao() != null) {
			quantidadeCaracteres = this.pais.getObservacao().length();
		}
	}

	public void voltar() {
		cadastroPaisBoolean = false;
		novoPaisBoolean = false;
		pais = null;
		editarPaisBoolean = false;

	}

	public void prepararParExcluir(Pais pais) {
		this.paisSelecionado = pais;
	}

	public void excluir() {
		try {
			paisServico.excluir(this.paisSelecionado);
			buscar();
			Mensagem.mensagemInfo("AVISO: país excluido com sucesso!");
			System.out.println("Chamou a funaco!");

		} catch (Exception e) {
			Mensagem.mensagemAlerta("ATENÇÃO: País não foi excluido através da dependência.");
		}

	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public List<Pais> getPaises() {
		return paises;
	}

	public void setPaises(List<Pais> paises) {
		this.paises = paises;
	}

	public Pais getPaisSelecionado() {
		return paisSelecionado;
	}

	public void setPaisSelecionado(Pais paisSelecionado) {
		this.paisSelecionado = paisSelecionado;
	}

	public List<Continente> getContinentes() {
		return continentes;
	}

	public void setContinentes(List<Continente> continentes) {
		this.continentes = continentes;
	}

	public Integer getQtdPaisesEncontrados() {
		return qtdPaisesEncontrados;
	}

	public void setQtdPaisesEncontrados(Integer qtdPaisesEncontrados) {
		this.qtdPaisesEncontrados = qtdPaisesEncontrados;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public boolean isEditarPaisBoolean() {
		return editarPaisBoolean;
	}

	public void setEditarPaisBoolean(boolean editarPaisBoolean) {
		this.editarPaisBoolean = editarPaisBoolean;
	}

	public boolean isNovoPaisBoolean() {
		return novoPaisBoolean;
	}

	public void setNovoPaisBoolean(boolean novoPaisBoolean) {
		this.novoPaisBoolean = novoPaisBoolean;
	}

	public boolean isCadastroPaisBoolean() {
		return cadastroPaisBoolean;
	}

	public void setCadastroPaisBoolean(boolean cadastroPaisBoolean) {
		this.cadastroPaisBoolean = cadastroPaisBoolean;
	}

	public int getQuantidadeCaracteres() {
		return quantidadeCaracteres;
	}

	public void setQuantidadeCaracteres(int quantidadeCaracteres) {
		this.quantidadeCaracteres = quantidadeCaracteres;
	}

}
