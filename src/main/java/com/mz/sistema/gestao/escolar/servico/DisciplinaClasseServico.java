package com.mz.sistema.gestao.escolar.servico;

import java.util.List;

import com.mz.sistema.gestao.escolar.modelo.DisciplinaClasse;

public interface DisciplinaClasseServico {
	public void salvar(DisciplinaClasse disciplinaClasse);	
	public void excluir(DisciplinaClasse disciplinaSelecionada);
	public List<DisciplinaClasse> obterDisciplinasClassePorArea( Long idArea);
	public DisciplinaClasse disciplinasClasseExistente(Long idClasse, Integer idDiscipina, Long idArea);
	public DisciplinaClasse obterDisciplinasClassePorId(long id);
	public List<DisciplinaClasse> obterTodasDisciplinasClasse();
	public List<DisciplinaClasse> obterDisciplinasPorClasse(long idClasse);
	public List<DisciplinaClasse> obterDisciplinasPorClasseEArea(long idClasse, Long idArea);
	public List<DisciplinaClasse> obterDisciplinasPorClassePorArea(long idClasse, String area);
	public List<DisciplinaClasse> obterDisciplinasClassePorIdMatriz(long idMatriz);
	public List<DisciplinaClasse> obterDisciplinasPorClassePorIdMatriz(long idMatriz);

	
}

