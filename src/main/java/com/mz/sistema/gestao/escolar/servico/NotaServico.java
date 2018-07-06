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
import com.mz.sistema.gestao.escolar.modelo.Nota;

public interface NotaServico {
	public void salvar(Nota nota);
	public List<Nota> listarNotas();
	public List<Nota> listarNotasAtivas();
	public List<Nota> obterNotaPorTurma(Integer idTurma);
	public List<Nota> obterNotaPorIdAluno(Long idAluno, long idClasse, Integer ano);
	public List<Nota> obterNotasPorIdTurmaEDisciplinaDoProfessor(Long idDisciplinaClasse, Integer idTurma);
	public Nota obterEscolaDoAluno(Long idEscola);
	public Nota obterAlunoPorMatriculaPorDisciplinaPorTrimestre(Integer idMatricula, Long idDisciplinaClasse,
			long idTrimestre);
	public Nota obterMediaAc(Integer idMatricula, Long idDisciplinaClasse, long idTrimestre);
	public Double obterMediaTrimestralOuAnulDoAluno(Long idAluno,long idClasse, String tipoMediatrimestre );
	public List<Nota> obterDisciplinaDoAlunoPorAnoAndId(Long idAluno, Integer ano);
	public List<Nota> obterNotasPorIdTurmaPorDisciplinaDoProfessorPorTrimetres(Long idDisciplinaClasse, Integer idTurma,
			Long idTrimetre);
	public List<Nota> obterNotasPorIdMatricula(Integer idMatricula);
	public Nota obterNotasPorIdMatriculaPorDisciplina(Integer id, Long idDisciplinaClasse);

	

}
