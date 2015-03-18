package com.excilys.cdb.cli;

import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.persistence.IDao;
import com.excilys.cdb.service.IService;

public abstract class Page <E> {

	protected IService serv;
	protected List<E> elements;
	protected Long currPage;
	protected Long pageSize;
	protected Long size;
	protected Long nbElements;
	protected IDao.Order sortingOrder; 
	protected ArrayList<String> orderBy; 

	public Page(IService serv){
		this.serv = serv;
	}
	
	public Page(IService serv, Long pageSize){
		this.serv = serv;
	}
	
	public void setSortingOrder (IDao.Order so) {
		sortingOrder = so;
	}
	
	public IDao.Order getSortingOrder () {
		return sortingOrder;
	}
	
	/**
	 * Display next page
	 * @return true if there is still pages to display, 0 else
	 */
	public abstract boolean nextPage();
	
	protected void printEndOfPage () {
		StringBuilder stgBuild = new StringBuilder();
		stgBuild.append("Page ");
		stgBuild.append(currPage+1);
		stgBuild.append("/");
		stgBuild.append(size);
		stgBuild.append("\t (enter '");
		stgBuild.append(CliCommands.quitPages);
		stgBuild.append("' to quit)");
		System.out.println(stgBuild.toString());
	}
}
