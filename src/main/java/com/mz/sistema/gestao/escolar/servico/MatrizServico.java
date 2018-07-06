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
import com.mz.sistema.gestao.escolar.enumerado.Ciclo;
import com.mz.sistema.gestao.escolar.enumerado.TipoCurso;
import com.mz.sistema.gestao.escolar.modelo.Matriz;

public interface MatrizServico {
	public void salvar(Matriz matriz);
	public void exclui(Matriz matriz);
	public Matriz disciplinaClasseExistenteSegundoCiclo(long idClasse, String curso, String tipoArea, Long idEscola);
	public Matriz obterMatrizPorIdELeftJoinAtiva(long idClasse, String curso, Long idEscola);
	public Matriz obterMatrizPorIdELeftJoin(Long idMatriz);
	public Matriz obterMatrizPorId(Long idMatriz);
	public Matriz disciplinaClasseExistente(long idClasse, String curso, Long idEscola);
	public Matriz obterDisciplinasAreaClasse(long idClasse, TipoCurso curso, String disciplinasArea, Integer ano);
	public Matriz obterMatrizPorClasseCursoAno(long idClasse, String curso, Integer ano);
	public List<Matriz> listartodasEscola();
	public List<Matriz> obterMatrizPorCiclo(Ciclo ciclo);
	public List<Matriz> obterMatrizPorCursoEscola(TipoCurso curso);
	public List<Matriz> obterMatrizPorEscolaPorCiclo(String ciclo, Long idEscola);	
	public List<Matriz> obterMatrizPorClasseCursoAtivo(long idClasse, String curso, Long idEscola);
	Matriz obterMatrizPorSegundoCiclo(long idClasse, String curso, String tipoArea, Long idEscola);
	Matriz obterMatrizPorPrimeiroCiclo(long idClasse, String curso, Long idEscola);
	public List<Matriz> obterMatriz2Ciclo(long idClasse, String curso, String tipoArea, Long idEscola);
	public Matriz obterMatriz2CicloPorIdELeftJoinAtiva(long idClasse, String curso, String tipoArea, Long idEscola);
	public List<Matriz> obterMatrizesPorIdClasse(Long idEscola, long idClasse);
	
}
