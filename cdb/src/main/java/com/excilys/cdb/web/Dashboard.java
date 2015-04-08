package com.excilys.cdb.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.persistence.DaoRequestParameter;
import com.excilys.cdb.persistence.DaoRequestParameter.NameFiltering;
import com.excilys.cdb.service.ComputersService;
import com.excilys.cdb.ui.util.ComputerPage;

@Controller
@WebServlet(urlPatterns = "/dashboard")
public class Dashboard extends AbstractServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6844149787037040594L;

	@Autowired
	private ComputersService computersService;
	@Autowired
	private ComputerMapper computerMapper;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String attrib;
		ComputerPage compPage;
		Long nbCompPerPage = 10l;
		attrib = request.getParameter("nbCompPerPage");
		if (attrib != null) {
			nbCompPerPage = Long.parseLong(attrib);
			if (nbCompPerPage != 10l && nbCompPerPage != 50l && nbCompPerPage != 100l) {
				nbCompPerPage = 10l;
			}
		}
		request.setAttribute("nbCompPerPage", nbCompPerPage);
		attrib = request.getParameter("search");
		if (attrib != null) {
			String filter = attrib;
			DaoRequestParameter param = new DaoRequestParameter(NameFiltering.POST,filter,null,null,nbCompPerPage,0l);
			compPage = new ComputerPage(computersService, nbCompPerPage, param);
		} else {
			compPage = new ComputerPage(computersService, nbCompPerPage, null);
		}
		attrib = request.getParameter("numPage");
		if (attrib != null) {
			Long numPage = Long.parseLong(attrib);
			compPage.goToPage(numPage);
		}

		request.setAttribute("page", compPage);
		request.setAttribute("computers", computerMapper.toDTOList(compPage.getElements()));
		RequestDispatcher dis=this.getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp");
		
		dis.forward(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
	
}
