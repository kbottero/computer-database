package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

/**
 * Mapper for com.excilys.cdb.Model.Computer using java.sql.ResultSet
 * 
 * @author Kevin Bottero
 *
 */
public enum ComputerMapper {
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
	 * Create a Computer from a ResultSet and a Company.
	 * Only id and name are mandatory to create a computer.
	 * @param curs
	 * 				Data on the Computer
	 * @param company
	 * 				Constructor of the Computer
	 * @return Created Computer instance
	 */
	public Computer parseComputer(ResultSet curs, Company company) {

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
		} catch (SQLException e) {
			throw new DaoException(e);
		}
		comp.setConstructor(company);
		
		return comp;
	}
}
