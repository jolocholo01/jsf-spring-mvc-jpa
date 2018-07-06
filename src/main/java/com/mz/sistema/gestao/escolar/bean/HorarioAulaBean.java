
package com.mz.sistema.gestao.escolar.bean;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.mz.sistema.gestao.escolar.modelo.HorarioAula;
import com.mz.sistema.gestao.escolar.modelo.Turno;
import com.mz.sistema.gestao.escolar.servico.HorarioAulaServico;
import com.mz.sistema.gestao.escolar.servico.TurnoServico;
import com.mz.sistema.gestao.escolar.util.DataUtils;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Named
@SessionScope
@Controller
public class HorarioAulaBean {
	private HorarioAula horarioAula = new HorarioAula();
	private HorarioAula horarioAulaSelecionado = new HorarioAula();
	private List<HorarioAula> horarioAulas = new ArrayList<>();
	private HorarioAula horarioAulaSelecionadoPraraEditar;
	private HorarioAula horarioAulaSelecionadoPraraExclusao;
	private static final String FORMATO_HORA_PADRAO = "HH:mm";

	@Autowired
	private HorarioAulaServico horarioAulaServico;
	@Autowired
	private TurnoServico turnoServico;
	@Autowired
	private AuthenticationContext authenticationContext;

	public void iniciarBean() {
		horarioAulas = null;
		this.horarioAula = new HorarioAula();
		this.horarioAulaSelecionado = new HorarioAula();
		this.horarioAulaSelecionadoPraraEditar = null;

	}

	public void salvar() {

		HorarioAula horarioAulaExistente = horarioAulaServico.horarioAulaExistenteDaEscola(
				horarioAula.getTurno().getId(), horarioAulaSelecionado.getAulaInicial(),
				horarioAulaSelecionado.getOrdem());
		if (horarioAulaExistente != null && horarioAulaExistente.getId() != horarioAula.getId()) {
			Mensagem.mensagemAlerta("ATENÇÃO: Já existe este Horário de Aula cadastrado no sistema.");
			return;
		}

		horarioAulaSelecionado.setEscola(authenticationContext.getFuncionarioEscolaLogada().getEscola());
		horarioAulaSelecionado.setTurno(horarioAula.getTurno());
		horarioAulaServico.salvar(horarioAulaSelecionado);
		Mensagem.mensagemInfo("AVISO: Horário de Aula cadastrado com sucesso!");

		// horarioAulas = horarioAulaServico.obterPorEscola();

		horarioAulaSelecionado = new HorarioAula();
	}

	public void cadastrarEdicao() {
		horarioAulaServico.salvar(horarioAulaSelecionadoPraraEditar);
		Mensagem.mensagemInfo("Horário de Aula atualizado com sucesso!");
		this.horarioAulaSelecionadoPraraEditar = null;
	}

	public void volatrDaSelecaoDeTurno() {
		this.horarioAula = null;
		this.horarioAula = new HorarioAula();
		this.horarioAulaSelecionado = new HorarioAula();
		this.horarioAulas = null;
	}

	public String cadastroHorarioAula() {
		volatrDaSelecaoDeTurno();
		this.horarioAulaSelecionadoPraraEditar = null;
		return "/academico/adjunto/horario/horarioAula?faces-redirect=true";
	}

	public String cadastroHorarioAulaDirector() {
		horarioAulas = null;
		this.horarioAula = new HorarioAula();
		this.horarioAulaSelecionado = new HorarioAula();
		this.horarioAulaSelecionadoPraraEditar = null;
		return "/academico/director/horario/horarioAula?faces-redirect=true";
	}

	public String manutencaoHorarioAulaDirector() {
		horarioAulas = null;
		this.horarioAula = new HorarioAula();
		this.horarioAulaSelecionado = new HorarioAula();
		return "/academico/director/horario/horarioAulaAlterar?faces-redirect=true";
	}

	public void editarHorarioAula(HorarioAula horarioAula) {
		this.horarioAulaSelecionadoPraraEditar = horarioAula;
	}

	public void voltarEditarHorarioAula() {
		this.horarioAulaSelecionadoPraraEditar = null;
	}

	public List<HorarioAula> getObterHorarioAulas() {
		List<HorarioAula> horarioAulas = null;
		if (this.horarioAula.getTurno() == null) {
		} else if (this.horarioAula.getTurno() != null) {
			horarioAulas = horarioAulaServico.obterHorarioAulaPorEscolaTurno(this.horarioAula.getTurno().getId());
		}
		return horarioAulas;
	}

	public Turno getObterDescricaoDoTurno() {
		Turno turno = null;
		if (this.horarioAula.getTurno() == null) {
		} else if (this.horarioAula.getTurno() != null) {
			turno = turnoServico.obterTurnoPorId(this.horarioAula.getTurno().getId());
			this.horarioAulas = horarioAulaServico.obterHorarioAulaPorEscolaTurno(this.horarioAula.getTurno().getId());

		}
		return turno;
	}

	public void prepararExcluirHorarioAula(HorarioAula horarioAula) {
		this.horarioAulaSelecionadoPraraExclusao = horarioAula;

	}

	public void excluirHorarioAula() {
		try {
			horarioAulaServico.excluir(this.horarioAulaSelecionadoPraraExclusao);
			this.horarioAulas = horarioAulaServico.obterHorarioAulaPorEscolaTurno(this.horarioAula.getTurno().getId());
			Mensagem.mensagemInfo("Horário de aula excluido com sucesso!");
			System.out.println("Chamou a funaco!");

		} catch (Exception e) {
			Mensagem.mensagemErro("Erro: Horário de aula não foi excluido através da dependência!");
		}

	}

	public void cancelarExclusao() {
		this.horarioAulaSelecionadoPraraExclusao = null;
		System.out.println("Foi Cancelado o Horario. ");
	}

	public HorarioAula getHorarioAula() {
		return horarioAula;
	}

	public void setHorarioAula(HorarioAula horarioAula) {
		this.horarioAula = horarioAula;
	}

	public List<HorarioAula> getHorarioAulas() {
		return horarioAulas;
	}

	public void setHorarioAulas(List<HorarioAula> horarioAulas) {
		this.horarioAulas = horarioAulas;
	}

	public HorarioAula getHorarioAulaSelecionado() {

		if (horarioAulaSelecionado.getAulaInicial() == null) {

		} else {
			Calendar dataCalculo = Calendar.getInstance();
			dataCalculo.setTime(horarioAulaSelecionado.getAulaInicial());
			dataCalculo.add(Calendar.MINUTE, 45);
			horarioAulaSelecionado.setAulaFinal(dataCalculo.getTime());
		}
		return horarioAulaSelecionado;
	}

	public void setHorarioAulaSelecionado(HorarioAula horarioAulaSelecionado) {
		this.horarioAulaSelecionado = horarioAulaSelecionado;
	}

	public HorarioAula getHorarioAulaSelecionadoPraraEditar() {
		if (horarioAulaSelecionadoPraraEditar == null) {

		} else {
			if (horarioAulaSelecionadoPraraEditar.getAulaInicial() == null) {

			} else {
				Calendar dataCalculo = Calendar.getInstance();
				dataCalculo.setTime(horarioAulaSelecionadoPraraEditar.getAulaInicial());
				dataCalculo.add(Calendar.MINUTE, 45);
				horarioAulaSelecionadoPraraEditar.setAulaFinal(dataCalculo.getTime());
			}
		}
		return horarioAulaSelecionadoPraraEditar;
	}

	public void setHorarioAulaSelecionadoPraraEditar(HorarioAula horarioAulaSelecionadoPraraEditar) {
		this.horarioAulaSelecionadoPraraEditar = horarioAulaSelecionadoPraraEditar;
	}

	public HorarioAula getHorarioAulaSelecionadoPraraExclusao() {
		if (horarioAulaSelecionadoPraraExclusao == null) {
		} else if (horarioAulaSelecionadoPraraExclusao != null) {
			if (horarioAulaSelecionadoPraraExclusao.getAulaInicial() != null) {
				horarioAulaSelecionadoPraraExclusao.setFormatoAulaInicial(DataUtils.obterDataFormatoBanco(
						horarioAulaSelecionadoPraraExclusao.getAulaInicial(), FORMATO_HORA_PADRAO));
			}
			if (horarioAulaSelecionadoPraraExclusao.getAulaInicial() != null) {
				horarioAulaSelecionadoPraraExclusao.setFormatoAulaFinal(DataUtils.obterDataFormatoBanco(
						horarioAulaSelecionadoPraraExclusao.getAulaFinal(), FORMATO_HORA_PADRAO));
			}
		}
		return horarioAulaSelecionadoPraraExclusao;
	}

	public void setHorarioAulaSelecionadoPraraExclusao(HorarioAula horarioAulaSelecionadoPraraExclusao) {
		this.horarioAulaSelecionadoPraraExclusao = horarioAulaSelecionadoPraraExclusao;
	}

}