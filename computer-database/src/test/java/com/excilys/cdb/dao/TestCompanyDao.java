package com.excilys.cdb.dao;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDao;

public class TestCompanyDao {

	@Test
	public void test() {
		ArrayList<Company> list = (ArrayList<Company>) CompanyDao.INSTANCE.getAll();
		System.out.println(list);
		
		Company company = CompanyDao.INSTANCE.getById(1l);
		assertNotNull(company);
		if (company != null) {
			System.out.println(company);
		}
	}



}
