package com.excilys.cdb.persistence;

import java.io.Serializable;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public interface IDao <T, I extends Serializable> {
	
	public static enum Order {
		ASC,DESC;
	}
	
	/**
	 * Return a List of the instances within the database.
	 * @return
	 * @throws DaoException
	 */
	public default List<T> getAll() throws DaoException {
		throw new NotImplementedException();
	}
	
	/**
	 * Return a List of the instances within the database.
	 * Limited to a given number, order in an ascending or descending order.
	 * 
	 * @param orderByCol 
	 * 				Label(s) to base the ordering. Primary Key By default.
	 * @param order 
	 * 				ASCending or DESCending
	 * @param limit 
	 * 				Number max of instance returned
	 * @param offset
	 * 				Offset
	 * @return		List<T>
	 * @throws DaoException
	 */
	public default List<T> getSome(List<String> orderByCol,
			Order order, Long limit, Long offset)  throws DaoException {
		throw new NotImplementedException();
	}
	
	/**
	 * Return the number of instances within the database.
	 * @return
	 * @throws DaoException
	 */
	public default Long getNb()  throws DaoException {
		throw new NotImplementedException();
	}
	
	/**
	 * Return a specific instance base on the primary key that define it.
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public default T getById(I id)  throws DaoException {
		throw new NotImplementedException();
	}
	
	/**
	 * Save an instance in the database.
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public default void save(T c)  throws DaoException {
		throw new NotImplementedException();
	}
	
	/**
	 * Delete an instance from the database.
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public default void delete(I id)  throws DaoException {
		throw new NotImplementedException();
	}
}
