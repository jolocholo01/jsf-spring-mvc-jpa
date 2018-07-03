package com.mz.sistema.gestao.escolar.servico.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.modelo.Trimestre;
import com.mz.sistema.gestao.escolar.servico.TrimestreServico;
import com.mz.sistema.gestao.escolar.util.DataUtils;

@Service
@Transactional
public class TrimestreServicoImpl implements TrimestreServico {

	@PersistenceContext
	private EntityManager em;
	private static final String FORMATO_BANCO = "yyyy-MM-dd";
	private static final String TIME_ZONE = "Africa/Harare";

	@Override
	public void salvar(Trimestre trimestre) {
		em.merge(trimestre);
	}

	@Override
	public void excluir(Trimestre trimestre) {
		em.remove(em.merge(trimestre));
		;
	}

	// Example patterns:

	// "0 0 * * * *" = the top of every hour of every day.
	// "*/10 * * * * *" = every ten seconds.
	// "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
	// "0 0 6,19 * * *" = 6:00 AM and 7:00 PM every day.
	// "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30, 10:00 and 10:30 every day.
	// "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
	// "0 0 0 25 12 ?" = every Christmas Day at midnight

	@Override // milisegundo, segundo, horas, dia mes, dia da semana
	@Scheduled(cron = "1 10 00 * * *", zone = TIME_ZONE)
	// @Scheduled(cron = "10 5 9 * * *")
	public void virificarTrimestreDeHoje() {
		@SuppressWarnings("unchecked")
		List<Trimestre> trimestres = em.createQuery("FROM Trimestre WHERE date(dataInicio) = '"
				+ DataUtils.obterDataFormatoBanco(new Date(), FORMATO_BANCO) + "'").getResultList();
		if (!trimestres.isEmpty()) {
			Trimestre trimestre = trimestres.get(0);
			Trimestre trimestreAtivo = obterTrimestreAtivo();
			trimestre.setAtivo(true);
			trimestreAtivo.setAtivo(false);
			salvar(trimestreAtivo);
			salvar(trimestre);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Trimestre> obterTrimestresPorCalendarioVigente() {
		List<Trimestre> trimestres = em.createQuery("FROM Trimestre WHERE calendario.ativo =true ORDER BY descricao")
				.getResultList();
		if (!trimestres.isEmpty()) {
			return trimestres;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Trimestre obterTrimestreAtivo() {
		List<Trimestre> trimestres = em.createQuery("FROM Trimestre WHERE ativo=true").getResultList();
		if (!trimestres.isEmpty()) {
			return trimestres.get(0);
		}
		return null;
	}

	@Override
	public List<Trimestre> obterTodosTrimestresPorAno(Integer ano) {
		@SuppressWarnings("unchecked")
		List<Trimestre> trimestres = em.createQuery("FROM Trimestre WHERE calendario.ano =:Ano ORDER BY descricao")
				.setParameter("Ano", ano).getResultList();
		if (!trimestres.isEmpty()) {
			return trimestres;
		}
		return null;
	}

	@Override
	public List<Trimestre> obterTrimestrePorIdCalendario(long idCalendario) {
		@SuppressWarnings("unchecked")
		List<Trimestre> trimestres = em
				.createQuery("FROM Trimestre WHERE calendario.id =:idCalendario ORDER BY descricao")
				.setParameter("idCalendario", idCalendario).getResultList();
		if (!trimestres.isEmpty()) {
			return trimestres;
		}
		return null;
	}

	@Override
	public Trimestre obterTrimestrePorId(long idTrimestre) {
		Trimestre trimestre = (Trimestre) em.createQuery("FROM Trimestre WHERE id =:idTrimestre")
				.setParameter("idTrimestre", idTrimestre).getSingleResult();
		if (trimestre == null) {
			return new Trimestre();
		}
		return trimestre;
	}

}
