package com.excilys.cdb.persistence;

import java.io.Serializable;
import java.util.List;

import com.excilys.cdb.exception.DaoException;

public interface IDao <T, I extends Serializable> {
	
	/**
	 * Return a List of the instances within the database.
	 * @return
	 * @throws DaoException
	 */
	public default List<T> getAll() throws DaoException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Return a List of the instances within the database.
	 * Ordered according to the given columns in an ascending or descending manner.
	 * 
	 * @param orderByCol 
	 * 				Label(s) to base the ordering. Primary Key By default.
	 * @param order 
	 * 				ASCending or DESCending
	 * @return		List<T>
	 * @throws DaoException
	 */
	public default List<T> getAll( DaoRequestParameter param)  throws DaoException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Return the number of instances within the database.
	 * @return
	 * @throws DaoException
	 */
	public default Long getNb()  throws DaoException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Return the number of instances within the database.
	 * @return
	 * @throws DaoException
	 */
	public default Long getNb( DaoRequestParameter param)  throws DaoException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Return a specific instance base on the primary key that define it.
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public default T getById( I id)  throws DaoException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Save an instance in the database.
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public default void save( T c)  throws DaoException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Delete an instance from the database.
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public default void delete( I id)  throws DaoException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return a List of the instances within the database.
	 * Instances should have an attribute name and result will be filtered according to the name given.
	 * (every instance that begin with the given string)
	 * Ordered according to the given columns. Limited to a given number. 
	 * Ordered in an ascending or descending order.  With a given offset.
	 * 
	 * @param param
	 * 				All the parameters related to the request.
	 * @throws DaoException
	 */
	public default List<T> getSome( DaoRequestParameter param) throws DaoException {
		throw new UnsupportedOperationException();
	}
}
