package com.excilys.cdb.dto;

public class ComputerDTO {

	private String id;
	private String name;
	private String introductionDate;
	private String discontinuedDate;
	private CompanyDTO constructor;
	
	public ComputerDTO(String id, String name, String introductionDate,
			String discontinuedDate, CompanyDTO constructor) {
		super();
		this.id = id;
		this.name = name;
		this.introductionDate = introductionDate;
		this.discontinuedDate = discontinuedDate;
		this.constructor = constructor;
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
	public CompanyDTO getConstructor() {
		return constructor;
	}
	public void setConstructor(CompanyDTO constructor) {
		this.constructor = constructor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((constructor == null) ? 0 : constructor.hashCode());
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
		if (constructor == null) {
			if (other.constructor != null)
				return false;
		} else if (!constructor.equals(other.constructor))
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
