package com.excilys.cdb.service;

import java.util.Collection;
import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.IDao.Order;

public interface IService {
	
	/**
	 * Return a list of all the computers in the database.
	 * @return
	 */
	public Collection<Computer> getAllComputer();

	/**
	 * Return a limited list of computers from the database.
	 * @return
	 */
	public Collection<Computer> getSomeComputer(List<String> orderByCol, Order order, Long limit, Long offset );

	/**
	 * Return the number of computer in the database.
	 * @return
	 */
	public Long getNbComputer();
	
	/**
	 * Return a specific Computer from the database, based on its id.
	 * @param id
	 * @return
	 */
	public Computer getOneComputer(long id);
	
	/**
	 * Save one Computer in the database.
	 * @param id
	 * @return
	 */
	public void saveOneComputer(Computer c);
	
	/**
	 * Delete a Computer in the database.
	 * @param id
	 * @return
	 */
	public void deleteOneComputer(long id);
	
	/**
	 * Return a list of all companies in the database.
	 * @return
	 */
	public Collection<Company> getAllCompany();

	/**
	 * Return a limited list of companies from the database.
	 * @return
	 */
	public Collection<Company> getSomeCompany(List<String> orderByCol, Order order, Long limit, Long offset );

	/**
	 * Return the number of companies in the database.
	 * @return
	 */
	public Long getNbCompany();
	
	/**
	 * Return a limited list of companies from the database.
	 * @return
	 */
	public Company getOneCompany(long id);
	
}
