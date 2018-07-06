package com.mz.sistema.gestao.escolar.servico.impl;

import java.sql.Connection;
import java.sql.SQLException;
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
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.sistema.gestao.escolar.servico.GeradorDeRelatoriosServico;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@Transactional
public class GeradorDeRelatoriosServicoImpl implements GeradorDeRelatoriosServico {

	@PersistenceContext
	private EntityManager em;

	@Override
	public String gerarRelatorio(String caminho, Map<String, Object> parametro) {
		String relatorio = null;
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
			String caminhoReal = servletContext.getRealPath(caminho);
			relatorio = JasperFillManager.fillReportToFile(caminhoReal, parametro, conexao());
			JasperExportManager.exportReportToPdfFile(relatorio, "doc.pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
		return relatorio;
	}

	private Connection conexao() {
		Session session = em.unwrap(Session.class);
		MyWork myWork = new MyWork();
		session.doWork(myWork);
		return myWork.getConnection();
	}

	private static class MyWork implements Work {

		Connection conn;

		@Override
		public void execute(Connection conn) throws SQLException {
			this.conn = conn;
		}

		Connection getConnection() {
			return conn;
		}

	}

	@Override
	// para funcionar este metodo e preciso criar um jar de fonte
	public void geraPdf(String caminho, Map<String, Object> parametros, String filename) {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			ServletOutputStream servletOutputStream = response.getOutputStream();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment;filename=" + filename);
			context.responseComplete();
			ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
			String caminhoReal = servletContext.getRealPath(caminho);
			byte[] bytes = JasperRunManager.runReportToPdf(caminhoReal, parametros, conexao());
			servletOutputStream.write(bytes, 0, bytes.length);

			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	// para funcionar este metodo e preciso criar um jar de fonte
	public void geraPdfComConexaoDataSource(String caminho, Map<String, Object> parametros, String filename,
			JRBeanCollectionDataSource dataSource) {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			ServletOutputStream servletOutputStream = response.getOutputStream();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment;filename=" + filename);
			context.responseComplete();
			ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
			String caminhoReal = servletContext.getRealPath(caminho);
			byte[] bytes = JasperRunManager.runReportToPdf(caminhoReal, parametros, dataSource);
			servletOutputStream.write(bytes, 0, bytes.length);

			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
