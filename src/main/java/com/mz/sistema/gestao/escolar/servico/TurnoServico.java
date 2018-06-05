package com.mz.sistema.gestao.escolar.servico;

import java.util.List;

import com.mz.sistema.gestao.escolar.enumerado.Provincia;
import com.mz.sistema.gestao.escolar.enumerado.TipoTurno;
import com.mz.sistema.gestao.escolar.modelo.Turno;

public interface TurnoServico {
	public void salvar(Turno turno);
	public void excluir(Turno turno);
	public List<Turno> listarTodosTurnos();
	public List<Turno> obterTurnoPorCurso(String curso);
	public Turno obterTurnoPorId(Long idTurno);
	public List<Turno> listarTodosTurnosDaEscola();
	public Turno obterTurnoCursoDiurno(String curso, TipoTurno descricao);
	public Turno obterTurnoExistente(TipoTurno descricao);
	public List<Turno> obterTurnoPorPesquisa(String pesquisa);

}
