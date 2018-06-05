package com.mz.sistema.gestao.escolar.servico;

import java.util.List;

import com.mz.sistema.gestao.escolar.modelo.Nota;

public interface NotaServico {
	public void salvar(Nota nota);
	public List<Nota> listarNotas();
	public List<Nota> listarNotasAtivas();
	public List<Nota> obterNotaPorTurma(Integer idTurma);
	public List<Nota> obterNotaPorIdAluno(Long idAluno, long idClasse, Integer ano);
	public List<Nota> obterNotasPorIdTurmaEDisciplinaDoProfessor(Integer idDisciplina, Integer idTurma);
	public Nota obterEscolaDoAluno(Long idEscola);
	public Nota obterAlunoPorMatriculaPorDisciplinaPorTrimestre(Integer idMatricula, Integer idDisciplina,
			long idTrimestre);
	public Nota obterMediaAc(Integer idMatricula, Integer idDisciplina, long idTrimestre);
	public Double obterMediaTrimestralOuAnulDoAluno(Long idAluno,long idClasse, String tipoMediatrimestre );
	public List<Nota> obterDisciplinaDoAlunoPorAnoAndId(Long idAluno, Integer ano);
	public List<Nota> obterNotasPorIdTurmaPorDisciplinaDoProfessorPorTrimetres(Integer idDisciplina, Integer idTurma,
			Long idTrimetre);
	public List<Nota> obterNotasPorIdMatricula(Integer idMatricula);

	

}
