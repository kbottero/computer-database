package com.excilys.cdb.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.service.CompaniesService;
import com.excilys.cdb.service.ComputersService;

@Controller
@WebServlet(urlPatterns = "/editComputer")
public class EditComputer extends AbstractServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6844149787037040594L;
	
	@Autowired
	private ComputersService computersService;
	@Autowired
	private CompaniesService companiesService;
	@Autowired
	private ComputerMapper computerMapper;
	@Autowired
	private CompanyMapper companyMapper;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
