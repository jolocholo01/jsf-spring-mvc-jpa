package com.mz.sistema.gestao.escolar.servico;

import java.util.List;

import com.mz.sistema.gestao.escolar.modelo.Distrital;

public interface DirecaoDistritalServico {
	public void salvar(Distrital distrital);
	public void excluir(Distrital distrital);
	public Distrital obterDirecaoDistritalPorId(long idDistrital);
	public List<Distrital> obterDirecaoDistritalPorIdDirecaoProvincial(long idDirecaoProvincial);
	public List<Distrital> obterDirecaoDistritalPorIdUsuario(Long idUsuario);
	public Distrital obterDirecaoDistritalExistente(Long idDistrito);
	public List<Distrital> obterDirecaoDistritalPorDescricao(String pesquisa);
	public List<Distrital> obterTodosServicosDistritais();
	

}
