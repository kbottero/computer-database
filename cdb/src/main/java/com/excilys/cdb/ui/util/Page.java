package com.excilys.cdb.ui.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.persistence.IDao;
import com.excilys.cdb.service.IService;

public abstract class Page <E,D, I extends Serializable> {

	protected IService<E, I> serv;
	protected List<D> elements;
	protected Long current;
	protected Long pageSize;
	protected Long count;
	protected Long nbElements;
	protected IDao.Order sortingOrder; 
	protected ArrayList<String> orderBy; 

	public Page(IService<E, I> serv){
		if (serv == null) {
			throw new IllegalArgumentException();
		}
		this.serv = serv;
		nbElements = serv.getNbInstance();
		current = 1l;
		pageSize = 20l;
		count = nbElements / pageSize;
		if (nbElements % count != 0) {
			++count;
		}
		sortingOrder = IDao.Order.ASC;
		orderBy = new ArrayList<String>();
	}
	
	public Page(IService<E, I> serv, Long pageSize){
		if ((serv == null)|| (pageSize == null) || (pageSize < 1)) {
			throw new IllegalArgumentException();
		}
		this.serv = serv;
		current = 1l;
		nbElements = serv.getNbInstance();
		if (pageSize < nbElements) {
			this.pageSize = pageSize;
			count = nbElements / pageSize;
			if (nbElements % count != 0) {
				++count;
			}
		} else {
			this.pageSize = nbElements;
			count = 1l;
		}
		sortingOrder = IDao.Order.ASC;
		orderBy = new ArrayList<String>();
	}
	
	public void setSortingOrder(IDao.Order so) {
		if (so == null) {
			throw new IllegalArgumentException();
		}
		sortingOrder = so;
	}
	
	public IDao.Order getSortingOrder() {
		return sortingOrder;
	}
	
	public Long getCurrent() {
		return current;
	}

	public Long getCount() {
		return count;
	}
	
	public List<D> getElements() {
		return elements;
	}
	
	/**
	 * Display next page
	 * @return true if there is still pages to display, 0 else
	 */
	public abstract boolean nextPage();

	/**
	 * Display previous page
	 * @return true if there is still pages to display, 0 else
	 */
	public abstract boolean previousPage();
	
	/**
	 * Go to a given page 
	 * @return true if there is still pages to display, 0 else
	 */
	public abstract boolean goToPage(Long numPage);
	
//	
//	/**
//	 * Display next page
//	 * @return true if there is still pages to display, 0 else
//	 */
//	public boolean nextPage() {
//		if (currPage != size) {
//			elements = serv.getSome(pageSize, currPage*pageSize,orderBy, sortingOrder);
//			++currPage;
//			return true;
//		} else {
//			return false;
//		}
//	}
}
