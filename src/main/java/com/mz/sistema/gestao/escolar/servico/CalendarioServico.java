package com.mz.sistema.gestao.escolar.servico;

import java.util.List;

import com.mz.sistema.gestao.escolar.modelo.Calendario;

public interface CalendarioServico {
	public void salvar(Calendario calendario);
	public Calendario salvarERetornarCalendario(Calendario calendario);
	public void excluir(Calendario calendario);
	public List<Calendario> listarTodos();
	public Calendario obterCalendarioPorIdAno(Long idCalendario, Integer ano);
	//public Calendario obterCalendario();
	public Calendario obterCalendarioPorId(long idCalendario);
	public Calendario obterCalendarioPorAno(Integer ano);
	public Calendario obterCalendarioVigente();
	public Calendario obterCalendarioVigente(boolean ativo);
	public List<Calendario> obterCalendarioPorAnoAtivo(Integer ano);
	public List<Calendario> obterCalendarioPorPesquisa(String pesquisa);
	

}
