package com.excilys.cdb.cli;

import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.IDao;
import com.excilys.cdb.service.IService;

public class ComputerPage extends Page<Computer>{
	
	public ComputerPage(IService serv) {
		super(serv);
		nbElements = serv.getNbComputer();
		currPage = 0l;
		pageSize = 20l;
		size = nbElements / pageSize;
		if (nbElements % size != 0) {
			++size;
		}
		sortingOrder = IDao.Order.ASC;
		orderBy = new ArrayList<String>();
		orderBy.add("id");
	}
	
	public ComputerPage(IService serv, Long pageSize) {
		super(serv,pageSize);
		currPage = 0l;
		nbElements = serv.getNbComputer();
		if (pageSize < nbElements) {
			this.pageSize = pageSize;
			size = nbElements / pageSize;
			if (nbElements % size != 0) {
				++size;
			}
		} else {
			this.pageSize = nbElements;
			size = 1l;
		}
		sortingOrder = IDao.Order.ASC;
		orderBy = new ArrayList<String>();
		orderBy.add("id");
	}

	@Override
	public boolean nextPage() {
		if (currPage != size) {
			elements = (List<Computer>) serv.getSomeComputer(orderBy, sortingOrder, pageSize, currPage*pageSize);
			for (Computer computer  : elements) {
				CliCommands.printComputer(computer);
			}
			printEndOfPage();
			++currPage;
			if (currPage == size ) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
}
