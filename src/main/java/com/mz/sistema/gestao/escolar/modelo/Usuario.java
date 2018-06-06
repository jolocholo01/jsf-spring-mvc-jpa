package com.mz.sistema.gestao.escolar.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mz.sistema.gestao.escolar.enumerado.EstadoCivil;

//Esta classe nao pode ser abstract porque esta ser estanciado na faze de recoperacao de senha
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario implements Serializable, UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 577703462298528982L;
	private Long id;
	private String nome;
	private String login;
	private String senha;
	private boolean ativo;
	private boolean sexo;
	private boolean ocultarEmail = false;
	private boolean senhaAlterada = false;	
	private Date dataNascimento;
	private String pai;
	private String mae;
	private String email;
	private Boolean enganoNoEnvioEmail = false;
	private String nascionalidade;
	private EstadoCivil estadoCivil;
	private String novaSenha;
	private String confirmaSenha;
	private String telefone;
	private List<Permissao> permissoes;
	private Endereco enderenco = new Endereco();

	private RecoperarSenha recoperarSenha;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(unique = true)
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public boolean isSexo() {
		return sexo;
	}

	public void setSexo(boolean sexo) {
		this.sexo = sexo;
	}

	@Temporal(TemporalType.DATE)
	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getPai() {
		return pai;
	}

	public void setPai(String pai) {
		this.pai = pai;
	}

	public String getMae() {
		return mae;
	}

	public void setMae(String mae) {
		this.mae = mae;
	}

	// Esse daddo nao pertence para Aluno
	@Enumerated(EnumType.STRING)
	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	// @Email
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ManyToMany
	@JoinTable(name = "usuario_permissao", joinColumns = { @JoinColumn(name = "id_usuario") }, inverseJoinColumns = {
			@JoinColumn(name = "id_permissao") })
	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	@Embedded
	public Endereco getEnderenco() {
		return enderenco;
	}

	public void setEnderenco(Endereco enderenco) {
		this.enderenco = enderenco;
	}

	@Transient
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> autorizacoes = new ArrayList<GrantedAuthority>();
		for (Permissao permissao : getPermissoes()) {
			autorizacoes.add(new SimpleGrantedAuthority(permissao.getDescricao().toString()));
		}

		return autorizacoes;
	}

	@Transient
	@Override
	public String getPassword() {
		return senha;
	}

	@Transient
	@Override
	public String getUsername() {
		return login;
	}

	@Transient
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Transient
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Transient
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Transient
	@Override
	public boolean isEnabled() {
		return ativo;
	}

	public String getNascionalidade() {
		return nascionalidade;
	}

	public void setNascionalidade(String nascionalidade) {
		this.nascionalidade = nascionalidade;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(name = "ocultar_email")
	public boolean isOcultarEmail() {
		return ocultarEmail;
	}

	public void setOcultarEmail(boolean ocultarEmail) {
		this.ocultarEmail = ocultarEmail;
	}

	@Transient
	// @NotNull(message="Aviso: o campo nova Nova Senha é obrigatório.")
	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	@Transient
	// @NotNull(message="Aviso: o campo Confirmar Senha é obrigatório. ")
	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@OneToOne( mappedBy = "usuario")
	public RecoperarSenha getRecoperarSenha() {
		return recoperarSenha;
	}

	public void setRecoperarSenha(RecoperarSenha recoperarSenha) {
		this.recoperarSenha = recoperarSenha;
	}

	public boolean isSenhaAlterada() {
		return senhaAlterada;
	}

	public void setSenhaAlterada(boolean senhaAlterada) {
		this.senhaAlterada = senhaAlterada;
	}

	public Boolean getEnganoNoEnvioEmail() {
		return enganoNoEnvioEmail;
	}

	public void setEnganoNoEnvioEmail(Boolean enganoNoEnvioEmail) {
		this.enganoNoEnvioEmail = enganoNoEnvioEmail;
	}

	

}
