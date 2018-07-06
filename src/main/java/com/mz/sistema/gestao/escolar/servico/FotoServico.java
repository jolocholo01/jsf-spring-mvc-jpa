package com.mz.sistema.gestao.escolar.servico;

/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */
import com.mz.sistema.gestao.escolar.modelo.Foto;

public interface FotoServico {
	public Foto salvar(Foto foto);

	public Foto obterFotoPorIdUsuario(Long idUsuario);

}
