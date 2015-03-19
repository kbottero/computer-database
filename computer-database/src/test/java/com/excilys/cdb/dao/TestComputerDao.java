package com.excilys.cdb.dao;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

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
		
//		System.out.println(list);
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
	public void getSome() {
		ArrayList<Computer> list = (ArrayList<Computer>) ComputerDao.INSTANCE.getSome(null, null, null, null);
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
