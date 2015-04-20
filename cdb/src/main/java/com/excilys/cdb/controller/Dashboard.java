package com.excilys.cdb.controller;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	protected String doGet(@ModelAttribute("pageDto") ComputerPageDTO pageDto,ModelMap model) {
		ComputerPage compPage;
		
		compPage = ComputerPageMapper.fromDTO(computersService,pageDto);
		if (!(compPage.getPageNumber()<=0)) {
			compPage.goToPage(compPage.getPageNumber());
		}
		
		model.addAttribute("nbCompPerPage", compPage.getNbElemPerPage());
		model.addAttribute("page", compPage);
		model.addAttribute("computers", computerMapper.toDTOList(compPage.getElements()));
		StringBuilder strgBuildCol = new StringBuilder();
		StringBuilder strgBuildOrder = new StringBuilder();
		if (compPage.getSort() != null) {
			for (Iterator<Order> it = compPage.getSort().iterator(); it.hasNext();) {
				Order order = it.next();
				strgBuildCol.append(order.getProperty());
				strgBuildOrder.append(order.getDirection().name());
			}
		}
		model.addAttribute("order", strgBuildCol.toString());
		model.addAttribute("direction", strgBuildOrder.toString());
		return "dashboard";
	}
}
