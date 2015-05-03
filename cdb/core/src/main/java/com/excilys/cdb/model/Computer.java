package com.excilys.cdb.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="computer")
public class Computer implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="introduced")
	//Needed as long as Hibernate doesn't support LocalDateTime serialization
	@Type(type="com.excilys.cdb.date.LocalDateTimeUserType") 
	private LocalDateTime introduced;
	
	@Column(name="discontinued") 
	//Needed as long as Hibernate doesn't support LocalDateTime serialization
	@Type(type="com.excilys.cdb.date.LocalDateTimeUserType")
	private LocalDateTime discontinued;
	
	@ManyToOne
    @JoinColumn(name="company_id")
	@OrderBy("company.name")
	private Company constructor;
	
	public Computer(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Computer() {
		super();
	}
	
	public Computer(Long id, String name, LocalDateTime introductionDate,
			LocalDateTime discontinuedDate, Company constructor) {
		super();
		this.id = id;
		this.name = name;
		this.introduced = introductionDate;
		this.discontinued = discontinuedDate;
		this.constructor = constructor;
	}

	public Company getConstructor() {
		return constructor;
	}

	public void setConstructor(Company constructor) {
		this.constructor = constructor;
	}

	public LocalDateTime getIntroductionDate() {
		return introduced;
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
		this.introduced = introductionDate;
	}

	public LocalDateTime getDiscontinuedDate() {
		return discontinued;
	}
	
	public void setDiscontinuedDate(LocalDateTime discontinuedDate) {
		this.discontinued = discontinuedDate;
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

	@Override
	public String toString() {
		return "Computer [id=" + id + ", name=" + name + ", introduced="
				+ introduced + ", discontinued=" + discontinued
				+ ", constructor=" + constructor + "]";
	}


}
