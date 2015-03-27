package com.excilys.cdb.ui.util;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DaoRequestParameter;
import com.excilys.cdb.service.IService;

public class ComputerPage extends Page<Computer, Long> {

	
	public ComputerPage(IService<Computer,Long> serv, Long pageSize,  DaoRequestParameter param) {
		super(serv,pageSize, param);
		elements = serv.getSome(this.param);
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
			elements = serv.getSome(param);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean previousPage() {
		if (current > 1) {
			--current;
			param.setOffset((current-1)*pageSize);
			elements = serv.getSome(param);
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
			elements = serv.getSome(param);
			return true;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}
	
	@Override
	public void setFilter (String filter) {
		param.setNameLike(filter);
		param.setOffset((current-1)*pageSize);
		elements = serv.getSome(param);
	}
}
