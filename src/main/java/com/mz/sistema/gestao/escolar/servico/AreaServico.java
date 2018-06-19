package com.mz.sistema.gestao.escolar.servico;

import java.util.List;

import com.mz.sistema.gestao.escolar.modelo.Area;

public interface AreaServico {
	public void salvar(Area area);
	public void excluir(Area area);

	public Area obterAreaExistente(String nomeArea, String ciclo);
	public List<Area> obterAreasPorPesquisa(String pesquisa);
	public List<Area> obterAreasPorCiclo(String ciclo);
	public List<Area> obterTodasAreasPorCiclo(String ciclo);
	public List<Area> obterTodasAreas();
	public List<Area> obterAreasPorCicloOrdenarPorOrdemDecrescente(String ciclo);
	public List<Area> obterAreasPorCicloOrdenarPorOrdemDecrescenteDiferenteDaArea(String ciclo, String tipoArea);

	
	
	

}
