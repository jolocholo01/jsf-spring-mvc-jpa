// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.enumerado.RoleName;
import com.mz.sistema.gestao.escolar.modelo.Permissao;
import com.mz.sistema.gestao.escolar.servico.PermissaoServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Named
@SessionScope
@Controller
public class PermissaoBean {
	private Permissao permissao = new Permissao();
	private List<Permissao> permissoes = new ArrayList<>();
	private List<RoleName> roleNames = Arrays.asList(RoleName.values());

	@Autowired
	private PermissaoServico permissaoServico;
	
	private boolean cadastroPermissaoBoolean;
	private boolean novoPermissaoBoolean;
	private boolean editarPermissaoBoolean;
	private String pesquisa;
	private Integer qtdPermissesEncontrados;
	private Integer quantidadeCaracteres;
	private Permissao permissaoSelecionada;

	public void iniciarBean() {

		try {
			cadastroPermissaoBoolean = false;
			novoPermissaoBoolean = false;
			editarPermissaoBoolean = false;
			permissoes = new ArrayList<>();
			pesquisa = null;
			qtdPermissesEncontrados = 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void salvar() {
		try {
			Permissao permissaoExistente=permissaoServico.obterPermissaoExistente(permissao.getDescricao().getLabel());
			if(permissaoExistente!=null && permissaoExistente.getId() !=permissao.getId()){
				Mensagem.mensagemErro("ERRO: Já existe uma permissão cadastrada no sistema!");
				return;
			}
			
			permissao.setNome(permissao.getDescricao().getLabel());
			permissaoServico.salvar(permissao);
			if(permissao.getId() ==null){
				Mensagem.mensagemInfo("AVISO: Permissao foi cadastrada com sucesso!");
			}else if(permissao.getId() ==null){
				Mensagem.mensagemInfo("AVISO: Permissao foi atualizada com sucesso!");
			}
			if(editarPermissaoBoolean == true){
				editarPermissaoBoolean = false;
				cadastroPermissaoBoolean=false;
			}
			permissao = new Permissao();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void prepararParExcluir(Permissao permissao) {
		this.permissaoSelecionada= permissao;
	}
	public void excluir() {
		try {
			permissaoServico.excluir(this.permissaoSelecionada);
			buscar();
			Mensagem.mensagemInfo("Aviso: permissão excluida com sucesso!");
			System.out.println("Chamou a funaco!");

		} catch (Exception e) {
			Mensagem.mensagemAlerta("Aviso: a permissão não foi excluida através da dependência.");
		}

	}

	public void buscar() {
		try {
			qtdPermissesEncontrados = 0;
			permissoes = permissaoServico.obterPermissoessPorDscricao(pesquisa.toUpperCase());
			if (permissoes != null) {
				qtdPermissesEncontrados = permissoes.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void contarQuantidadeCarateres() {
		quantidadeCaracteres = 0;
		try {
			if (permissao.getObservacao() != null) {
				quantidadeCaracteres = permissao.getObservacao().length();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void nova() {
		cadastroPermissaoBoolean= true;
		novoPermissaoBoolean = true;
		permissao = new Permissao();
		permissao.setDataCadastro(new Date() );
		editarPermissaoBoolean = false;
	}

	public void editar(Permissao permissao) {
		cadastroPermissaoBoolean = true;
		novoPermissaoBoolean = false;
		editarPermissaoBoolean = true;
		//roleName.=permissao.getDescricao().toString();
		this.permissao = permissao;
		if(this.permissao.getDataCadastro()==null){
			this.permissao.setDataCadastro(new Date() );
		}
		contarQuantidadeCarateres();
	}

	public void voltar() {
		cadastroPermissaoBoolean = false;
		novoPermissaoBoolean = false;
		permissao = null;
		editarPermissaoBoolean = false;

	}

	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public boolean isCadastroPermissaoBoolean() {
		return cadastroPermissaoBoolean;
	}

	public void setCadastroPermissaoBoolean(boolean cadastroPermissaoBoolean) {
		this.cadastroPermissaoBoolean = cadastroPermissaoBoolean;
	}

	public boolean isNovoPermissaoBoolean() {
		return novoPermissaoBoolean;
	}

	public void setNovoPermissaoBoolean(boolean novoPermissaoBoolean) {
		this.novoPermissaoBoolean = novoPermissaoBoolean;
	}

	public boolean isEditarPermissaoBoolean() {
		return editarPermissaoBoolean;
	}

	public void setEditarPermissaoBoolean(boolean editarPermissaoBoolean) {
		this.editarPermissaoBoolean = editarPermissaoBoolean;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public Integer getQtdPermissesEncontrados() {
		return qtdPermissesEncontrados;
	}

	public void setQtdPermissesEncontrados(Integer qtdPermissesEncontrados) {
		this.qtdPermissesEncontrados = qtdPermissesEncontrados;
	}

	public List<RoleName> getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(List<RoleName> roleNames) {
		this.roleNames = roleNames;
	}

	public Integer getQuantidadeCaracteres() {
		return quantidadeCaracteres;
	}

	public void setQuantidadeCaracteres(Integer quantidadeCaracteres) {
		this.quantidadeCaracteres = quantidadeCaracteres;
	}

	public Permissao getPermissaoSelecionada() {
		return permissaoSelecionada;
	}

	public void setPermissaoSelecionada(Permissao permissaoSelecionada) {
		this.permissaoSelecionada = permissaoSelecionada;
	}

	
}
