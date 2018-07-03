// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.modelo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@PrimaryKeyJoinColumn(name = "id") // id da tabela aluno
@SqlResultSetMapping(name = "deleteResult", columns = { @ColumnResult(name = "count") })

public class Aluno extends Usuario {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5559126231996807607L;
	private Date dataEmissao;
	private Date dataCadastro;
	private String numeroDocumento;
	private String numeroAvenida;
	private String localEmissao;

	// private TipoDocumento tipoDoc;
	private String localNascimento;
	private String avenida;
	private String bairro;
	// dados de encarregado de educacao
	private String nomeEncaregado;
	private String bairroEnca;
	private String avenidaEnca;
	private String numeroAvenidaEnca;
	private String localTrabalhoEnca;
	private String telEncaregado;
	private String funcaoEnca;
	private String quarteraoEnca;
	private String observacao;
	private String profissao;
	private List<Matricula> matriculas;
	private Escola escola;
	private Pais pais;
	private Funcionario funcionario;

	// dados que nao sao guardado no banco
	private String armazenaLogin;
	private String armazenaCodigoEscola;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "aluno")
	public List<Matricula> getMatriculas() {
		return matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	@Temporal(TemporalType.DATE)
	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	@Column(name = "numero_doc")
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Column(name = "nome_encarregado")
	public String getNomeEncaregado() {
		return nomeEncaregado;
	}

	public void setNomeEncaregado(String nomeEncaregado) {
		this.nomeEncaregado = nomeEncaregado;
	}

	@Column(name = "bairro")
	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	@Column(name = "avenida_encaregado")
	public String getAvenida() {
		return avenida;
	}

	public void setAvenida(String avenida) {
		this.avenida = avenida;
	}

	@Column(name = "profissao")
	public String getProfissao() {
		return profissao;
	}

	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}

	public String getTelEncaregado() {
		return telEncaregado;
	}

	public void setTelEncaregado(String telEncaregado) {
		this.telEncaregado = telEncaregado;
	}

	/*
	 * @Embedded
	 * 
	 * @AttributeOverrides({
	 * 
	 * @AttributeOverride(name = "rua", column = @Column(name = "local_nasc"))
	 * })
	 * 
	 * public Enderenco getEnderenco() { return enderenco; }
	 * 
	 * public void setEnderenco(Enderenco enderenco) { this.enderenco =
	 * enderenco; }
	 */

	public String getLocalNascimento() {
		return localNascimento;
	}

	public void setLocalNascimento(String localNascimento) {
		this.localNascimento = localNascimento;
	}

	/*
	 * @ManyToOne
	 * 
	 * @NotNull(message = "O campo classe � obrigat�rio") public Classe
	 * getClasse() { return classe; }
	 * 
	 * public void setClasse(Classe classe) { this.classe = classe; }
	 */
	@Column(name = "local_emissao")
	public String getLocalEmissao() {
		return localEmissao;
	}

	public void setLocalEmissao(String localEmissao) {
		this.localEmissao = localEmissao;
	}

	@Transient
	public String getArmazenaLogin() {
		return armazenaLogin;
	}

	public void setArmazenaLogin(String armazenaLogin) {
		this.armazenaLogin = armazenaLogin;
	}

	@Transient
	public String getArmazenaCodigoEscola() {
		return armazenaCodigoEscola;
	}

	public void setArmazenaCodigoEscola(String armazenaCodigoEscola) {
		this.armazenaCodigoEscola = armazenaCodigoEscola;
	}

	// Sql que busca dados dos alunos de uma escola para o relatorio
	/*
	 * SELECT usuario.nome, usuario.sexo, usuario.datanascimento, distrito.nome
	 * AS dnome, distrito.provincia, escola.descricao, escola.id FROM escola
	 * INNER JOIN distrito ON escola.distrito_id = distrito.id , aluno INNER
	 * JOIN usuario ON aluno.id = usuario.id , escola_usuario WHERE
	 * escola_usuario.id_usuario = usuario.id AND escola_usuario.id_escola =
	 * escola.id AND escola_usuario.id_escola = $P{idEscola} ORDER BY
	 * usuario.nome ASC
	 */

	public String getNumeroAvenida() {
		return numeroAvenida;
	}

	public void setNumeroAvenida(String numeroAvenida) {
		this.numeroAvenida = numeroAvenida;
	}

	public String getLocalTrabalhoEnca() {
		return localTrabalhoEnca;
	}

	public void setLocalTrabalhoEnca(String localTrabalhoEnca) {
		this.localTrabalhoEnca = localTrabalhoEnca;
	}

	public String getAvenidaEnca() {
		return avenidaEnca;
	}

	public void setAvenidaEnca(String avenidaEnca) {
		this.avenidaEnca = avenidaEnca;
	}

	public String getBairroEnca() {
		return bairroEnca;
	}

	public void setBairroEnca(String bairroEnca) {
		this.bairroEnca = bairroEnca;
	}

	public String getFuncaoEnca() {
		return funcaoEnca;
	}

	public void setFuncaoEnca(String funcaoEnca) {
		this.funcaoEnca = funcaoEnca;
	}

	public String getQuarteraoEnca() {
		return quarteraoEnca;
	}

	public void setQuarteraoEnca(String quarteraoEnca) {
		this.quarteraoEnca = quarteraoEnca;
	}

	public String getNumeroAvenidaEnca() {
		return numeroAvenidaEnca;
	}

	public void setNumeroAvenidaEnca(String numeroAvenidaEnca) {
		this.numeroAvenidaEnca = numeroAvenidaEnca;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
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
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	@ManyToOne
	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	@ManyToOne
	public Escola getEscola() {
		return escola;
	}

	public void setEscola(Escola escola) {
		this.escola = escola;
	}
	@ManyToOne
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	
}
