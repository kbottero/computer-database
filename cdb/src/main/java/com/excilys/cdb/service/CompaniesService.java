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
	 * Return a list of all companies in the database.
	 * Ordered according to given columns.
	 * @return
	 */
	@Override
	public List<Company> getAll(List<String> orderByCol) throws ServiceException {
		try {
			return CompanyDao.INSTANCE.getAll(orderByCol);
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
	public List<Company> getAll(List<String> orderByCol,
			Order order)  throws ServiceException {
		try {
			return CompanyDao.INSTANCE.getAll(orderByCol, order);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Return a limited list of companies from the database.
	 *  Ordered according to given columns. In an ascending or descending order.
	 * @return
	 */
	@Override
	public List<Company> getSome(Long limit, Long offset) throws ServiceException {
		try {
			return CompanyDao.INSTANCE.getSome(limit, offset);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Return a limited list of companies from the database.
	 *  Ordered according to given columns. In an ascending or descending order.
	 * @return
	 */
	@Override
	public List<Company> getSome(Long limit, Long offset, List<String> orderByCol)  throws ServiceException {
		try {
			return CompanyDao.INSTANCE.getSome(limit, offset, orderByCol);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Return a limited list of companies from the database.
	 *  Ordered according to given columns. In an ascending or descending order.
	 * @return
	 */
	@Override
	public List<Company> getSome(Long limit, Long offset, List<String> orderByCol,
			Order order) throws ServiceException {
		try {
			return CompanyDao.INSTANCE.getSome(limit, offset, orderByCol, order );
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
	public List<Company> getSome(String nameFilter, Long limit, Long offset ) throws ServiceException {
		try {
			return CompanyDao.INSTANCE.getSome(nameFilter, limit, offset);
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
	public List<Company> getSome(String nameFilter, Long limit, Long offset, List<String> orderByCol ) throws ServiceException {
		try {
			return CompanyDao.INSTANCE.getSome(nameFilter, limit, offset, orderByCol );
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
	public List<Company> getSome(String nameFilter, Long limit, Long offset, List<String> orderByCol,
			Order order) throws ServiceException {
		try {
			return CompanyDao.INSTANCE.getSome(nameFilter, limit, offset, orderByCol, order );
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
}
