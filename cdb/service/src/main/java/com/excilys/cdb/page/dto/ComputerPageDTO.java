package com.excilys.cdb.page.dto;

public class ComputerPageDTO {
	
	private String search;
	private String numPage;
	private String nbCompPerPage;
	private String sortColumn;
	private String sortColumnOrder;
	private String language;
	
	public ComputerPageDTO(){}
	
	public ComputerPageDTO(String search, String numPage,
			String nbCompPerPage, String sortColumn, String sortColumnOrder,
			String language) {
		super();
		this.search = search;
		this.numPage = numPage;
		this.nbCompPerPage = nbCompPerPage;
		this.sortColumn = sortColumn;
		this.sortColumnOrder = sortColumnOrder;
		this.language = language;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getNumPage() {
		return numPage;
	}
	public void setNumPage(String numPage) {
		this.numPage = numPage;
	}
	public String getNbCompPerPage() {
		return nbCompPerPage;
	}
	public void setNbCompPerPage(String nbCompPerPage) {
		this.nbCompPerPage = nbCompPerPage;
	}
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	public String getSortColumnOrder() {
		return sortColumnOrder;
	}
	public void setSortColumnOrder(String sortColumnOrder) {
		this.sortColumnOrder = sortColumnOrder;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
		result = prime * result
				+ ((nbCompPerPage == null) ? 0 : nbCompPerPage.hashCode());
		result = prime * result + ((numPage == null) ? 0 : numPage.hashCode());
		result = prime * result + ((search == null) ? 0 : search.hashCode());
		result = prime * result
				+ ((sortColumn == null) ? 0 : sortColumn.hashCode());
		result = prime * result
				+ ((sortColumnOrder == null) ? 0 : sortColumnOrder.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComputerPageDTO other = (ComputerPageDTO) obj;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (nbCompPerPage == null) {
			if (other.nbCompPerPage != null)
				return false;
		} else if (!nbCompPerPage.equals(other.nbCompPerPage))
			return false;
		if (numPage == null) {
			if (other.numPage != null)
				return false;
		} else if (!numPage.equals(other.numPage))
			return false;
		if (search == null) {
			if (other.search != null)
				return false;
		} else if (!search.equals(other.search))
			return false;
		if (sortColumn == null) {
			if (other.sortColumn != null)
				return false;
		} else if (!sortColumn.equals(other.sortColumn))
			return false;
		if (sortColumnOrder == null) {
			if (other.sortColumnOrder != null)
				return false;
		} else if (!sortColumnOrder.equals(other.sortColumnOrder))
			return false;
		return true;
	}
}
