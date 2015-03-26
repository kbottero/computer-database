package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.exception.ServiceException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ComputerDao;
import com.excilys.cdb.persistence.DaoRequestParameter;
import com.excilys.cdb.validation.ValidatorComputer;

public class ComputersService implements IService<Computer,Long> {

	/**
	 * Return a list of all the computers in the database.
	 * @return
	 */
	@Override
	public List<Computer> getAll() throws ServiceException {
		try {
			return ComputerDao.INSTANCE.getAll();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Return a list of all the computers in the database.
	 * Ordered according to given columns.
	 * @return
	 */
	@Override
	public List<Computer> getAll(DaoRequestParameter param) throws ServiceException {
		try {
			return ComputerDao.INSTANCE.getAll(param);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Return a limited list of computers from the database. 
	 * Filtered according to a name (the instance should begin with the filter value)
	 * Ordered according to given columns. In an ascending or descending order.
	 * @return
	 */
	@Override
	public List<Computer> getSome(DaoRequestParameter param) throws ServiceException {
		try {
			return ComputerDao.INSTANCE.getSome(param);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Return the number of computer in the database.
	 * @return
	 */
	@Override
	public Long getNbInstance() throws ServiceException {
		try {
			return ComputerDao.INSTANCE.getNb();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Return the number of instance in the database.
	 * @return
	 */
	public Long getNbInstance(DaoRequestParameter param) throws ServiceException {
		try {
			return ComputerDao.INSTANCE.getNb(param);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Return a specific Computer from the database, based on its id.
	 * @param id
	 * @return
	 */
	@Override
	public Computer getOne(Long id) throws ServiceException {
		try {
			return ComputerDao.INSTANCE.getById(id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Save one Computer in the database.
	 * @param id
	 * @return
	 */
	@Override
	public void saveOne(Computer c) throws ServiceException {
		if (!ValidatorComputer.check(c)) {
			throw new ServiceException();
		}
		try {
			ComputerDao.INSTANCE.save(c);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Delete a Computer in the database.
	 * @param id
	 * @return
	 */
	@Override
	public void deleteOne(Long id) throws ServiceException {
		try {
			ComputerDao.INSTANCE.delete(id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
