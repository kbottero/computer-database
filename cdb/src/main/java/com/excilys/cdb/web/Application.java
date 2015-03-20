package com.excilys.cdb.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.service.ServiceImpl;

public class Application extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6070537336825013977L;
	
	private ServiceImpl service;
       
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(){
		// TODO Auto-generated method stub
		service = new ServiceImpl();
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getPathInfo();

		if(action == null)
		{
			action = "/list";
		}
		if(action.equals("/list"))
		{
			doListeComputerRequest(request, response);
			return;
		}
		
	}

	private void doListeComputerRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		request.setAttribute("computers", service.getAllComputer());
		request.setAttribute("nbComputers", service.getNbComputer());
		RequestDispatcher dis=this.getServletContext().getRequestDispatcher("/views/dashboard.jsp");
		
		dis.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
