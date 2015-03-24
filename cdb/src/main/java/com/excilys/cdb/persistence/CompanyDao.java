package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.model.Company;

/**
 * CompanyDao
 * @author Kevin Bottero
 *
 */
public enum CompanyDao implements IDao<Company, Long>{
	INSTANCE;
	
	private static enum preparedStatement {
		COUNT_ALL ("SELECT COUNT(id) FROM company;"),
		SELECT_ALL ("SELECT id, name FROM company;"),
		SELECT_ALL_ORDERED ("SELECT id, name FROM company ORDER BY ? ?;"),
		SELECT_ONE ("SELECT id, name FROM company WHERE id=?;"),
		SELECT_SOME ("SELECT id, name FROM company ORDER BY ? ? LIMIT ? OFFSET ?;"),
		SELECT_SOME_FILTERED ("SELECT id, name FROM company WHERE name LIKE ? ORDER BY ? ? LIMIT ? OFFSET ?;");
		
		private final String request;
		
		preparedStatement(String request) {
	        this.request = request;
	    }
		
	    private String getRequest() { return request; }
	}
	
	@Override
	public List<Company> getAll() throws DaoException {
		List<Company> list = new ArrayList<Company>();
		Connection conn  = getConnection();
		Statement statement = createStatement(conn);
		try {
			ResultSet curs = statement.executeQuery(preparedStatement.SELECT_ALL.getRequest());
			while ( curs.next() ) {
				list.add(CompanyMapper.INSTANCE.mapFromRow(curs));
    		}
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT,e);
		} finally {
			closeConnAndStat(statement, conn);
		}
		return list;
	}
	
	@Override
	public List<Company> getAll(List<String> orderByCol) throws DaoException {
		return this.getAll(orderByCol, null);
	}
	
	@Override
	public List<Company> getAll(List<String> orderByCol,
			Order order) throws DaoException {
		List<Company> list = new ArrayList<Company>();
		Connection conn = getConnection();
		PreparedStatement statement = createPreparedStatement(conn,preparedStatement.SELECT_ALL_ORDERED.getRequest());

		try {
			//ORDER BY ?
			if ((orderByCol == null) || (orderByCol.size() == 0)){
				statement.setString(1,CompanyMapper.DEFAULT_ID);
			} else {
				StringBuilder strgBuilder = new StringBuilder();
				for (String strg : orderByCol) {
					 if (CompanyMapper.mapBDModel.containsKey(strg)) {
						 if (strgBuilder.length() != 0) {
							 strgBuilder.append(", ");
						 }
						 strgBuilder.append(CompanyMapper.mapBDModel.get(strg));
					 } else {
						 throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT+preparedStatement.SELECT_ALL_ORDERED.getRequest());
					 }
				}
				statement.setString(1,strgBuilder.toString());
			}
			//ASC or DESC ?
			if (order == null) {
				statement.setString(2,"ASC"); 
			} else {
				switch (order) {
				case ASC:
					statement.setString(2,"ASC");
					break;
				case DESC:
					statement.setString(2,"DESC");
					break;
				default:
					throw new DaoException("Invalid order for request : "+preparedStatement.SELECT_ALL_ORDERED.getRequest());
				}
			}
			//Execute Request
			ResultSet curs = statement.executeQuery();
			//Mapping
			while ( curs.next() ) {
				list.add(CompanyMapper.INSTANCE.mapFromRow(curs));
			}
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_SET_PREPAREDSTATEMENT,e);
		} finally {
			closeConnAndStat(statement, conn);
		}
		return list;
	}

	@Override
	public Long getNb() throws DaoException {
		Long nbElements;
		Connection conn = getConnection();
		Statement statement = createStatement(conn);
		try {
			ResultSet curs = statement.executeQuery(preparedStatement.COUNT_ALL.getRequest());
			if ( curs.next() ) {
				nbElements = curs.getLong(1); 
    		} else {
    			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT);
    		}
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT,e);
		} finally {
			closeConnAndStat(statement, conn);
		}
		return nbElements;
	}

	@Override
	public List<Company> getSome( Long limit, Long offset) throws DaoException {
		return getSome( limit, offset, null, null);
	}

	@Override
	public List<Company> getSome( Long limit, Long offset,List<String> orderByCol) throws DaoException {
		return getSome( limit, offset,orderByCol, null);
	}

	
	@Override
	public List<Company> getSome( Long limit, Long offset, List<String> orderByCol, Order order ) throws DaoException {
		
		List<Company> list = new ArrayList<Company>();
		Connection conn = getConnection();
		PreparedStatement statement = createPreparedStatement(conn,preparedStatement.SELECT_SOME.getRequest());
		
		try {
			if ((orderByCol == null) || (orderByCol.size() == 0)){
				statement.setString(1,"id");
			} else {
				StringBuilder strgBuilder = new StringBuilder();
				for (String strg : orderByCol) {
					if (CompanyMapper.mapBDModel.containsKey(strg)) {
						if (strgBuilder.length() != 0) {
							strgBuilder.append(", ");
						}
						strgBuilder.append(CompanyMapper.mapBDModel.get(strg));
					} else {
						closeConnAndStat(statement, conn);
						throw new DaoException("Incorrect field for request : "+preparedStatement.SELECT_SOME.getRequest());
					}
				}
				statement.setString(1,strgBuilder.toString());
			}
			if (order == null) {
				statement.setString(2,"ASC"); 
			} else {
				switch (order) {
				case ASC:
					statement.setString(2,"ASC");
					break;
				case DESC:
					statement.setString(2,"DESC");
					break;
				default:
					closeConnAndStat(statement, conn);
					throw new DaoException("Invalid order for request : "+preparedStatement.SELECT_SOME.getRequest());
				}
			}
			if (limit == null) {
				statement.setLong(3,0); 
			} else {
				if (limit < 0) {
					closeConnAndStat(statement, conn);
					throw new DaoException("Invalid limit ("+limit+") for request : "+preparedStatement.SELECT_SOME.getRequest());
				} else {
					statement.setLong(3,limit); 
				}
			}
			if (offset == null) {
				statement.setLong(4,0); 
			} else {
				if (offset < 0) {
					closeConnAndStat(statement, conn);
					throw new DaoException("Invalid offset ("+offset+") for request : "+preparedStatement.SELECT_SOME.getRequest());
				} else {
					statement.setLong(4,offset); 
				}
			}
			ResultSet curs = statement.executeQuery();
			while ( curs.next() ) {
				list.add(CompanyMapper.INSTANCE.mapFromRow(curs));
    		}
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			closeConnAndStat(statement, conn);
		}
		return list;
	}
	
	

	@Override
	public List<Company> getSome(String nameFilter, Long limit, Long offset) throws DaoException {
		return getSome(nameFilter, limit, offset, null, null);
	}

	@Override
	public List<Company> getSome(String nameFilter,  Long limit, Long offset,List<String> orderByCol) throws DaoException {
		return getSome(nameFilter, limit, offset,orderByCol, null);
	}

	
	@Override
	public List<Company> getSome(String nameFilter,  Long limit, Long offset, List<String> orderByCol, Order order ) throws DaoException {
		
		ResultSet curs;
		List<Company> list = new ArrayList<Company>();
		Connection conn = getConnection();
		PreparedStatement statement = createPreparedStatement(conn,preparedStatement.SELECT_SOME_FILTERED.getRequest());
		
		if (nameFilter == null || nameFilter.isEmpty()) {
			return getSome(limit, offset,orderByCol, order);
		}
		try {
			Pattern patt = Pattern.compile("[a-zA-Z0-9]*");
			Matcher matcher = patt.matcher(nameFilter);
			
			if (matcher.matches()) {
				matcher.reset();
				statement.setString(1,nameFilter+'%');
			} else {
				throw new DaoException("Illegal value for LIKE "+nameFilter);
			}
			
			if ((orderByCol == null) || (orderByCol.size() == 0)){
				statement.setString(2,"id");
			} else {
				StringBuilder strgBuilder = new StringBuilder();
				for (String strg : orderByCol) {
					if (CompanyMapper.mapBDModel.containsKey(strg)) {
						if (strgBuilder.length() != 0) {
							strgBuilder.append(", ");
						}
						strgBuilder.append(CompanyMapper.mapBDModel.get(strg));
					} else {
						throw new DaoException("Incorrect field for request : "+preparedStatement.SELECT_SOME_FILTERED.getRequest());
					}
				}
				statement.setString(2,strgBuilder.toString());
			}
			if (order == null) {
				statement.setString(3,"ASC"); 
			} else {
				switch (order) {
				case ASC:
					statement.setString(3,"ASC");
					break;
				case DESC:
					statement.setString(3,"DESC");
					break;
				default:
					throw new DaoException("Invalid order for request : "+preparedStatement.SELECT_SOME_FILTERED.getRequest());
				}
			}
			if (limit == null) {
				statement.setLong(4,0); 
			} else {
				if (limit < 0) {
					throw new DaoException("Invalid limit ("+limit+") for request : "+preparedStatement.SELECT_SOME_FILTERED.getRequest());
				} else {
					statement.setLong(4,limit); 
				}
			}
			if (offset == null) {
				statement.setLong(5,0); 
			} else {
				if (offset < 0) {
					throw new DaoException("Invalid offset ("+offset+") for request : "+preparedStatement.SELECT_SOME_FILTERED.getRequest());
				} else {
					statement.setLong(5,offset); 
				}
			}
			curs = statement.executeQuery();
			while ( curs.next() ) {
				list.add(CompanyMapper.INSTANCE.mapFromRow(curs));
    		}
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			closeConnAndStat(statement, conn);
		}
		return list;
	}

	@Override
	public Company getById(Long id) throws DaoException {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		ResultSet curs;
		Company comp=null;
		Connection conn = getConnection();
		PreparedStatement statement = createPreparedStatement(conn,preparedStatement.SELECT_ONE.getRequest());
		try {
			statement.setLong(1,id);
			curs = statement.executeQuery();
			if ( curs.next() ) {
				comp = CompanyMapper.INSTANCE.mapFromRow(curs);
    		}
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			closeConnAndStat(statement, conn);
		}
		return comp;
	}
	
	private PreparedStatement createPreparedStatement (Connection conn, String request) {
		PreparedStatement statement = null;
		request.trim();
		try {
			conn = DaoManager.INSTANCE.getConnection();
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_CREATE_CONNECTION, e);
		}
		try {
			statement = conn.prepareStatement(request);
		} catch (SQLException e) {
			try {
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e2) {
				throw new DaoException(DaoException.CAN_NOT_CLOSE_CONNECTION,e2);
			}
			throw new DaoException(DaoException.CAN_NOT_CREATE_STATEMENT,e);
		}
		return statement;
	}
	
	private Statement createStatement (Connection conn) {
		Statement statement = null;
		try {
			conn = DaoManager.INSTANCE.getConnection();
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_CREATE_CONNECTION, e);
		}
		try {
			statement = conn.createStatement();
		} catch (SQLException e) {
			try {
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e2) {
				throw new DaoException(DaoException.CAN_NOT_CLOSE_CONNECTION,e2);
			}
			throw new DaoException(DaoException.CAN_NOT_CREATE_STATEMENT,e);
		}
		return statement;
	}
	
	private Connection getConnection () {
		Connection conn = null;
		try {
			conn = DaoManager.INSTANCE.getConnection();
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_CREATE_CONNECTION, e);
		}
		return conn;
	} 
	
	private void closeConnAndStat (Statement statement, Connection connection) throws DaoException{
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_CLOSE_STATEMENT,e);
		}
		try {
			if(connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_CLOSE_CONNECTION,e);
		}
	}
	
	
}
