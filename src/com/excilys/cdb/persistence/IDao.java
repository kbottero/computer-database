package com.excilys.cdb.persistence;

import java.util.List;

public interface IDao <T> {
	
	public List<T> getAll();
	
	public T getOne(long id);
	
	public void saveOne(T c);
	
	public void deleteOne(long id);
}
