// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.chave.composta.NotaId;
import com.mz.sistema.gestao.escolar.enumerado.Continente;
import com.mz.sistema.gestao.escolar.enumerado.EstadoCivil;
import com.mz.sistema.gestao.escolar.enumerado.Provincia;
import com.mz.sistema.gestao.escolar.enumerado.TipoCurso;
import com.mz.sistema.gestao.escolar.enumerado.TipoDocumento;
import com.mz.sistema.gestao.escolar.modelo.Aluno;
import com.mz.sistema.gestao.escolar.modelo.Calendario;
import com.mz.sistema.gestao.escolar.modelo.Classe;
import com.mz.sistema.gestao.escolar.modelo.DisciplinaClasse;
import com.mz.sistema.gestao.escolar.modelo.Distrito;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.modelo.Matricula;
import com.mz.sistema.gestao.escolar.modelo.Matriz;
import com.mz.sistema.gestao.escolar.modelo.Nota;
import com.mz.sistema.gestao.escolar.modelo.Pais;
import com.mz.sistema.gestao.escolar.servico.ClasseServico;
import com.mz.sistema.gestao.escolar.servico.DistritoServico;
import com.mz.sistema.gestao.escolar.servico.MatriculaServico;
import com.mz.sistema.gestao.escolar.servico.MatrizServico;
import com.mz.sistema.gestao.escolar.servico.NotaServico;
import com.mz.sistema.gestao.escolar.servico.PaisServico;
import com.mz.sistema.gestao.escolar.servico.UsuarioServico;
import com.mz.sistema.gestao.escolar.util.DataUtils;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Named
@Controller
@SessionScope
public class AlunoMatriculaBean {
	private Aluno aluno = new Aluno();
	private Matricula matricula = new Matricula();
	private List<Provincia> provincias = Arrays.asList(Provincia.values());
	private List<EstadoCivil> estadoCivils = Arrays.asList(EstadoCivil.values());
	private List<TipoDocumento> tipoDocumentos = Arrays.asList(TipoDocumento.values());
	private List<Continente> continentes = Arrays.asList(Continente.values());
	private List<TipoCurso> cursos = Arrays.asList(TipoCurso.values());
	private List<Pais> paises = new ArrayList<>();
	private Aluno buscarDadosEstudante;
	private Aluno estudanteSeleconado;
	private Distrito distrito = new Distrito();
	private Pais pais = new Pais();
	private Distrito distritoSelecionado;
	private TipoCurso curso;
	private Classe classe = new Classe();
	private Long idDistrito;

	private static final String FORMATA_DATA_NESCIMENTO_PARA_SENHA_PADRAO = "ddMMyyyy";

	@Autowired
	private DistritoServico distritoServico;
	@Autowired
	private AuthenticationContext authenticationContext;
	@Autowired
	private ClasseServico classeServico;

	@Autowired
	private MatriculaServico matriculaServico;
	@Autowired
	private NotaServico notaServico;
	@Autowired
	private MatrizServico matrizServico;
	@Autowired
	private PaisServico paisServico;
	@Autowired
	private UsuarioServico usuarioServico;

	public void salvar() {
		// Aluno alunoSalvao = alunoServico.salvar(buscarDadosEstudante);
		buscarDadosEstudante.setNome(buscarDadosEstudante.getNome().toUpperCase());
		buscarDadosEstudante.setNomeEncaregado(buscarDadosEstudante.getNomeEncaregado().toUpperCase());
		matricula.setAluno(buscarDadosEstudante);
		matricula.setEscola(authenticationContext.getFuncionarioEscolaLogada().getEscola());
		// buscarDadosEstudante.getEscolas().add(matricula.getEscola());
		matricula.setAno(2017);
		matriculaServico.salvar(matricula);
		// if(buscarDadosEstudante.getId()==0){
		Mensagem.mensagemInfo("Aluno Cadastrado com sucesso");
		// }
		this.aluno = new Aluno();
		this.buscarDadosEstudante = null;
	}

	// salsar aluno com disciplina na turma
	public void matricularAlunoNaClasse() {
		Calendario calendario = authenticationContext.getCalendarioEscolar();
		if (calendario == null) {
			Mensagem.mensagemInfo("Não está difinido o calendário Escolar");
			return;
		}
		
		buscarDadosEstudante.setNome(buscarDadosEstudante.getNome().toUpperCase());
		buscarDadosEstudante.setNomeEncaregado(buscarDadosEstudante.getNomeEncaregado().toUpperCase());

		if (buscarDadosEstudante.getDataNascimento() == null) {
		} else if (buscarDadosEstudante.getDataNascimento() != null) {
			String senha = DataUtils.obterDataFormatoBanco(buscarDadosEstudante.getDataNascimento(),
					FORMATA_DATA_NESCIMENTO_PARA_SENHA_PADRAO);
			System.out.println("Senha.............:  " + senha);
			String senhaCriptografada = usuarioServico.criptografarSenha(senha);
			System.out.println("Senha Criptgrafada.: '" + senhaCriptografada + "'");
			buscarDadosEstudante.setSenha(senhaCriptografada);
		}
		matricula.setAluno(buscarDadosEstudante);
		FuncionarioEscola funcionarioEscola = authenticationContext.getFuncionarioEscolaLogada();
		if (funcionarioEscola != null) {
			Escola escola = funcionarioEscola.getEscola();
			if (escola != null) {
				matricula.setEscola(escola);
			}
		}

		matricula.setAno(calendario.getAno());

		NotaId id = null;
		Matriz matrizDaClassse = matrizServico.obterMatrizPorClasseCursoAno(matricula.getClasse().getId(),
				matricula.getCurso(), matricula.getAno());
		if (matrizDaClassse == null) {
			Mensagem.mensagemInfo("Não pode cadastrar Aluno, pois não existem matriz curricular da classe!");
			return;
		}
		Matricula matriculaFK = matriculaServico.salvarRetornarMatricula(matricula);

		Nota nota = new Nota();
		for (DisciplinaClasse disciplina : matrizDaClassse.getDisciplinaClasses()) {
			if (nota.getId() == null) {
				id = new NotaId();
			}
			id.setId_matricula(matriculaFK.getId());
			id.setId_disciplina(disciplina.getDisciplina().getId());
			nota.setId(id);
			notaServico.salvar(nota);
			nota = new Nota();

		}
		Mensagem.mensagemAlerta("Aviso: Aluno, " + matricula.getAluno().getNome() + " cadastrado com sucesso!");
		this.aluno = new Aluno();
		matricula.setObservacao("");
		matricula.setRepetente(false);
		this.buscarDadosEstudante = null;

	}

	public void setarEstudante(Aluno aluno) {
		this.buscarDadosEstudante = aluno;
		this.aluno = null;
	}

	public void obterDadosEstudanteParaMatricula(Aluno aluno) {
		this.estudanteSeleconado = aluno;
		this.buscarDadosEstudante = null;
		this.aluno = null;
	}

	public void voltar(Aluno aluno) {
		this.aluno = aluno;
		this.buscarDadosEstudante = null;
	}

	public void voltarDaSelecaoTurma(Aluno aluno) {
		this.buscarDadosEstudante = aluno;
		this.estudanteSeleconado = null;
	}

	public List<Distrito> getDistritosDaProvincia() {
		List<Distrito> distritos = null;
		if (distrito.getProvincia() != null) {
			distritos = distritoServico.obterDistritosDaProvincia(distrito.getProvincia());
		}
		return distritos;
	}

	public List<Pais> getObterPaisesDoContinente() {
		this.paises = null;
		if (pais.getContinente() != null) {
			this.paises = paisServico.obterPaisPorContinente(pais.getContinente());
		}
		return this.paises;
	}

	public List<Classe> getObterClasseDaEScola() {
		return classeServico.obterTodasClasses();
	}

	public String cadastroAlunoNaEscola() {
		this.aluno = new Aluno();
		matricula = new Matricula();
		this.buscarDadosEstudante = null;
		return "/academico/adjunto/aluno/cadastro?faces-redirect=true";
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Matricula getMatricula() {
		return matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

	public List<Provincia> getProvincias() {
		return provincias;
	}

	public void setProvincias(List<Provincia> provincias) {
		this.provincias = provincias;
	}

	public List<EstadoCivil> getEstadoCivils() {
		return estadoCivils;
	}

	public void setEstadoCivils(List<EstadoCivil> estadoCivils) {
		this.estadoCivils = estadoCivils;
	}

	public List<TipoDocumento> getTipoDocumentos() {
		return tipoDocumentos;
	}

	public void setTipoDocumentos(List<TipoDocumento> tipoDocumentos) {
		this.tipoDocumentos = tipoDocumentos;
	}

	public Aluno getBuscarDadosEstudante() {
		return buscarDadosEstudante;
	}

	public void setBuscarDadosEstudante(Aluno buscarDadosEstudante) {
		this.buscarDadosEstudante = buscarDadosEstudante;
	}

	public Distrito getDistrito() {
		return distrito;
	}

	public void setDistrito(Distrito distrito) {
		this.distrito = distrito;
	}

	public Distrito getDistritoSelecionado() {
		return distritoSelecionado;
	}

	public void setDistritoSelecionado(Distrito distritoSelecionado) {
		this.distritoSelecionado = distritoSelecionado;
	}

	public Long getIdDistrito() {
		return idDistrito;
	}

	public void setIdDistrito(Long idDistrito) {
		this.idDistrito = idDistrito;
	}

	public List<TipoCurso> getCursos() {
		return cursos;
	}

	public void setCursos(List<TipoCurso> cursos) {
		this.cursos = cursos;
	}

	public TipoCurso getCurso() {
		return curso;
	}

	public void setCurso(TipoCurso curso) {
		this.curso = curso;
	}

	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	public Aluno getEstudanteSeleconado() {
		return estudanteSeleconado;
	}

	public void setEstudanteSeleconado(Aluno estudanteSeleconado) {
		this.estudanteSeleconado = estudanteSeleconado;
	}

	public List<Continente> getContinentes() {
		return continentes;
	}

	public void setContinentes(List<Continente> continentes) {
		this.continentes = continentes;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

}
