package com.mz.sistema.gestao.escolar.repositorio;

import org.springframework.data.repository.CrudRepository;

import com.mz.sistema.gestao.escolar.modelo.Usuario;

public interface UsuarioRepositorio extends CrudRepository<Usuario, Long>{

}
