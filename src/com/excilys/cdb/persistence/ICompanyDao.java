package com.excilys.cdb.persistence;

import java.util.Collection;

import com.excilys.cdb.model.Company;

public interface ICompanyDao {
	
	public Collection<Company> getAll();
	
	public Company getOne(long id);
//	
//	public void saveOne(Company c);
//	
//	public void deleteOne(long id);

}
