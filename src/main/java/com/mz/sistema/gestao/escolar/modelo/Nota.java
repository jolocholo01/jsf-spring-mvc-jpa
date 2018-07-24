package com.mz.sistema.gestao.escolar.modelo;

import java.io.Serializable;
/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.mz.sistema.gestao.escolar.chave.composta.NotaId;

@Entity
public class Nota implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7759791288880599017L;
	private NotaId id;
	private Matricula matricula;
	private Disciplina disciplina;

	/* notas do aluno do primeiro trimetre */
	private Trimestre trimestre;
	private Funcionario professor;
	private Double ac1;
	private Double ac2;
	private Double ac3;
	private Double mediaAc;
	private Double as1;
	private Double as2;
	private Double mediaAs;
	private Double avaliacaoFinal;
	private int mediaTrimestral;
	private String situacao;

	/* notas do aluno do segundo trimetre */
	private Trimestre trimestre2;
	private Funcionario professor2;
	private Double ac12;
	private Double ac22;
	private Double ac32;
	private Double mediaAc2;
	private Double as12;
	private Double as22;
	private Double mediaAs2;
	private Double avaliacaoFinal2;
	private int mediaTrimestral2;
	private String situacao2;

	/* notas do aluno do terceiro trimetre */
	private Trimestre trimestre3;
	private Funcionario professor3;
	private Double ac13;
	private Double ac23;
	private Double ac33;
	private Double mediaAc3;
	private Double as13;
	private Double as23;
	private Double mediaAs3;
	private Double avaliacaoFinal3;
	private int mediaTrimestral3;
	private String situacao3;

	private int mediaAnual;
	private String situacaoAnual;
	private Integer ordem;

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "id_matricula", column = @Column(name = "id_matricula", nullable = false)),
			@AttributeOverride(name = "id_disciplina", column = @Column(name = "id_disciplina", nullable = false)) })
	public NotaId getId() {
		return id;
	}

	public void setId(NotaId id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "id_matricula", nullable = false, insertable = false, updatable = false)
	public Matricula getMatricula() {
		return matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

	@ManyToOne
	@JoinColumn(name = "id_disciplina", nullable = false, insertable = false, updatable = false)
	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	@ManyToOne
	@JoinColumn(name = "id_trimestre")
	public Trimestre getTrimestre() {
		return trimestre;
	}

	public void setTrimestre(Trimestre trimestre) {
		this.trimestre = trimestre;
	}

	@ManyToOne
	public Funcionario getProfessor() {
		return professor;
	}

	public void setProfessor(Funcionario professor) {
		this.professor = professor;
	}

	public Double getAc1() {
		return ac1;
	}

	public void setAc1(Double ac1) {
		this.ac1 = ac1;
	}

	public Double getAc2() {
		return ac2;
	}

	public void setAc2(Double ac2) {
		this.ac2 = ac2;
	}

	public Double getAc3() {
		return ac3;
	}

	public void setAc3(Double ac3) {
		this.ac3 = ac3;
	}

	public Double getMediaAc() {
		return mediaAc;
	}

	public void setMediaAc(Double mediaAc) {
		this.mediaAc = mediaAc;
	}

	public Double getAs1() {
		return as1;
	}

	public void setAs1(Double as1) {
		this.as1 = as1;
	}

	public Double getAs2() {
		return as2;
	}

	public void setAs2(Double as2) {
		this.as2 = as2;
	}

	public Double getMediaAs() {
		return mediaAs;
	}

	public void setMediaAs(Double mediaAs) {
		this.mediaAs = mediaAs;
	}

	public Double getAvaliacaoFinal() {
		return avaliacaoFinal;
	}

	public void setAvaliacaoFinal(Double avaliacaoFinal) {
		this.avaliacaoFinal = avaliacaoFinal;
	}

	public int getMediaAnual() {
		return mediaAnual;
	}

	public void setMediaAnual(int mediaAnual) {
		this.mediaAnual = mediaAnual;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public int getMediaTrimestral() {
		return mediaTrimestral;
	}

	public void setMediaTrimestral(int mediaTrimestral) {
		this.mediaTrimestral = mediaTrimestral;
	}

	@ManyToOne
	public Trimestre getTrimestre2() {
		return trimestre2;
	}

	public void setTrimestre2(Trimestre trimestre2) {
		this.trimestre2 = trimestre2;
	}

	@ManyToOne
	public Funcionario getProfessor2() {
		return professor2;
	}

	public void setProfessor2(Funcionario professor2) {
		this.professor2 = professor2;
	}

	public Double getAc12() {
		return ac12;
	}

	public void setAc12(Double ac12) {
		this.ac12 = ac12;
	}

	public Double getAc22() {
		return ac22;
	}

	public void setAc22(Double ac22) {
		this.ac22 = ac22;
	}

	public Double getAc32() {
		return ac32;
	}

	public void setAc32(Double ac32) {
		this.ac32 = ac32;
	}

	public Double getMediaAc2() {
		return mediaAc2;
	}

	public void setMediaAc2(Double mediaAc2) {
		this.mediaAc2 = mediaAc2;
	}

	public Double getAs12() {
		return as12;
	}

	public void setAs12(Double as12) {
		this.as12 = as12;
	}

	public Double getAs22() {
		return as22;
	}

	public void setAs22(Double as22) {
		this.as22 = as22;
	}

	public Double getMediaAs2() {
		return mediaAs2;
	}

	public void setMediaAs2(Double mediaAs2) {
		this.mediaAs2 = mediaAs2;
	}

	public Double getAvaliacaoFinal2() {
		return avaliacaoFinal2;
	}

	public void setAvaliacaoFinal2(Double avaliacaoFinal2) {
		this.avaliacaoFinal2 = avaliacaoFinal2;
	}

	public int getMediaTrimestral2() {
		return mediaTrimestral2;
	}

	public void setMediaTrimestral2(int mediaTrimestral2) {
		this.mediaTrimestral2 = mediaTrimestral2;
	}

	public String getSituacao2() {
		return situacao2;
	}

	public void setSituacao2(String situacao2) {
		this.situacao2 = situacao2;
	}

	@ManyToOne
	public Trimestre getTrimestre3() {
		return trimestre3;
	}

	public void setTrimestre3(Trimestre trimestre3) {
		this.trimestre3 = trimestre3;
	}

	@ManyToOne
	public Funcionario getProfessor3() {
		return professor3;
	}

	public void setProfessor3(Funcionario professor3) {
		this.professor3 = professor3;
	}

	public Double getAc13() {
		return ac13;
	}

	public void setAc13(Double ac13) {
		this.ac13 = ac13;
	}

	public Double getAc23() {
		return ac23;
	}

	public void setAc23(Double ac23) {
		this.ac23 = ac23;
	}

	public Double getAc33() {
		return ac33;
	}

	public void setAc33(Double ac33) {
		this.ac33 = ac33;
	}

	public Double getMediaAc3() {
		return mediaAc3;
	}

	public void setMediaAc3(Double mediaAc3) {
		this.mediaAc3 = mediaAc3;
	}

	public Double getAs13() {
		return as13;
	}

	public void setAs13(Double as13) {
		this.as13 = as13;
	}

	public Double getAs23() {
		return as23;
	}

	public void setAs23(Double as23) {
		this.as23 = as23;
	}

	public Double getMediaAs3() {
		return mediaAs3;
	}

	public void setMediaAs3(Double mediaAs3) {
		this.mediaAs3 = mediaAs3;
	}

	public Double getAvaliacaoFinal3() {
		return avaliacaoFinal3;
	}

	public void setAvaliacaoFinal3(Double avaliacaoFinal3) {
		this.avaliacaoFinal3 = avaliacaoFinal3;
	}

	public int getMediaTrimestral3() {
		return mediaTrimestral3;
	}

	public void setMediaTrimestral3(int mediaTrimestral3) {
		this.mediaTrimestral3 = mediaTrimestral3;
	}

	public String getSituacaoAnual() {
		return situacaoAnual;
	}

	public void setSituacaoAnual(String situacaoAnual) {
		this.situacaoAnual = situacaoAnual;
	}

	public String getSituacao3() {
		return situacao3;
	}

	public void setSituacao3(String situacao3) {
		this.situacao3 = situacao3;
	}

	@Transient
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

}
