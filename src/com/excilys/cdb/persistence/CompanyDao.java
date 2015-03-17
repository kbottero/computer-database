package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.model.Company;

public enum CompanyDao implements IDao<Company, Long>{
	INSTANCE;
	
	private static enum preparedStatement {
		SELECT_ONE ("SELECT * FROM company WHERE id=?;");
		
		private final String request;
		
		preparedStatement(String request) {
	        this.request = request;
	    }
	    private String getRequest() { return request; }
	}
	
	@Override
	public List<Company> getAll() {

		ResultSet curs;
		List<Company> list = new ArrayList<Company>();
		Connection conn = null;
		try {
			conn = DaoManager.INSTANCE.getConnection();
			curs = conn.createStatement().executeQuery("SELECT * FROM company;");
			while ( curs.next() ) {
				list.add(CompanyMapper.INSTANCE.parseCompany(curs));
    		}
		} catch (SQLException | NumberFormatException e) {
			throw new DaoException(e);
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new DaoException(e);
				}
			}
		}
		return list;
	}

	@Override
	public Company getById(Long id) {
		
		ResultSet curs;
		Company comp=null;
		Connection conn = null;
		try {
			conn = DaoManager.INSTANCE.getConnection();
			PreparedStatement selectOneCompany = conn.prepareStatement(preparedStatement.SELECT_ONE.getRequest());
			selectOneCompany.setLong(1,id);
			curs = selectOneCompany.executeQuery();
			if ( curs.next() ) {
				comp = CompanyMapper.INSTANCE.parseCompany(curs);
    		}
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new DaoException(e);
				}
			}
		}
		return comp;
	}
}
