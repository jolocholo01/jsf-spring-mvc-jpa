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
import com.mz.sistema.gestao.escolar.modelo.Trimestre;

public interface TrimestreServico {
	public void salvar(Trimestre trimestre);
	public void excluir(Trimestre trimestre);
	public List<Trimestre> obterTrimestresPorCalendarioVigente();
	public Trimestre obterTrimestreAtivo();
	public void virificarTrimestreDeHoje();
	public List<Trimestre> obterTodosTrimestresPorAno(Integer ano);
	public List<Trimestre> obterTrimestrePorIdCalendario(long idCalendario);
	public Trimestre obterTrimestrePorId(long idTrimestre);

}
