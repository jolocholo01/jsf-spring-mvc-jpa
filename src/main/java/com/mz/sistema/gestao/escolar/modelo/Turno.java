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
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.mz.sistema.gestao.escolar.enumerado.TipoTurno;

@Entity
public class Turno implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5083101781752557820L;
	private Long id;
	private Integer ordem;
	private String curso;
	private TipoTurno descricao;
	private String sigla;
	private List<HorarioAula> horarioAulas;
	private Date horaInicio;
	private Date horaFim;
	private String observacao;
	private Date dataCadastro;

	public Turno() {

	}

	public Turno(String curso, TipoTurno descricao, String sigla) {
		super();
		this.curso = curso;
		this.descricao = descricao;
		this.sigla = sigla;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	@Enumerated(EnumType.STRING)
	public TipoTurno getDescricao() {
		return descricao;
	}

	public void setDescricao(TipoTurno descricao) {
		this.descricao = descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@OneToMany(mappedBy = "turno")
	public List<HorarioAula> getHorarioAulas() {
		return horarioAulas;
	}

	public void setHorarioAulas(List<HorarioAula> horarioAulas) {
		this.horarioAulas = horarioAulas;
	}

	

	@Temporal(TemporalType.TIME)
	public Date getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Date horaInicio) {
		this.horaInicio = horaInicio;
	}

	@Temporal(TemporalType.TIME)
	public Date getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(Date horaFim) {
		this.horaFim = horaFim;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Turno other = (Turno) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

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

}
