package com.mz.sistema.gestao.escolar.servico;

import java.util.List;

import com.mz.sistema.gestao.escolar.modelo.Trimestre;

public interface TrimestreServico {
	public void salvar(Trimestre trimestre);
	public void excluir(Trimestre trimestre);
	public List<Trimestre> obterTrimestrePorIdCalendario(Long idCalendario);
	public Trimestre obterTrimestreAtivo();
	public void virificarTrimestreDeHoje();
	public List<Trimestre> obterTodosTrimestresPorAno(Integer ano);

}
