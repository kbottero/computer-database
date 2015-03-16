package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.cdb.model.Company;

public enum CompanyMapper {
	
	INSTANCE;
	
	public Company parseCompany(ResultSet curs) {
		Company comp=null;
		try {
			comp = new Company(curs.getLong("id"),
					curs.getString("name"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return comp;
	}
}
