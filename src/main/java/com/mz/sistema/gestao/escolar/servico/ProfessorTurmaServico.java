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
import com.mz.sistema.gestao.escolar.modelo.ProfessorTurma;

public interface ProfessorTurmaServico {
	public void salvar(ProfessorTurma professorTurma);
	public void excluir(ProfessorTurma professorTurma);
	public List<ProfessorTurma> obterTurmasDoProfessorPorEscolaAno(Long idEscolaProfessor, Integer ANO);
	public List<ProfessorTurma> obterProfessorTurmaPorTurma(Long idTurma);
	public List<ProfessorTurma> obterProfessorTurmaPorTurmaOrdenarPorIdDisciplina(Long idTurma);
	public ProfessorTurma obterProfessorTurmaPorIdTurmarPorIdDisciplina(Long idTurma, Long idDisciplina);
	public List<ProfessorTurma> obterProfessorTurmaPorTurmaOrdernarPorNome(Long idTurma);
	
}
