package com.excilys.cdb.ui.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.IService;

public class CompanyPage extends Page<Company, Long> {
	
	private static Logger logger = LoggerFactory.getLogger(CompanyPage.class);

	protected IService<Company,Long> companiesService;
	
	private List<Company> elements;

	
	public CompanyPage(IService<Company,Long> companiesService, Integer nbElemPerPage) {
		super(nbElemPerPage);
		this.companiesService = companiesService;
		nbElements = companiesService.getNbInstance(this).intValue();
		if (this.nbElemPerPage < nbElements) {
			numberOfPages = nbElements / nbElemPerPage;
			if (nbElements % nbElemPerPage != 0) {
				++numberOfPages;
			}
		} else {
			this.nbElemPerPage = nbElements;
			numberOfPages = 1;
		}
		elements = companiesService.getSome(this);
	}
	
	@Override
	public List<Company> getElements () {
		return elements;
	}
	
	/**
	 * Display next page
	 * @return true if there is still pages to display, 0 else
	 */
	@Override
	public boolean nextPage() {
		if (pageNumber < numberOfPages) {
			++pageNumber;
			offset = (pageNumber-1)*nbElemPerPage;
			elements  = companiesService.getSome(this);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean previousPage() {
		if (pageNumber != 1) {
			--pageNumber;
			offset = (pageNumber-1)*nbElemPerPage;
			elements  = companiesService.getSome(this);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean goToPage(Integer numPage) {
		if (numPage > 0 && numPage < numberOfPages+1) {
			pageNumber = numPage;
			offset = (pageNumber-1)*nbElemPerPage;
			elements  = companiesService.getSome(this);
			return true;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}
}
