package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.DaoException;

/**
 * Mapper for com.excilys.cdb.Model.Company using java.sql.ResultSet
 * 
 * @author Kevin Bottero
 *
 */
public enum CompanyMapper {
	INSTANCE;

	/** Primary Key.	 */
	public static final String DEFAULT_ID = "id";

	/** Map DB labels -> Model attributes. */
	public static final HashMap<String,String> mapBDModel;
	static {
		mapBDModel = new HashMap<String,String>();
		mapBDModel.put("id","id");
		mapBDModel.put("name","name");
	}
	
	/**
	 * Create a Company from a ResultSet
	 * Only id and name are mandatory to create a computer.
	 * @param curs
	 * 				Data on the Computer
	 * @return Created Company instance
	 */
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
