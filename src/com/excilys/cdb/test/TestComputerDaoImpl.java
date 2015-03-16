package com.excilys.cdb.test;

import java.util.ArrayList;

import org.junit.Test;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ComputerDaoImpl;
import com.excilys.cdb.persistence.IDao;

public class TestComputerDaoImpl {

	@Test
	public void test() {
		IDao<Computer> comp = new ComputerDaoImpl();
		ArrayList<Computer> list = (ArrayList<Computer>) comp.getAll();
		System.out.println(list);
		
		Computer computer = comp.getOne(list.get(list.size()-1).getId());
		System.out.println(computer);
		
		comp.deleteOne(computer.getId());

		System.out.println(computer.getId());
		System.out.println(comp.getOne(computer.getId()));
		comp.saveOne(computer);
		System.out.println(computer.getId());
		System.out.println(comp.getOne(computer.getId()));
		computer.setName(computer.getName()+"_");
		comp.saveOne(computer);
		System.out.println(computer);
		System.out.println(comp.getOne(computer.getId()));
		
	}

}
