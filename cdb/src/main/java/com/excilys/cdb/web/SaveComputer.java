package com.excilys.cdb.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.excilys.cdb.validation.ValidatorDate;

@Controller
@RequestMapping("/saveComputer")
public class SaveComputer {

	@Autowired
	private IService<Computer,Long> computersService;
	@Autowired
	private IService<Company,Long> companiesService;
	@Autowired
	private IMapper<Computer,ComputerDTO> computerMapper;

	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})
    @ExceptionHandler(NoSuchRequestHandlingMethodException.class)
	protected String doGet(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="computerId", defaultValue="", required=false) final String computerId,
			@RequestParam(value="computerName", defaultValue="", required=false) final String computerName,
			@RequestParam(value="introduced", defaultValue="", required=false) final String introduced,
			@RequestParam(value="discontinued", defaultValue="", required=false) final String discontinued,
			@RequestParam(value="companyId", defaultValue="", required=false) final String companyId) throws IOException {
		Long compId = null;
		compId = Long.parseLong(computerId);
		
		if (computerName.isEmpty() || compId == null) {
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Incorrect computerName date send.");
		}
		if(!introduced.isEmpty() && !ValidatorDate.check(introduced)) {
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Incorrect introduced date send.");
		}
		if(!discontinued.isEmpty() && !ValidatorDate.check(discontinued)) {
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Incorrect discontinued date send.");
		}
		Long companyIdent = null;
		String companyName = "";
		if (!companyId.isEmpty()) {
				companyIdent = Long.parseLong(companyId);
				if (!companyIdent.equals(0l)) {
					Company company = companiesService.getOne(companyIdent);
					companyName = company.getName();
				}
		} else {
			companyIdent = 0l;
		}
		ComputerDTO computerDTO = new ComputerDTO(compId,computerName, introduced, discontinued, companyIdent, companyName );
		computersService.saveOne(computerMapper.fromDTO(computerDTO));
		return "redirect:/dashboard";
	}
}
