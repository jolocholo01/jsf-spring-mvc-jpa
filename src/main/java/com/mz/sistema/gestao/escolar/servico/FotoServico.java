package com.mz.sistema.gestao.escolar.servico;

import com.mz.sistema.gestao.escolar.modelo.Foto;

public interface FotoServico {
	public Foto salvar(Foto foto);

	public Foto obterFotoPorIdUsuario(Long idUsuario);

}
