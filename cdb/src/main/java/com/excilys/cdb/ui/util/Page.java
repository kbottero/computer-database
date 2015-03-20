package com.excilys.cdb.ui.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.IDao;
import com.excilys.cdb.service.IService;

public abstract class Page <E, I extends Serializable> {

	protected IService<E, I> serv;
	protected List<E> elements;
	protected Long currPage;
	protected Long pageSize;
	protected Long size;
	protected Long nbElements;
	protected IDao.Order sortingOrder; 
	protected ArrayList<String> orderBy; 

	public Page(IService<E, I> serv){
		this.serv = serv;
		nbElements = serv.getNbInstance();
		currPage = 0l;
		pageSize = 20l;
		size = nbElements / pageSize;
		if (nbElements % size != 0) {
			++size;
		}
		sortingOrder = IDao.Order.ASC;
		orderBy = new ArrayList<String>();
	}
	
	public Page(IService<E, I> serv, Long pageSize){
		this.serv = serv;
		currPage = 0l;
		nbElements = serv.getNbInstance();
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
	}
	
	public void setSortingOrder(IDao.Order so) {
		sortingOrder = so;
	}
	
	public IDao.Order getSortingOrder() {
		return sortingOrder;
	}
	
	public Long getCurrPage() {
		return currPage;
	}

	public Long getSize() {
		return size;
	}
	
	public List<E> getElements() {
		return elements;
	}
	
	/**
	 * Display next page
	 * @return true if there is still pages to display, 0 else
	 */
	public boolean nextPage() {
		if (currPage != size) {
			elements = serv.getSome(orderBy, sortingOrder, pageSize, currPage*pageSize);
			++currPage;
			return true;
		} else {
			return false;
		}
	}
}
