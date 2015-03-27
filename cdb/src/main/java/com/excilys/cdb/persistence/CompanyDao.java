package com.excilys.cdb.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Company;

/**
 * CompanyDao
 * @author Kevin Bottero
 *
 */
public enum CompanyDao implements IDao<Company, Long>{
	INSTANCE;
	
	private static Logger logger = LoggerFactory.getLogger(CompanyDao.class);
	
	private static enum preparedStatement {
		COUNT_ALL ("SELECT COUNT(id) FROM company;"),
		SELECT_ALL ("SELECT id, name FROM company;"),
		SELECT_ALL_ORDERED ("SELECT id, name FROM company ORDER BY"),
		SELECT_ONE ("SELECT id, name FROM company WHERE id=?;"),
		SELECT_SOME ("SELECT id, name FROM company ORDER BY"),
		SELECT_SOME_FILTERED ("SELECT id, name FROM company WHERE name LIKE ? ORDER BY"), 
		DELETE_COMPUTER ("DELETE FROM computer WHERE company_id=?;"),
		DELETE_ONE("DELETE FROM company WHERE id=?;");
		
		private final String request;
		
		preparedStatement(String request) {
	        this.request = request;
	    }
	    private String getRequest() { return request; }
	}
	
	@Override
	public List<Company> getAll() throws DaoException {
		logger.info("getAll() method");
		List<Company> list = new ArrayList<Company>();
		Statement statement = TransactionFactory.INSTANCE.createStatement();
		try {
			ResultSet curs = statement.executeQuery(preparedStatement.SELECT_ALL.getRequest());
			logger.info("Excuted request : "+preparedStatement.SELECT_ALL.getRequest());
			while ( curs.next() ) {
				list.add(CompanyMapper.INSTANCE.mapFromRow(curs));
    		}
			curs.close();
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT,e);
		} finally {
			TransactionFactory.INSTANCE.closeStat(statement);
		}
		return list;
	}
	
	@Override
	public List<Company> getAll(DaoRequestParameter param) throws DaoException {
		logger.info("getAll(param) method");
		StringBuilder request = new StringBuilder();
		List<Company> list = new ArrayList<Company>();
		

		request.append(preparedStatement.SELECT_ALL_ORDERED.getRequest());
		request.append(" ");
		addOrderByToRequest(request, param);
		request.append(";");
		Statement statement = TransactionFactory.INSTANCE.createStatement();
		
		try {
			//Execute Request
			ResultSet curs = statement.executeQuery(request.toString());
			logger.info("Excuted request : "+request.toString());
			//Mapping
			while ( curs.next() ) {
				list.add(CompanyMapper.INSTANCE.mapFromRow(curs));
			}
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_SET_PREPAREDSTATEMENT,e);
		} finally {
			TransactionFactory.INSTANCE.closeStat(statement);
		}
		return list;
	}

	@Override
	public Long getNb() throws DaoException {
		logger.info("getNb() method");
		Long nbElements;
		
		Statement statement = TransactionFactory.INSTANCE.createStatement();
		try {
			ResultSet curs = statement.executeQuery(preparedStatement.COUNT_ALL.getRequest());
			logger.debug("Executed request : "+preparedStatement.COUNT_ALL.getRequest());
			if ( curs.next() ) {
				nbElements = curs.getLong(1); 
    		} else {
    			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT);
    		}
			curs.close();
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT,e);
		} finally {
			TransactionFactory.INSTANCE.closeStat(statement);
		}
		return nbElements;
	}
	
	@Override
	public List<Company> getSome(DaoRequestParameter param) throws DaoException {
		logger.info("getSome(param) method");
		int numArg = 1;
		List<Company> list = new ArrayList<Company>();
		
		PreparedStatement statement = null;
		
		StringBuilder request = new StringBuilder();

		if (param.getNameLike() != null) {
			request.append(preparedStatement.SELECT_SOME_FILTERED.getRequest());	
		} else {
			request.append(preparedStatement.SELECT_SOME.getRequest());
		}
		request.append(" ");
		addOrderByToRequest(request, param);
		request.append(" ");
		request.append(" LIMIT ? OFFSET ?;");
		
		statement = TransactionFactory.INSTANCE.createPreparedStatement(request.toString());
		try {
			if (param.getNameLike() != null) {			
				numArg = setWhenCondition (statement,param, numArg);
			}
			if (param.getLimit() == null) {
				TransactionFactory.INSTANCE.closeStat(statement);
				throw new DaoException(DaoException.INVALID_ARGUMENT);
			} else {
				if (param.getLimit() < 0) {
					TransactionFactory.INSTANCE.closeStat(statement);
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
			ResultSet curs = statement.executeQuery();
			logger.debug("Excuted request : "+request.toString());
			while ( curs.next() ) {
				list.add(CompanyMapper.INSTANCE.mapFromRow(curs));
    		}
			curs.close();
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT,e);
		} finally {
			TransactionFactory.INSTANCE.closeStat(statement);
		}
		return list;
	}

	@Override
	public Company getById(Long id) throws DaoException {
		logger.info("getById(id) method");
		if (id == null) {
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		Company comp=null;
		
		PreparedStatement statement = TransactionFactory.INSTANCE.createPreparedStatement(preparedStatement.SELECT_ONE.getRequest());
		try {
			statement.setLong(1,id);
			ResultSet curs = statement.executeQuery();
			logger.debug("Excuted request : "+preparedStatement.SELECT_ONE.getRequest());
			if ( curs.next() ) {
				comp = CompanyMapper.INSTANCE.mapFromRow(curs);
    		} else {
    			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT);
    		}
			curs.close();
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			TransactionFactory.INSTANCE.closeStat(statement);
		}
		return comp;
	}
	
	@Override
	public void delete(Long id)  throws DaoException {
		logger.info("delete(id) method");
		if (id == null) {
			throw new IllegalArgumentException();
		}
		
		PreparedStatement statement = TransactionFactory.INSTANCE.createPreparedStatement( preparedStatement.DELETE_ONE.getRequest());
		logger.debug("Excuted request : "+statement.toString());
		try {
			statement.setLong(1, id);
			int nb = statement.executeUpdate();
			if (nb==0) {
				throw new DaoException(DaoException.CAN_NOT_DELETE_ELEMENT);
			}
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_DELETE_ELEMENT,e);
		} finally {
			TransactionFactory.INSTANCE.closeStat(statement);
		}
	}
	
	public void addOrderByToRequest(StringBuilder request, DaoRequestParameter param) throws DaoException {
		if ((param.getColToOrderBy() == null) || (param.getColToOrderBy().size() == 0)){
			request.append(ComputerMapper.DEFAULT_ID);
			request.append(" ");
			//ASC or DESC ?
			if (param.getOrder() == null) {
				request.append("ASC");
			} else {
				switch (param.getOrder()) {
				case ASC:
					request.append("ASC");
					break;
				case DESC:
					request.append("DESC");
					break;
				default:
					throw new DaoException(DaoException.INVALID_ARGUMENT);
				}
			}
		} else {
			StringBuilder strgBuild = new StringBuilder();
			for (String strg : param.getColToOrderBy()) {
				if (ComputerMapper.mapBDModel.containsKey(strg)) {
					if (strgBuild.length() != 0) {
						strgBuild.append(",");
						strgBuild.append(ComputerMapper.mapBDModel.get(strg));
					} else {
						strgBuild.append(ComputerMapper.mapBDModel.get(strg));
						strgBuild.append(" ");
						//ASC or DESC ?
						if (param.getOrder() == null) {
							strgBuild.append("ASC");
						} else {
							switch (param.getOrder()) {
							case ASC:
								strgBuild.append("ASC");
								break;
							case DESC:
								strgBuild.append("DESC");
								break;
							default:
								throw new DaoException(DaoException.INVALID_ARGUMENT);
							}
						}
					}
				} else {
					throw new DaoException(DaoException.INVALID_ARGUMENT);
				}
			}
			request.append(strgBuild.toString());
		}
	}
	
	public Integer setWhenCondition (PreparedStatement statement,DaoRequestParameter param, Integer numArg) throws SQLException {
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
		return numArg;
	}
	
	
}
