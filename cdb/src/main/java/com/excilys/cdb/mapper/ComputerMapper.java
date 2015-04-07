package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompaniesService;

/**
 * Mapper for com.excilys.cdb.Model.Computer using java.sql.ResultSet
 * 
 * @author Kevin Bottero
 *
 */
@Component
public class ComputerMapper implements IMapper<Computer, ComputerDTO> {

	/** Primary Key.	 */
	public static final String DEFAULT_ID = "id";

	@Autowired
	private CompaniesService companiesService;

	/** Map DB labels -> Model attributes. */
	public static final HashMap<String,String> mapBDModel;
	static {
		mapBDModel = new HashMap<String,String>();
		mapBDModel.put("id","id");
		mapBDModel.put("name","name");
		mapBDModel.put("introduced","introductionDate");
		mapBDModel.put("discontinued","discontinuedDate");
		mapBDModel.put("company_id","constructor");
	}
	
	/**
	 * Create a Computer from a ResultSet.
	 * Only id and name are mandatory to create a computer.
	 * @param curs
	 * 				Data on the Computer
	 * @return Created Computer instance
	 */
	public Computer mapFromRow(ResultSet curs) {

		Computer comp;
		try {
			comp = new Computer(curs.getLong("id"),
					curs.getString("name"));
			if (curs.getString("introduced") != null) {
				comp.setIntroductionDate(Timestamp.valueOf((curs.getString("introduced"))).toLocalDateTime());
			}
			if (curs.getString("discontinued") != null) {
				comp.setDiscontinuedDate(Timestamp.valueOf((curs.getString("discontinued"))).toLocalDateTime());
			}
			if (curs.getString("company_id") != null) {
				comp.setConstructor(companiesService.getOne(curs.getLong("company_id")));
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		}
		return comp;
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
			introductionDate = computer.getIntroductionDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
		}
		if (computer.getDiscontinuedDate() != null) {
			discontinuedDate = computer.getDiscontinuedDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
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
		
		if (computerDTO.getIntroductionDate() != null && !computerDTO.getIntroductionDate().isEmpty()) {
			introductionDate = LocalDateTime.parse(computerDTO.getIntroductionDate()+"T00:00:00", DateTimeFormatter.ISO_DATE_TIME);
		}
		if (computerDTO.getDiscontinuedDate() != null && !computerDTO.getDiscontinuedDate().isEmpty()) {
			discontinuedDate = LocalDateTime.parse(computerDTO.getDiscontinuedDate()+"T00:00:00", DateTimeFormatter.ISO_DATE_TIME);
		}
		if (computerDTO.getConstructorId() != null &&
				!computerDTO.getConstructorId().equals(0l) &&
				computerDTO.getConstructorName() != null &&
				!computerDTO.getConstructorId().equals("")
				) {
			constructor = new Company(computerDTO.getConstructorId(), computerDTO.getConstructorName());
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
