package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.model.Company;

public enum CompanyDao implements IDao<Company, Long>{
	INSTANCE;
	
	private static HashSet<String> setFileds;
	static {
		setFileds= new HashSet<String>();
		setFileds.add("id");
		setFileds.add("name");
	}
	
	private static enum preparedStatement {
		SELECT_ONE ("SELECT id, name FROM company WHERE id=?;"),
		SELECT_SOME ("SELECT id, name FROM company ORDER BY ? ? LIMIT ? OFFSET ?");
		
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
			curs = conn.createStatement().executeQuery("SELECT id, name FROM company;");
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
	public Long getNb(){
		
		ResultSet curs;
		Connection conn = null;
		Long nbElements = null;
		try {
			conn = DaoManager.INSTANCE.getConnection();
			curs = conn.createStatement().executeQuery("SELECT COUNT(id) FROM company;");
			if ( curs.next() ) {
				nbElements = curs.getLong(1); 
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
		return nbElements;
	}

	@Override
	public List<Company> getSome( List<String> orderByCol, Order order, Long limit, Long offset ){
		
		ResultSet curs;
		List<Company> list = new ArrayList<Company>();
		Connection conn = null;
		
		try {
			conn = DaoManager.INSTANCE.getConnection();
			PreparedStatement selectSomeCompanies = conn.prepareStatement(preparedStatement.SELECT_SOME.getRequest());
			if ((orderByCol == null) || (orderByCol.size() == 0)){
				selectSomeCompanies.setString(1,"id");
			} else {
				StringBuilder strgBuilder = new StringBuilder();
				for (String strg : orderByCol) {
					 if (setFileds.contains(strg)) {
						 if (strgBuilder.length() != 0) {
							 strgBuilder.append(", ");
						 }
						 strgBuilder.append("strg");
					 } else {
						 throw new DaoException("Incorrect field for request : "+preparedStatement.SELECT_SOME.getRequest());
					 }
				}
				selectSomeCompanies.setString(1,strgBuilder.toString());
			}
			if (order == null) {
				selectSomeCompanies.setString(2,"ASC"); 
			} else {
				switch (order) {
				case ASC:
					selectSomeCompanies.setString(2,"ASC");
					break;
				case DESC:
					selectSomeCompanies.setString(2,"DESC");
					break;
				default:
					throw new DaoException("Invalid order for request : "+preparedStatement.SELECT_SOME.getRequest());
				}
			}
			if (limit == null) {
				selectSomeCompanies.setLong(3,0); 
			} else {
				if (limit < 0) {
					throw new DaoException("Invalid limit ("+limit+") for request : "+preparedStatement.SELECT_SOME.getRequest());
				} else {
					selectSomeCompanies.setLong(3,limit); 
				}
			}
			if (offset == null) {
				selectSomeCompanies.setLong(4,0); 
			} else {
				if (offset < 0) {
					throw new DaoException("Invalid offset ("+offset+") for request : "+preparedStatement.SELECT_SOME.getRequest());
				} else {
					selectSomeCompanies.setLong(4,offset); 
				}
			}
			curs = selectSomeCompanies.executeQuery();
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
