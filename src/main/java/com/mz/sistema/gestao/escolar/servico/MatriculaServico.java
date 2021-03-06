package com.mz.sistema.gestao.escolar.servico;

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
import com.mz.sistema.gestao.escolar.modelo.Matricula;

public interface MatriculaServico {
	public void salvar(Matricula matricula);
	public Matricula salvarRetornarMatricula(Matricula matricula);
	public void excluir(Matricula matricula);
	public Matricula obterAlunoMatriculado(Long idAlunoMatriculado, Integer ano);
	public Matricula obterMatriculaExistente(Integer ano, Long idAluno);
	public List<Matricula> obterEstudanteMatriculado(Long idEscola, Integer ano, Long idClasse);
	public List<Matricula> listarTodas();
	public Long totalEstudanteMatriculadoClasse(Long idEscola, Long idClasse);
	public List<Matricula> obtereMatriculadoPorEscola(Long idEscola);
	public List<Matricula> obterEstatistcaMatriculasPorEscolaPorAno(Long idEscolaOrigem, Integer ano);
	public List<Matricula> obterMatriculasPorEscolaPorAnoTemTurmaPergunta(Long idEscola, Integer ano, Boolean temTurma);
	public Matricula obterMatriculaPorId(Long idMatricula);
	public List<Matricula> obterMatriculasDaClasseTrurnoAnoNome(Long idClasse, String curso, String nome);
	public List<Matricula> obterMatriculasPorId(Long idMatricula);
	public List<Matricula> obterAlunoDaEscolaPorNome(String nome);
	public List<Matricula> obterMatriculasDaclasse(long idClasse,  String curso);
	public List<Matricula> obterMatriculaPorTurmaAtivas(Long idTurma);
	public Matricula obterMatriculaPorIdAlunoPorAno(Long idAluno, Integer ano);
	public List<Matricula> obterMatriculasPorNomeDoAlunoAndPorAno(String consultarNomeAluno, Integer ano);
	public List<Matricula> obterMATRICULASdeALUNOSporESCOLA(String nomeAlunoOUCodigo);
	public List<Matricula> obterMatriculadoPorEscolaPorAnoPorTransferecia(Long idEscola, Integer ano, String label);
	public Long obterMatriculadoPorSexo(Long idEscola, Integer ano, boolean sexo);
	public List<Matricula> obterAlunosPorPesquisa(String pesquisa);
	public List<Matricula> obterMatriculaPorAluno(String pesquisa, Integer ano);
	public List<Matricula> obterMatriculasPorClassePorCurso(long idClasse, Long idEscola, String curso, Integer ano);
	public List<Matricula> obterMatriculasPorNome(Long idEscola, String nome, Integer ano);
	public List<Matricula> obterMatriculasPorClassePorAnoPorCurso(long idClasse, Long idEscola,  String curso,
			Integer ano);
	public List<Matricula> obterMatriculaPorTurma(Long idTurma);
	public List<Matricula> obterMatriculasPorClassePorCursoPorArea(long idClasse, Long idEscola, String curso, Integer ano,
			String tipoArea);
	public List<Matricula> obterMatriculasPorIdAluno(Long idAluno);
	public Long obterNumeroUltimaMatricula(Integer ano);
	public Long obterNumeroReciboUltimaMatricula();
	public Matricula obterMatriculaPorIdAluno(Long idAluno);
	public List<Matricula> obterMatriculasPorClassePorCursoPorTurma(long idClasse, Long idEscola, String curso, Integer ano,
			Integer listarTODASMatriculaOUPorTURMAouPORsemTURMA);
	public List<Matricula> obterMatriculasPorClassePorCursoPorAreaPorTurma(long idClasse, Long idEscola, String curso, Integer ano,
			String tipoArea, Integer listarTODASMatriculaOUPorTURMAouPORsemTURMA);
	public Integer obterNumeroUltimoAlunoNaTurma(Long idTurma);
	public List<Matricula> obterMatriculasPorEscolaPorAno(Long idEscola, Integer ano);
	public Long obterTotalAlunosMatriculasPorEscolaPorAno(Long idEscola, Integer ano);
	public List<Matricula> obterMatriculasPorClassePorCursoPorTurmaSemArea(Long idEscola, long idClasse, String curso, Integer ano,
			Integer listarMatricula, Integer idadeInicial, Integer idadeFinal);
	public List<Matricula> obterMatriculasPorClassePorCursoPorAreaPorTurma(Long idEscola, long idClasse, String curso, Integer ano,
			String tipoArea, Integer listarTODASMatriculaOUPorTURMAouPORsemTURMA, Integer idadeInicial, Integer idadeFinal);
	}
