package com.excilys.cdb.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.exception.ServiceException;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.CompaniesService;
import com.excilys.cdb.service.ComputersService;
import com.excilys.cdb.validation.ValidatorDate;

@WebServlet(urlPatterns = "/insertComputer")
public class InsertComputer extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6819960873094583968L;
	private ComputersService computersService = new ComputersService();
	private CompaniesService companiesService = new CompaniesService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String computerName = request.getParameter("computerName");
		String attrib = null;
		if (computerName == null) {
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Computer name wasn'treceived.");
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
		ComputerDTO computerDTO = new ComputerDTO(0l,computerName, introduced, discontinued, compId, companyName );
		computersService.saveOne(ComputerMapper.INSTANCE.fromDTO(computerDTO));
		
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
