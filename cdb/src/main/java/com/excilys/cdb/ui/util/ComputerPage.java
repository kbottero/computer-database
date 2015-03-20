package com.excilys.cdb.ui.util;

import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.IService;

public class ComputerPage extends Page<Computer, Long>{
	
	public ComputerPage(IService<Computer,Long> serv) {
		super(serv);
		orderBy.add(CompanyMapper.DEFAULT_ID);
	}
	
	public ComputerPage(IService<Computer,Long> serv, Long pageSize) {
		super(serv,pageSize);
		orderBy.add(CompanyMapper.DEFAULT_ID);
	}
}
