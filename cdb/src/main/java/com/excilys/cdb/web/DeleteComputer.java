package com.excilys.cdb.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.IService;

@Controller
@RequestMapping("/deleteComputer")
public class DeleteComputer {
	private static Logger logger = LoggerFactory.getLogger(DeleteComputer.class);

	@Autowired
	private IService<Computer,Long> computersService;

	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})
	protected String doGet(@RequestParam(value="selection", required=true) final String[] selection) {
		if (selection != null) {
		    logger.debug(selection.toString());
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
