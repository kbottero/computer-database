package com.excilys.cdb.service;

import java.util.Collection;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.CompanyDaoImpl;
import com.excilys.cdb.persistence.ComputerDaoImpl;
import com.excilys.cdb.persistence.IDao;

public class ServiceImpl implements IService{
	
	private IDao<Computer> computerDao;
	private IDao<Company> companyDao;
	
	public ServiceImpl(){
		computerDao = new ComputerDaoImpl();
		companyDao = new CompanyDaoImpl();
	}

	@Override
	public Collection<Computer> getAllComputer() {
		
		return computerDao.getAll();
	}

	@Override
	public Computer getOneComputer(long id) {
		// TODO Auto-generated method stub
		return computerDao.getOne(id);
	}

	@Override
	public void saveOneComputer(Computer c) {
		// TODO Auto-generated method stub
		computerDao.saveOne(c);
	}

	@Override
	public void deleteOneComputer(long id) {
		// TODO Auto-generated method stub
		computerDao.deleteOne(id);
	}

	@Override
	public Collection<Company> getAllCompany() {
		// TODO Auto-generated method stub
		return companyDao.getAll();
	}

	@Override
	public Company getOneCompany(long id) {
		// TODO Auto-generated method stub
		return companyDao.getOne(id);
	}
	
}
