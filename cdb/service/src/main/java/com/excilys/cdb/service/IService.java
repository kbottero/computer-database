package com.excilys.cdb.service;

import java.io.Serializable;
import java.util.List;

import com.excilys.cdb.exception.ServiceException;
import com.excilys.cdb.page.Page;

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
	public default List<T> getAll(Page<T,I> page) throws ServiceException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Return a limited list of instance from the database.
	 * Filtered according to a name (the instance should begin with the filter value)
	 * Ordered according to given columns. In an ascending or descending order.
	 * @return
	 */
	public default List<T> getSome(Page<T,I> page) throws ServiceException {
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
	 * Return the number of instance in the database.
	 * @return
	 */
	public default Long getNbInstance(Page<T,I> page) throws ServiceException {
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
