package com.excilys.cdb.dao;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.DaoRequestParameter;

/**
 * TODO: Complete with DbUnit
 * @author Kevin Bottero
 *
 */
public class TestCompanyDao {

	@Before
	public void setUp() {
			
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void getAll() {
		ArrayList<Company> list = (ArrayList<Company>) CompanyDao.INSTANCE.getAll();
		assertNotNull(list);
		assertNotNull(list.size());
	}
	
	@Test
	public void getSome() {
		Long l = null;
		ArrayList<Company> list = (ArrayList<Company>) CompanyDao.INSTANCE.getSome(new DaoRequestParameter(null,null, null, null, 10l, null));
		assertNotNull(list);
		assertNotNull(list.size());
	}
	
	@Test
	public void getNb() {
		assertNotNull(CompanyDao.INSTANCE.getNb());
	}
	
	@Test
	public void getById() {
		assertNotNull(CompanyDao.INSTANCE.getById(1l));
	}

}
