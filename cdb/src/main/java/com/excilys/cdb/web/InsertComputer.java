package com.excilys.cdb.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.IMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.IService;

@Controller
@RequestMapping("/insertComputer")
public class InsertComputer {

	@Autowired
	private IService<Computer,Long> computersService;
	@Autowired
	private IService<Company,Long> companiesService;
	@Autowired
	private IMapper<Computer,ComputerDTO> computerMapper;

	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})
    @ExceptionHandler(NoSuchRequestHandlingMethodException.class)
	protected String doGet(
			@RequestParam(value="computerName", defaultValue="", required=false) final String computerName,
			@RequestParam(value="introduced", defaultValue="", required=false) final String introduced,
			@RequestParam(value="discontinued", defaultValue="", required=false) final String discontinued,
			@RequestParam(value="companyId", defaultValue="", required=false) final String companyId) {
		
		Long compId = null;
		String companyName = "";
		if (!companyId.isEmpty()) {
			compId = Long.parseLong(companyId);
			if (!compId.equals(0l)) {
				Company company = companiesService.getOne(compId);
				companyName = company.getName();
			}
		} else {
			compId = 0l;
		}
		ComputerDTO computerDTO = new ComputerDTO(0l,computerName, introduced, discontinued, compId, companyName );
		computersService.saveOne(computerMapper.fromDTO(computerDTO));
		return "redirect:/dashboard";
	}
}
