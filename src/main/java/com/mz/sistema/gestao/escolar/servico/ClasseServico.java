package com.mz.sistema.gestao.escolar.servico;

import java.util.List;

import com.mz.sistema.gestao.escolar.modelo.Classe;

public interface ClasseServico {
	public void salvar(Classe classe);
	public void excluir(Classe classe);
	public List<Classe> obterTodasClasses();
	public List<Classe> obterTodasClassesAtivas();
	public Classe obterClassePorId(Long idClasse);
	public Classe classeExisente(String descricao);
	public List<Classe> obterClassesPorCiclo(String ciclo);
	public Classe obterClassePorId(long idClasse);
	public List<Classe> obterClassePorDescricao(String descicao);
	public List<Classe> obterClassesPorEscola();


}

