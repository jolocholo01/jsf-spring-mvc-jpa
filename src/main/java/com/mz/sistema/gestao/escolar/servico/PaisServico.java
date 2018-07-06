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
import com.mz.sistema.gestao.escolar.enumerado.Continente;
import com.mz.sistema.gestao.escolar.modelo.Pais;

public interface PaisServico {
	public void salvar(Pais pais);
	public void excluir(Pais pais);
	public List<Pais> obterPaisPorContinente(Continente continente);
	public List<Pais> obterTodosPaises();
	public List<Pais> obterPaisPorPesquisa(String pesquisa);
	public Pais obterPaisExistente(Continente continente, String nome);
	
	
	

}
