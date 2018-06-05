package com.mz.sistema.gestao.escolar.servico;

import java.util.List;

import com.mz.sistema.gestao.escolar.modelo.Transferencia;

public interface TransferenciaServico {
	public void salvar(Transferencia transferencia);
	public void excluir(Transferencia transferencia);
	public List<Transferencia> receberTransferenciasPorIdEscolaPorAno(String pesquisa, Long idEscola, Integer ano);
	public List<Transferencia> obterTransferenciasPorIdEscolaPorAno(String pesquisa, Long idEscola, Integer ano);
	public List<Transferencia> receberTransferenciasPorIdEscolaPorAnoComEstadoFalse(Long idEscola, Integer ano);
	public Transferencia obterTransferenciasExistente(Long idEscolaOrigem, Long idEscolaDestino, Integer idMatricula);
}
