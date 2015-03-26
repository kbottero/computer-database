package com.excilys.cdb.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.service.ComputersService;

@WebServlet(urlPatterns = "/deleteComputer")
public class DeleteComputer  extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3419392631122313675L;
	private static Logger logger = LoggerFactory.getLogger(DeleteComputer.class);
	private ComputersService computersService = new ComputersService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String[] attrib = request.getParameterValues("selection");
	    logger.debug(request.getParameterNames().toString());
		if (attrib != null) {
		    for (String ids : attrib) {
		    		String[] num = ids.split(",");
		    		for (String id : num) {
						Long idValue = Long.parseLong(id);
						computersService.deleteOne(idValue);
		    		}
		    }
		}
		RequestDispatcher dis=this.getServletContext().getRequestDispatcher("/index.jsp");
		dis.forward(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
}
