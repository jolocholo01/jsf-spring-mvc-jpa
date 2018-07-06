package com.mz.sistema.gestao.escolar.servico;

import java.util.Map;
/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


public interface GeradorDeRelatoriosServico {
	public String gerarRelatorio(String caminho, Map<String, Object> parametro);
	public void geraPdf(String caminho, Map<String, Object> parametro, String nomeDoc);
	void geraPdfComConexaoDataSource(String caminho, Map<String, Object> parametros, String filename,
			JRBeanCollectionDataSource dataSource);

}
