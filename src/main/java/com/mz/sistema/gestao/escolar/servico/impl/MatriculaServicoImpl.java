package com.mz.sistema.gestao.escolar.servico.impl;

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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.modelo.Calendario;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.Matricula;
import com.mz.sistema.gestao.escolar.servico.MatriculaServico;
import com.mz.sistema.gestao.escolar.util.Mensagem;

@Service
@Transactional
public class MatriculaServicoImpl implements MatriculaServico {

	@PersistenceContext
	private EntityManager em;
	@Autowired
	private AuthenticationContext authenticationContext;

	@Override
	public void salvar(Matricula matricula) {
		em.merge(matricula);
	}

	@Override
	public void excluir(Matricula matricula) {
		em.remove(em.merge(matricula));

	}

	@Override
	public Matricula salvarRetornarMatricula(Matricula matricula) {
		matricula = em.merge(matricula);
		return matricula;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Matricula obterAlunoMatriculado(Long idAlunoMatriculado, Integer ano) {
		List<Matricula> matriculas = em.createQuery("FROM Matricula WHERE aluno.id=:idAluno AND ano=:Ano")
				.setParameter("idAluno", idAlunoMatriculado).setParameter("Ano", ano).getResultList();
		if (!matriculas.isEmpty()) {
			return matriculas.get(0);
		}
		return null;
	}

	@Override
	public Matricula obterMatriculaExistente(Integer ano, Long idAluno) {
		@SuppressWarnings("unchecked")
		List<Matricula> matriculas = em.createQuery("FROM Matricula WHERE aluno.id=:idAluno AND ano=:Ano")
				.setParameter("idAluno", idAluno).setParameter("Ano", ano).getResultList();
		if (!matriculas.isEmpty()) {
			return matriculas.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Matricula> obterEstudanteMatriculado(Long idEscola, Integer ano, Long idClasse) {
		/*
		 * SELECT * FROM Matricula, classe ,escola_usuario WHERE ativo=true AND
		 * classe.escola_id=1 AND ano=2016 AND classe.id=1 AND
		 * Matricula.idclasse=classe.id AND
		 * escola_usuario.id_usuario=Matricula.id_aluno AND
		 * escola_usuario.id_escola=classe.escola_id
		 */

		List<Matricula> matriculas = em
				.createQuery(
						"FROM  Matricula m WHERE m.ativo=true AND m.classe.escola.id=:idEscola AND m.ano=:ANO AND m.classe.id=:idClasse AND m.classe.escola.id=m.escola.id")
				.setParameter("idEscola", idEscola).setParameter("ANO", ano).setParameter("idClasse", idClasse)
				.getResultList();

		return matriculas;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Matricula> listarTodas() {
		return em.createQuery("FROM Matricula").getResultList();
	}

	// funacao que retorna todos alunos matriculado na classe
	@SuppressWarnings("unchecked")
	@Override
	public List<Matricula> obterAlunoDaEscolaPorNome(String nome) {
		Escola escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();
		List<Matricula> matriculas = em
				.createQuery("FROM Matricula WHERE ativo=false AND escola.id=:IdEscola AND aluno.nome LIKE '%"
						+ nome.toUpperCase() + "%'")
				.setParameter("IdEscola", escola.getId()).getResultList();
		if (!matriculas.isEmpty()) {
			return matriculas;
		}
		return null;
	}

	@Override
	public Long totalEstudanteMatriculadoClasse(Long idEscola, Long idClesse) {
		return (Long) em
				.createQuery("SELECT COUNT (id) FROM Matricula WHERE escola.id=:idEscola AND classe.id=:idClesse")
				.setParameter("idEscola", idEscola).setParameter("idClesse", idClesse).getSingleResult();
	}

	/*
	 * @Override public Long totalEstudanteMatriculadoTurma(Long idEscola,
	 * Integer idTurma) { return (Long) em .createQuery(
	 * "SELECT COUNT (m.id) FROM Matricula m,ProfessorTurma t WHERE t.turma.classe.escola.id=:idEscola AND t.professor.id=:idProfessor"
	 * + " AND m.turma.id=t.turma.id AND m.turma.id=:idTurma")
	 * .setParameter("idTurma", idTurma).setParameter("idEscola", idEscola)
	 * .setParameter("idProfessor",
	 * authenticationContext.getUsuarioLogado().getId()).getSingleResult(); }
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Matricula> obtereMatriculadoPorEscola(Long idEscola) {
		return em.createQuery("FROM Matricula m  WHERE m.escola.id=:idEscola").setParameter("idEscola", idEscola)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Matricula> obterEstatistcaMatriculasPorEscolaPorAno(Long idEscolaOrigem, Integer ano) {
		return em.createQuery("FROM Matricula m  WHERE m.escolaOrigem.id=:idEscolaOrigem AND m.ano=:ANO")
				.setParameter("idEscolaOrigem", idEscolaOrigem).setParameter("ANO", ano).getResultList();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Matricula> obterMatriculasPorEscolaPorAno(Long idEscola, Integer ano) {
		return em.createQuery("FROM Matricula m  WHERE m.escola.id=:idEscola AND m.ano=:ANO")
				.setParameter("idEscola", idEscola).setParameter("ANO", ano).getResultList();

	}

	@SuppressWarnings("unchecked")
	@Override
	public Matricula obterMatriculaPorIdAlunoPorAno(Long idAluno, Integer ano) {
		List<Matricula> matriculas = em.createQuery("FROM Matricula m  WHERE m.aluno.id=:idAluno AND m.ano=:ano")
				.setParameter("idAluno", idAluno).setParameter("ano", ano).getResultList();
		if (!matriculas.isEmpty()) {
			return matriculas.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Matricula obterMatriculaPorId(Long idMatricula) {
		List<Matricula> matriculas = em.createQuery("FROM Matricula m  WHERE id=:idMatricula")
				.setParameter("idMatricula", idMatricula).getResultList();
		if (!matriculas.isEmpty()) {
			return matriculas.get(0);
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Matricula> obterMatriculasPorId(Long idMatricula) {
		List<Matricula> matriculas = em.createQuery("FROM Matricula m  WHERE m.id=:idMatricula")
				.setParameter("idMatricula", idMatricula).getResultList();
		if (!matriculas.isEmpty()) {
			return matriculas;
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Matricula> obterMatriculasDaClasseTrurnoAnoNome(Long idClasse, String curso, String nome) {
		Calendario calendario = authenticationContext.getCalendarioEscolar();
		List<Matricula> matriculas = em
				.createQuery(
						"FROM Matricula m WHERE m.ativo=false AND m.classe.id=:idClasse AND m.ano=:Ano AND m.escola.id=:idEscola AND m.curso=:Curso  AND m.aluno.nome LIKE '%"
								+ nome.toUpperCase() + "%' order by m.aluno.dataNascimento desc")
				.setParameter("idClasse", idClasse).setParameter("Ano", calendario.getAno())
				.setParameter("idEscola", authenticationContext.getFuncionarioEscolaLogada().getEscola().getId())
				.setParameter("Curso", curso).getResultList();
		if (!matriculas.isEmpty()) {
			return matriculas;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Matricula> obterMatriculasDaclasse(long idClasse, String curso) {
		Calendario calendario = authenticationContext.getCalendarioEscolar();
		if (calendario == null) {
			Mensagem.mensagemInfo("Aviso: Não listou alunos matriculado pois não existe Calendario escolar");
			return null;
		}
		List<Matricula> matriculas = em
				.createQuery(
						"FROM Matricula m WHERE  m.classe.id=:idClasse AND m.ano=:Ano AND m.escola.id=:idEscola AND m.curso=:Curso order by m.aluno.dataNascimento desc")
				.setParameter("idClasse", idClasse).setParameter("Ano", calendario.getAno())
				.setParameter("idEscola", authenticationContext.getFuncionarioEscolaLogada().getEscola().getId())
				.setParameter("Curso", curso).getResultList();
		if (!matriculas.isEmpty()) {
			return matriculas;
		}
		return null;
	}

	@Override
	public List<Matricula> obterMatriculaPorTurmaAtivas(Long idTurma) {
		@SuppressWarnings("unchecked")
		List<Matricula> matriculas = em
				.createQuery("FROM Matricula m WHERE  m.turma.id=:idTurma  AND m.ativo=true order by m.aluno.nome")
				.setParameter("idTurma", idTurma).getResultList();

		if (!matriculas.isEmpty()) {
			return matriculas;
		}
		return null;

	}

	@Override
	public List<Matricula> obterMatriculasPorNomeDoAlunoAndPorAno(String consultarNomeAluno, Integer ano) {
		@SuppressWarnings("unchecked")
		List<Matricula> matriculas = em.createQuery("FROM Matricula m WHERE  m.aluno.nome like '%" + consultarNomeAluno
				+ "%' AND m.ano=:Ano AND m.ativo=true").setParameter("Ano", ano).getResultList();

		if (!matriculas.isEmpty()) {
			return matriculas;
		}
		return null;
	}

	@Override
	public List<Matricula> obterMATRICULASdeALUNOSporESCOLA(String nomeAlunoOUCodigo) {
		Long IDEscola = authenticationContext.getFuncionarioEscolaLogada().getEscola().getId();

		Integer ano = authenticationContext.getCalendarioEscolar().getAno();
		if (ano == null) {
			Mensagem.mensagemErro("Aviso: Nao e possivel executar o pedido pois, nao esxite callendadio Escolar");
			return null;
		}
		@SuppressWarnings("unchecked")
		List<Matricula> matriculas = em
				.createQuery("FROM Matricula m  WHERE m.escola.id=:IDEscola AND (m.aluno.nome like '%"
						+ nomeAlunoOUCodigo.trim().toUpperCase() + "%' OR m.aluno.login like '%"
						+ nomeAlunoOUCodigo.trim().toUpperCase() + "%') AND m.ano=:Ano AND m.ativo=true")
				.setParameter("Ano", ano).setParameter("IDEscola", IDEscola).getResultList();

		if (!matriculas.isEmpty()) {
			return matriculas;
		}
		return null;
	}

	@Override
	public List<Matricula> obterMatriculadoPorEscolaPorAnoPorTransferecia(Long idEscola, Integer ano, String label) {
		@SuppressWarnings("unchecked")
		List<Matricula> matriculas = em
				.createQuery("FROM Matricula m  WHERE m.escola.id=:IDEscola AND m.ano=:Ano AND m.situacao=:label")
				.setParameter("IDEscola", idEscola).setParameter("Ano", ano).setParameter("label", label)
				.getResultList();

		if (!matriculas.isEmpty()) {
			return matriculas;
		}
		return null;
	}

	@Override
	public Long obterMatriculadoPorSexo(Long idEscola, Integer ano, boolean sexo) {

		Long count = (Long) em
				.createQuery(
						"SELECT COUNT(m.aluno.id) FROM Matricula m  WHERE m.escola.id=:IDEscola AND m.ano=:Ano AND m.aluno.sexo=:Sexo")
				.setParameter("IDEscola", idEscola).setParameter("Ano", ano).setParameter("Sexo", sexo)
				.getSingleResult();

		return count;

	}

	@Override
	public List<Matricula> obterAlunosPorPesquisa(String pesquisa) {
		@SuppressWarnings("unchecked")

		List<Matricula> alunos = em
				.createQuery("from Matricula m where (m.aluno.nome like '%" + pesquisa.trim().toUpperCase()
						+ "%' or m.aluno.login like '%" + pesquisa.trim() + "%') and ativo = false")
				.getResultList();
		if (!alunos.isEmpty()) {
			return alunos;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Matricula> obterMatriculaPorAluno(String pesquisa, Integer ano) {
		List<Matricula> alunos = em
				.createQuery("from Matricula m  where (m.aluno.nome like '%" + pesquisa.trim().toUpperCase()
						+ "%' or m.aluno.login like '%" + pesquisa.trim() + "%') and m.ano=:Ano")
				.setParameter("Ano", ano).getResultList();
		if (!alunos.isEmpty()) {
			return alunos;
		}
		return null;
	}

	@Override
	public List<Matricula> obterMatriculasPorClassePorAnoPorCurso(long idClasse, Long idEscola, String curso,
			Integer ano) {
		@SuppressWarnings("unchecked")
		List<Matricula> alunos = em
				.createQuery(
						"from Matricula m   where  m.classe.id=:idClasse AND m.escola.id=:idEscola AND m.curso=:CURSO AND m.ano=:ANO")
				.setParameter("idClasse", idClasse).setParameter("idEscola", idEscola).setParameter("CURSO", curso)
				.setParameter("ANO", ano).getResultList();
		if (!alunos.isEmpty()) {
			return alunos;
		}
		return null;
	}

	@Override
	public List<Matricula> obterMatriculasPorNome(Long idEscola, String nome, Integer ano) {
		@SuppressWarnings("unchecked")
		List<Matricula> alunos = em
				.createQuery("from Matricula m where (m.aluno.nome like '%" + nome.trim().toUpperCase()
						+ "%')  AND m.escola.id=:idEscola  AND m.ano=:ANO")
				.setParameter("idEscola", idEscola).setParameter("ANO", ano).getResultList();
		if (!alunos.isEmpty()) {
			return alunos;
		}
		return null;
	}

	@Override
	public List<Matricula> obterMatriculasPorClassePorCurso(long idClasse, Long idEscola, String curso, Integer ano) {
		@SuppressWarnings("unchecked")
		List<Matricula> alunos = em
				.createQuery(
						"from Matricula m  where m.classe.id=:idClasse AND m.escola.id=:idEscola AND m.curso=:CURSO AND m.ano=:ANO")
				.setParameter("idClasse", idClasse).setParameter("idEscola", idEscola).setParameter("CURSO", curso)
				.setParameter("ANO", ano).getResultList();
		if (!alunos.isEmpty()) {
			return alunos;
		}
		return null;
	}

	@Override
	public List<Matricula> obterMatriculaPorTurma(Long idTurma) {
		@SuppressWarnings("unchecked")
		List<Matricula> matriculas = em
				.createQuery("FROM Matricula m  WHERE  m.turma.id=:idTurma order by m.numeroAlunoTurma")
				.setParameter("idTurma", idTurma).getResultList();

		if (!matriculas.isEmpty()) {
			return matriculas;
		}
		return null;

	}

	@Override
	public List<Matricula> obterMatriculasPorClassePorCursoPorArea(long idClasse, Long idEscola, String curso,
			Integer ano, String tipoArea) {
		@SuppressWarnings("unchecked")
		List<Matricula> alunos = em
				.createQuery(
						"from Matricula m where m.classe.id=:idClasse AND m.escola.id=:idEscola AND m.curso=:CURSO AND m.ano=:ANO AND m.tipoArea like '%"
								+ tipoArea + "%' order by m.numeroAlunoTurma")
				.setParameter("idClasse", idClasse).setParameter("idEscola", idEscola).setParameter("CURSO", curso)
				.setParameter("ANO", ano).getResultList();
		if (!alunos.isEmpty()) {
			return alunos;
		}
		return null;
	}

	@Override
	public List<Matricula> obterMatriculasPorIdAluno(Long idAluno) {
		@SuppressWarnings("unchecked")
		List<Matricula> matriculas = em.createQuery("FROM Matricula m WHERE  m.aluno.id=:idAluno order by m.ano DESC")
				.setParameter("idAluno", idAluno).getResultList();

		if (!matriculas.isEmpty()) {
			return matriculas;
		}
		return null;
	}

	@Override
	public Matricula obterMatriculaPorIdAluno(Long idAluno) {
		@SuppressWarnings("unchecked")
		List<Matricula> matriculas = em.createQuery("FROM Matricula m  WHERE  m.aluno.id=:idAluno order by m.ano")
				.setParameter("idAluno", idAluno).getResultList();

		if (!matriculas.isEmpty()) {
			return matriculas.get(0);
		}
		return null;
	}

	@Override
	public Long obterNumeroReciboUltimaMatricula() {
		Long numeroRecibo = (Long) em.createQuery("select max(cast(numeroRecibo as long)) from Matricula")
				.getSingleResult();

		return numeroRecibo;
	}

	@Override
	public Long obterNumeroUltimaMatricula(Integer ano) {
		Long numeroRecibo = (Long) em.createQuery("select max(cast(numero as long)) from Matricula WHERE ano=:ANO")
				.setParameter("ANO", ano).getSingleResult();

		return numeroRecibo;
	}

	@Override
	public Integer obterNumeroUltimoAlunoNaTurma(Long idTurma) {
		Integer numeroRecibo = (Integer) em
				.createQuery("select max(cast(numeroAlunoTurma as integer)) from Matricula WHERE turma.id=:idTurma")
				.setParameter("idTurma", idTurma).getSingleResult();

		return numeroRecibo;
	}

	@Override
	public List<Matricula> obterMatriculasPorClassePorCursoPorTurma(long idClasse, Long idEscola, String curso,
			Integer ANO, Integer listarTODASMatriculaOUPorTURMAouPORsemTURMA) {

		StringBuilder builder = new StringBuilder(
				"from Matricula  where escola.id=:idEscola AND classe.id=:idClasse AND ano=:ANO AND curso=:CURSO  ");
		
		if (listarTODASMatriculaOUPorTURMAouPORsemTURMA == 2) {
			builder.append(" AND  temTurma=false");
		}
		if (listarTODASMatriculaOUPorTURMAouPORsemTURMA == 3) {
			builder.append(" AND  temTurma=true");
		}

		Query query = em.createQuery(builder.toString());

		query.setParameter("idEscola", idEscola);
		query.setParameter("idClasse", idClasse);
		query.setParameter("CURSO", curso);
		query.setParameter("ANO", ANO);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Matricula> obterMatriculasPorClassePorCursoPorAreaPorTurma(long idClasse, Long idEscola, String curso,
			Integer ano, String tipoArea, Integer listarTODASMatriculaOUPorTURMAouPORsemTURMA) {
		StringBuilder builder = new StringBuilder(
				"from Matricula  where escola.id=:idEscola AND classe.id=:idClasse AND ano=:ANO AND curso=:CURSO AND tipoArea like '%"
								+ tipoArea + "%' ");
		
		if (listarTODASMatriculaOUPorTURMAouPORsemTURMA == 2) {
			builder.append(" AND  temTurma=false");
		}
		if (listarTODASMatriculaOUPorTURMAouPORsemTURMA == 3) {
			builder.append(" AND  temTurma=true");
		}

		Query query = em.createQuery(builder.toString());

		query.setParameter("idEscola", idEscola);
		query.setParameter("idClasse", idClasse);
		query.setParameter("CURSO", curso);
		query.setParameter("ANO", ano);

		return query.getResultList();
	}

	@Override
	public List<Matricula> obterMatriculasPorEscolaPorAnoTemTurmaPergunta(Long idEscola, Integer ano,
			Boolean temTurma) {
		@SuppressWarnings("unchecked")
		List<Matricula> alunos = em
				.createQuery("from Matricula m  where  m.escola.id=:idEscola  AND m.ano=:ANO AND m.temTurma=:temTurma ")
				.setParameter("idEscola", idEscola).setParameter("ANO", ano).setParameter("temTurma", temTurma)
				.getResultList();
		if (!alunos.isEmpty()) {
			return alunos;
		}
		return null;

	}

	@Override
	public Long obterTotalAlunosMatriculasPorEscolaPorAno(Long idEscola, Integer ano) {
		Long count = (Long) em
				.createQuery("SELECT COUNT(m.id) FROM Matricula m  WHERE m.escola.id=:IDEscola AND m.ano=:Ano")
				.setParameter("IDEscola", idEscola).setParameter("Ano", ano).getSingleResult();

		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Matricula> obterMatriculasPorClassePorCursoPorTurmaSemArea(Long idEscola, long idClasse, String curso,
			Integer ANO, Integer listarTODASMatriculaOUPorTURMAouPORsemTURMA, Integer idadeInicial, Integer idadeFinal) {

		StringBuilder builder = new StringBuilder(
				"from Matricula  where escola.id=:idEscola AND classe.id=:idClasse AND ano=:ANO AND curso=:CURSO  ");
		if (idadeInicial != null && idadeFinal != null) {
			builder.append(" AND  year(aluno.dataNascimento) between '" + idadeInicial + "' AND '" + idadeFinal + "' ");
		}
		if (listarTODASMatriculaOUPorTURMAouPORsemTURMA == 2) {
			builder.append(" AND  temTurma=false");
		}
		if (listarTODASMatriculaOUPorTURMAouPORsemTURMA == 3) {
			builder.append(" AND  temTurma=true");
		}

		Query query = em.createQuery(builder.toString());

		query.setParameter("idEscola", idEscola);
		query.setParameter("idClasse", idClasse);
		query.setParameter("CURSO", curso);
		query.setParameter("ANO", ANO);

		return query.getResultList();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Matricula> obterMatriculasPorClassePorCursoPorAreaPorTurma(Long idEscola, long idClasse, String curso,
			Integer ANO, String tipoArea, Integer listarTODASMatriculaOUPorTURMAouPORsemTURMA, Integer idadeInicial, Integer idadeFinal) {
		StringBuilder builder = new StringBuilder(
				"from Matricula  where escola.id=:idEscola AND classe.id=:idClasse AND ano=:ANO AND curso=:CURSO AND m.tipoArea like '%"
								+ tipoArea + "%' ");
		if (idadeInicial != null && idadeFinal != null) {
			builder.append(" AND  year(aluno.dataNascimento) between '" + idadeInicial + "' AND '" + idadeFinal + "' ");
		}
		if (listarTODASMatriculaOUPorTURMAouPORsemTURMA == 2) {
			builder.append(" AND  temTurma=false");
		}
		if (listarTODASMatriculaOUPorTURMAouPORsemTURMA == 3) {
			builder.append(" AND  temTurma=true");
		}

		Query query = em.createQuery(builder.toString());

		query.setParameter("idEscola", idEscola);
		query.setParameter("idClasse", idClasse);
		query.setParameter("CURSO", curso);
		query.setParameter("ANO", ANO);

		return query.getResultList();

	}

}
