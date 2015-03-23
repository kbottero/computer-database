package com.excilys.cdb.persistence;

import java.io.Serializable;
import java.util.List;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.model.Computer;

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
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Return a List of the instances within the database.
	 * Ordered according to the given columns in an ascending manner.
	 * 
	 * @param orderByCol 
	 * 				Label(s) to base the ordering. Primary Key By default.
	 * @return		List<T>
	 * @throws DaoException
	 */
	public default List<T> getAll(List<String> orderByCol)  throws DaoException {
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
	public default List<T> getAll(List<String> orderByCol,
			Order order)  throws DaoException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Return a List of the instances within the database.
	 * Ordered according to the primary_key. Limited to a given number. 
	 * Ordered in an ascending order.  With a given offset.
	 * 
	 * @param limit 
	 * 				Number max of instance returned
	 * @param offset
	 * 				Offset
	 * @return		List<T>
	 * @throws DaoException
	 */
	public default List<T> getSome(Long limit, Long offset)  throws DaoException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Return a List of the instances within the database.
	 * Ordered according to the given columns. Limited to a given number. 
	 * Ordered in an ascending order.  With a given offset.
	 * 
	 * @param limit 
	 * 				Number max of instance returned
	 * @param offset
	 * 				Offset
	 * @param orderByCol 
	 * 				Label(s) to base the ordering. Primary Key By default.
	 * @return		List<T>
	 * @throws DaoException
	 */
	public default List<T> getSome(Long limit, Long offset, List<String> orderByCol)  throws DaoException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Return a List of the instances within the database.
	 * Ordered according to the given columns. Limited to a given number. 
	 * Ordered in an ascending or descending order.  With a given offset.
	 * 
	 * @param limit 
	 * 				Number max of instance returned
	 * @param offset
	 * 				Offset
	 * @param orderByCol 
	 * 				Label(s) to base the ordering. Primary Key By default.
	 * @param order 
	 * 				ASCending or DESCending
	 * @return		List<T>
	 * @throws DaoException
	 */
	public default List<T> getSome(Long limit, Long offset, List<String> orderByCol,
			Order order)  throws DaoException {
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
	 * Return a specific instance base on the primary key that define it.
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public default T getById(I id)  throws DaoException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Save an instance in the database.
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public default void save(T c)  throws DaoException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Delete an instance from the database.
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public default void delete(I id)  throws DaoException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return a List of the instances within the database.
	 * Instances should have an attribute name and result will be filtered according to the name given.
	 * (every instance that begin with the given string)
	 * Ordered according to the given columns. Limited to a given number. 
	 * Ordered in an ascending order.
	 * 
	 * @param nameFilter 
	 * 				Beginning of the instance name to return
	 * @param limit 
	 * 				Number max of instance returned
	 * @param offset
	 * 				Offset
	 * @return		List<T>
	 * @throws DaoException
	 */
	public default List<T> getSome(String nameFilter, Long limit, Long offset)
			throws DaoException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return a List of the instances within the database.
	 * Instances should have an attribute name and result will be filtered according to the name given.
	 * (every instance that begin with the given string)
	 * Ordered according to the given columns. Limited to a given number. 
	 * Ordered in an ascending order.  With a given offset.
	 * 
	 * @param nameFilter 
	 * 				Beginning of the instance name to return
	 * @param limit 
	 * 				Number max of instance returned
	 * @param offset
	 * 				Offset
	 * @param orderByCol 
	 * 				Label(s) to base the ordering. Primary Key By default.
	 * @return		List<T>
	 * @throws DaoException
	 */
	public default List<T> getSome(String nameFilter, Long limit, Long offset,
			List<String> orderByCol) throws DaoException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return a List of the instances within the database.
	 * Instances should have an attribute name and result will be filtered according to the name given.
	 * (every instance that begin with the given string)
	 * Ordered according to the given columns. Limited to a given number. 
	 * Ordered in an ascending or descending order.  With a given offset.
	 * 
	 * @param nameFilter 
	 * 				Beginning of the instance name to return
	 * @param limit 
	 * 				Number max of instance returned
	 * @param offset
	 * 				Offset
	 * @param orderByCol 
	 * 				Label(s) to base the ordering. Primary Key By default.
	 * @param order 
	 * 				ASCending or DESCending
	 * @return		List<T>
	 * @throws DaoException
	 */
	public default List<T> getSome(String nameFilter, Long limit, Long offset,
			List<String> orderByCol, Order order) throws DaoException {
		throw new UnsupportedOperationException();
	}
}
