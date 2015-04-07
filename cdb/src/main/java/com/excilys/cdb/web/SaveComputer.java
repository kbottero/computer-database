package com.excilys.cdb.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.exception.ServiceException;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.CompaniesService;
import com.excilys.cdb.service.ComputersService;
import com.excilys.cdb.validation.ValidatorDate;

@WebServlet(urlPatterns = "/saveComputer")
public class SaveComputer extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8015371304857282287L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
		ComputersService computersService = (ComputersService) context.getBean("computersService");
		CompaniesService companiesService = (CompaniesService) context.getBean("companiesService");
		String computerName = request.getParameter("computerName");
		Long computerId = null;
		String attrib = request.getParameter("computerId");
		if (attrib == null) {
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"No computer id received.");
		    return;
		} 
		try {
			computerId = Long.parseLong(attrib);
		} catch (NumberFormatException e) {
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Computer id received is not a number.");
		    return;
		}
		
		if (computerName == null || computerId == null) {
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Computer id or name wasn'treceived.");
		    return;
		}
		String introduced = request.getParameter("introduced");
		if(introduced == null || 
				(!introduced.isEmpty() && !ValidatorDate.check(introduced))) {
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Incorrect introduced date send.");
		    return;
		}
		String discontinued = request.getParameter("discontinued");
		if(discontinued == null || 
				(!discontinued.isEmpty() && !ValidatorDate.check(discontinued))) {
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Incorrect discontinued date send.");
		    return;
		}
		attrib = request.getParameter("companyId");
		Long compId = null;
		String companyName = "";
		if (attrib != null) {
			if (!attrib.isEmpty()) {
				try {
					compId = Long.parseLong(attrib);
					if (!compId.equals(0l)) {
						Company company = companiesService.getOne(compId);
						companyName = company.getName();
					}
				} catch (NumberFormatException | ServiceException e) {
				    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"The company id received is not valid.");
				    return;
				}
			} else {
				compId = 0l;
			}
		} else {
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"No company id received.");
		    return;
		}
		ComputerDTO computerDTO = new ComputerDTO(computerId,computerName, introduced, discontinued, compId, companyName );

		ComputerMapper computerMapper = (ComputerMapper) context.getBean("computerMapper");
		computersService.saveOne(computerMapper.fromDTO(computerDTO));
		
		RequestDispatcher dis = this.getServletContext().getRequestDispatcher("/index.jsp");
		dis.forward(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
}
