package com.excilys.cdb.web;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.dto.ComputerPageDTO;
import com.excilys.cdb.mapper.ComputerPageMapper;
import com.excilys.cdb.mapper.IMapper;
import com.excilys.cdb.model.Computer;
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
		
		compPage = ComputerPageMapper.fromDTO(computersService,new ComputerPageDTO(search, numPage, nbCompPerPage, sortColumn, sortColumnOrder, language));
		if (numPage != null && !numPage.isEmpty()) {
			Integer num = Integer.parseInt(numPage);
			compPage.goToPage(num);
		}

		model.addObject("nbCompPerPage", nbCompPerPage);
		model.addObject("page", compPage);
		model.addObject("computers", computerMapper.toDTOList(compPage.getElements()));
		StringBuilder strgBuildCol = new StringBuilder();
		StringBuilder strgBuildOrder = new StringBuilder();
		if (compPage.getSort() != null) {
			for (Iterator<Order> it = compPage.getSort().iterator(); it.hasNext();) {
				Order order = it.next();
				strgBuildCol.append(order.getProperty());
				strgBuildOrder.append(order.getDirection().name());
			}
		}
		model.addObject("order", strgBuildCol.toString());
		model.addObject("direction", strgBuildOrder.toString());
		return model;
	}
}
