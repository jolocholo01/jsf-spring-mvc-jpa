package com.mz.sistema.gestao.escolar.modelo;

import java.io.Serializable;
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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class HorarioAula implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7862318987094038184L;
	private long id;
	private Turno turno;
	private Integer ordem;
	private Date aulaInicial;
	private String formatoAulaInicial;
	private Date aulaFinal;
	private String formatoAulaFinal;
	private Date dataCadastro;
	private Funcionario funCadastro;
	private Date dataAlteracao;
	private Funcionario funAlteracao;
	private List<Horario> horarios;
	private Escola escola;

	// Verificaco de horario de professor
	// Horario(s) ocupado(s) pelo professor escolhido, nesta turma
	private boolean horarioProfNestaTurmaSeg;
	private boolean horarioProfNestaTurmaTerc;
	private boolean horarioProfNestaTurmaQua;
	private boolean horarioProfNestaTurmaQui;
	private boolean horarioProfNestaTurmaSex;

	// Horario(s) ocupado(s) pelo professor escolhido, em outra turma
	private boolean horarioProfOutraTurmaSeg;
	private boolean horarioProfOutraTurmaTerc;
	private boolean horarioProfOutraTurmaQua;
	private boolean horarioProfOutraTurmaQui;
	private boolean horarioProfOutraTurmaSex;

	// Horario(s) ocupado(s) por outro(s) professores(as) nesta turma
	private boolean horariosOutrosProfNestaTurmaSeg;
	private boolean horariosOutrosProfNestaTurmaTerc;
	private boolean horariosOutrosProfNestaTurmaQua;
	private boolean horariosOutrosProfNestaTurmaQui;
	private boolean horariosOutrosProfNestaTurmaSex;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "id_turno")
	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

	@Temporal(TemporalType.TIME)
	public Date getAulaInicial() {
		return aulaInicial;
	}

	public void setAulaInicial(Date aulaInicial) {
		this.aulaInicial = aulaInicial;
	}

	@Temporal(TemporalType.TIME)
	public Date getAulaFinal() {
		return aulaFinal;
	}

	public void setAulaFinal(Date aulaFinal) {
		this.aulaFinal = aulaFinal;
	}

	@ManyToOne
	@JoinColumn(name = "id_fun_cadastro")
	public Funcionario getFunCadastro() {
		return funCadastro;
	}

	public void setFunCadastro(Funcionario funCadastro) {
		this.funCadastro = funCadastro;
	}

	@Temporal(TemporalType.DATE)
	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	@ManyToOne
	@JoinColumn(name = "id_fun_alteracao")
	public Funcionario getFunAlteracao() {
		return funAlteracao;
	}

	public void setFunAlteracao(Funcionario funAlteracao) {
		this.funAlteracao = funAlteracao;
	}

	@Temporal(TemporalType.TIME)
	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "horarioAula")
	public List<Horario> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<Horario> horarios) {
		this.horarios = horarios;
	}

	@Transient
	public String getFormatoAulaInicial() {

		return formatoAulaInicial;
	}

	public void setFormatoAulaInicial(String formatoAulaInicial) {
		this.formatoAulaInicial = formatoAulaInicial;
	}

	@Transient
	public String getFormatoAulaFinal() {
		return formatoAulaFinal;
	}

	public void setFormatoAulaFinal(String formatoAulaFinal) {
		this.formatoAulaFinal = formatoAulaFinal;
	}

	@Transient
	public boolean isHorarioProfNestaTurmaSeg() {
		return horarioProfNestaTurmaSeg;
	}

	public void setHorarioProfNestaTurmaSeg(boolean horarioProfNestaTurmaSeg) {
		this.horarioProfNestaTurmaSeg = horarioProfNestaTurmaSeg;
	}

	@Transient
	public boolean isHorarioProfNestaTurmaTerc() {
		return horarioProfNestaTurmaTerc;
	}

	public void setHorarioProfNestaTurmaTerc(boolean horarioProfNestaTurmaTerc) {
		this.horarioProfNestaTurmaTerc = horarioProfNestaTurmaTerc;
	}

	@Transient
	public boolean isHorarioProfNestaTurmaQua() {
		return horarioProfNestaTurmaQua;
	}

	public void setHorarioProfNestaTurmaQua(boolean horarioProfNestaTurmaQua) {
		this.horarioProfNestaTurmaQua = horarioProfNestaTurmaQua;
	}

	@Transient
	public boolean isHorarioProfNestaTurmaQui() {
		return horarioProfNestaTurmaQui;
	}

	public void setHorarioProfNestaTurmaQui(boolean horarioProfNestaTurmaQui) {
		this.horarioProfNestaTurmaQui = horarioProfNestaTurmaQui;
	}

	@Transient
	public boolean isHorarioProfNestaTurmaSex() {
		return horarioProfNestaTurmaSex;
	}

	public void setHorarioProfNestaTurmaSex(boolean horarioProfNestaTurmaSex) {
		this.horarioProfNestaTurmaSex = horarioProfNestaTurmaSex;
	}

	@Transient
	public boolean isHorariosOutrosProfNestaTurmaSeg() {
		return horariosOutrosProfNestaTurmaSeg;
	}

	public void setHorariosOutrosProfNestaTurmaSeg(boolean horariosOutrosProfNestaTurmaSeg) {
		this.horariosOutrosProfNestaTurmaSeg = horariosOutrosProfNestaTurmaSeg;
	}

	@Transient
	public boolean isHorariosOutrosProfNestaTurmaTerc() {
		return horariosOutrosProfNestaTurmaTerc;
	}

	public void setHorariosOutrosProfNestaTurmaTerc(boolean horariosOutrosProfNestaTurmaTerc) {
		this.horariosOutrosProfNestaTurmaTerc = horariosOutrosProfNestaTurmaTerc;
	}
	@Transient
	public boolean isHorariosOutrosProfNestaTurmaQua() {
		return horariosOutrosProfNestaTurmaQua;
	}

	public void setHorariosOutrosProfNestaTurmaQua(boolean horariosOutrosProfNestaTurmaQua) {
		this.horariosOutrosProfNestaTurmaQua = horariosOutrosProfNestaTurmaQua;
	}
	@Transient
	public boolean isHorariosOutrosProfNestaTurmaQui() {
		return horariosOutrosProfNestaTurmaQui;
	}

	public void setHorariosOutrosProfNestaTurmaQui(boolean horariosOutrosProfNestaTurmaQui) {
		this.horariosOutrosProfNestaTurmaQui = horariosOutrosProfNestaTurmaQui;
	}
	@Transient
	public boolean isHorariosOutrosProfNestaTurmaSex() {
		return horariosOutrosProfNestaTurmaSex;
	}

	public void setHorariosOutrosProfNestaTurmaSex(boolean horariosOutrosProfNestaTurmaSex) {
		this.horariosOutrosProfNestaTurmaSex = horariosOutrosProfNestaTurmaSex;
	}

	@Transient
	public boolean isHorarioProfOutraTurmaSeg() {
		return horarioProfOutraTurmaSeg;
	}

	public void setHorarioProfOutraTurmaSeg(boolean horarioProfOutraTurmaSeg) {
		this.horarioProfOutraTurmaSeg = horarioProfOutraTurmaSeg;
	}

	@Transient
	public boolean isHorarioProfOutraTurmaTerc() {
		return horarioProfOutraTurmaTerc;
	}

	public void setHorarioProfOutraTurmaTerc(boolean horarioProfOutraTurmaTerc) {
		this.horarioProfOutraTurmaTerc = horarioProfOutraTurmaTerc;
	}

	@Transient
	public boolean isHorarioProfOutraTurmaQua() {
		return horarioProfOutraTurmaQua;
	}

	public void setHorarioProfOutraTurmaQua(boolean horarioProfOutraTurmaQua) {
		this.horarioProfOutraTurmaQua = horarioProfOutraTurmaQua;
	}

	@Transient
	public boolean isHorarioProfOutraTurmaQui() {
		return horarioProfOutraTurmaQui;
	}

	public void setHorarioProfOutraTurmaQui(boolean horarioProfOutraTurmaQui) {
		this.horarioProfOutraTurmaQui = horarioProfOutraTurmaQui;
	}

	@Transient
	public boolean isHorarioProfOutraTurmaSex() {
		return horarioProfOutraTurmaSex;
	}

	public void setHorarioProfOutraTurmaSex(boolean horarioProfOutraTurmaSex) {
		this.horarioProfOutraTurmaSex = horarioProfOutraTurmaSex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HorarioAula other = (HorarioAula) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@ManyToOne
	public Escola getEscola() {
		return escola;
	}

	public void setEscola(Escola escola) {
		this.escola = escola;
	}

}
