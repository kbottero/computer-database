package com.excilys.cdb.mapper.model.impl;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.date.LocalDateTimeMapper;
import com.excilys.cdb.mapper.model.IMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.IService;

/**
 * Mapper for com.excilys.cdb.Model.Computer using java.sql.ResultSet
 * 
 * @author Kevin Bottero
 *
 */
@Component
public class ComputerMapper implements IMapper<Computer, ComputerDTO> {

	/** Primary Key.	 */
	public static final String DEFAULT_ID = "computer.id";

	@Autowired
	private LocalDateTimeMapper localDateTimeMapper;

	@Autowired
	private IService<Company,Long> companiesService;

	/** Map DB labels -> Model attributes. */
	public static final HashMap<String,String> mapBDModel;
	static {
		mapBDModel = new HashMap<String,String>();
		mapBDModel.put("id","computer.id");
		mapBDModel.put("name","computer.name");
		mapBDModel.put("introduced","computer.introduced");
		mapBDModel.put("discontinued","computer.discontinued");
		mapBDModel.put("constructor","company.name");
	}
	
	/**
	 * Create a ComputerDTO from a Computer.
	 * @param computer
	 * 				Data on the Computer
	 * @return Created ComputerDTO instance
	 */
	public ComputerDTO toDTO(Computer computer) {
		
		String introductionDate = "";
		String discontinuedDate = "";
		String constructorName = "";
		Long constructorId = 0l;
		
		if (computer.getConstructor() != null) {
			constructorName = computer.getConstructor().getName();
			constructorId = computer.getConstructor().getId();
		}
		if (computer.getIntroductionDate() != null) {
			introductionDate = localDateTimeMapper.toDTO(computer.getIntroductionDate());
		}
		if (computer.getDiscontinuedDate() != null) {
			discontinuedDate = localDateTimeMapper.toDTO(computer.getDiscontinuedDate());
		}
		if (computer.getConstructor() != null) {
			constructorName = computer.getConstructor().getName();
		}

		ComputerDTO compDto = new ComputerDTO (
				computer.getId(),
				computer.getName(),
				introductionDate,
				discontinuedDate,
				constructorId,
				constructorName) ;
		return compDto;
	}
	
	/**
	 * Create a Computer from a ComputerDTO.
	 * @param computerDTO
	 * 				Data on the Computer
	 * @return Created Computer instance
	 */
	public Computer fromDTO(ComputerDTO computerDTO) {
		
		LocalDateTime introductionDate = null;
		LocalDateTime discontinuedDate = null;
		Company constructor = null;
		
		if (computerDTO.getIntroduced() != null && !computerDTO.getIntroduced().isEmpty()) {
			introductionDate = localDateTimeMapper.fromDTO(computerDTO.getIntroduced());
		}
		if (computerDTO.getDiscontinued() != null && !computerDTO.getDiscontinued().isEmpty()) {
			discontinuedDate = localDateTimeMapper.fromDTO(computerDTO.getDiscontinued());
		}
		if (computerDTO.getCompanyId() != null &&
				!computerDTO.getCompanyId().equals(0l)) {
			if (computerDTO.getCompanyName() == null || computerDTO.getCompanyName().equals("")) {
				Company company = companiesService.getOne(computerDTO.getCompanyId());
				if (company != null) {
					constructor = company;
				}
			} else {
				constructor = new Company(computerDTO.getCompanyId(), computerDTO.getCompanyName());
			}
		}

		Computer computer = new Computer (
				computerDTO.getId(),
				computerDTO.getName(),
				introductionDate,
				discontinuedDate,
				constructor) ;
		return computer;
	}
	
	
}
