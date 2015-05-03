package com.excilys.cdb.page.impl;

import java.util.List;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.page.Page;
import com.excilys.cdb.service.IService;

public class ComputerPage extends Page<Computer, Long> {

	protected IService<Computer, Long> computersService;
	
	private List<Computer> elements;
	
	public ComputerPage(IService<Computer, Long> computersService, Integer nbElemPerPage) {
		super(nbElemPerPage);
		this.computersService = computersService;
		nbElements = computersService.getNbInstance(this).intValue();
		if (this.nbElemPerPage < nbElements) {
			numberOfPages = nbElements / nbElemPerPage;
			if (nbElements % nbElemPerPage != 0) {
				++numberOfPages;
			}
		} else {
			this.nbElemPerPage = nbElements;
			numberOfPages = 1;
		}
		elements = computersService.getSome(this);
	}
	
	@Override
	public List<Computer> getElements() {
		return elements;
	}
	
	/**
	 * Load next page
	 * @return true if there is still pages to load, 0 else
	 */
	@Override
	public boolean nextPage() {
		if (pageNumber < numberOfPages) {
			++pageNumber;
			offset = (pageNumber-1)*nbElemPerPage;
			elements = computersService.getSome(this);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Load previous page
	 * @return true if there is still pages to load, 0 else
	 */
	@Override
	public boolean previousPage() {
		if (pageNumber > 1) {
			--pageNumber;
			offset = (pageNumber-1)*nbElemPerPage;
			elements = computersService.getSome(this);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Load the given page
	 * @return
	 */
	@Override
	public boolean goToPage(Integer numPage) {
		if (numPage > 0 && numPage < numberOfPages+1) {
			pageNumber = numPage;
			offset = (pageNumber-1)*nbElemPerPage;
			elements = computersService.getSome(this);
			return true;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}
}
