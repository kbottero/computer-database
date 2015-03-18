package com.excilys.cdb.persistence;

import java.io.Serializable;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public interface IDao <T, I extends Serializable> {
	
	public static enum Order {
		ASC,DESC;
	}
	
	public default List<T> getAll() throws DaoException {
		throw new NotImplementedException();
	}
	
	public default List<T> getSome(List<String> orderByCol,
			Order order, Long limit, Long offset)  throws DaoException {
		throw new NotImplementedException();
	}
	
	public default Long getNb()  throws DaoException {
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
