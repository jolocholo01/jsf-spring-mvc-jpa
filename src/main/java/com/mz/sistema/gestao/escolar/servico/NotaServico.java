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
	public void excluir(Nota nota);
	public List<Nota> listarNotas();
	public List<Nota> listarNotasAtivas();
	public List<Nota> obterNotaPorTurma(Long idTurma);
	public List<Nota> obterNotaPorIdAluno(Long idAluno, long idClasse, Integer ano);
	public List<Nota> obterNotasPorIdTurmaEDisciplinaDoProfessor(Long idDisciplina, Long idTurma);
	public Nota obterEscolaDoAluno(Long idEscola);
	public Nota obterAlunoPorMatriculaPorDisciplinaPorTrimestre(Long idMatricula, Long idDisciplina,
			long idTrimestre);
	public Nota obterMediaAc(Long idMatricula, Long idDisciplina, long idTrimestre);
	public Double obterMediaTrimestralOuAnulDoAluno(Long idAluno,long idClasse, String tipoMediatrimestre );
	public List<Nota> obterDisciplinaDoAlunoPorAnoAndId(Long idAluno, Integer ano);
	public List<Nota> obterNotasPorIdTurmaPorDisciplinaDoProfessorPorTrimetres(Long idDisciplina, Long idTurma,
			Long idTrimetre);
	public List<Nota> obterNotasPorIdMatricula(Long idMatricula);
	public Nota obterNotasPorIdMatriculaPorDisciplina(Long idMatricula, Long idDisciplina);
	

	

}
