
package com.mz.sistema.gestao.escolar.autenticacao;

import org.springframework.security.core.userdetails.UserDetails;

/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Transactional
	// metodo chamado pela autenticação 'remember me' para pegar os dados do
	// usuario logado
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return null;
	}

}
