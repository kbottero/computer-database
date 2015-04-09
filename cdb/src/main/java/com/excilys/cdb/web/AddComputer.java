package com.excilys.cdb.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.mapper.IMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.IService;

@Controller
@RequestMapping("/addComputer")
public class AddComputer {
	
	@Autowired
	private IService<Company, Long> companiesService;
	@Autowired
	private IMapper<Company, CompanyDTO> companyMapper;

	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})
	protected ModelAndView doGet(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("addComputer");
		List<CompanyDTO> listCompany = companyMapper.toDTOList(companiesService.getAll());
		model.addObject("companies", listCompany);
		model.addObject("prev", request.getHeader("Referer"));
		return model;
	}
}
