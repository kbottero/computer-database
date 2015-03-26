package com.excilys.cdb.ui.util;

import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.DaoRequestParameter;
import com.excilys.cdb.service.IService;

public class CompanyPage extends Page<Company, CompanyDTO, Long> {
	
	public CompanyPage(IService<Company,Long> serv, Long pageSize,  DaoRequestParameter param) {
		super(serv,pageSize, param);
		List<Company> list = serv.getSome(this.param);
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
			param.setOffset((current-1)*pageSize);
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
			param.setOffset((current-1)*pageSize);
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
			param.setOffset((current-1)*pageSize);
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
		param.setNameLike(filter);
		param.setOffset((current-1)*pageSize);
		List<Company> list = serv.getSome(param);
		elements = new ArrayList<CompanyDTO>();
		for ( Company comp : list) {
			elements.add(CompanyMapper.INSTANCE.toDTO(comp));
		}
	}
}
