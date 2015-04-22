package com.excilys.cdb.controller;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.model.IMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.IService;

@Controller
@RequestMapping("/Computer")
public class ComputerController {

	@Autowired
	private IService<Computer,Long> computersService;
	@Autowired
	private IService<Company,Long> companiesService;
	@Autowired
	private IMapper<Computer,ComputerDTO> computerMapper;
	@Autowired
	private IMapper<Company, CompanyDTO> companyMapper;

	@RequestMapping(value="/edit", method = {RequestMethod.GET,RequestMethod.POST})
	protected ModelAndView edit(HttpServletRequest request, @RequestParam(value="id", required=true) final String idComputer) {
		ModelAndView model = new ModelAndView("editComputer");
		if (idComputer != null) {
			if (! Pattern.matches("-?\\d+",idComputer.trim())) { 
				return new ModelAndView("redirect:/dashboard");
			}
			Long id = Long.parseLong(idComputer);
			ComputerDTO computer = computerMapper.toDTO(computersService.getOne(id));
			model.addObject("computer", computer);
			List<CompanyDTO> listCompany = companyMapper.toDTOList(companiesService.getAll());
			model.addObject("companies", listCompany);
			model.addObject("prev", request.getHeader("Referer"));
		}
		return model;
	}
	
	@RequestMapping(value="/add",method = {RequestMethod.GET,RequestMethod.POST})
	protected ModelAndView addComputer(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("addComputer");
		List<CompanyDTO> listCompany = companyMapper.toDTOList(companiesService.getAll());
		model.addObject("companies", listCompany);
		model.addObject("prev", request.getHeader("Referer"));
		return model;
	}
	
	@RequestMapping(value="/save", method = {RequestMethod.GET,RequestMethod.POST})
    @ExceptionHandler(NoSuchRequestHandlingMethodException.class)
	protected String save(@Valid @ModelAttribute("computerDto") ComputerDTO computerDto) throws IOException {
		computersService.saveOne(computerMapper.fromDTO(computerDto));
		return "redirect:/dashboard";
	}
	
	@RequestMapping(value="/delete", method = {RequestMethod.GET,RequestMethod.POST})
	protected String delete(@RequestParam(value="selection", required=true) final String[] selection) {
		if (selection != null) {
		    for (String ids : selection) {
		    		String[] num = ids.split(",");
		    		for (String id : num) {
						Long idValue = Long.parseLong(id);
						computersService.deleteOne(idValue);
		    		}
		    }
		}
		return "redirect:/dashboard";
	}
}
