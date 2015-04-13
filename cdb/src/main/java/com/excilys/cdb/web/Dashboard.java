package com.excilys.cdb.web;

import java.util.ArrayList;
import java.util.List;

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
			@RequestParam(value="search", defaultValue="", required=false) final String search,
			@RequestParam(value="numPage", defaultValue="", required=false) final String numPage,
			@RequestParam(value="nbCompPerPage", defaultValue="", required=false) final String nbCompPerPage,
			@RequestParam(value="sortColumn", defaultValue="", required=false) final String sortColumn,
			@RequestParam(value="sortColumnOrder", defaultValue="", required=false) final String sortColumnOrder,
			@RequestParam(value="language", defaultValue="", required=false) final String language) {
		ComputerPage compPage;
		ModelAndView model = new ModelAndView("dashboard");
		
		Long nbPerPage = 10l;
		if (nbCompPerPage != null && !nbCompPerPage.isEmpty()) {
			nbPerPage = Long.parseLong(nbCompPerPage);
			if (nbPerPage != 10l && nbPerPage != 50l && nbPerPage != 100l) {
				nbPerPage = 10l;
			}
		}
		
		DaoRequestParameter param = getDaoRequestParameter(search, numPage, nbCompPerPage, sortColumn, sortColumnOrder, language);
		compPage = new ComputerPage(computersService,nbPerPage, param);
		if (numPage != null && !numPage.isEmpty()) {
			Long num = Long.parseLong(numPage);
			compPage.goToPage(num);
		}

		model.addObject("nbCompPerPage", nbCompPerPage);
		model.addObject("page", compPage);
		model.addObject("computers", computerMapper.toDTOList(compPage.getElements()));
		return model;
	}
	
	private DaoRequestParameter getDaoRequestParameter (
					final String search,
					final String numPage,
					final String nbCompPerPage,
					final String sortColumn,
					final String sortColumnOrder,
					final String language) {
		DaoRequestParameter daoRequestParameter = new DaoRequestParameter();
		
		if (search != null && !search.isEmpty()) {
			daoRequestParameter.setNameLike(search);
		}
		if ((nbCompPerPage != null) && (! nbCompPerPage.isEmpty()) && (numPage != null) && (!numPage.isEmpty())) {
			Long limit = Long.parseLong(nbCompPerPage);
			daoRequestParameter.setLimit(limit);
			Long page = Long.parseLong(numPage);
			daoRequestParameter.setOffset((page-1)*limit);
		} else {
			daoRequestParameter.setLimit(10l);
		}
		List<String> l = null;
		if (sortColumn != null && !sortColumn.isEmpty()) {
			String [] list = sortColumn.split(",");
			l = new ArrayList<String>();
			for (String s : list) {
				l.add(s);
			}
		}
		daoRequestParameter.setColToOrderBy(l);
		DaoRequestParameter.Order order = DaoRequestParameter.Order.ASC;;
		if (sortColumnOrder != null && !sortColumnOrder.isEmpty()) {
			switch (sortColumnOrder) {
			case "ASC":
				order = DaoRequestParameter.Order.ASC;
				break;
			case "DESC":
				order = DaoRequestParameter.Order.DESC;
				break;
			default:
				break;
			}
		}
		daoRequestParameter.setOrder(order);
		
		return daoRequestParameter;
	}
	
}
