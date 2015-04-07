package com.excilys.cdb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.exception.ServiceException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.ComputerDao;
import com.excilys.cdb.persistence.DaoRequestParameter;
import com.excilys.cdb.persistence.IDao;
import com.excilys.cdb.persistence.TransactionFactory;

@Service
public class CompaniesService implements IService<Company,Long>{

	@Autowired @Qualifier("companyDao")
	private IDao<Company,Long> dao;
	
	/**
	 * Return a list of all companies in the database.
	 * @return
	 */
	@Override
	public List<Company> getAll()  throws ServiceException{
		try {
			return dao.getAll();
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
			return dao.getAll(param);
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
			return dao.getSome(param);
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
			return dao.getNb();
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
			return dao.getNb(param);
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
			return dao.getById(id);
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
			TransactionFactory.INSTANCE.startTransaction();
			new ComputerDao().deleteByCompany(id);
			dao.delete(id);
			TransactionFactory.INSTANCE.commitTransaction();
		} catch (DaoException e) {
			TransactionFactory.INSTANCE.cancelTransaction();
			throw new ServiceException(e);
		} finally {
			TransactionFactory.INSTANCE.endTransaction();
		}
	}
}
