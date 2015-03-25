package com.excilys.cdb.model;

public class Company {

	private Long id;
	private String name;

	public Company(Long id,String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		StringBuilder strgBuil = new StringBuilder();
		strgBuil.append(name);
		strgBuil.append("(id:");
		strgBuil.append(id);
		strgBuil.append(")");
		return strgBuil.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Company other = (Company) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
