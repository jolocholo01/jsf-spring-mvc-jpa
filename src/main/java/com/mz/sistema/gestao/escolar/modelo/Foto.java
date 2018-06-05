package com.mz.sistema.gestao.escolar.modelo;

import java.io.ByteArrayInputStream;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Entity
public class Foto {
	private Long id;
	private byte[] fotografia;
	@SuppressWarnings("unused")
	private StreamedContent fotoMontada;
	private Usuario usuario;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	public byte[] getFotografia() {
		return fotografia;
	}

	public void setFotografia(byte[] fotografia) {
		this.fotografia = fotografia;
	}

	@OneToOne(fetch=FetchType.LAZY)
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Transient
	public StreamedContent getFotoMontada() {
		if (this.getFotografia() != null) {
			return new DefaultStreamedContent(new ByteArrayInputStream(this.getFotografia()));
		}

		return new DefaultStreamedContent();
	}

	public void setFotoMontada(StreamedContent fotoMontada) {
		this.fotoMontada = fotoMontada;
	}
}
