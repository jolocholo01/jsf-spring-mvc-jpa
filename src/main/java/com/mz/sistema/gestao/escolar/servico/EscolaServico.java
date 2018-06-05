package com.mz.sistema.gestao.escolar.servico;

import java.util.List;

import com.mz.sistema.gestao.escolar.modelo.Classe;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.FuncionarioEscola;
import com.mz.sistema.gestao.escolar.modelo.Turno;

public interface EscolaServico {
	public void salvar(Escola escola);
	public void excluir(Escola escola);
	public List<Escola> listarTodas();
	public Escola obterEscolaPorId(Long id);
	public List<Escola> obterEscolasPorId(Long idEscola);
	public Long totalEstudantesDaEscola(Long idEscolo, Integer ano);
	public Long totalProfessoresDaEscola(Long idEscola);
	public Long totalTurmaDaEscola(Long idEscola);
	public Long totalEstudantesAlocadoEmTurmasDaEscola(Long idEscola);
	public Escola obterEscolaExistente(Long idDirecaoDitrital, String nomeEscola, String localidade);
	public List<Escola> obterTodasEsolasPorDirecaoDistrital();
	public List<Escola> obterEscolasPorPesquisa(String pesquisa);
	public List<Classe> obterClassesPorIdEscola(Long idEscola);
	public List<Turno> obterTurnosPorIdEscola(Long idEscola);
//	public List<Turno> obterTurnosPorIdEscolaPorCurso(Long idEscola, String curso);
	public List<FuncionarioEscola> obterEscolasPorIFuncionario(Long idFuncionario);
	public List<Classe> obterClassesPorIdEscolaPorIdClasse(Long idEscola, long idClasse);
	List<Escola> obterEscolasSemRepiticaoPorIFuncionario(Long idFuncionario);
	


	
}
