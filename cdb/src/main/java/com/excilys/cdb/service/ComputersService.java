package com.excilys.cdb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.exception.ServiceException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DaoRequestParameter;
import com.excilys.cdb.persistence.IDao;
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
	@Transactional(readOnly=true)
	public List<Computer> getAll() {
		return dao.getAll();
	}
	
	/**
	 * Return a list of all the computers in the database.
	 * Ordered according to given columns.
	 * @return
	 */
	@Override
	public List<Computer> getAll(DaoRequestParameter param) {
		return dao.getAll(param);
	}
	
	/**
	 * Return a limited list of computers from the database. 
	 * Filtered according to a name (the instance should begin with the filter value)
	 * Ordered according to given columns. In an ascending or descending order.
	 * @return
	 */
	@Override
	public List<Computer> getSome(DaoRequestParameter param) {
		return dao.getSome(param);
	}

	/**
	 * Return the number of computer in the database.
	 * @return
	 */
	@Override
	public Long getNbInstance() {
		return dao.getNb();
	}
	
	/**
	 * Return the number of instance in the database.
	 * @return
	 */
	public Long getNbInstance(DaoRequestParameter param) {
		return dao.getNb(param);
	}

	/**
	 * Return a specific Computer from the database, based on its id.
	 * @param id
	 * @return
	 */
	@Override
	public Computer getOne(Long id) {
			return dao.getById(id);
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
	@Transactional(rollbackFor=RuntimeException.class)
	public void deleteOne(Long id) throws ServiceException {
		dao.delete(id);
	}

}
