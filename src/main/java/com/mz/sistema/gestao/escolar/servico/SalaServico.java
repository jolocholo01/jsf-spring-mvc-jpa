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
import com.mz.sistema.gestao.escolar.modelo.Sala;

public interface SalaServico {
	public void salvar(Sala sala);
	public void excluir(Sala sala);
	public List<Sala> obterSalaPorEscolaAtivas();
	public Sala obterSalaPorId(long idSala);
	public Sala salaExistenteDaEscola(String sala);
	public List<Sala> obterSalaPorEscolaPorPesquisa(String salaPesquisada, Long idEscola);
	public Sala obterSalaPorEscolaPorCodigoANDSala(String sala);
	Long obterNumeroUltimaSalaPorEscola(Long idEscola);
	
}
