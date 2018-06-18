package com.mz.sistema.gestao.escolar.servico.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.modelo.Transferencia;
import com.mz.sistema.gestao.escolar.servico.TransferenciaServico;

@Service
@Transactional
public class TransferenciaServicoImpl implements TransferenciaServico {
	@PersistenceContext
	private EntityManager em;

	@Override
	public void salvar(Transferencia transferencia) {
		em.merge(transferencia);

	}

	@Override
	public void excluir(Transferencia transferencia) {
		em.remove(em.merge(transferencia));

	}

	@Override
	public List<Transferencia> receberTransferenciasPorIdEscolaPorAno(String pesquisa, Long idEscola, Integer ano) {
		@SuppressWarnings("unchecked")
		List<Transferencia> transferencias = em
				.createQuery("from Transferencia t  where (t.matricula.aluno.nome like '%"
						+ pesquisa.toUpperCase().trim() + "%' OR t.matricula.aluno.login like '%" + pesquisa.trim()
						+ "%') AND t.escolaDestino.id=:idEscola AND t.matricula.ano=:ANO ")
				.setParameter("idEscola", idEscola).setParameter("ANO", ano).getResultList();
		if (!transferencias.isEmpty()) {
			return transferencias;
		}
		return null;
	}

	@Override
	public List<Transferencia> obterTransferenciasPorIdEscolaPorAno(String pesquisa, Long idEscola, Integer ano) {
		@SuppressWarnings("unchecked")
		List<Transferencia> transferencias = em
				.createQuery("from Transferencia t  where (t.matricula.aluno.nome like '%"
						+ pesquisa.toUpperCase().trim() + "%' OR t.matricula.aluno.login like '%" + pesquisa.trim()
						+ "%') AND t.escolaOrigem.id=:idEscola AND t.matricula.ano=:ANO ")
				.setParameter("idEscola", idEscola).setParameter("ANO", ano).getResultList();
		if (!transferencias.isEmpty()) {
			return transferencias;
		}
		return null;
	}

	@Override
	public List<Transferencia> receberTransferenciasPorIdEscolaPorAnoComEstadoFalse(Long idEscola, Integer ano) {
		@SuppressWarnings("unchecked")
		List<Transferencia> transferencias = em
				.createQuery(
						"from Transferencia t  where t.escolaDestino.id=:idEscola AND t.matricula.ano=:ANO  AND t.finalizada=false")
				.setParameter("idEscola", idEscola).setParameter("ANO", ano).getResultList();
		if (!transferencias.isEmpty()) {
			return transferencias;
		}
		return null;
	}

	@Override
	public Transferencia obterTransferenciasExistente(Long idEscolaOrigem, Long idEscolaDestino, Integer idMatricula) {
		@SuppressWarnings("unchecked")
		List<Transferencia> transferencias = em
				.createQuery(
						"from Transferencia t  where t.escolaOrigem.id=:idEscolaOrigem AND t.escolaDestino.id=:idEscolaDestino AND t.matricula.id=:idMatricula  AND t.finalizada=false")
				.setParameter("idEscolaOrigem", idEscolaOrigem).setParameter("idEscolaDestino", idEscolaDestino)
				.setParameter("idMatricula", idMatricula).getResultList();
		if (!transferencias.isEmpty()) {
			return transferencias.get(0);
		}
		return null;
	}

	@Override
	public Long obterTransferidosPorIdEscolaPorAno(Long idEscolaOrigem, Integer ano, boolean finalizada) {
		Long  qtdTransferencias = (Long) em
				.createQuery(
						"SELECT COUNT(t.id) from Transferencia t  where t.escolaOrigem.id=:idEscolaOrigem AND t.matricula.ano=:ano  AND t.finalizada=:finalizada")
				.setParameter("idEscolaOrigem", idEscolaOrigem)
				.setParameter("ano", ano)
				.setParameter("finalizada", finalizada).getSingleResult();
		
		return qtdTransferencias;
	}

}
