package com.excilys.cdb.dao;

import java.io.FileInputStream;
import java.util.ArrayList;

import org.dbunit.DBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ComputerDao;

/**
 * TODO: Complete with DbUnit
 * @author Kevin Bottero
 *
 */
public class TestComputerDao extends DBTestCase {
	
	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/resources/dbTest.xml"));
	}

	@Before
	public void setUp() {
		
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void testGetAll() {
		ArrayList<Computer> list = (ArrayList<Computer>) ComputerDao.INSTANCE.getAll();
		assertNotNull(list);
		assertNotNull(list.size());
		
//		System.out.println(list);
//		
//		
//		Computer computer = ComputerDao.INSTANCE.getById(list.get(list.size()-1).getId());
//		System.out.println(computer);
//		
//		ComputerDao.INSTANCE.delete(computer.getId());
//
//		System.out.println(computer.getId());
//		System.out.println(ComputerDao.INSTANCE.getById(computer.getId()));
//		ComputerDao.INSTANCE.save(computer);
//		System.out.println(computer.getId());
//		System.out.println(ComputerDao.INSTANCE.getById(computer.getId()));
//		computer.setName(computer.getName()+"_");
//		ComputerDao.INSTANCE.save(computer);
//		System.out.println(computer);
//		System.out.println(ComputerDao.INSTANCE.getById(computer.getId()));
		
	}

	@Test
	public void testGetSome() {
		ArrayList<Computer> list = (ArrayList<Computer>) ComputerDao.INSTANCE.getSome(null, null, null, null);
		assertNotNull(list);
		assertNotNull(list.size());
	}
	@Test
	public void testGetNb() {
		assertNotNull(ComputerDao.INSTANCE.getNb());
	}
	
	@Test
	public void testGetById() {
		assertNotNull(ComputerDao.INSTANCE.getById(1l));
	}
	
	@Test
	public void testSave() {
		
	}
	
	@Test
	public void testDelete() {
		
	}



}
