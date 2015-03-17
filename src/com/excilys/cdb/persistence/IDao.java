package com.excilys.cdb.persistence;

import java.io.Serializable;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public interface IDao <T, I extends Serializable> {
	
	public default List<T> getAll() throws DaoException {
		throw new NotImplementedException();
	}
	
	public default T getById(I id)  throws DaoException {
		throw new NotImplementedException();
	}
	
	public default void save(T c)  throws DaoException {
		throw new NotImplementedException();
	}
	
	public default void delete(I id)  throws DaoException {
		throw new NotImplementedException();
	}
}
