package com.excilys.cdb.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.service.CompaniesService;
import com.excilys.cdb.service.ComputersService;
import com.excilys.cdb.ui.util.ComputerPage;

public class Application extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6070537336825013977L;
	
	private ComputersService computersService;
	private CompaniesService companiesService;
       
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(){
		computersService = new ComputersService();
		companiesService = new CompaniesService();
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getPathInfo();

		if (action == null) {
			action = "/list";
		}
		if (action.equals("/list")) {
			doListeComputerRequest(request, response);
			return;
		}
	}

	private void doListeComputerRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ComputerPage compPage= new ComputerPage(computersService);
		compPage.nextPage();
		request.setAttribute("computersPage", compPage);
		request.setAttribute("nbComputers", computersService.getNbInstance());
		RequestDispatcher dis=this.getServletContext().getRequestDispatcher("/views/dashboard.jsp");
		
		dis.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
