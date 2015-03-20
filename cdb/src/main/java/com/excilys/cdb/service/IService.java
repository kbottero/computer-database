package com.excilys.cdb.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

import com.excilys.cdb.exception.ServiceException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.IDao.Order;

public interface IService <T, I extends Serializable> {
	
	/**
	 * Return a list of all the instances in the database.
	 * @return
	 */
	public default List<T> getAll() throws ServiceException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return a limited list of instance from the database.
	 * @return
	 */
	public default List<T> getSome(List<String> orderByCol, Order order, Long limit, Long offset ) throws ServiceException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return the number of instance in the database.
	 * @return
	 */
	public default Long getNbInstance() throws ServiceException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Return a specific instance from the database, based on its id.
	 * @param id
	 * @return
	 */
	public default T getOne(I id) throws ServiceException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Save one instance in the database.
	 * @param id
	 * @return
	 */
	public default void saveOne(T c) throws ServiceException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Delete a one instance in the database base on its Id.
	 * @param id
	 * @return
	 */
	public default void deleteOne(I id) throws ServiceException {
		throw new UnsupportedOperationException();
	}
}
