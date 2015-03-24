package com.excilys.cdb.dao;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ComputerDao;
import com.excilys.cdb.persistence.DaoRequestParameter;

/**
 * TODO: Complete with DbUnit
 * @author Kevin Bottero
 *
 */
public class TestComputerDao {

	@Before
	public void setUp() {
		
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void getAll() {
		ArrayList<Computer> list = (ArrayList<Computer>) ComputerDao.INSTANCE.getAll();
		assertNotNull(list);
		assertNotNull(list.size());
	}

	@Test
	public void getSome() {
		Long l = null;
		ArrayList<Computer> list = (ArrayList<Computer>) ComputerDao.INSTANCE.getSome(new DaoRequestParameter(null, null, null, null, 10l, null));
		assertNotNull(list);
		assertNotNull(list.size());
		list = (ArrayList<Computer>) ComputerDao.INSTANCE.getSome(new DaoRequestParameter(null, "M", null, null, 10l, null));
		assertNotNull(list);
		assertNotNull(list.size());
	}
	@Test
	public void getNb() {
		assertNotNull(ComputerDao.INSTANCE.getNb());
	}
	
	@Test
	public void getById() {
		assertNotNull(ComputerDao.INSTANCE.getById(1l));
	}
	
	@Test
	public void save() {
		
	}
	
	@Test
	public void delete() {
		
	}



}
