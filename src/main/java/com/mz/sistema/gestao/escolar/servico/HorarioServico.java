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
import com.mz.sistema.gestao.escolar.modelo.Horario;

public interface HorarioServico {
	
	public void salvar(Horario horario);
	public void excluir(Horario horario);
	public List<Horario> obterHorarioPorIdHorarioAulaPoridProfessor(Long idHorarioAula, Long idProfessor, Integer ano);
	public List<Horario> obterHorarioPorIdHorarioAulaPoridTurma(Long idHorarioAula, Integer idTurma);
	public List<Horario> obterHorarioPorIdTurno(Long idTurno, Long idEscola, Integer ano);
	public Horario obterHorarioPorIdHorarioAulaIdProfessorIdTurmaIdDiaSemaIdDiciplina(long idHorarioAula, Long idFuncionario, Long idDiaSemana, Integer idTurma, Integer idDisciplina);
	public Horario obterHorarioPorIdHorarioAulaIdTurmaIdDiaSemaIdDiciplina(long idHorarioAula, Long idDiaSemana, Integer idTurma, Integer idDisciplina);
	public List<Horario> obterHorarioPorIdTurmaPorIdDiciplina(Integer idTurma, Integer idDisciplina);
	public List<Horario> obterHorarioIdTurma(Integer idTurma);
	public boolean verificarHorarioMinhaTurma(Integer id);
	public List<Horario> obterHorarioPorIdTurma(Integer idTurma);

}
