package com.excilys.cdb.persistence.dao;

import java.io.Serializable;
import java.util.List;

import com.excilys.cdb.persistence.exception.DaoException;
import com.excilys.cdb.persistence.util.DaoRequestParameter;

public interface IDao <T, I extends Serializable> {
	
	/**
	 * Return a List of the instances within the database.
	 * @return List of all instances
	 */
	public default List<T> getAll() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Return a List of the instances within the database.
	 * Ordered according to the given parameters.
	 * 
	 * @param param Parameters related to the research.
	 * @return	List<T> of all instances that responds to the parameter of the research.
	 * @throws DaoException if param is null or contains invalid values.
	 */
	public default List<T> getAll( DaoRequestParameter param) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Return the number of instances within the database.
	 * @return
	 * @throws DaoException if we can't count instances.
	 */
	public default Long getNb() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Return the number of instances within the database.
	 * @return Number of instances
	 * @throws DaoException if we can't count instances or param is null or contains invalid values.
	 */
	public default Long getNb( DaoRequestParameter param) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Return a specific instance base on the primary key that define it.
	 * An instance with the given id must exist in the database, otherwise
	 * an exception is throw.
	 * @param id of the researched instance
	 * @return Instance with the given id. 
	 * @throws DaoException id is null or invalid.
	 */
	public default T getById( I id) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Save an instance in the database.
	 * @param <T> Instance to save
	 * @throws DaoException if the entities is not valid.
	 */
	public default void save( T c) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Delete an instance from the database.
	 * @param id of the instance to delete
	 * @throws DaoException if id is invalid or the entity can not be deleted.
	 */
	public default void delete( I id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return a List of the instances within the database.
	 * Instances should have an attribute name and result will be filtered according to the given parameters.
	 * 
	 * @param param
	 * 				All the parameters related to the request.
	 * @return List<T> of the instance that respond to the parameters
	 * @throws DaoException if param is null or contains invalid values.
	 */
	public default List<T> getSome( DaoRequestParameter param) {
		throw new UnsupportedOperationException();
	}
}
