
package com.mz.sistema.gestao.escolar.autenticacao;

import java.io.IOException;
import java.net.URLDecoder;

/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component("authorizationFilter")
public class AuthorizationFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		String path = ((HttpServletRequest) req).getServletPath();

		if (((HttpServletRequest) req).getQueryString() != null) {
			path = path + "?" + ((HttpServletRequest) req).getQueryString();
		}
		path = URLDecoder.decode(path, "UTF-8");
		
		if(path.contains("/login/login.jsp")){
			chain.doFilter(req, resp);
			return;
		}		
		if(path.contains("/academico/")){
			chain.doFilter(req, resp);
			return;
		}
		
		if (path.contains("resources") || path.contains("css")|| path.endsWith("js") || path.contains("fonts") || path.contains("images") 
				|| path.contains("javax.faces.resource")) {
			chain.doFilter(req, resp);
			return;
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
