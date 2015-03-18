package com.excilys.cdb.service;

import java.util.Collection;
import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.IDao.Order;

public interface IService {
	
	public Collection<Computer> getAllComputer();

	public Collection<Computer> getSomeComputer(List<String> orderByCol, Order order, Long limit, Long offset );

	public Long getNbComputer();
	
	public Computer getOneComputer(long id);
	
	public void saveOneComputer(Computer c);
	
	public void deleteOneComputer(long id);
	
	public Collection<Company> getAllCompany();
	
	public Company getOneCompany(long id);
	
	public Collection<Company> getSomeCompany(List<String> orderByCol, Order order, Long limit, Long offset );

	public Long getNbCompany();
}
