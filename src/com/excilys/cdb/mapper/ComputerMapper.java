package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public enum ComputerMapper {
	INSTANCE;
	
	public Computer parserComputer(ResultSet curs, Company company) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Computer comp;
		try {
			comp = new Computer(curs.getLong("id"),
					curs.getString("name"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		try	{
			comp.setIntroductionDate(dateFormat.parse(curs.getString("introduced")));
		} catch (SQLException | ParseException | NullPointerException e) {
			comp.setIntroductionDate(null);
		}
		try	{
			comp.setIntroductionDate(dateFormat.parse(curs.getString("discontinued")));
		} catch (SQLException | ParseException | NullPointerException e) {
			comp.setIntroductionDate(null);
		}
		comp.setConstructor(company);
//		comp.setConstructor(getOneCompany(curs.getLong("company_id")));
		
		return comp;
		
	}
	
}
