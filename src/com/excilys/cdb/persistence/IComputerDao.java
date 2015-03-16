package com.excilys.cdb.persistence;

import java.util.Collection;

import com.excilys.cdb.model.Computer;

public interface IComputerDao {
	
	public Collection<Computer> getAll();
	
	public Computer getOne(long id);
	
	public void saveOne(Computer c);
	
	public void deleteOne(long id);

}
