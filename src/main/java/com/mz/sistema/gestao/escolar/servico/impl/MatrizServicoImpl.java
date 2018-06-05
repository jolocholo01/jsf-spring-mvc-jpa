package com.mz.sistema.gestao.escolar.servico.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.autenticacao.AuthenticationContext;
import com.mz.sistema.gestao.escolar.enumerado.Ciclo;
import com.mz.sistema.gestao.escolar.enumerado.TipoCurso;
import com.mz.sistema.gestao.escolar.modelo.Escola;
import com.mz.sistema.gestao.escolar.modelo.Matriz;
import com.mz.sistema.gestao.escolar.servico.MatrizServico;

@Service
@Transactional
public class MatrizServicoImpl implements MatrizServico {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private AuthenticationContext authenticationContext;

	@Override
	public void salvar(Matriz matriz) {
		em.merge(matriz);
	}

	@Override
	public void exclui(Matriz matriz) {
		em.remove(em.merge(matriz));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Matriz> listartodasEscola() {
		Escola escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();
		List<Matriz> matrizes = em.createQuery("SELECT disciplinaClasses FROM Matriz WHERE id_escola=:idEscola")
				.setParameter("idEscola", escola.getId()).getResultList();
		if (!matrizes.isEmpty()) {
			return matrizes;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Matriz obterMatrizPorId(Long idMatriz) {
		List<Matriz> matrizes = em.createQuery("FROM Matriz WHERE id=:idMatriz").setParameter("idMatriz", idMatriz)
				.getResultList();
		if (!matrizes.isEmpty()) {
			return matrizes.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Matriz obterMatrizPorIdELeftJoinAtiva(long idClasse, String curso, Long idEscola) {
		List<Matriz> matrizes =  em
				.createQuery(
						"FROM Matriz m left join fetch m.disciplinaClasses WHERE m.classe.id=:idClasse AND m.curso =:Curso AND  m.escola.id=:IdEscola AND m.ativa=true")
				.setParameter("idClasse", idClasse).setParameter("Curso", curso).setParameter("IdEscola", idEscola)
				.getResultList();

		if(!matrizes.isEmpty()){
			return matrizes.get(0);
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Matriz obterMatrizPorIdELeftJoin(Long idMatriz) {
		List<Matriz> matrizes = em.createQuery("FROM Matriz m left join fetch m.disciplinaClasses WHERE m.id=:idMatriz")
				.setParameter("idMatriz", idMatriz).getResultList();

		if (!matrizes.isEmpty()) {
			return matrizes.get(0);
		}
		return null;

	}
	@SuppressWarnings("unchecked")
	@Override
	public Matriz disciplinaClasseExistente(long idClasse, String curso, Long idEscola) {

		List<Matriz> matrizes = em
				.createQuery("FROM Matriz WHERE classe.id=:idClasse AND curso=:Curso AND escola.id=:IdEscola ")
				.setParameter("idClasse", idClasse).setParameter("Curso", curso).setParameter("IdEscola", idEscola)
				.getResultList();
		if (!matrizes.isEmpty()) {
			return matrizes.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Matriz obterDisciplinasAreaClasse(long idClasse, TipoCurso curso, String opcao, Integer ano) {
		List<Matriz> matrizes = em
				.createQuery("FROM Matriz WHERE classe.id=:idClasse AND curso=:Curso AND opcao=:OPCAO AND ano=:ANO")
				.setParameter("idClasse", idClasse).setParameter("Curso", curso).setParameter("ANO", ano)
				.setParameter("OPCAO", opcao).getResultList();
		if (!matrizes.isEmpty()) {
			return matrizes.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Matriz> obterMatrizPorCiclo(Ciclo ciclo) {
		List<Matriz> matrizes = em.createQuery("FROM Matriz WHERE classe.ciclo=:ciclo ").setParameter("ciclo", ciclo)
				.getResultList();
		if (!matrizes.isEmpty()) {
			return matrizes;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Matriz obterMatrizPorClasseCursoAno(long idClasse, String curso, Integer ano) {
		List<Matriz> matrizes = em.createQuery("FROM Matriz WHERE classe.id=:idClasse AND curso=:Curso AND ano=:ANO")
				.setParameter("idClasse", idClasse).setParameter("Curso", curso).setParameter("ANO", ano)
				.getResultList();
		if (!matrizes.isEmpty()) {
			return matrizes.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Matriz> obterMatrizPorCursoEscola(TipoCurso curso) {
		Escola escola = authenticationContext.getFuncionarioEscolaLogada().getEscola();
		List<Matriz> matrizes = em.createQuery("FROM Matriz WHERE classe.escola.id=:idEscola AND curso=:Curso")
				.setParameter("idEscola", escola.getId()).setParameter("Curso", curso).getResultList();
		if (!matrizes.isEmpty()) {
			return matrizes;
		}
		return null;
	}

	@Override
	public List<Matriz> obterMatrizPorEscolaPorCiclo( String ciclo, Long idEscola) {
		@SuppressWarnings("unchecked")
		List<Matriz> matrizes = em
				.createQuery(
						"FROM Matriz WHERE  escola.id=:idEscola AND ciclo=:Ciclo ORDER BY classe.ordem")
				.setParameter("idEscola", idEscola).setParameter("Ciclo", ciclo)
				.getResultList();
		if (!matrizes.isEmpty()) {
			return matrizes;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Matriz disciplinaClasseExistenteSegundoCiclo(long idClasse, String curso, String tipoArea, Long idEscola) {

		List<Matriz> matrizes = em
				.createQuery(
						"FROM Matriz WHERE classe.id=:idClasse AND curso=:Curso AND  escola.id=:IdEscola AND tipoArea=:TipoArea ")
				.setParameter("idClasse", idClasse).setParameter("Curso", curso).setParameter("IdEscola", idEscola)
				.setParameter("TipoArea", tipoArea).getResultList();
		if (!matrizes.isEmpty()) {
			return matrizes.get(0);
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Matriz> obterMatrizPorClasseCursoAtivo(long idClasse, String curso, Long idEscola) {

		List<Matriz> matrizes = em
				.createQuery(
						"FROM Matriz WHERE classe.id=:idClasse AND curso =:Curso AND  escola.id=:IdEscola AND ativa=true")
				.setParameter("idClasse", idClasse).setParameter("Curso", curso).setParameter("IdEscola", idEscola)
				.getResultList();
		if (!matrizes.isEmpty()) {
			return matrizes;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Matriz obterMatrizPorSegundoCiclo(long idClasse, String curso, String tipoArea, Long idEscola) {

		List<Matriz> matrizes = em
				.createQuery(
						"FROM Matriz  m left join fetch m.disciplinaClasses WHERE m.classe.id=:idClasse AND m.curso=:Curso AND  m.escola.id=:IdEscola AND m.tipoArea=:TipoArea AND m.ativa=true")
				.setParameter("idClasse", idClasse).setParameter("Curso", curso).setParameter("IdEscola", idEscola)
				.setParameter("TipoArea", tipoArea).getResultList();
		if (!matrizes.isEmpty()) {
			return matrizes.get(0);
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Matriz obterMatrizPorPrimeiroCiclo(long idClasse, String curso, Long idEscola) {

		List<Matriz> matrizes = em
				.createQuery(
						"FROM Matriz  m left join fetch m.disciplinaClasses WHERE m.classe.id=:idClasse AND m.curso=:Curso AND  m.escola.id=:IdEscola  AND m.ativa=true")
				.setParameter("idClasse", idClasse).setParameter("Curso", curso).setParameter("IdEscola", idEscola)
				.getResultList();
		if (!matrizes.isEmpty()) {
			return matrizes.get(0);
		}
		return null;

	}

	@Override
	public List<Matriz> obterMatriz2Ciclo(long idClasse, String curso, String tipoArea, Long idEscola) {
		@SuppressWarnings("unchecked")
		List<Matriz> matrizes = em
				.createQuery(
						"FROM Matriz  m  WHERE m.classe.id=:idClasse AND m.curso=:Curso AND  m.escola.id=:IdEscola AND m.tipoArea=:TipoArea AND m.ativa=true")
				.setParameter("idClasse", idClasse).setParameter("Curso", curso).setParameter("IdEscola", idEscola)
				.setParameter("TipoArea", tipoArea).getResultList();
		if (!matrizes.isEmpty()) {
			return matrizes;
		}
		return null;

	}

	@Override
	public Matriz obterMatriz2CicloPorIdELeftJoinAtiva(long idClasse, String curso, String tipoArea, Long idEscola) {
		@SuppressWarnings("unchecked")
		List<Matriz> matrizes = em
				.createQuery(
						"FROM Matriz  m left join fetch m.disciplinaClasses  WHERE m.classe.id=:idClasse AND m.curso=:Curso AND  m.escola.id=:IdEscola AND m.tipoArea=:TipoArea AND m.ativa=true")
				.setParameter("idClasse", idClasse).setParameter("Curso", curso).setParameter("IdEscola", idEscola)
				.setParameter("TipoArea", tipoArea).getResultList();
		if (!matrizes.isEmpty()) {
			return matrizes.get(0);
		}
		return null;
	}

}