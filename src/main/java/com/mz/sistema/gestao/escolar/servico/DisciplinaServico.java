package com.mz.sistema.gestao.escolar.servico;

import java.util.List;

import com.mz.sistema.gestao.escolar.enumerado.Ciclo;
import com.mz.sistema.gestao.escolar.modelo.Disciplina;

public interface DisciplinaServico {
	public void salvar(Disciplina disciplina);
	public Disciplina salvarRetornar(Disciplina disciplina);
	public void excluir(Disciplina disciplina);
	public List<Disciplina> obterTodasDisciplinas();
	public List<Disciplina> obterDisciplinasAtivas();
	public List<Disciplina> obterDisciplinaPorCiclo(Ciclo ciclo);
	public Disciplina obterDisciplinaPorId(Integer idDisciplina);
	public List<Disciplina> obterDisciplinaPorNome(String descricao);
	public Disciplina disciplinaExisente(String descricao);
	public List<Disciplina> obterDisciplinasPorAreas(Long idArea);
	public List<Disciplina> obterDisciplinaPorPesquisa(String pesquisa);
	
}
