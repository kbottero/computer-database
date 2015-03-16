package com.excilys.cdb.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDaoImpl;
import com.excilys.cdb.persistence.IDao;

public class TestCompanyDaoImpl {

	@Test
	public void test() {
		IDao<Company> comp = new CompanyDaoImpl();
		ArrayList<Company> list = (ArrayList<Company>) comp.getAll();
		System.out.println(list);
		
		//Company company = comp.getOne(list.get(list.size()-1).getId());
		Company company = comp.getOne(1);
		assertNotNull(company);
		if (company != null) {
			System.out.println(company);
//			
//			comp.deleteOne(company.getId());
//	
//			System.out.println(company.getId());
//			System.out.println(comp.getOne(company.getId()));
//			comp.saveOne(company);
//			System.out.println(company.getId());
//			System.out.println(comp.getOne(company.getId()));
//			company.setName(company.getName()+"_");
//			comp.saveOne(company);
//			System.out.println(company);
//			System.out.println(comp.getOne(company.getId()));
		}
	}



}
