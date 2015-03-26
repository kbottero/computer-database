package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private static Logger logger = LoggerFactory.getLogger(ComputerDao.class);

	private static enum preparedStatement {
		COUNT_ALL ("SELECT COUNT(id) FROM computer;"),
		COUNT_ALL_FILTERED ("SELECT COUNT(id) FROM computer WHERE name LIKE ? ORDER BY"),
		SELECT_ALL ("SELECT id, name, introduced, discontinued, company_id FROM computer;"),
		SELECT_ALL_ORDERED ("SELECT id, name, introduced, discontinued, company_id FROM computer ORDER BY"),
		SELECT_SOME ("SELECT id, name, introduced, discontinued, company_id FROM computer ORDER BY"),
		SELECT_SOME_FILTERED ("SELECT id, name, introduced, discontinued, company_id FROM computer WHERE name LIKE ? ORDER BY"),
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
		logger.info("getAll() method");
		List<Computer> list = new ArrayList<Computer>();
		Connection conn =  DaoManager.INSTANCE.getConnection();
		Statement statement = DaoManager.INSTANCE.createStatement(conn);
		try {
			ResultSet curs = statement.executeQuery(preparedStatement.SELECT_ALL.getRequest());
			logger.debug("Excuted request : "+statement.toString());
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
		logger.info("getAll(param) method");

		StringBuilder request = new StringBuilder();
		List<Computer> list = new ArrayList<Computer>();
		Connection conn = DaoManager.INSTANCE.getConnection();	

		request.append(preparedStatement.SELECT_ALL_ORDERED.getRequest());
		request.append(" ");

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

		request.append(";");

		Statement statement = DaoManager.INSTANCE.createStatement(conn);
		logger.debug("Excuted request : "+statement.toString());
		try{
			//Execute Request
			ResultSet curs = statement.executeQuery( request.toString());
			//Mapping
			while ( curs.next() ) {
				list.add(ComputerMapper.INSTANCE.mapFromRow(curs));
			}
			curs.close();
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_CREATE_STATEMENT,e);
		} finally {
			DaoManager.INSTANCE.closeConnAndStat(statement, conn);
		}
		return list;
	}

	@Override
	public Long getNb() throws DaoException {
		logger.info("getNb() method");

		Connection conn =  DaoManager.INSTANCE.getConnection();
		Long nbElements = null;
		Statement statement = DaoManager.INSTANCE.createStatement(conn);
		try {
			ResultSet curs = statement.executeQuery(preparedStatement.COUNT_ALL.getRequest());
			logger.debug("Excuted request : "+statement.toString());
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
	public Long getNb(DaoRequestParameter param) throws DaoException {
		logger.info("getNb(param) method");
		if (param == null) {
			return getNb();
		}
		int numArg = 1;
		Connection conn =  DaoManager.INSTANCE.getConnection();
		Long nbElements = null;

		PreparedStatement statement;

		StringBuilder request = new StringBuilder();

		if (param.getNameLike() != null) {
			request.append(preparedStatement.COUNT_ALL_FILTERED.getRequest());
			request.append(" ");
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
			request.append(" LIMIT ? OFFSET ?;");
			statement = DaoManager.INSTANCE.createPreparedStatement(conn, request.toString());
			try {
				if (param.getNameLike() != null) {

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
			} catch (SQLException e) {
				DaoManager.INSTANCE.closeConnAndStat(statement, conn);
				throw new DaoException(DaoException.CAN_NOT_SET_PREPAREDSTATEMENT,e);
			}
		} else {
			request.append(preparedStatement.COUNT_ALL.getRequest());
			statement = DaoManager.INSTANCE.createPreparedStatement(conn, request.toString());
		}

		try {
			ResultSet curs;
			curs = statement.executeQuery();

			logger.debug("Excuted request : "+statement.toString());
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
		logger.info("getSome(param) method");
		int numArg = 1;
		List<Computer> list = new ArrayList<Computer>();
		Connection conn = DaoManager.INSTANCE.getConnection();
		PreparedStatement statement = null;

		StringBuilder request = new StringBuilder();

		if (param.getNameLike() != null) {
			request.append(preparedStatement.SELECT_SOME_FILTERED.getRequest());	
		} else {
			request.append(preparedStatement.SELECT_SOME.getRequest());
		}

		request.append(" ");

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
		request.append(" ");
		request.append(" LIMIT ? OFFSET ?;");
		
		statement = DaoManager.INSTANCE.createPreparedStatement(conn,request.toString());

		try {
			if (param.getNameLike() != null) {

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
		} catch (SQLException e) {
			DaoManager.INSTANCE.closeConnAndStat(statement, conn);
			throw new DaoException(DaoException.CAN_NOT_SET_PREPAREDSTATEMENT,e);
		}
		try{
			ResultSet curs = statement.executeQuery();
			logger.debug("Excuted request : "+statement.toString());
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
	public Computer getById(Long id) throws DaoException {
		logger.info("getById(id) method");
		if (id == null) {
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		Computer comp=null;
		Connection conn = DaoManager.INSTANCE.getConnection();
		PreparedStatement statement = DaoManager.INSTANCE.createPreparedStatement(conn, preparedStatement.SELECT_ONE.getRequest());
		try {
			statement.setLong(1,id);
			ResultSet curs = statement.executeQuery();
			logger.debug("Excuted request : "+statement.toString());
			if ( curs.next() ) {
				comp = ComputerMapper.INSTANCE.mapFromRow(curs);
			} else {
				throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT);
			}
			curs.close();
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT,e);
		} finally {
			DaoManager.INSTANCE.closeConnAndStat(statement, conn);
		}
		return comp;
	}

	@Override
	public void save(Computer computer) throws DaoException {
		logger.info("save(computer) method");
		if (computer == null) {
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		int nb;
		boolean update=false;
		Connection conn = DaoManager.INSTANCE.getConnection();
		PreparedStatement statement = DaoManager.INSTANCE.createPreparedStatement(conn, preparedStatement.SELECT_ONE.getRequest());
		try {
			statement.setLong(1,computer.getId());
			ResultSet curs = statement.executeQuery();
			if (curs.next()) {
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
				if (computer.getName() != null) {
					statement.setString(1,computer.getName());
				} else {
					throw new DaoException(DaoException.INVALID_ARGUMENT);
				}
				if (computer.getIntroductionDate() != null) {
					statement.setTimestamp(2, Timestamp.valueOf(computer.getIntroductionDate()));
				} else {
					statement.setTimestamp(2, null);
				}
				if (computer.getDiscontinuedDate() != null) {
					statement.setTimestamp(3, Timestamp.valueOf(computer.getDiscontinuedDate()));
				} else {
					statement.setTimestamp(3, null);
				}
				if (computer.getConstructor() != null) {
					statement.setLong(4,computer.getConstructor().getId());
				} else {
					statement.setTimestamp(4, null);
				}
				nb = statement.executeUpdate();
				if (nb==0) {
					throw new DaoException(DaoException.CAN_NOT_INSERT_ELEMENT);
				}
				ResultSet rs = statement.getGeneratedKeys();

				if (rs.next()) {
					computer.setId(rs.getLong(1));
				}
				rs.close();

			} else {
				statement = DaoManager.INSTANCE.createPreparedStatement(conn, preparedStatement.UPDATE_ONE.getRequest());
				statement.setString(1,computer.getName());
				if (computer.getIntroductionDate() != null) {
					statement.setTimestamp(2, Timestamp.valueOf(computer.getIntroductionDate()));
				} else {
					statement.setTimestamp(2, null);
				}
				if (computer.getDiscontinuedDate() != null) {
					statement.setTimestamp(3, Timestamp.valueOf(computer.getDiscontinuedDate()));
				}else {
					statement.setTimestamp(3, null);
				}
				if (computer.getConstructor() != null) {
					statement.setLong(4,computer.getConstructor().getId());
				} else {
					statement.setTimestamp(4, null);
				}
				statement.setLong(5, computer.getId());
				nb = statement.executeUpdate();
				logger.debug("Excuted request : "+statement.toString());
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
		logger.info("delete(id) method");
		if (id == null) {
			throw new IllegalArgumentException();
		}
		Connection conn = DaoManager.INSTANCE.getConnection();
		PreparedStatement statement = DaoManager.INSTANCE.createPreparedStatement(conn, preparedStatement.DELETE_ONE.getRequest());
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
			DaoManager.INSTANCE.closeConnAndStat(statement, conn);
		}
	}
}
