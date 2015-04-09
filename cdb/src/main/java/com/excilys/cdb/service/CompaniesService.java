package com.excilys.cdb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.ComputerDao;
import com.excilys.cdb.persistence.DaoRequestParameter;
import com.excilys.cdb.persistence.IDao;

@Service
public class CompaniesService implements IService<Company,Long>{

	@Autowired @Qualifier("companyDao")
	private IDao<Company,Long> dao;
	
	/**
	 * Return a list of all companies in the database.
	 * @return
	 */
	@Override
	public List<Company> getAll() {
		return dao.getAll();
	}
	/**
	 * Return a list of all companies in the database.
	 * Ordered according to given columns. In an ascending or descending order.
	 * @return
	 */
	@Override
	public List<Company> getAll(DaoRequestParameter param) {
			return dao.getAll(param);
	}
	
	/**
	 * Return a limited list of companies from the database.
	 * Filtered according to a name (the instance should begin with the filter value)
	 * Ordered according to given columns. In an ascending or descending order.
	 * @return
	 */
	@Override
	public List<Company> getSome(DaoRequestParameter param) {
		return dao.getSome(param);
	}

	/**
	 * Return the number of companies in the database.
	 * @return
	 */
	@Override
	public Long getNbInstance() {
		return dao.getNb();
	}
	
	/**
	 * Return the number of companies in the database.
	 * @return
	 */
	@Override
	public Long getNbInstance(DaoRequestParameter param) {
		return dao.getNb(param);
	}
	
	/**
	 * Return a limited list of companies from the database.
	 * @return
	 */
	@Override
	public Company getOne(Long id) {
		return dao.getById(id);
	}
	
	/**
	 * Delete a Company in the database.
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor=RuntimeException.class)
	public void deleteOne(Long id){
		new ComputerDao().deleteByCompany(id);
		dao.delete(id);
	}
}
