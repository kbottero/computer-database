package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
		Connection conn  = DaoManager.INSTANCE.getConnection();
		Statement statement = DaoManager.INSTANCE.createStatement(conn);
		try {
			ResultSet curs = statement.executeQuery(preparedStatement.SELECT_ALL.getRequest());
			while ( curs.next() ) {
				list.add(CompanyMapper.INSTANCE.mapFromRow(curs));
    		}
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT,e);
		} finally {
			DaoManager.INSTANCE.closeConnAndStat(statement, conn);
		}
		return list;
	}
	
	@Override
	public List<Company> getAll(DaoRequestParameter param) throws DaoException {
		List<Company> list = new ArrayList<Company>();
		Connection conn = DaoManager.INSTANCE.getConnection();
		PreparedStatement statement = DaoManager.INSTANCE.createPreparedStatement(conn,preparedStatement.SELECT_ALL_ORDERED.getRequest());

		try {
			//ORDER BY ?
			if ((param.getColToOrderBy() == null) || (param.getColToOrderBy().size() == 0)){
				statement.setString(1,CompanyMapper.DEFAULT_ID);
			} else {
				StringBuilder strgBuilder = new StringBuilder();
				for (String strg : param.getColToOrderBy()) {
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
			if (param.getOrder() == null) {
				statement.setString(2,"ASC"); 
			} else {
				switch (param.getOrder()) {
				case ASC:
					statement.setString(2,"ASC");
					break;
				case DESC:
					statement.setString(2,"DESC");
					break;
				default:
					throw new DaoException(DaoException.INVALID_ARGUMENT);
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
			DaoManager.INSTANCE.closeConnAndStat(statement, conn);
		}
		return list;
	}

	@Override
	public Long getNb() throws DaoException {
		Long nbElements;
		Connection conn = DaoManager.INSTANCE.getConnection();
		Statement statement = DaoManager.INSTANCE.createStatement(conn);
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
			DaoManager.INSTANCE.closeConnAndStat(statement, conn);
		}
		return nbElements;
	}
	
	@Override
	public List<Company> getSome(DaoRequestParameter param) throws DaoException {
		ResultSet curs;
		int numArg = 1;
		List<Company> list = new ArrayList<Company>();
		Connection conn = DaoManager.INSTANCE.getConnection();
		PreparedStatement statement = null;
		try {
			if (param.getNameLike() != null) {
				statement = DaoManager.INSTANCE.createPreparedStatement(conn,preparedStatement.SELECT_SOME_FILTERED.getRequest());
				
					StringBuilder filter = new StringBuilder();
					switch (param.getNameFiltering()) {
					case POST:
						filter.append(param.getNameLike());
						filter.append('%');
						break;
					case PRE:
						filter.append('%');
						filter.append(param.getNameLike());
						break;
					case POST_AND_PRE:
						filter.append('%');
						filter.append(param.getNameLike());
						filter.append('%');
						break;
					case NONE:
						filter.append(param.getNameLike());
						break;
						default:
							throw new DaoException(DaoException.INVALID_ARGUMENT);
					}
					statement.setString(numArg++,filter.toString());
			
			} else {		
				statement = DaoManager.INSTANCE.createPreparedStatement(conn,preparedStatement.SELECT_SOME.getRequest());
			}
			if ((param.getColToOrderBy() == null) || (param.getColToOrderBy().size() == 0)){
				statement.setString(numArg++,CompanyMapper.DEFAULT_ID);
			} else {
				StringBuilder strgBuilder = new StringBuilder();
				for (String strg : param.getColToOrderBy()) {
					if (CompanyMapper.mapBDModel.containsKey(strg)) {
						if (strgBuilder.length() != 0) {
							strgBuilder.append(", ");
						}
						strgBuilder.append(CompanyMapper.mapBDModel.get(strg));
					} else {
						throw new DaoException(DaoException.INVALID_ARGUMENT);
					}
				}
				statement.setString(numArg++,strgBuilder.toString());
			}
			switch (param.getOrder()) {
			case ASC:
				statement.setString(numArg++,"ASC");
				break;
			case DESC:
				statement.setString(numArg++,"DESC");
				break;
			default:
				throw new DaoException(DaoException.INVALID_ARGUMENT);
			}
			if (param.getLimit() == null) {
				throw new DaoException(DaoException.INVALID_ARGUMENT);
			} else {
				if (param.getLimit() < 0) {
					throw new DaoException(DaoException.INVALID_ARGUMENT);
				} else {
					statement.setLong(numArg++,param.getLimit()); 
				}
			}
			if (param.getOffset() == null) {
				statement.setLong(numArg++,0); 
			} else {
				if (param.getOffset() < 0) {
					throw new DaoException(DaoException.INVALID_ARGUMENT);
				} else {
					statement.setLong(numArg++,param.getOffset()); 
				}
			}
			curs = statement.executeQuery();
			while ( curs.next() ) {
				list.add(CompanyMapper.INSTANCE.mapFromRow(curs));
    		}
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			DaoManager.INSTANCE.closeConnAndStat(statement, conn);
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
		Connection conn = DaoManager.INSTANCE.getConnection();
		PreparedStatement statement = DaoManager.INSTANCE.createPreparedStatement(conn,preparedStatement.SELECT_ONE.getRequest());
		try {
			statement.setLong(1,id);
			curs = statement.executeQuery();
			if ( curs.next() ) {
				comp = CompanyMapper.INSTANCE.mapFromRow(curs);
    		}
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			DaoManager.INSTANCE.closeConnAndStat(statement, conn);
		}
		return comp;
	}
	
	
	
	
	
}
