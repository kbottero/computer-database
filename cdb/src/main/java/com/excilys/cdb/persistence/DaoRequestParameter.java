package com.excilys.cdb.persistence;
import java.util.LinkedHashMap;

public class DaoRequestParameter {
	
	public static enum Order {
		ASC,
		DESC;
	}
	
	public static enum NameFiltering {
		POST, //LIKE filter%
		PRE,  //LIKE %filter
		POST_AND_PRE, //LIKE %filter%
		NONE;  //LIKE filter
	}

	private NameFiltering nameFiltering;
	private String nameLike;
	
	private LinkedHashMap<String, Order> orders;
	private Long limit;
	private Long offset;
	
	public DaoRequestParameter() {}
	
	public DaoRequestParameter(NameFiltering nameFiltering, String nameLike,
			LinkedHashMap<String, Order> orders, Long limit, Long offset) {
		super();
		if (nameFiltering == null) {
			this.nameFiltering = NameFiltering.POST;
		} else {
			this.nameFiltering = nameFiltering;
		}
		this.nameLike = nameLike;
		this.orders = orders;
		this.limit = limit;
		this.offset = offset;
	}

	public NameFiltering getNameFiltering() {
		return nameFiltering;
	}

	public void setNameFiltering(NameFiltering nameFiltering) {
		this.nameFiltering = nameFiltering;
	}

	public String getNameLike() {
		return nameLike;
	}

	public void setNameLike(String nameLike) {
		if (nameFiltering == null) {
			nameFiltering = NameFiltering.POST;
		} 
		this.nameLike = nameLike;
	}

	public Long getLimit() {
		return limit;
	}

	public void setLimit(Long limit) {
		this.limit = limit;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public void setOrders(LinkedHashMap<String, Order> orders) {
		this.orders = orders;
	}
	
	public LinkedHashMap<String, Order> getOrders() {
		return this.orders;
	}
	
}
