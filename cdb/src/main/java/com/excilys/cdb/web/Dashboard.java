package com.excilys.cdb.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.IMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DaoRequestParameter;
import com.excilys.cdb.persistence.DaoRequestParameter.NameFiltering;
import com.excilys.cdb.service.IService;
import com.excilys.cdb.ui.util.ComputerPage;

@Controller
@RequestMapping("/dashboard")
public class Dashboard {

	@Autowired
	private IService<Computer, Long> computersService;
	@Autowired
	private IMapper<Computer, ComputerDTO> computerMapper;
	 
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})
	protected ModelAndView doGet(
			@RequestParam(value="nbCompPerPage", defaultValue="", required=false) final String nbCompPerPage,
			@RequestParam(value="numPage", defaultValue="", required=false) final String numPage,
			@RequestParam(value="search", defaultValue="", required=false) final String searchParam) {
		ComputerPage compPage;
		ModelAndView model = new ModelAndView("dashboard");
		
		Long nbPerPage = 10l;
		if (nbCompPerPage != null && !nbCompPerPage.isEmpty()) {
			nbPerPage = Long.parseLong(nbCompPerPage);
			if (nbPerPage != 10l && nbPerPage != 50l && nbPerPage != 100l) {
				nbPerPage = 10l;
			}
		}
		
		if (searchParam != null && !searchParam.isEmpty()) {
			DaoRequestParameter param = new DaoRequestParameter(NameFiltering.POST,searchParam,null,null,nbPerPage,0l);
			compPage = new ComputerPage(computersService,nbPerPage, param);
		} else {
			compPage = new ComputerPage(computersService, nbPerPage, null);
		}
		
		if (numPage != null && !numPage.isEmpty()) {
			Long num = Long.parseLong(numPage);
			compPage.goToPage(num);
		}

		model.addObject("nbCompPerPage", nbCompPerPage);
		model.addObject("page", compPage);
		model.addObject("computers", computerMapper.toDTOList(compPage.getElements()));
		return model;
	}
	
}
