package com.excilys.cdb.dto;

public class ComputerDTO {

	private String id;
	private String name;
	private String introductionDate;
	private String discontinuedDate;
	private String constructorName;
	
	public ComputerDTO(String id, String name, String introductionDate,
			String discontinuedDate, String constructor) {
		super();
		this.id = id;
		this.name = name;
		this.introductionDate = introductionDate;
		this.discontinuedDate = discontinuedDate;
		this.constructorName = constructor;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntroductionDate() {
		return introductionDate;
	}
	public void setIntroductionDate(String introductionDate) {
		this.introductionDate = introductionDate;
	}
	public String getDiscontinuedDate() {
		return discontinuedDate;
	}
	public void setDiscontinuedDate(String discontinuedDate) {
		this.discontinuedDate = discontinuedDate;
	}
	public String getConstructorName() {
		return constructorName;
	}
	public void setConstructorName(String constructorName) {
		this.constructorName = constructorName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((constructorName == null) ? 0 : constructorName.hashCode());
		result = prime
				* result
				+ ((discontinuedDate == null) ? 0 : discontinuedDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((introductionDate == null) ? 0 : introductionDate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ComputerDTO other = (ComputerDTO) obj;
		if (constructorName == null) {
			if (other.constructorName != null)
				return false;
		} else if (!constructorName.equals(other.constructorName))
			return false;
		if (discontinuedDate == null) {
			if (other.discontinuedDate != null)
				return false;
		} else if (!discontinuedDate.equals(other.discontinuedDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (introductionDate == null) {
			if (other.introductionDate != null)
				return false;
		} else if (!introductionDate.equals(other.introductionDate))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
