package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
		SELECT_ONE ("SELECT id, name,introduced, discontinued, company_id FROM computer WHERE id=?;"),
		SELECT_SOME ("SELECT id, name,introduced, discontinued, company_id FROM computer ORDER BY ? ? LIMIT ? OFFSET ?"),
		INSERT_ONE ("INSERT INTO computer (name, introduced, discontinued,company_id) VALUES (?,?,?,?) ;"),
		UPDATE_ONE ("UPDATE computer SET name=?,introduced=?,discontinued=?,company_id=? WHERE id=?;"),
		DELETE_ONE ("DELETE FROM computer WHERE id=?;");
		
		private final String request;
		
		preparedStatement(String request) {
	        this.request = request;
	    }
	    private String getRequest() { return request; }
	}

	@Override
	public List<Computer> getAll(){
		
		ResultSet curs;
		List<Computer> list = new ArrayList<Computer>();
		Connection conn = null;
		try {
			conn = DaoManager.INSTANCE.getConnection();
			curs = conn.createStatement().executeQuery("SELECT id, name,introduced, discontinued, company_id FROM computer;");
			while ( curs.next() ) {
				list.add(ComputerMapper.INSTANCE.parseComputer(curs,
						CompanyDao.INSTANCE.getById(curs.getLong("company_id"))));
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
			curs = conn.createStatement().executeQuery("SELECT COUNT(id) FROM computer;");
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
	public List<Computer> getSome( List<String> orderByCol, Order order, Long limit, Long offset ){
		
		ResultSet curs;
		List<Computer> list = new ArrayList<Computer>();
		Connection conn = null;
		
		
		
		try {
			conn = DaoManager.INSTANCE.getConnection();
			PreparedStatement selectSomeComputer = conn.prepareStatement(preparedStatement.SELECT_SOME.getRequest());
			if ((orderByCol == null) || (orderByCol.size() == 0)){
				selectSomeComputer.setString(1,"id");
			} else {
				StringBuilder strgBuilder = new StringBuilder();
				for (String strg : orderByCol) {
					 if (ComputerMapper.mapBDModel.containsKey(strg)) {
						 if (strgBuilder.length() != 0) {
							 strgBuilder.append(", ");
						 }
						 strgBuilder.append(ComputerMapper.mapBDModel.get(strg));
					 } else {
						 throw new DaoException("Incorrect field for request : "+preparedStatement.SELECT_SOME.getRequest());
					 }
				}
				selectSomeComputer.setString(1,strgBuilder.toString());
			}
			if (order == null) {
				selectSomeComputer.setString(2,"ASC"); 
			} else {
				switch (order) {
				case ASC:
					selectSomeComputer.setString(2,"ASC");
					break;
				case DESC:
					selectSomeComputer.setString(2,"DESC");
					break;
				default:
					throw new DaoException("Invalid order for request : "+preparedStatement.SELECT_SOME.getRequest());
				}
			}
			if (limit == null) {
				selectSomeComputer.setLong(3,20);
			} else {
				if (limit < 0) {
					throw new DaoException("Invalid limit ("+limit+") for request : "+preparedStatement.SELECT_SOME.getRequest());
				} else {
					selectSomeComputer.setLong(3,limit); 
				}
			}
			if (offset == null) {
				selectSomeComputer.setLong(4,0); 
			} else {
				if (offset < 0) {
					throw new DaoException("Invalid offset ("+offset+") for request : "+preparedStatement.SELECT_SOME.getRequest());
				} else {
					selectSomeComputer.setLong(4,offset); 
				}
			}
			curs = selectSomeComputer.executeQuery();
			while ( curs.next() ) {
				list.add(ComputerMapper.INSTANCE.parseComputer(curs,
						CompanyDao.INSTANCE.getById(curs.getLong("company_id"))));
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
	public Computer getById(Long id) {
		ResultSet curs;
		Computer comp=null;
		Connection conn = null;
		try {
			conn = DaoManager.INSTANCE.getConnection();
			PreparedStatement selectOneComputer = conn.prepareStatement(preparedStatement.SELECT_ONE.getRequest());
			selectOneComputer.setLong(1,id);
			curs = selectOneComputer.executeQuery();
			if ( curs.next() ) {
				comp = ComputerMapper.INSTANCE.parseComputer(curs, 
						CompanyDao.INSTANCE.getById(curs.getLong("company_id")));
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
		return comp;
	}

	@Override
	public void save(Computer c) {
		boolean update=false;
		ResultSet curs;

		Connection conn = null;
		try {
			conn = DaoManager.INSTANCE.getConnection();
			PreparedStatement selectOneComputer = conn.prepareStatement(preparedStatement.SELECT_ONE.getRequest());
			selectOneComputer.setLong(1,c.getId());
			curs = selectOneComputer.executeQuery();
			int nb;
			if(curs.next())
			{
				update = true;
			}
			if(!update)
			{
				PreparedStatement insertOneComputer = conn.prepareStatement(preparedStatement.INSERT_ONE.getRequest(),
						PreparedStatement.RETURN_GENERATED_KEYS);
				insertOneComputer.setString(1,c.getName());
				if(c.getIntroductionDate() != null) {
					insertOneComputer.setTimestamp(2, Timestamp.valueOf(c.getIntroductionDate()));
				} else {
					insertOneComputer.setTimestamp(2, null);
				}
				if(c.getDiscontinuedDate() != null) {
					insertOneComputer.setTimestamp(3, Timestamp.valueOf(c.getDiscontinuedDate()));
				} else {
					insertOneComputer.setTimestamp(3, null);
				}
				if(c.getConstructor() != null) {
					insertOneComputer.setLong(4,c.getConstructor().getId());
				} else {
					insertOneComputer.setTimestamp(4, null);
				}
				nb = insertOneComputer.executeUpdate();
				ResultSet rs = insertOneComputer.getGeneratedKeys();

				if (rs.next()) {
					c.setId(rs.getLong(1));
				}
				
			}
			else
			{
				PreparedStatement updateOneComputer = conn.prepareStatement(preparedStatement.UPDATE_ONE.getRequest());
				updateOneComputer.setString(1,c.getName());
				if(c.getIntroductionDate() != null) {
					updateOneComputer.setTimestamp(2, Timestamp.valueOf(c.getIntroductionDate()));
				} else {
					updateOneComputer.setTimestamp(2, null);
				}
				if(c.getDiscontinuedDate() != null) {
					updateOneComputer.setTimestamp(3, Timestamp.valueOf(c.getDiscontinuedDate()));
				}else {
					updateOneComputer.setTimestamp(3, null);
				}
				if(c.getConstructor() != null) {
					updateOneComputer.setLong(4,c.getConstructor().getId());
				} else {
					updateOneComputer.setTimestamp(4, null);
				}
				updateOneComputer.setLong(5, c.getId());
				nb = updateOneComputer.executeUpdate();
			}
			if(nb==0)
			{
				throw new DaoException("Computer update not in database.");
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
	}

	@Override
	public void delete(Long id) {

		Connection conn = null;
		try {
			conn = DaoManager.INSTANCE.getConnection();
			PreparedStatement deleteOneComputer = conn.prepareStatement(preparedStatement.DELETE_ONE.getRequest());
			deleteOneComputer.setLong(1, id);
			int nb = deleteOneComputer.executeUpdate();
			if(nb==0)
			{
				throw new DaoException("Computer not in database.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new DaoException(e);
				}
			}
		}
		
	}
}
