package com.mz.sistema.gestao.escolar.servico;

import java.util.Date;
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
import com.mz.sistema.gestao.escolar.modelo.Aluno;

public interface AlunoServico {
	public Aluno salvarRetornarAluno(Aluno aluno);
	public void salvar(Aluno aluno);
	public void excluir(Aluno aluno);
	public List<Aluno> listartodos();
	public Aluno obterAlunoPorId(Long idAluno);
	public List<Aluno> obterAlunosPorNome(String nome);
	public List<Aluno> obterAlunosPorPesquisa(String pesquisa);
	public Aluno obterAlunoExistente(String nome, Date dataNascimento, boolean sexo, String nascionalidade,
			String pai, String mae);
	
}
