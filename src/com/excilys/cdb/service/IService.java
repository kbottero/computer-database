package com.excilys.cdb.service;

import java.util.Collection;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public interface IService {
	
	public Collection<Computer> getAllComputer();
	
	public Computer getOneComputer(long id);
	
	public void saveOneComputer(Computer c);
	
	public void deleteOneComputer(long id);
	
	public Collection<Company> getAllCompany();
	
	public Company getOneCompany(long id);
}
