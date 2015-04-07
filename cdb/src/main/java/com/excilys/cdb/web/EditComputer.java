package com.excilys.cdb.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.service.CompaniesService;
import com.excilys.cdb.service.ComputersService;

@WebServlet(urlPatterns = "/editComputer")
public class EditComputer extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6844149787037040594L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
		ComputersService computersService = (ComputersService) context.getBean("computersService");
		CompaniesService companiesService = (CompaniesService) context.getBean("companiesService");
		ComputerMapper computerMapper = (ComputerMapper) context.getBean("computerMapper");
		CompanyMapper companyMapper = (CompanyMapper) context.getBean("companyMapper");
		String attrib = request.getParameter("id");
		if (attrib != null) {
			Long id = Long.parseLong(attrib);
			ComputerDTO computer = computerMapper.toDTO(computersService.getOne(id));
			request.setAttribute("computer", computer);
			List<CompanyDTO> listCompany = companyMapper.toDTOList(companiesService.getAll());
			request.setAttribute("companies", listCompany);
			request.setAttribute("prev", request.getHeader("Referer"));
			RequestDispatcher dis=this.getServletContext().getRequestDispatcher("/WEB-INF/views/editComputer.jsp");
			dis.forward(request, response);
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
	
}
