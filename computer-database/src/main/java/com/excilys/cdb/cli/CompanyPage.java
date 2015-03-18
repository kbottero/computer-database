package com.excilys.cdb.cli;

import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.IDao;
import com.excilys.cdb.service.IService;

public class CompanyPage extends Page<Company>{

	public CompanyPage(IService serv) {
		super(serv);
		nbElements = serv.getNbCompany();
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
	
	public CompanyPage(IService serv, Long pageSize) {
		super(serv, pageSize);
		currPage = 0l;
		nbElements = serv.getNbCompany();
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
			elements = (List<Company>) serv.getSomeCompany(orderBy, sortingOrder, pageSize, currPage*pageSize);
			for (Company company  : elements) {
				CliCommands.printCompany(company);
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