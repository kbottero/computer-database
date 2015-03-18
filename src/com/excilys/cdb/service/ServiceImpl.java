package com.excilys.cdb.service;

import java.util.Collection;
import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.ComputerDao;
import com.excilys.cdb.persistence.IDao.Order;

public class ServiceImpl implements IService{

	@Override
	public Collection<Computer> getAllComputer() {
		
		return ComputerDao.INSTANCE.getAll();
	}

	@Override
	public Collection<Computer> getSomeComputer( List<String> orderByCol, Order order, Long limit, Long offset ) {
		// TODO Auto-generated method stub
		return ComputerDao.INSTANCE.getSome( orderByCol, order, limit, offset );
	}

	@Override
	public Long getNbComputer() {
		// TODO Auto-generated method stub
		return ComputerDao.INSTANCE.getNb();
	}

	@Override
	public Computer getOneComputer(long id) {
		// TODO Auto-generated method stub
		return ComputerDao.INSTANCE.getById(id);
	}

	@Override
	public void saveOneComputer(Computer c) {
		// TODO Auto-generated method stub
		ComputerDao.INSTANCE.save(c);
	}

	@Override
	public void deleteOneComputer(long id) {
		// TODO Auto-generated method stub
		ComputerDao.INSTANCE.delete(id);
	}

	@Override
	public Collection<Company> getAllCompany() {
		// TODO Auto-generated method stub
		return CompanyDao.INSTANCE.getAll();
	}

	@Override
	public Company getOneCompany(long id) {
		// TODO Auto-generated method stub
		return CompanyDao.INSTANCE.getById(id);
	}

	@Override
	public Collection<Company> getSomeCompany(List<String> orderByCol,
			Order order, Long limit, Long offset) {
		// TODO Auto-generated method stub
		return CompanyDao.INSTANCE.getSome( orderByCol, order, limit, offset );
	}

	@Override
	public Long getNbCompany() {
		// TODO Auto-generated method stub
		return CompanyDao.INSTANCE.getNb();
	}

	
}
