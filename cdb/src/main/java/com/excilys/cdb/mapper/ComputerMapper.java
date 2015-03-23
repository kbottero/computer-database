package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompaniesService;

/**
 * Mapper for com.excilys.cdb.Model.Computer using java.sql.ResultSet
 * 
 * @author Kevin Bottero
 *
 */
public enum ComputerMapper implements IMapper<Computer> {
	INSTANCE;

	/** Primary Key.	 */
	public static final String DEFAULT_ID = "id";

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
				comp.setConstructor(new CompaniesService().getOne(curs.getLong("company_id")));
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
		
		if (computer.getIntroductionDate() != null) {
			introductionDate = computer.getIntroductionDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
		}
		if (computer.getDiscontinuedDate() != null) {
			discontinuedDate = computer.getDiscontinuedDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
		}

		ComputerDTO compDto = new ComputerDTO (
				computer.getId().toString(),
				computer.getName(),
				introductionDate,
				discontinuedDate,
				CompanyMapper.INSTANCE.toDTO(computer.getConstructor())) ;
		return compDto;
	}
	
	
	
}
