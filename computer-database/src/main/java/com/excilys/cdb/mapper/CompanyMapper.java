package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.DaoException;

public enum CompanyMapper {
	INSTANCE;
	
	public static final HashMap<String,String> mapBDModel;
	public static final String DEFAULT_ID = "id";
	static {
		mapBDModel = new HashMap<String,String>();
		mapBDModel.put("id","id");
		mapBDModel.put("name","name");
	}
	
	public Company parseCompany(ResultSet curs) {
		Company comp=null;
		try {
			comp = new Company(curs.getLong("id"),
					curs.getString("name"));
		} catch (SQLException e) {
			throw new DaoException(e);
		}
		return comp;
	}
}
