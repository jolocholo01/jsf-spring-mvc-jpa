package com.mz.sistema.gestao.escolar.servico;

import java.io.File;
import java.util.List;

import com.mz.sistema.gestao.escolar.modelo.RecuperarSenha;
import com.mz.sistema.gestao.escolar.modelo.Usuario;

public interface EnvioEmailServico {
	public void enviarEmail(String assunto, String texto, 
			List<File> anexos,  List<String> destinatarios);
	public void enviarEmailUsuario(Usuario usuario,RecuperarSenha recoperarSenha, String url);
}
