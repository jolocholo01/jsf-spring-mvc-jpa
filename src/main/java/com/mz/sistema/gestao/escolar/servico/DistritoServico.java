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
import com.mz.sistema.gestao.escolar.modelo.Distrito;

public interface DistritoServico {
	public void salvar(Distrito distrito);
	public void excluir(Distrito distrito);
	public List<Distrito> listarTodas();
	public List<Distrito> obterDistritosDaProvincia(String provincia);
	public Distrito obterDistritosPorId(Long idDistrito);
	public Distrito obterDistritoExistente(String provincia, String nome);

	

}
