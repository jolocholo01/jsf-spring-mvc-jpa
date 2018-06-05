package com.mz.sistema.gestao.escolar.servico;

import java.util.List;

import com.mz.sistema.gestao.escolar.modelo.ProfessorTurma;

public interface ProfessorTurmaServico {
	public void salvar(ProfessorTurma professorTurma);
	public void excluir(ProfessorTurma professorTurma);
	public List<ProfessorTurma> obterTurmasDoProfessorPorEscolaAno(Long idEscolaProfessor, Integer ANO);
	public List<ProfessorTurma> obterProfessorTurmaPorTurma(Integer idTurma);
	public List<ProfessorTurma> obterProfessorTurmaPorTurmaOrdenarPorIdDisciplina(Integer idTurma);
	public ProfessorTurma obterProfessorTurmaPorIdTurmarPorIdDisciplina(Integer idTurma, Integer idDisciplina);
	
}
