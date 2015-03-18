package com.excilys.cdb.dao;

import java.util.ArrayList;

import org.junit.Test;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ComputerDao;

public class TestComputerDao {

	@Test
	public void test() {
		ArrayList<Computer> list = (ArrayList<Computer>) ComputerDao.INSTANCE.getAll();
		System.out.println(list);
		
		Computer computer = ComputerDao.INSTANCE.getById(list.get(list.size()-1).getId());
		System.out.println(computer);
		
		ComputerDao.INSTANCE.delete(computer.getId());

		System.out.println(computer.getId());
		System.out.println(ComputerDao.INSTANCE.getById(computer.getId()));
		ComputerDao.INSTANCE.save(computer);
		System.out.println(computer.getId());
		System.out.println(ComputerDao.INSTANCE.getById(computer.getId()));
		computer.setName(computer.getName()+"_");
		ComputerDao.INSTANCE.save(computer);
		System.out.println(computer);
		System.out.println(ComputerDao.INSTANCE.getById(computer.getId()));
		
	}

}
