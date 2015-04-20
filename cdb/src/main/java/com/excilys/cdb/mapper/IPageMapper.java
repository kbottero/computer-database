package com.excilys.cdb.mapper;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.excilys.cdb.persistence.DaoRequestParameter;
import com.excilys.cdb.util.Page;

public abstract class IPageMapper<T,I extends Serializable> {
	
	public DaoRequestParameter pageToDaoRequestParameter (Page<T,I> page) {
		
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
					orderMap.put(order.getProperty(), DaoRequestParameter.Order.ASC);
				} else {
					orderMap.put(order.getProperty(), DaoRequestParameter.Order.DESC);
				}
			}
		}
		daoRequestParameter.setOrders(orderMap);
		
		return daoRequestParameter;
	}
}
