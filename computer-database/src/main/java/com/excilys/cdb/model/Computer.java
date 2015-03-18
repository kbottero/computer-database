package com.excilys.cdb.model;

import java.time.LocalDateTime;

public class Computer {
	
	private Long id;
	
	private String name;
	private Company constructor;
	
	private LocalDateTime introductionDate;
	private LocalDateTime discontinuedDate;
	
	public Computer(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Company getConstructor() {
		return constructor;
	}

	public void setConstructor(Company constructor) {
		this.constructor = constructor;
	}

	public LocalDateTime getIntroductionDate() {
		return introductionDate;
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

	public void setIntroductionDate(LocalDateTime introductionDate) {
		this.introductionDate = introductionDate;
	}

	public LocalDateTime getDiscontinuedDate() {
		return discontinuedDate;
	}
	
	public void setDiscontinuedDate(LocalDateTime discontinuedDate) {
		this.discontinuedDate = discontinuedDate;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("id:"+id+":"+name+" [");
		
		if (constructor!=null) {
			str.append(constructor);
		}
		if (introductionDate!=null) {
			if (constructor!=null) {
				str.append(",");
			}
			str.append(introductionDate);
		}
		if (discontinuedDate!=null) {
			if (introductionDate!=null) {
				str.append("/");
			} else {
				str.append(",");
			}
			str.append(discontinuedDate);
		}
		str.append("]");
		return  str.toString();
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
		Computer other = (Computer) obj;
		if (id != other.id)
			return false;
		return true;
	}


}
