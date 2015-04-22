package com.excilys.cdb.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.page.Page;
import com.excilys.cdb.page.mapper.IPageMapper;
import com.excilys.cdb.persistence.dao.IDao;
import com.excilys.cdb.persistence.dao.impl.ComputerDao;
import com.excilys.cdb.service.IService;

@Service
public class CompaniesService implements IService<Company,Long>{

	@Autowired @Qualifier("companyDao")
	private IDao<Company,Long> dao;

	@Autowired
	private IPageMapper<Company,Long> companyPageMapper;
	
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
	public List<Company> getAll(Page<Company,Long>  page) {
			return dao.getAll(companyPageMapper.pageToDaoRequestParameter(page));
	}
	
	/**
	 * Return a limited list of companies from the database.
	 * Filtered according to a name (the instance should begin with the filter value)
	 * Ordered according to given columns. In an ascending or descending order.
	 * @return
	 */
	@Override
	public List<Company> getSome(Page<Company,Long>  page) {
		return dao.getSome(companyPageMapper.pageToDaoRequestParameter(page));
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
	public Long getNbInstance(Page<Company,Long>  page) {
		return dao.getNb(companyPageMapper.pageToDaoRequestParameter(page));
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
