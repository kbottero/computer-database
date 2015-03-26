package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.exception.ServiceException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.DaoRequestParameter;

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
	 * Return a list of all companies in the database.
	 * Ordered according to given columns. In an ascending or descending order.
	 * @return
	 */
	@Override
	public List<Company> getAll(DaoRequestParameter param) throws ServiceException {
		try {
			return CompanyDao.INSTANCE.getAll(param);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Return a limited list of companies from the database.
	 * Filtered according to a name (the instance should begin with the filter value)
	 * Ordered according to given columns. In an ascending or descending order.
	 * @return
	 */
	@Override
	public List<Company> getSome(DaoRequestParameter param) throws ServiceException {
		try {
			return CompanyDao.INSTANCE.getSome(param);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Return the number of companies in the database.
	 * @return
	 */
	@Override
	public Long getNbInstance() throws ServiceException {
		try {
			return CompanyDao.INSTANCE.getNb();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Return the number of companies in the database.
	 * @return
	 */
	@Override
	public Long getNbInstance(DaoRequestParameter param) throws ServiceException {
		try {
			return CompanyDao.INSTANCE.getNb(param);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Return a limited list of companies from the database.
	 * @return
	 */
	@Override
	public Company getOne(Long id) throws ServiceException {
		try {
			return CompanyDao.INSTANCE.getById(id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Delete a Company in the database.
	 * @param id
	 * @return
	 */
	@Override
	public void deleteOne(Long id) throws ServiceException {
		try {
			CompanyDao.INSTANCE.delete(id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
