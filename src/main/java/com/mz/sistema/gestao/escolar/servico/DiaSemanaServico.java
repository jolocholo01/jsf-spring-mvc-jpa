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
import com.mz.sistema.gestao.escolar.modelo.DiaSemana;

public interface DiaSemanaServico {
	public void salvar(DiaSemana diaSemana);
	public void excluir(DiaSemana diaSemana);
	public List<DiaSemana> obterDiaSemanaPorPesquisa(String pesquisa);
	public DiaSemana obterDiaSemanaPorSigla(String sigla);
	public DiaSemana obterDiaSemanaExistente(String sigla);

}
