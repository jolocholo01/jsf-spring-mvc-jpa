// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.enumerado.Provincia;
import com.mz.sistema.gestao.escolar.modelo.Distrital;
import com.mz.sistema.gestao.escolar.modelo.Distrito;
import com.mz.sistema.gestao.escolar.servico.DirecaoDistritalServico;
import com.mz.sistema.gestao.escolar.servico.DistritoServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Named
@Controller
@SessionScope
public class DireccaoDistritalBean implements Serializable {

	private static final long serialVersionUID = -8077768006424832717L;

	private Distrital direccaoDistrital = new Distrital();
	private List<Distrital> direccoesDistritais = new ArrayList<>();
	private Distrital direccaoDistritalSelecionada;
	private List<Provincia> provincias = Arrays.asList(Provincia.values());
	private String provincia;

	private int quantidadeCaracteres;

	private List<Distrito> distritos;

	private Integer qtdDireccoesDistritaisEncontrados;
	private String pesquisa;
	private boolean editardireccaoDistritalBoolean;
	private boolean novadireccaoDistritalBoolean;
	private boolean cadastrodireccaoDistritalBoolean;

	@Autowired
	private DirecaoDistritalServico direcaoDistritalServico;
	@Autowired
	private DistritoServico distritoServico;

	// prettyCofing
	public void iniciarBean() {

		try {
			cadastrodireccaoDistritalBoolean = false;
			novadireccaoDistritalBoolean = false;
			editardireccaoDistritalBoolean = false;
			direccoesDistritais = new ArrayList<>();
			setDistritos(new ArrayList<>());
			pesquisa = null;
			qtdDireccoesDistritaisEncontrados = 0;
			quantidadeCaracteres = 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void salvar() {
		try {
			Distrital direccaoDistritalExistente = direcaoDistritalServico
					.obterDirecaoDistritalExistente(direccaoDistrital.getEndereco().getDistrito().getId());
			if (direccaoDistritalExistente != null && direccaoDistritalExistente.getId() != direccaoDistrital.getId()) {
				Mensagem.mensagemErro("ERRO: Já existe essa direcção distrital cadastrada no sistema!");
				return;
			}
			direccaoDistrital.setDescricao(direccaoDistrital.getDescricao().replace("Servico", "Serviço").replace("Educacao", "Educação"));
			direcaoDistritalServico.salvar(direccaoDistrital);
			if (direccaoDistrital.getId() == null) {
				Mensagem.mensagemInfo("AVISO: Direcção distrital foi cadastrada com sucesso!");
			} else {
				Mensagem.mensagemInfo("AVISO: Direcção distrital foi atualizada com sucesso!");
			}
			if (editardireccaoDistritalBoolean == true) {
				editardireccaoDistritalBoolean = false;
				cadastrodireccaoDistritalBoolean = false;
				buscar();
			}
			direccaoDistrital = new Distrital();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void contarQuantidadeCarateres() {
		quantidadeCaracteres = 0;
		try {
			if (direccaoDistrital.getObservacao() != null) {
				quantidadeCaracteres = direccaoDistrital.getObservacao().length();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void bucarDistritoDaProvincia() {
		try {

			if (provincia != null)
				setDistritos(distritoServico.obterDistritosDaProvincia(provincia));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscar() {
		try {
			qtdDireccoesDistritaisEncontrados = 0;
			direccoesDistritais = direcaoDistritalServico.obterDirecaoDistritalPorDescricao(pesquisa);
			if (direccoesDistritais != null) {
				qtdDireccoesDistritaisEncontrados = direccoesDistritais.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void nova() {
		cadastrodireccaoDistritalBoolean = true;
		novadireccaoDistritalBoolean = true;
		provincia = null;
		distritos=new ArrayList<>();
		direccaoDistrital = new Distrital();
		direccaoDistrital.setDescricao("Serviço Distrital de Educação, Juventude e Tecnologia");
		direccaoDistrital.setDataCadastro(new Date());
		quantidadeCaracteres = 0;
		editardireccaoDistritalBoolean = false;
	}

	public void editar(Distrital direccaoDistrital) {
		cadastrodireccaoDistritalBoolean = true;
		novadireccaoDistritalBoolean = false;
		editardireccaoDistritalBoolean = true;
		this.direccaoDistrital = direccaoDistrital;
		provincia = direccaoDistrital.getEndereco().getDistrito().getProvincia();
		if (this.direccaoDistrital.getDataCadastro() == null) {
			this.direccaoDistrital.setDataCadastro(new Date());
		}
		if (direccaoDistrital.getObservacao() != null) {
			quantidadeCaracteres = direccaoDistrital.getObservacao().length();
		}
		bucarDistritoDaProvincia();
	}

	public void voltar() {
		cadastrodireccaoDistritalBoolean = false;
		novadireccaoDistritalBoolean = false;
		direccaoDistrital = null;
		editardireccaoDistritalBoolean = false;
		if(pesquisa!=null){
			buscar();
		}

	}

	public void prepararParExcluir(Distrital direccaoDistrital) {
		this.direccaoDistritalSelecionada = direccaoDistrital;
	}

	public void excluir() {
		try {
			direcaoDistritalServico.excluir(this.direccaoDistritalSelecionada);
			buscar();
			Mensagem.mensagemInfo("Aviso: distrito excluido com sucesso!");
			System.out.println("Chamou a funaco!");

		} catch (Exception e) {
			Mensagem.mensagemAlerta("Aviso: distrito não foi excluido através da dependência.");
		}

	}

	public int getQuantidadeCaracteres() {
		return quantidadeCaracteres;
	}

	public void setQuantidadeCaracteres(int quantidadeCaracteres) {
		this.quantidadeCaracteres = quantidadeCaracteres;
	}

	public Distrital getDireccaoDistritalSelecionada() {
		return direccaoDistritalSelecionada;
	}

	public void setDireccaoDistritalSelecionada(Distrital direccaoDistritalSelecionada) {
		this.direccaoDistritalSelecionada = direccaoDistritalSelecionada;
	}

	public Integer getQtdDireccoesDistritaisEncontrados() {
		return qtdDireccoesDistritaisEncontrados;
	}

	public void setQtdDireccoesDistritaisEncontrados(Integer qtdDireccoesDistritaisEncontrados) {
		this.qtdDireccoesDistritaisEncontrados = qtdDireccoesDistritaisEncontrados;
	}

	public boolean isEditardireccaoDistritalBoolean() {
		return editardireccaoDistritalBoolean;
	}

	public void setEditardireccaoDistritalBoolean(boolean editardireccaoDistritalBoolean) {
		this.editardireccaoDistritalBoolean = editardireccaoDistritalBoolean;
	}

	public boolean isNovadireccaoDistritalBoolean() {
		return novadireccaoDistritalBoolean;
	}

	public void setNovadireccaoDistritalBoolean(boolean novadireccaoDistritalBoolean) {
		this.novadireccaoDistritalBoolean = novadireccaoDistritalBoolean;
	}

	public boolean isCadastrodireccaoDistritalBoolean() {
		return cadastrodireccaoDistritalBoolean;
	}

	public void setCadastrodireccaoDistritalBoolean(boolean cadastrodireccaoDistritalBoolean) {
		this.cadastrodireccaoDistritalBoolean = cadastrodireccaoDistritalBoolean;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public List<Distrital> getDireccoesDistritais() {
		return direccoesDistritais;
	}

	public void setDireccoesDistritais(List<Distrital> direccoesDistritais) {
		this.direccoesDistritais = direccoesDistritais;
	}

	public Distrital getDireccaoDistrital() {
		return direccaoDistrital;
	}

	public void setDireccaoDistrital(Distrital direccaoDistrital) {
		this.direccaoDistrital = direccaoDistrital;
	}

	public List<Provincia> getProvincias() {
		return provincias;
	}

	public void setProvincias(List<Provincia> provincias) {
		this.provincias = provincias;
	}

	

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public List<Distrito> getDistritos() {
		return distritos;
	}

	public void setDistritos(List<Distrito> distritos) {
		this.distritos = distritos;
	}

}
