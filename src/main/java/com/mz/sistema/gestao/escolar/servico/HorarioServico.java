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
	public List<Horario> obterHorarioPorIdHorarioAulaPoridTurma(Long idHorarioAula, Long idTurma);
	public List<Horario> obterHorarioPorIdTurno(Long idTurno, Long idEscola, Integer ano);
	public Horario obterHorarioPorIdHorarioAulaIdProfessorIdTurmaIdDiaSemaIdDiciplina(long idHorarioAula, Long idFuncionario, Long idDiaSemana, Long idTurma, Long idDisciplina);
	public Horario obterHorarioPorIdHorarioAulaIdTurmaIdDiaSemaIdDiciplina(long idHorarioAula, Long idDiaSemana, Long idTurma, Long idDisciplina);
	public List<Horario> obterHorarioPorIdTurmaPorIdDiciplina(Long idTurma, Long idDisciplina);
	public List<Horario> obterHorarioIdTurma(Long idTurma);
	public boolean verificarHorarioMinhaTurma(Long idTurma);
	public List<Horario> obterHorarioPorIdTurma(Long idTurma);

}
