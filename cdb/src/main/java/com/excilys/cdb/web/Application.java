package com.excilys.cdb.web;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Company;
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
		if (action.equals("/edit")) {
			doEditComputerRequest(request, response);
			return;
		}
		if (action.equals("/add")) {
			doAddComputerRequest(request, response);
			return;
		}
	}

	private void doListeComputerRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ComputerPage compPage= new ComputerPage(computersService);
		String attrib = request.getParameter("numPage");
		if (attrib != null) {
			Long numPage = Long.parseLong(attrib);
			compPage.goToPage(numPage);
		}
		request.setAttribute("page", compPage);
		request.setAttribute("nbComputers", computersService.getNbInstance());
		RequestDispatcher dis=this.getServletContext().getRequestDispatcher("/views/dashboard.jsp");
		
		dis.forward(request, response);
	}
	
	private void doEditComputerRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String attrib = request.getParameter("id");
		if (attrib != null) {
			Long id = Long.parseLong(attrib);
			ComputerDTO computer = ComputerMapper.INSTANCE.toDTO(computersService.getOne(id));
			request.setAttribute("computer", computer);
			ArrayList<CompanyDTO> listCompany = new ArrayList<CompanyDTO>();
			for (Company company : companiesService.getAll()) {
				listCompany.add(CompanyMapper.INSTANCE.toDTO(company));
			}
			request.setAttribute("companies", listCompany);
			request.setAttribute("prev", request.getHeader("Referer"));
			RequestDispatcher dis=this.getServletContext().getRequestDispatcher("/views/editComputer.jsp");
			dis.forward(request, response);
		}
	}
	
	private void doAddComputerRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		ArrayList<CompanyDTO> listCompany = new ArrayList<CompanyDTO>();
		for (Company company : companiesService.getAll()) {
			listCompany.add(CompanyMapper.INSTANCE.toDTO(company));
		}
		request.setAttribute("companies", listCompany);
		request.setAttribute("prev", request.getHeader("Referer"));
		RequestDispatcher dis=this.getServletContext().getRequestDispatcher("/views/addComputer.jsp");
		dis.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
