package com.excilys.cdb.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

public abstract class AbstractServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4291401736235992350L;

	@Override
	public void init() throws ServletException {
		super.init();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
}
