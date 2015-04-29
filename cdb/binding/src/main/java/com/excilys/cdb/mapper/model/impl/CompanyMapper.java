package com.excilys.cdb.mapper.model.impl;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.mapper.model.IMapper;
import com.excilys.cdb.model.Company;

/**
 * Mapper for com.excilys.cdb.Model.Company using java.sql.ResultSet
 * 
 * @author Kevin Bottero
 *
 */
@Component
public class CompanyMapper implements IMapper<Company, CompanyDTO> {

	/** Primary Key.	 */
	public static final String DEFAULT_ID = "id";

	/** Map DB labels -> Model attributes. */
	public static final HashMap<String,String> mapBDModel;
	static {
		mapBDModel = new HashMap<String,String>();
		mapBDModel.put("id","id");
		mapBDModel.put("name","name");
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