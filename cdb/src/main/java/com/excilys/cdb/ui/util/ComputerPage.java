package com.excilys.cdb.ui.util;

import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.IService;

public class ComputerPage extends Page<Computer,ComputerDTO, Long> {
	
	public ComputerPage(IService<Computer,Long> serv) {
		super(serv);
		orderBy.add(CompanyMapper.DEFAULT_ID);
		
		List<Computer> list = serv.getSome(pageSize, (current-1)*pageSize,orderBy, sortingOrder);
		elements = new ArrayList<ComputerDTO>();
		for ( Computer comp : list) {
			elements.add(ComputerMapper.INSTANCE.toDTO(comp));
		}
		
	}
	
	public ComputerPage(IService<Computer,Long> serv, Long pageSize) {
		super(serv,pageSize);
		orderBy.add(CompanyMapper.DEFAULT_ID);
		
		List<Computer> list = serv.getSome(pageSize, (current-1)*pageSize,orderBy, sortingOrder);
		elements = new ArrayList<ComputerDTO>();
		for ( Computer comp : list) {
			elements.add(ComputerMapper.INSTANCE.toDTO(comp));
		}
	}	
	

	public ComputerPage(IService<Computer,Long> serv, String filter) {
		super(serv);
		orderBy.add(CompanyMapper.DEFAULT_ID);
		this.filter = filter;
		List<Computer> list = serv.getSome(filter, pageSize, (current-1)*pageSize,orderBy, sortingOrder);
		elements = new ArrayList<ComputerDTO>();
		for ( Computer comp : list) {
			elements.add(ComputerMapper.INSTANCE.toDTO(comp));
		}
	}
	
	public ComputerPage(IService<Computer,Long> serv, Long pageSize,  String filter) {
		super(serv, pageSize);
		orderBy.add(CompanyMapper.DEFAULT_ID);
		this.filter = filter;
		this.pageSize = pageSize;
		List<Computer> list = serv.getSome(filter, pageSize, (current-1)*pageSize,orderBy, sortingOrder);
		elements = new ArrayList<ComputerDTO>();
		for ( Computer comp : list) {
			elements.add(ComputerMapper.INSTANCE.toDTO(comp));
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
			List<Computer> list = serv.getSome(filter, pageSize, (current-1)*pageSize,orderBy, sortingOrder);
			elements = new ArrayList<ComputerDTO>();
			for ( Computer comp : list) {
				elements.add(ComputerMapper.INSTANCE.toDTO(comp));
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean previousPage() {
		if (current > 1) {
			--current;
			List<Computer> list = serv.getSome(filter, pageSize, (current-1)*pageSize,orderBy, sortingOrder);
			elements = new ArrayList<ComputerDTO>();
			for ( Computer comp : list) {
				elements.add(ComputerMapper.INSTANCE.toDTO(comp));
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
			List<Computer> list = serv.getSome(filter, pageSize, (current-1)*pageSize,orderBy, sortingOrder);
			elements = new ArrayList<ComputerDTO>();
			for ( Computer comp : list) {
				elements.add(ComputerMapper.INSTANCE.toDTO(comp));
			}
			return true;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}
	
	@Override
	public void setFilter (String filter) {
		this.filter = filter;
		List<Computer> list = serv.getSome(filter, pageSize, (current-1)*pageSize,orderBy, sortingOrder);
		elements = new ArrayList<ComputerDTO>();
		for ( Computer comp : list) {
			elements.add(ComputerMapper.INSTANCE.toDTO(comp));
		}
	}
}
