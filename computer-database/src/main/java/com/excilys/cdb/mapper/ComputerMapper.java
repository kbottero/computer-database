package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DaoException;

public enum ComputerMapper {
	INSTANCE;
	
	public static final HashMap<String,String> mapBDModel;
	public static final String DEFAULT_ID = "id";
	static {
		mapBDModel = new HashMap<String,String>();
		mapBDModel.put("id","id");
		mapBDModel.put("name","name");
		mapBDModel.put("introduced","introductionDate");
		mapBDModel.put("discontinued","discontinuedDate");
		mapBDModel.put("company_id","constructor");
	}
	
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
