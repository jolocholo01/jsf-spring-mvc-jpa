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

import com.mz.sistema.gestao.escolar.enumerado.Ciclo;
import com.mz.sistema.gestao.escolar.modelo.Area;
import com.mz.sistema.gestao.escolar.servico.AreaServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;
import com.mz.sistema.gestao.escolar.util.Replace;

@Named
@Controller
@SessionScope
public class AreaBean implements Serializable {

	private static final long serialVersionUID = -1205249789398248234L;
	private Area area;
	private Area areaSelecionada;
	private Area areaExclusao;
	private List<Area> areas = new ArrayList<>();
	private List<Ciclo> ciclos = Arrays.asList(Ciclo.PRIMEIRO_CICLO, Ciclo.SEGUNDO_CICLO);

	private boolean cadastroAreaBoolean = false;
	private boolean novaAreaBoolean = false;
	private boolean editarAreaBoolean = false;
	private int quantidadeCaracteres;

	private Integer qtdAreasEncontradas = 0;
	private String pesquisa = new String();

	@Autowired
	private AreaServico areaServico;



	public void salvar() {
		try {
			Area areaExistente = areaServico.obterAreaExistente(area.getDescricao(), area.getCiclo());
			if (areaExistente != null && areaExistente.getId() != areaExistente.getId()) {
				Mensagem.mensagemInfo("AVISO: já existe esta área cadastrada no sistema!");
				return;
			}
			if (area.getId() == null) {
				area.setDataCadastro(new Date());
				areaServico.salvar(area);
				Mensagem.mensagemInfo("AVISO: área cadastrada com sucesso!");
				area = new Area();				
				this.quantidadeCaracteres = 0;
			} else if (area.getId() != null) {				
				areaServico.salvar(area);
				voltarParaPequisa();
				Mensagem.mensagemInfo("AVISO: área atualizada com sucesso!");
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.mensagemErro("ERRO: houve erro ao cadastrar área!");
		}
	}

	public void obterQtdCarateres() {
		if (area.getObservacao() == null) {
		} else
			this.quantidadeCaracteres = area.getObservacao().length();

	}

	public String cadastroArea() {
		inicializarDados();
		return "/academico/director-ditrital/area/cadastro?faces-redirect=true";
	}

	public void novaArea() {
		this.cadastroAreaBoolean = true;
		this.novaAreaBoolean = true;
		editarAreaBoolean = false;
		this.quantidadeCaracteres = 0;
		// pesquisa = null;
		area = new Area();
		area.setDataCadastro(new Date());
		

	}

	public void voltarParaPequisa() {
		area = null;
		editarAreaBoolean= false;
		this.cadastroAreaBoolean= false;
		if (novaAreaBoolean == true) {
			novaAreaBoolean = false;
		}

	try {
		if(!areas.isEmpty()){
			buscarArea();
		}
	} catch (Exception e) {
		e.printStackTrace();
	}

	}

	public void prepararParExcluir(Area area) {
		this.areaExclusao= area;
		this.areaExclusao.setDescricao(Replace.rescreverTexto(area.getDescricao()));

	}

	public void excluir() {
		try {
			this.areaExclusao.setDescricao(this.areaExclusao.getDescricao().toUpperCase());
			areaServico.excluir(this.areaExclusao);
			Mensagem.mensagemInfo("AVISO: área excluida com sucesso!");
			buscarArea();
		} catch (Exception e) {
			Mensagem.mensagemErro("ERRO: a área não foi excluida através da dependência.");
		}
	}

	public void buscarArea() {
		if (pesquisa == null) {
			areas = new ArrayList<>();
		} else {
			areas = areaServico.obterAreasPorPesquisa(pesquisa);
		}
		if (areas == null) {
			this.qtdAreasEncontradas = 0;
		} else {
			this.qtdAreasEncontradas = areas.size();
		}
	}

	private void inicializarDados() {
		cadastroAreaBoolean = false;
		novaAreaBoolean = false;
		editarAreaBoolean = false;
		qtdAreasEncontradas = 0;
		area = null;
		areas = new ArrayList<>();
	}

	public void editar(Area area) {
		this.area = area;
		// this.disciplina.setCiclo(disciplina.getArea().getCiclo());
		editarAreaBoolean = true;
		cadastroAreaBoolean = true;
		novaAreaBoolean = false;
		obterQtdCarateres();

	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Area getAreaSelecionada() {
		return areaSelecionada;
	}

	public void setAreaSelecionada(Area areaSelecionada) {
		this.areaSelecionada = areaSelecionada;
	}

	public Area getAreaExclusao() {
		return areaExclusao;
	}

	public void setAreaExclusao(Area areaExclusao) {
		this.areaExclusao = areaExclusao;
	}

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

	public boolean isCadastroAreaBoolean() {
		return cadastroAreaBoolean;
	}

	public void setCadastroAreaBoolean(boolean cadastroAreaBoolean) {
		this.cadastroAreaBoolean = cadastroAreaBoolean;
	}

	public boolean isNovaAreaBoolean() {
		return novaAreaBoolean;
	}

	public void setNovaAreaBoolean(boolean novaAreaBoolean) {
		this.novaAreaBoolean = novaAreaBoolean;
	}

	public boolean isEditarAreaBoolean() {
		return editarAreaBoolean;
	}

	public void setEditarAreaBoolean(boolean editarAreaBoolean) {
		this.editarAreaBoolean = editarAreaBoolean;
	}

	public int getQuantidadeCaracteres() {
		return quantidadeCaracteres;
	}

	public void setQuantidadeCaracteres(int quantidadeCaracteres) {
		this.quantidadeCaracteres = quantidadeCaracteres;
	}

	public Integer getQtdAreasEncontradas() {
		return qtdAreasEncontradas;
	}

	public void setQtdAreasEncontradas(Integer qtdAreasEncontradas) {
		this.qtdAreasEncontradas = qtdAreasEncontradas;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public List<Ciclo> getCiclos() {
		return ciclos;
	}

	public void setCiclos(List<Ciclo> ciclos) {
		this.ciclos = ciclos;
	}

	

}
