package com.excilys.cdb.ui.util;

import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.IService;

public class CompanyPage extends Page<Company, Long>{
	
	public CompanyPage(IService<Company,Long> serv) {
		super(serv);
		orderBy.add(ComputerMapper.DEFAULT_ID);
	}
	
	public CompanyPage(IService<Company,Long> serv, Long pageSize) {
		super(serv, pageSize);
		orderBy.add(ComputerMapper.DEFAULT_ID);
	}
}
