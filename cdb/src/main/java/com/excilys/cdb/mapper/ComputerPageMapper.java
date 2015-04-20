package com.excilys.cdb.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.ComputerPageDTO;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DaoRequestParameter;
import com.excilys.cdb.service.IService;
import com.excilys.cdb.ui.util.ComputerPage;
import com.excilys.cdb.ui.util.Page;

@Component
public class ComputerPageMapper extends IPageMapper<Computer,Long> {

	public static ComputerPage fromDTO (IService<Computer,Long> service, ComputerPageDTO pageDTO) {
		
		Integer pageNumber;
		Integer nbElemPerPage;
		Sort sort = null;
		String search;
		
		if (pageDTO.getNumPage() != null && !pageDTO.getNumPage().isEmpty()) {		
			if (! Pattern.matches("\\d+",pageDTO.getNumPage().trim())) {
				throw new IllegalArgumentException("Invalid Page Number (Not a Number).");
			}
			pageNumber = Integer.parseInt(pageDTO.getNumPage().trim());
			if (pageNumber < 0) {
				throw new IllegalArgumentException("Invalid Page Number (Negative Number).");
			}
		} else {
			pageNumber = 1;
		}
		
		if (pageDTO.getNbCompPerPage() != null && !pageDTO.getNbCompPerPage().isEmpty()) {
			if (! Pattern.matches("\\d+",pageDTO.getNbCompPerPage().trim())) {
				throw new IllegalArgumentException("Invalid NumberOfElementPerPage (Not a Number).");
			}
			nbElemPerPage = Integer.parseInt(pageDTO.getNbCompPerPage().trim());
			if (nbElemPerPage < 0 || (nbElemPerPage != 10 && nbElemPerPage != 50 && nbElemPerPage != 100)) {
				throw new IllegalArgumentException("Invalid NumberOfElementPerPage (Negative Number).");
			}
		} else {
			nbElemPerPage = new Integer(10);
		}
		search = pageDTO.getSearch();
		if (search != null && !search.isEmpty()) {
			if (! Pattern.matches("[A-Za-z0-9_~\\-@#\\$\\^&\\*\\(\\).]+",search.trim())) {
				throw new IllegalArgumentException("Invalid Search (Illegal character).");
			}
		}
		
		if (pageDTO.getSortColumn() != null &&
			!pageDTO.getSortColumn().isEmpty() &&
			pageDTO.getSortColumnOrder() != null &&
			!pageDTO.getSortColumnOrder().isEmpty())
		{
			ArrayList<Order> order = new ArrayList<Order>();
			String [] listProperty = pageDTO.getSortColumn().split(",");
			String [] listDirection = pageDTO.getSortColumnOrder().split(",");
			if (listProperty.length == listDirection.length) {
				for (int i=0;i<listProperty.length;++i) {
					switch (listDirection[i]) {
					case "ASC" :
						order.add(new Order(Direction.ASC,listProperty[i]));
						break;
					case "DESC" :
						order.add(new Order(Direction.DESC,listProperty[i]));
						break;
					default:
						throw new IllegalArgumentException("Unknown direction for sorting.");
					}
				}
				sort = new Sort(order);
			} else {
				throw new IllegalArgumentException("Number of column to sort and order doesn't match.");
			}
		}
		
		ComputerPage page = new ComputerPage(service, nbElemPerPage);
		page.setPageNumber(pageNumber);
		page.setNbElemPerPage(nbElemPerPage);
		page.setSearch(search);
		page.setSort(sort);

		return page;
	}
	
	@Override
	public DaoRequestParameter pageToDaoRequestParameter (Page<Computer,Long> page) {
		
		DaoRequestParameter daoRequestParameter = new DaoRequestParameter();
		
		String search = page.getSearch();
		if (search != null && !search.isEmpty()) {
			daoRequestParameter.setNameLike(search);
		}
		
		Integer nbCompPerPage = page.getNbElemPerPage();
		Integer numPage = page.getPageNumber();
		if ((nbCompPerPage != null) && (nbCompPerPage != 0) && (numPage != null) && (numPage > 0)) {
			daoRequestParameter.setLimit(nbCompPerPage.longValue());
			daoRequestParameter.setOffset((numPage-1)*nbCompPerPage.longValue());
		} else {
			daoRequestParameter.setLimit(10l);
		}

		LinkedHashMap<String, DaoRequestParameter.Order> orderMap = new LinkedHashMap<String, DaoRequestParameter.Order>();
		Sort sort = page.getSort();
		if (sort != null) {
			for (Iterator<Order> it = sort.iterator(); it.hasNext();) {
				Order order = it.next();
				if (order.getDirection().equals(Direction.ASC)) {
					orderMap.put(ComputerMapper.mapBDModel.get(order.getProperty()), DaoRequestParameter.Order.ASC);
				} else {
					orderMap.put(ComputerMapper.mapBDModel.get(order.getProperty()), DaoRequestParameter.Order.DESC);
				}
			}
		}
		daoRequestParameter.setOrders(orderMap);
		
		return daoRequestParameter;
	}
	
}
