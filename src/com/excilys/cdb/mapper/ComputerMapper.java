package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DaoException;

public enum ComputerMapper {
	INSTANCE;
	
	public Computer parseComputer(ResultSet curs, Company company) {

		Computer comp;
		try {
			comp = new Computer(curs.getLong("id"),
					curs.getString("name"));
			if (curs.getString("introduced") != null) {
				comp.setIntroductionDate(Timestamp.valueOf((curs.getString("introduced"))).toLocalDateTime());
			}
			if (curs.getString("discontinued") != null) {
				comp.setIntroductionDate(Timestamp.valueOf((curs.getString("discontinued"))).toLocalDateTime());
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		}
		comp.setConstructor(company);
		
		return comp;
	}
}
