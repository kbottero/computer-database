package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		SELECT_ALL_ORDERED ("SELECT id, name, introduced, discontinued, company_id FROM computer ORDER BY ? ?"),
		SELECT_SOME_ORDERED ("SELECT id, name, introduced, discontinued, company_id FROM computer ORDER BY ? ? LIMIT ? OFFSET ?"),
		SELECT_SOME_FILTERED ("SELECT id, name, introduced, discontinued, company_id FROM computer WHERE name LIKE ? ORDER BY ? ? LIMIT ? OFFSET ?"),
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
		
		ResultSet curs;
		List<Computer> list = new ArrayList<Computer>();
		Connection conn = null;
		Statement statement = null;
		try {
			conn = DaoManager.INSTANCE.getConnection();
			statement = conn.createStatement();
			curs = statement.executeQuery("SELECT id, name,introduced, discontinued, company_id FROM computer;");
			while ( curs.next() ) {
				list.add(ComputerMapper.INSTANCE.mapFromRow(curs));
    		}
		} catch (SQLException | NumberFormatException e) {
			throw new DaoException(e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if(conn != null) {
						conn.close();
				}
			} catch (SQLException e) {
				throw new DaoException(e);
			}
		}
		return list;
	}
	
	@Override
	public List<Computer> getAll(List<String> orderByCol) throws DaoException {
		return this.getAll(orderByCol, null);
	}
	
	@Override
	public List<Computer> getAll(List<String> orderByCol,
			Order order) throws DaoException {
		
		ResultSet curs;
		List<Computer> list = new ArrayList<Computer>();
		Connection conn = null;
		PreparedStatement selectAllOrdredComputer = null;
		try {
			conn = DaoManager.INSTANCE.getConnection();
			selectAllOrdredComputer = conn.prepareStatement(preparedStatement.SELECT_ALL_ORDERED.getRequest());
			//ORDER BY ?
			if ((orderByCol == null) || (orderByCol.size() == 0)){
				selectAllOrdredComputer.setString(1,"id");
			} else {
				StringBuilder strgBuilder = new StringBuilder();
				for (String strg : orderByCol) {
					 if (ComputerMapper.mapBDModel.containsKey(strg)) {
						 if (strgBuilder.length() != 0) {
							 strgBuilder.append(", ");
						 }
						 strgBuilder.append(ComputerMapper.mapBDModel.get(strg));
					 } else {
						 throw new DaoException("Incorrect field for request : "+preparedStatement.SELECT_ALL_ORDERED.getRequest());
					 }
				}
				selectAllOrdredComputer.setString(1,strgBuilder.toString());
			}
			//ASC or DESC ?
			if (order == null) {
				selectAllOrdredComputer.setString(2,"ASC"); 
			} else {
				switch (order) {
				case ASC:
					selectAllOrdredComputer.setString(2,"ASC");
					break;
				case DESC:
					selectAllOrdredComputer.setString(2,"DESC");
					break;
				default:
					throw new DaoException("Invalid order for request : "+preparedStatement.SELECT_ALL_ORDERED.getRequest());
				}
			}
			//Execute Request
			curs = selectAllOrdredComputer.executeQuery();
			//Mapping
			while ( curs.next() ) {
				list.add(ComputerMapper.INSTANCE.mapFromRow(curs));
    		}
		} catch (SQLException | NumberFormatException e) {
			throw new DaoException(e);
		} finally {
			try {
				if (selectAllOrdredComputer != null) {
					selectAllOrdredComputer.close();
				}
				if(conn != null) {
						conn.close();
				}
			} catch (SQLException e) {
				throw new DaoException(e);
			}
		}
		return list;
	}

	@Override
	public Long getNb() throws DaoException {
		
		ResultSet curs;
		Connection conn = null;
		Long nbElements = null;
		Statement statement = null;
		try {
			conn = DaoManager.INSTANCE.getConnection();
			statement = conn.createStatement();
			curs = statement.executeQuery("SELECT COUNT(id) FROM computer;");
			if ( curs.next() ) {
				nbElements = curs.getLong(1); 
    		}
		} catch (SQLException | NumberFormatException e) {
			throw new DaoException(e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if(conn != null) {
						conn.close();
				}
			} catch (SQLException e) {
				throw new DaoException(e);
			}
		}
		return nbElements;
	}
	
	@Override
	public List<Computer> getSome( Long limit, Long offset) throws DaoException {
		return getSome( limit, offset, null, null);
	}

	@Override
	public List<Computer> getSome( Long limit, Long offset,List<String> orderByCol) throws DaoException {
		return getSome( limit, offset,orderByCol, null);
	}

	@Override
	public List<Computer> getSome( Long limit, Long offset,List<String> orderByCol, Order order ) throws DaoException {
		
		ResultSet curs;
		List<Computer> list = new ArrayList<Computer>();
		Connection conn = null;
		PreparedStatement selectSomeComputer = null;
		try {
			conn = DaoManager.INSTANCE.getConnection();
			selectSomeComputer = conn.prepareStatement(preparedStatement.SELECT_SOME_ORDERED.getRequest());
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
						 throw new DaoException("Incorrect field for request : "+preparedStatement.SELECT_SOME_ORDERED.getRequest());
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
					throw new DaoException("Invalid order for request : "+preparedStatement.SELECT_SOME_ORDERED.getRequest());
				}
			}
			if (limit == null) {
				selectSomeComputer.setLong(3,20);
			} else {
				if (limit < 0) {
					throw new DaoException("Invalid limit ("+limit+") for request : "+preparedStatement.SELECT_SOME_ORDERED.getRequest());
				} else {
					selectSomeComputer.setLong(3,limit); 
				}
			}
			if (offset == null) {
				selectSomeComputer.setLong(4,0); 
			} else {
				if (offset < 0) {
					throw new DaoException("Invalid offset ("+offset+") for request : "+preparedStatement.SELECT_SOME_ORDERED.getRequest());
				} else {
					selectSomeComputer.setLong(4,offset); 
				}
			}
			curs = selectSomeComputer.executeQuery();
			while ( curs.next() ) {
				list.add(ComputerMapper.INSTANCE.mapFromRow(curs));
    		}
		} catch (SQLException | NumberFormatException e) {
			throw new DaoException(e);
		} finally {
			try {
				if (selectSomeComputer != null) {
					selectSomeComputer.close();
				}
				if(conn != null) {
						conn.close();
				}
			} catch (SQLException e) {
				throw new DaoException(e);
			}
		}
		return list;
	}
	

	@Override
	public List<Computer> getSome( String nameFilter, Long limit, Long offset) throws DaoException {
		return getSome(nameFilter, limit, offset, null, null);
	}

	@Override
	public List<Computer> getSome( String nameFilter, Long limit, Long offset,List<String> orderByCol) throws DaoException {
		return getSome(nameFilter, limit, offset,orderByCol, null);
	}

	@Override
	public List<Computer> getSome( String nameFilter, Long limit, Long offset,List<String> orderByCol, Order order ) throws DaoException {
		
		ResultSet curs;
		List<Computer> list = new ArrayList<Computer>();
		Connection conn = null;
		PreparedStatement selectSomeComputer = null;
		if (nameFilter == null || nameFilter.isEmpty()) {
			return getSome(limit, offset,orderByCol, order);
		}
		try {
			conn = DaoManager.INSTANCE.getConnection();
			selectSomeComputer = conn.prepareStatement(preparedStatement.SELECT_SOME_FILTERED.getRequest());

			Pattern patt = Pattern.compile("[a-zA-Z0-9]*");
			Matcher matcher = patt.matcher(nameFilter);
			
			if (matcher.matches()) {
				matcher.reset();
				selectSomeComputer.setString(1,nameFilter+'%');
			} else {
				throw new DaoException("Illegal value for LIKE "+nameFilter);
			}
			
			if ((orderByCol == null) || (orderByCol.size() == 0)){
				selectSomeComputer.setString(2,"id");
			} else {
				StringBuilder strgBuilder = new StringBuilder();
				for (String strg : orderByCol) {
					 if (ComputerMapper.mapBDModel.containsKey(strg)) {
						 if (strgBuilder.length() != 0) {
							 strgBuilder.append(", ");
						 }
						 strgBuilder.append(ComputerMapper.mapBDModel.get(strg));
					 } else {
						 throw new DaoException("Incorrect field for request : "+preparedStatement.SELECT_SOME_FILTERED.getRequest());
					 }
				}
				selectSomeComputer.setString(2,strgBuilder.toString());
			}
			if (order == null) {
				selectSomeComputer.setString(3,"ASC"); 
			} else {
				switch (order) {
				case ASC:
					selectSomeComputer.setString(3,"ASC");
					break;
				case DESC:
					selectSomeComputer.setString(3,"DESC");
					break;
				default:
					throw new DaoException("Invalid order for request : "+preparedStatement.SELECT_SOME_FILTERED.getRequest());
				}
			}
			if (limit == null) {
				selectSomeComputer.setLong(4,20);
			} else {
				if (limit < 0) {
					throw new DaoException("Invalid limit ("+limit+") for request : "+preparedStatement.SELECT_SOME_FILTERED.getRequest());
				} else {
					selectSomeComputer.setLong(4,limit); 
				}
			}
			if (offset == null) {
				selectSomeComputer.setLong(5,0); 
			} else {
				if (offset < 0) {
					throw new DaoException("Invalid offset ("+offset+") for request : "+preparedStatement.SELECT_SOME_FILTERED.getRequest());
				} else {
					selectSomeComputer.setLong(5,offset); 
				}
			}
			curs = selectSomeComputer.executeQuery();
			while ( curs.next() ) {
				list.add(ComputerMapper.INSTANCE.mapFromRow(curs));
    		}
		} catch (SQLException | NumberFormatException e) {
			throw new DaoException(e);
		} finally {
			try {
				if (selectSomeComputer != null) {
					selectSomeComputer.close();
				}
				if(conn != null) {
						conn.close();
				}
			} catch (SQLException e) {
				throw new DaoException(e);
			}
		}
		return list;
	}

	@Override
	public Computer getById(Long id) throws DaoException {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		ResultSet curs;
		Computer comp=null;
		Connection conn = null;
		PreparedStatement selectOneComputer = null;
		try {
			conn = DaoManager.INSTANCE.getConnection();
			selectOneComputer = conn.prepareStatement(preparedStatement.SELECT_ONE.getRequest());
			selectOneComputer.setLong(1,id);
			curs = selectOneComputer.executeQuery();
			if ( curs.next() ) {
				comp = ComputerMapper.INSTANCE.mapFromRow(curs);
    		}
		} catch (SQLException | NumberFormatException e) {
			throw new DaoException(e);
		} finally {
			if(conn != null) {
				try {
					if (selectOneComputer != null) {
						selectOneComputer.close();
					}
					if(conn != null) {
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
		boolean update=false;
		ResultSet curs;
		PreparedStatement updateOneComputer = null;
		PreparedStatement insertOneComputer = null;
		PreparedStatement selectOneComputer = null;
		Connection conn = null;
		try {
			conn = DaoManager.INSTANCE.getConnection();
			selectOneComputer = conn.prepareStatement(preparedStatement.SELECT_ONE.getRequest());
			selectOneComputer.setLong(1,c.getId());
			curs = selectOneComputer.executeQuery();
			int nb;
			if(curs.next())
			{
				update = true;
			}
			if(!update)
			{
				insertOneComputer = conn.prepareStatement(preparedStatement.INSERT_ONE.getRequest(),
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
				updateOneComputer = conn.prepareStatement(preparedStatement.UPDATE_ONE.getRequest());
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
				throw new DaoException("Can not Create or Update  the computer in database. id = "+c.getId());
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			try {
				if (updateOneComputer != null) {
					updateOneComputer.close();
				}
				if (insertOneComputer != null) {
					insertOneComputer.close();
				}
				if (selectOneComputer != null) {
					selectOneComputer.close();
				}
				if(conn != null) {
						conn.close();
				}
			} catch (SQLException e) {
				throw new DaoException(e);
			}
		}
	}

	@Override
	public void delete(Long id) throws DaoException {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		Connection conn = null;
		PreparedStatement deleteOneComputer = null;
		try {
			conn = DaoManager.INSTANCE.getConnection();
			deleteOneComputer = conn.prepareStatement(preparedStatement.DELETE_ONE.getRequest());
			deleteOneComputer.setLong(1, id);
			int nb = deleteOneComputer.executeUpdate();
			if(nb==0)
			{
				throw new DaoException("Computer not in database. id = "+id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (deleteOneComputer != null) {
					deleteOneComputer.close();
				}
				if(conn != null) {
						conn.close();
				}
			} catch (SQLException e) {
				throw new DaoException(e);
			}
		}
		
	}
}
