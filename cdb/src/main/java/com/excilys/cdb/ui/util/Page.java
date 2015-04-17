package com.excilys.cdb.ui.util;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public abstract class Page <E, I extends Serializable> implements Pageable {

	protected Integer pageNumber;
	protected Integer nbElemPerPage;
	protected Integer numberOfPages;
	protected Integer nbElements;
	protected Integer offset;
	protected Sort sort;
	protected String search;
	
	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public Page(){
		pageNumber = 1;
	}
	
	public Page(Integer nbElemPerPage){
		pageNumber = 1;
		this.nbElemPerPage = nbElemPerPage;
		if (this.nbElemPerPage == null) {
			this.nbElemPerPage = 20;
		}
	}

	public Integer getNumberOfPages() {
		return numberOfPages;
	}
	
	public Integer getNbElements() {
		return nbElements;
	}

	public int getNbElemPerPage() {
		return nbElemPerPage.intValue();
	}
	
	public void setNbElemPerPage(Integer nbElemPerPage) {
		this.nbElemPerPage = nbElemPerPage;
		if (pageNumber != null && pageNumber > 0) {
			this.offset = (pageNumber-1)*nbElemPerPage;
		}
	}
	
	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public int getPageNumber() {
		return pageNumber;
	}

	@Override
	public boolean hasPrevious() {
		if (numberOfPages > 1) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Pageable first() {
		goToPage(1);
		return this;
	}

	@Override
	public Sort getSort() {
		return this.sort;
	}
	
	public void setSort(Sort sort) {
		this.sort = sort;
	}

	@Override
	public Pageable next() {
		nextPage();
		return this;
	}

	@Override
	public Pageable previousOrFirst() {
		previousPage();
		return this;
	}
	
	@Override
	public int getPageSize() {
		return this.nbElemPerPage;
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
	public abstract boolean goToPage(Integer numPage);
	
	public abstract List<E> getElements();
}
