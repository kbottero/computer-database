package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.model.Company;

/**
 * Mapper for com.excilys.cdb.Model.Company using java.sql.ResultSet
 * 
 * @author Kevin Bottero
 *
 */
public enum CompanyMapper implements IMapper<Company, CompanyDTO> {
	INSTANCE;

	/** Primary Key.	 */
	public static final String DEFAULT_ID = "id";

	public enum DBFields {
		ID ("id"),
		NAME ("name");
		
		DBFields (String value) {
			this.value = value;
		}
		
		public String value;
	}
	
	/**
	 * Create a Company from a ResultSet
	 * Only id and name are mandatory to create a computer.
	 * @param curs
	 * 				Data on the Computer
	 * @return Created Company instance
	 */
	public Company mapFromRow(ResultSet curs) {
		Company comp=null;
		try {
			comp = new Company(curs.getLong("id"),
					curs.getString("name"));
		} catch (SQLException e) {
			throw new DaoException(e);
		}
		return comp;
	}
	
	/**
	 * Create a CompanyDTO from a Company.
	 * @param company
	 * 				Data on the Company
	 * @return Created CompanyDTO instance
	 */
	public CompanyDTO toDTO(Company company) {
		CompanyDTO compDto = null;
		if (company != null) {
			compDto = new CompanyDTO (
					company.getId(),
					company.getName());
		}
		return compDto;
	}
	
	/**
	 * Create a Company from a CompanyDTO.
	 * @param companyDTO
	 * 				Data on the Company
	 * @return Created Company instance
	 */
	public Company fromDTO(CompanyDTO companyDTO) {
		Company company = null;
		if (companyDTO != null) {
			company = new Company (
					companyDTO.getId(),
					companyDTO.getName());
		}
		return company;
	}
}
