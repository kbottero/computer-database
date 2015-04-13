package com.excilys.cdb.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.IMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.IService;

@Controller
@RequestMapping("/editComputer")
public class EditComputer {

	@Autowired
	private IService<Computer,Long> computersService;
	@Autowired
	private IService<Company,Long> companiesService;
	@Autowired
	private IMapper<Computer,ComputerDTO> computerMapper;
	@Autowired
	private IMapper<Company, CompanyDTO> companyMapper;

	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})
	protected ModelAndView doGet(HttpServletRequest request, @RequestParam(value="id", required=true) final String idComputer) {
		ModelAndView model = new ModelAndView("editComputer");
		if (idComputer != null) {
			Long id = Long.parseLong(idComputer);
			ComputerDTO computer = computerMapper.toDTO(computersService.getOne(id));
			model.addObject("computer", computer);
			List<CompanyDTO> listCompany = companyMapper.toDTOList(companiesService.getAll());
			model.addObject("companies", listCompany);
			model.addObject("prev", request.getHeader("Referer"));
		}
		return model;
	}
}
