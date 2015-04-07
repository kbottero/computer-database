package com.excilys.cdb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.exception.ServiceException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DaoRequestParameter;
import com.excilys.cdb.persistence.IDao;
import com.excilys.cdb.persistence.TransactionFactory;
import com.excilys.cdb.validation.ValidatorComputer;

@Service
public class ComputersService implements IService<Computer,Long> {

	@Autowired @Qualifier("computerDao")
	private IDao<Computer,Long> dao;
	
	public IDao<Computer,Long> getDao() {
		return this.dao;
	}
	
	public void setDao(IDao<Computer,Long> dao) {
		this.dao = dao;
	}
	
	/**
	 * Return a list of all the computers in the database.
	 * @return
	 */
	@Override
	public List<Computer> getAll() throws ServiceException {
		try {
			return dao.getAll();
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
			return dao.getAll(param);
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
			return dao.getSome(param);
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
			return dao.getNb();
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
			return dao.getNb(param);
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
			return dao.getById(id);
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
			dao.save(c);
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
			TransactionFactory.INSTANCE.startTransaction();
			dao.delete(id);
		} catch (DaoException e) {
			TransactionFactory.INSTANCE.cancelTransaction();
			throw new ServiceException(e);
		} finally {
			TransactionFactory.INSTANCE.endTransaction();
		}
	}

}
