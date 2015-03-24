package com.excilys.cdb.ui.util;

import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.DaoRequestParameter;
import com.excilys.cdb.service.IService;

public class CompanyPage extends Page<Company, CompanyDTO, Long> {
	
	public CompanyPage(IService<Company,Long> serv) {
		super(serv);
		orderBy.add(ComputerMapper.DEFAULT_ID);
		
		DaoRequestParameter param = new DaoRequestParameter(null, null, orderBy, sortingOrder, pageSize, (current-1)*pageSize);
		List<Company> list = serv.getSome(param);
		elements = new ArrayList<CompanyDTO>();
		for ( Company comp : list) {
			elements.add(CompanyMapper.INSTANCE.toDTO(comp));
		}
	}
	
	public CompanyPage(IService<Company,Long> serv, Long pageSize) {
		super(serv, pageSize);
		orderBy.add(ComputerMapper.DEFAULT_ID);

		DaoRequestParameter param = new DaoRequestParameter(null, null, orderBy, sortingOrder, pageSize, (current-1)*pageSize);
		List<Company> list = serv.getSome(param);
		elements = new ArrayList<CompanyDTO>();
		for ( Company comp : list) {
			elements.add(CompanyMapper.INSTANCE.toDTO(comp));
		}
	}
	
	public CompanyPage(IService<Company,Long> serv, String filter) {
		super(serv);
		orderBy.add(ComputerMapper.DEFAULT_ID);
		this.filter = filter;
		DaoRequestParameter param = new DaoRequestParameter(DaoRequestParameter.NameFiltering.POST, filter, orderBy, sortingOrder, pageSize, (current-1)*pageSize);
		List<Company> list = serv.getSome(param);
		elements = new ArrayList<CompanyDTO>();
		for ( Company comp : list) {
			elements.add(CompanyMapper.INSTANCE.toDTO(comp));
		}
	}
	
	public CompanyPage(IService<Company,Long> serv, Long pageSize, String filter) {
		super(serv, pageSize);
		orderBy.add(ComputerMapper.DEFAULT_ID);
		this.filter = filter;
		this.pageSize = pageSize;
		DaoRequestParameter param = new DaoRequestParameter(DaoRequestParameter.NameFiltering.POST, filter, orderBy, sortingOrder, pageSize, (current-1)*pageSize);
		List<Company> list = serv.getSome(param);
		elements = new ArrayList<CompanyDTO>();
		for ( Company comp : list) {
			elements.add(CompanyMapper.INSTANCE.toDTO(comp));
		}
	}
	
	/**
	 * Display next page
	 * @return true if there is still pages to display, 0 else
	 */
	@Override
	public boolean nextPage() {
		if (current < count) {
			++current;
			DaoRequestParameter param = new DaoRequestParameter(DaoRequestParameter.NameFiltering.POST, filter, orderBy, sortingOrder, pageSize, (current-1)*pageSize);
			List<Company> list = serv.getSome(param);
			elements = new ArrayList<CompanyDTO>();
			for ( Company comp : list) {
				elements.add(CompanyMapper.INSTANCE.toDTO(comp));
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean previousPage() {
		if (current != 1) {
			--current;
			DaoRequestParameter param = new DaoRequestParameter(DaoRequestParameter.NameFiltering.POST, filter, orderBy, sortingOrder, pageSize, (current-1)*pageSize);
			List<Company> list = serv.getSome(param);
			elements = new ArrayList<CompanyDTO>();
			for ( Company comp : list) {
				elements.add(CompanyMapper.INSTANCE.toDTO(comp));
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean goToPage(Long numPage) {
		if (numPage > 0 && numPage < count+1) {
			current = numPage;
			DaoRequestParameter param = new DaoRequestParameter(DaoRequestParameter.NameFiltering.POST, filter, orderBy, sortingOrder, pageSize, (current-1)*pageSize);
			List<Company> list = serv.getSome(param);
			elements = new ArrayList<CompanyDTO>();
			for ( Company comp : list) {
				elements.add(CompanyMapper.INSTANCE.toDTO(comp));
			}
			return true;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}
	
	@Override
	public void setFilter (String filter) {
		this.filter = filter;
		DaoRequestParameter param = new DaoRequestParameter(DaoRequestParameter.NameFiltering.POST, filter, orderBy, sortingOrder, pageSize, (current-1)*pageSize);
		List<Company> list = serv.getSome(param);
		elements = new ArrayList<CompanyDTO>();
		for ( Company comp : list) {
			elements.add(CompanyMapper.INSTANCE.toDTO(comp));
		}
	}
}
