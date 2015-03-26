package com.excilys.cdb.ui.util;

import java.io.Serializable;
import java.util.List;

import com.excilys.cdb.persistence.DaoRequestParameter;
import com.excilys.cdb.service.IService;

public abstract class Page <E,D, I extends Serializable> {

	protected IService<E, I> serv;
	protected List<D> elements;
	protected Long current;
	protected Long pageSize;
	protected Long count;
	protected Long nbElements;
	protected DaoRequestParameter param;
	
	public Page(IService<E, I> serv){
		this(serv,null);
	}
	
	public Page(IService<E, I> serv, Long pageSize){
		this(serv,pageSize,null);
	}
	
	public Page(IService<E, I> serv, Long pageSize, DaoRequestParameter param){
		if ((serv == null)|| (pageSize == null) || (pageSize < 1)) {
			throw new IllegalArgumentException();
		}
		this.serv = serv;
		current = 1l;
		this.pageSize = pageSize;
		if (this.pageSize == null) {
			this.pageSize = 20l;
		}
		nbElements = serv.getNbInstance(param);
		if (this.pageSize < nbElements) {
			count = nbElements / pageSize;
			if (nbElements % pageSize != 0) {
				++count;
			}
		} else {
			this.pageSize = nbElements;
			count = 1l;
		}
		if (param == null) {
			this.param = new DaoRequestParameter (null, null, null,null,this.pageSize,0l);
		} else {
			this.param = param;
		}
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
	
	public Long getNbElements() {
		return nbElements;
	}
	
	public abstract void setFilter(String filter);
	
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
}
