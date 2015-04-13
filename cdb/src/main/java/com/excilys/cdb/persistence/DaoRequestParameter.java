package com.excilys.cdb.persistence;
import java.util.List;

public class DaoRequestParameter {
	
	public static enum Order {
		ASC,DESC;
	}
	
	public static enum NameFiltering {
		POST, //LIKE filter%
		PRE,  //LIKE %filter
		POST_AND_PRE, //LIKE %filter%
		NONE;  //LIKE filter
	}

	private NameFiltering nameFiltering;
	private String nameLike;
	
	private List<String> colToOrderBy;
	private Order order;
	private Long limit;
	private Long offset;
	
	public DaoRequestParameter() {}
	
	public DaoRequestParameter(NameFiltering nameFiltering, String nameLike,
			List<String> colToOrderBy, Order order, Long limit, Long offset) {
		super();
		if (nameFiltering == null) {
			this.nameFiltering = NameFiltering.POST;
		} else {
			this.nameFiltering = nameFiltering;
		}
		this.nameLike = nameLike;
		this.colToOrderBy = colToOrderBy;
		if (order == null) { 
			this.order = Order.ASC;
		} else {
			this.order = order;
		}
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

	public List<String> getColToOrderBy() {
		return colToOrderBy;
	}

	public void setColToOrderBy(List<String> colToOrderBy) {
		this.colToOrderBy = colToOrderBy;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
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
	
}
