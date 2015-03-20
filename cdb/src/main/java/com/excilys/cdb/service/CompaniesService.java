package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.exception.ServiceException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.IDao.Order;

public class CompaniesService implements IService<Company,Long>{


	/**
	 * Return a list of all companies in the database.
	 * @return
	 */
	@Override
	public List<Company> getAll()  throws ServiceException{
		try {
			return CompanyDao.INSTANCE.getAll();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Return a limited list of companies from the database.
	 * @return
	 */
	@Override
	public List<Company> getSome(List<String> orderByCol,
			Order order, Long limit, Long offset)  throws ServiceException{
		try {
			return CompanyDao.INSTANCE.getSome( orderByCol, order, limit, offset );
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Return the number of companies in the database.
	 * @return
	 */
	@Override
	public Long getNbInstance()  throws ServiceException{
		try {
			return CompanyDao.INSTANCE.getNb();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Return a limited list of companies from the database.
	 * @return
	 */
	@Override
	public Company getOne(Long id)  throws ServiceException{
		try {
			return CompanyDao.INSTANCE.getById(id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
