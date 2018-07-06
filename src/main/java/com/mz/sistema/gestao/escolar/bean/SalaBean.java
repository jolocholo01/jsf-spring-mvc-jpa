package com.mz.sistema.gestao.escolar.bean;

import java.io.Serializable;
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
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.Funcionario;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.modelo.Sala;
import com.mz.sistema.gestao.escolar.servico.SalaServico;
import com.mz.sistema.gestao.escolar.util.GeradorCodigo;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Controller
@Named
@SessionScope
public class SalaBean implements Serializable {

	private static final long serialVersionUID = 2585423111608804899L;

	private Sala sala = new Sala();
	private List<Sala> salas = new ArrayList<>();
	private Sala salaSelecionadaExclusao;

	@Autowired
	private SalaServico salaServico;
	@Autowired
	private AuthenticationContext authenticationContext;

	private String pesquisa;

	private boolean novaSalaBoolean;

	private boolean cadastroSalaBoolean;
	private Integer qtdSalasEncontradas = 0;

	public void salvar() {
		try {
			Sala salaExistente = salaServico.salaExistenteDaEscola(sala.getNumero());
			if (salaExistente != null && sala.getId() != salaExistente.getId()) {
				Mensagem.mensagemInfo("Já existe esta sala incluida no sitema.");
				return;
			}

			Escola escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();
			Funcionario funcionario = (Funcionario) authenticationContext.getUsuarioLogado();
			if (sala.getId() == 0) {
				sala.setDataCadastro(new Date());
				sala.setFunCadastro(funcionario);
			} else if (sala.getId() != 0) {
				sala.setDataEdicao(new Date());
				sala.setFunAlteracao(funcionario);
			}
			sala.setDescricao(sala.getDescricao().toUpperCase());
			if (cadastroSalaBoolean == true) {
				if (novaSalaBoolean == true) {
					String codigoSala = GeradorCodigo.gerarCodigoAleatorio(3);
					sala.setCodigo(codigoSala);
					Mensagem.mensagemInfo("AVISO: Sala incluida com sucesso!");
				}
				if (novaSalaBoolean == false) {
					Mensagem.mensagemInfo("AVISO: a sala foi atualizada com sucesso!");
					cadastroSalaBoolean = false;
				}
			}
			sala.setEscola(escola);
			salaServico.salvar(sala);
			sala = new Sala();
			buscarUltimaSala();
		} catch (Exception e) {
			e.printStackTrace();
			if (cadastroSalaBoolean == true) {
				if (novaSalaBoolean == true) {
					String codigoSala = GeradorCodigo.gerarCodigoAleatorio(3);
					sala.setCodigo(codigoSala);
					Mensagem.mensagemErro("AVISO: hove erro ao incluir sala!");

				}
				if (novaSalaBoolean == false) {
					Mensagem.mensagemInfo("AVISO: hove erro ao atualizar sala!");
					// cadastroSalaBoolean = false;
				}
			}
		}
	}

	public void buscar() {
		try {
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();

			this.salas = salaServico.obterSalaPorEscolaPorPesquisa(pesquisa.trim().toUpperCase(), escola.getId());
			if (salas == null) {
			} else
				qtdSalasEncontradas = salas.size();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void editar(Sala sala) {
		this.sala = sala;
		novaSalaBoolean = false;
		cadastroSalaBoolean = true;
	}

	public void prepararParaExcluir(Sala sala) {
		this.salaSelecionadaExclusao = sala;

	}

	public void novaSala() {
		sala = new Sala();
		sala.setDataCadastro(new Date());
		this.cadastroSalaBoolean = true;
		this.novaSalaBoolean = true;
		buscarUltimaSala();
	}

	private void buscarUltimaSala() {
		try {
			FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
			Escola escola = funcionarioEscola.getEscola();
			Long numeroUltimaSala = salaServico.obterNumeroUltimaSalaPorEscola(escola.getId());
			if (numeroUltimaSala == null) {
				numeroUltimaSala = 1L;
			} else {
				numeroUltimaSala++;
			}
			if (numeroUltimaSala <= 9) {
				sala.setOrdem("0" + numeroUltimaSala.toString());
			} else {
				sala.setOrdem(numeroUltimaSala.toString());
			}
			if (sala.getNumero() == null)
				sala.setNumero(sala.getOrdem());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void voltarParaPequisa() {
		sala = null;
		this.cadastroSalaBoolean = false;
		if (novaSalaBoolean == true) {
			novaSalaBoolean = false;
		}
		buscar();

	}

	public void excluir() {
		try {
			salaServico.excluir(salaSelecionadaExclusao);
			Mensagem.mensagemInfo("AVISO: Sala excluida com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.mensagemErro("ERRO: não foi excluida a sala pois existe dependência.");
		}
	}

	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	public List<Sala> getSalas() {
		return salas;
	}

	public void setSalas(List<Sala> salas) {
		this.salas = salas;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public Sala getSalaSelecionadaExclusao() {
		return salaSelecionadaExclusao;
	}

	public void setSalaSelecionadaExclusao(Sala salaSelecionadaExclusao) {
		this.salaSelecionadaExclusao = salaSelecionadaExclusao;
	}

	public boolean isNovaSalaBoolean() {
		return novaSalaBoolean;
	}

	public void setNovaSalaBoolean(boolean novaSalaBoolean) {
		this.novaSalaBoolean = novaSalaBoolean;
	}

	public boolean isCadastroSalaBoolean() {
		return cadastroSalaBoolean;
	}

	public void setCadastroSalaBoolean(boolean cadastroSalaBoolean) {
		this.cadastroSalaBoolean = cadastroSalaBoolean;
	}

	public Integer getQtdSalasEncontradas() {
		return qtdSalasEncontradas;
	}

	public void setQtdSalasEncontradas(Integer qtdSalasEncontradas) {
		this.qtdSalasEncontradas = qtdSalasEncontradas;
	}

}
