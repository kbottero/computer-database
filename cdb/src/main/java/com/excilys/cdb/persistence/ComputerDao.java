package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Computer;

/**
 * ComputerDao
 * @author Kevin Bottero
 *
 */
public enum ComputerDao  implements IDao<Computer, Long> {
	
	INSTANCE;
	
	private static enum preparedStatement {
		COUNT_ALL ("SELECT COUNT(id) FROM computer;"),
		SELECT_ALL ("SELECT id, name, introduced, discontinued, company_id FROM computer;"),
		SELECT_ALL_ORDERED ("SELECT id, name, introduced, discontinued, company_id FROM computer ORDER BY ? ?;"),
		SELECT_SOME ("SELECT id, name, introduced, discontinued, company_id FROM computer ORDER BY ? ? LIMIT ? OFFSET ?;"),
		SELECT_SOME_FILTERED ("SELECT id, name, introduced, discontinued, company_id FROM computer WHERE name LIKE ? ORDER BY ? ? LIMIT ? OFFSET ?;"),
		SELECT_ONE ("SELECT id, name,introduced, discontinued, company_id FROM computer WHERE id=?;"),
		INSERT_ONE ("INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?,?,?,?) ;"),
		UPDATE_ONE ("UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE id=?;"),
		DELETE_ONE ("DELETE FROM computer WHERE id=?;");
		
		private final String request;
		
		preparedStatement(String request) {
	        this.request = request;
	    }
	    private String getRequest() { return request; }
	}

	@Override
	public List<Computer> getAll() throws DaoException {
		List<Computer> list = new ArrayList<Computer>();
		Connection conn =  DaoManager.INSTANCE.getConnection();
		Statement statement = DaoManager.INSTANCE.createStatement(conn);
		try {
			ResultSet curs = statement.executeQuery(preparedStatement.SELECT_ALL.getRequest());
			while ( curs.next() ) {
				list.add(ComputerMapper.INSTANCE.mapFromRow(curs));
    		}
			curs.close();
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT,e);
		} finally {
				DaoManager.INSTANCE.closeConnAndStat(statement, conn);
		}
		return list;
	}
	
	@Override
	public List<Computer> getAll(DaoRequestParameter param) throws DaoException {
		
		List<Computer> list = new ArrayList<Computer>();
		Connection conn = DaoManager.INSTANCE.getConnection();
		PreparedStatement statement = DaoManager.INSTANCE.createPreparedStatement(conn, preparedStatement.SELECT_ALL_ORDERED.getRequest());
		try {
			//ORDER BY ?
			if ((param.getColToOrderBy() == null) || (param.getColToOrderBy().size() == 0)){
				statement.setString(1,ComputerMapper.DEFAULT_ID);
			} else {
				StringBuilder strgBuilder = new StringBuilder();
				for (String strg : param.getColToOrderBy()) {
					 if (ComputerMapper.mapBDModel.containsKey(strg)) {
						 if (strgBuilder.length() != 0) {
							 strgBuilder.append(", ");
						 }
						 strgBuilder.append(ComputerMapper.mapBDModel.get(strg));
					 } else {
						 throw new DaoException(DaoException.INVALID_ARGUMENT);
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
				list.add(ComputerMapper.INSTANCE.mapFromRow(curs));
    		}
			curs.close();
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_SET_PREPAREDSTATEMENT,e);
		} finally {
			 DaoManager.INSTANCE.closeConnAndStat(statement, conn);
		}
		return list;
	}

	@Override
	public Long getNb() throws DaoException {
		
		Connection conn =  DaoManager.INSTANCE.getConnection();
		Long nbElements = null;
		Statement statement = DaoManager.INSTANCE.createStatement(conn);
		try {
			ResultSet curs = statement.executeQuery(preparedStatement.COUNT_ALL.getRequest());
			if ( curs.next() ) {
				nbElements = curs.getLong(1); 
    		}
			curs.close();
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT, e);
		} finally {
			DaoManager.INSTANCE.closeConnAndStat(statement, conn);
		}
		return nbElements;
	}
	
	@Override
	public List<Computer> getSome(DaoRequestParameter param) throws DaoException {
		int numArg = 1;
		List<Computer> list = new ArrayList<Computer>();
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
				statement.setString(numArg++,ComputerMapper.DEFAULT_ID);
			} else {
				StringBuilder strgBuilder = new StringBuilder();
				for (String strg : param.getColToOrderBy()) {
					if (ComputerMapper.mapBDModel.containsKey(strg)) {
						if (strgBuilder.length() != 0) {
							strgBuilder.append(", ");
						}
						strgBuilder.append(ComputerMapper.mapBDModel.get(strg));
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
			ResultSet curs = statement.executeQuery();
			while ( curs.next() ) {
				list.add(ComputerMapper.INSTANCE.mapFromRow(curs));
    		}
			curs.close();
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			DaoManager.INSTANCE.closeConnAndStat(statement, conn);
		}
		return list;
	}

	@Override
	public Computer getById(Long id) throws DaoException {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		Computer comp=null;
		Connection conn = DaoManager.INSTANCE.getConnection();
		PreparedStatement selectOneComputer = DaoManager.INSTANCE.createPreparedStatement(conn, preparedStatement.SELECT_ONE.getRequest());
		try {
			selectOneComputer.setLong(1,id);
			ResultSet curs = selectOneComputer.executeQuery();
			if ( curs.next() ) {
				comp = ComputerMapper.INSTANCE.mapFromRow(curs);
    		}
			curs.close();
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT,e);
		} finally {
			if (conn != null) {
				try {
					if (selectOneComputer != null) {
						selectOneComputer.close();
					}
					if (conn != null) {
							conn.close();
					}
				} catch (SQLException e) {
					throw new DaoException(e);
				}
			}
		}
		return comp;
	}

	@Override
	public void save(Computer c) throws DaoException {
		if (c == null) {
			throw new IllegalArgumentException();
		}
		int nb;
		boolean update=false;
		Connection conn = DaoManager.INSTANCE.getConnection();
		PreparedStatement statement = DaoManager.INSTANCE.createPreparedStatement(conn, preparedStatement.SELECT_ONE.getRequest());
		try {
			statement.setLong(1,c.getId());
			ResultSet curs = statement.executeQuery();
			if (curs.next())	{
				update = true;
			}
			curs.close();
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT,e);
		}
		try {
			statement.close();
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_CLOSE_STATEMENT,e);
		}
		try{
			if (!update)
			{
				statement = DaoManager.INSTANCE.createPreparedStatement(conn, preparedStatement.INSERT_ONE.getRequest(),
						PreparedStatement.RETURN_GENERATED_KEYS);
				statement.setString(1,c.getName());
				if (c.getIntroductionDate() != null) {
					statement.setTimestamp(2, Timestamp.valueOf(c.getIntroductionDate()));
				} else {
					statement.setTimestamp(2, null);
				}
				if (c.getDiscontinuedDate() != null) {
					statement.setTimestamp(3, Timestamp.valueOf(c.getDiscontinuedDate()));
				} else {
					statement.setTimestamp(3, null);
				}
				if (c.getConstructor() != null) {
					statement.setLong(4,c.getConstructor().getId());
				} else {
					statement.setTimestamp(4, null);
				}
				nb = statement.executeUpdate();
				if (nb==0) {
					throw new DaoException(DaoException.CAN_NOT_INSERT_ELEMENT);
				}
				ResultSet rs = statement.getGeneratedKeys();

				if (rs.next()) {
					c.setId(rs.getLong(1));
				}
				rs.close();
				
			} else {
				statement = DaoManager.INSTANCE.createPreparedStatement(conn, preparedStatement.UPDATE_ONE.getRequest());
				statement.setString(1,c.getName());
				if (c.getIntroductionDate() != null) {
					statement.setTimestamp(2, Timestamp.valueOf(c.getIntroductionDate()));
				} else {
					statement.setTimestamp(2, null);
				}
				if (c.getDiscontinuedDate() != null) {
					statement.setTimestamp(3, Timestamp.valueOf(c.getDiscontinuedDate()));
				}else {
					statement.setTimestamp(3, null);
				}
				if (c.getConstructor() != null) {
					statement.setLong(4,c.getConstructor().getId());
				} else {
					statement.setTimestamp(4, null);
				}
				statement.setLong(5, c.getId());
				nb = statement.executeUpdate();
				if (nb==0) {
					throw new DaoException(DaoException.CAN_NOT_UPDATE_ELEMENT);
				}
			}
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_SET_PREPAREDSTATEMENT,e);
		} finally {
			DaoManager.INSTANCE.closeConnAndStat(statement, conn);
		}
	}

	@Override
	public void delete(Long id) throws DaoException {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		Connection conn = DaoManager.INSTANCE.getConnection();
		PreparedStatement statement = DaoManager.INSTANCE.createPreparedStatement(conn, preparedStatement.DELETE_ONE.getRequest());
		try {
			statement.setLong(1, id);
			int nb = statement.executeUpdate();
			if (nb==0) {
				throw new DaoException(DaoException.CAN_NOT_DELETE_ELEMENT);
			}
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_DELETE_ELEMENT,e);
		} finally {
			DaoManager.INSTANCE.closeConnAndStat(statement, conn);
		}
		
	}
}
