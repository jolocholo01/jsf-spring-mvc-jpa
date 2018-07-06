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
import com.mz.sistema.gestao.escolar.modelo.Turma;

public interface TurmaServico {
	public void salvar(Turma turma);
	public Turma salvarRetornarTurma(Turma turma);
	public void excluir(Turma turma);
	public Turma obterTurmaPorId(Integer idTurma);
	public List<Turma> obterTurmaPorIdClasse(Long idClasse , Integer ano);	
	public List<Turma> obterTurmaPorIdTurno(Long idTurno , Integer ano);
	public List<Turma> obterTurmaPorDescricao(String descricao, Integer ano);
	public List<Turma> obterTurmaPorDescricaoTurno(String descricao, Long idTurno, Integer ano);
	public List<Turma> obterTurmaPorClasseDescricao(long idClasse, String descricao, Integer ano);
	public List<Turma> obterTurmaPorClasseTurnoDescricao(long idClasse, Long idTurno, String descricao, Integer ano);
	public List<Turma> obterTurmaPorClasseTurno(long idClasse, Long idTurno, Integer ano);
	public List<Turma> obterTurmaPorClassePorEscola(long idClasse, Integer idTurma, Integer ano, Long idEscola);
	public List<Turma> obterTurmasPorClasseCurso(long idClasse, String curso, Integer ano, Long idEscola);
	public List<Turma> obterTurmasPorClasseAreaCurso(long idClasse, String tipoArea, String curso, Integer ano, Long idEscola);
	public Turma turmaExistente(Integer ano, Long idTurno, long idSala, Long idEscola);
	public List<Turma> obterTurmasPorClasseAreaCursoDiferenteIdTurma(long idClasse, String tipoArea, String curso,
			Integer ano, Long idEscola, Integer idTurma);
	public List<Turma> obterTurmasPorClasseCursoDiferenteIdTurma(long idClasse, String curso, Integer ano, Long idEscola,
			Integer idTurma);
	
	public Turma obterTurmaExistentePorDscricao(String descricao, Long idClasse, String CURSO, Integer ANO, Long idEscola);
	public Long obterNumeroReciboUltimaTurma();
	
	
	
}
