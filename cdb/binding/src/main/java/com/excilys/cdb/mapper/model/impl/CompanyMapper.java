package com.excilys.cdb.mapper.model.impl;

import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.mapper.model.IMapper;
import com.excilys.cdb.model.Company;

@Component
public class CompanyMapper implements IMapper<Company, CompanyDTO> {
	
	@Override
	public CompanyDTO toDTO(Company company) {
		CompanyDTO compDto = null;
		if (company != null) {
			compDto = new CompanyDTO (
					company.getId(),
					company.getName());
		}
		return compDto;
	}
	
	@Override
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
