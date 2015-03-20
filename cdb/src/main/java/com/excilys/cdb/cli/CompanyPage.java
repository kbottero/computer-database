package com.excilys.cdb.cli;

import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.IDao;
import com.excilys.cdb.service.IService;

public class CompanyPage extends Page<Company>{
	
	private ArrayList<String> orderBy;

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
		orderBy.add(ComputerMapper.DEFAULT_ID);
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
		orderBy.add(ComputerMapper.DEFAULT_ID);
	}

	@Override
	public boolean nextPage() {
		if (currPage != size) {
			elements = (List<Company>) serv.getSomeCompany(orderBy, sortingOrder, pageSize, currPage*pageSize);
			++currPage;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Company> getElements() {
		return elements;
	}
}
