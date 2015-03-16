package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public class ComputerDaoImpl  implements IDao<Computer> {
		
	private Connection conn;
	private PreparedStatement selectAllComputer;
	private PreparedStatement selectOneComputer;
	private PreparedStatement deleteOneComputer;
	private PreparedStatement updateOneComputer;
	private PreparedStatement insertOneComputer;
	
	private static enum preparedStatement {
		SELECT_ALL ("SELECT * FROM computer;"),
		SELECT_ONE ("SELECT * FROM computer WHERE id=?;"),
		INSERT_ONE ("INSERT INTO computer (name, introduced, discontinued,company_id) VALUES (?,?,?,?) ;"),
		UPDATE_ONE ("UPDATE computer SET name=?,introduced=?,discontinued=?,company_id=? WHERE id=?;"),
		DELETE_ONE ("DELETE FROM computer WHERE id=?;");
		
		private final String request;
		
		preparedStatement(String request) {
	        this.request = request;
	    }
	    private String getRequest() { return request; }
	}
	
	public ComputerDaoImpl() {
		try {
			conn = DaoManager.INSTANCE.getConnection();
			initPreparedStatement(conn);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ComputerDaoImpl(Connection conn) {
		try {
			this.conn = conn;
			
			initPreparedStatement(conn);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void initPreparedStatement(Connection conn) throws SQLException {
		selectAllComputer = conn.prepareStatement(preparedStatement.SELECT_ALL.getRequest());
		
		selectOneComputer = conn.prepareStatement(preparedStatement.SELECT_ONE.getRequest());
		
		deleteOneComputer = conn.prepareStatement(preparedStatement.DELETE_ONE.getRequest());

		insertOneComputer = conn.prepareStatement(preparedStatement.INSERT_ONE.getRequest(),
								PreparedStatement.RETURN_GENERATED_KEYS);
		
		updateOneComputer = conn.prepareStatement(preparedStatement.UPDATE_ONE.getRequest());
	}
	
	@Override
	protected void finalize() {
		
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			
			selectAllComputer.close();
			selectOneComputer.close();
			deleteOneComputer.close();
			insertOneComputer.close();
			updateOneComputer.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Computer> getAll(){
		
		ResultSet curs;
		List<Computer> list = new ArrayList<Computer>();
		
		try {
			curs = selectAllComputer.executeQuery();
			while ( curs.next() ) {
				list.add(ComputerMapper.INSTANCE.parseComputer(curs, getOneCompany(curs.getLong("company_id"))));
    		}
		} catch (SQLException | NumberFormatException e) {
			////////////////////////////////////////////////////////
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Computer getOne(long id) {
		ResultSet curs;
		Computer comp=null;
		try {
			selectOneComputer.setLong(1,id);
			curs = selectOneComputer.executeQuery();
			if ( curs.next() ) {
				comp = ComputerMapper.INSTANCE.parseComputer(curs, getOneCompany(curs.getLong("company_id")));
    		}
		} catch (SQLException | NumberFormatException e) {
			////////////////////////////////////////////////////////
			e.printStackTrace();
		}
		return comp;
	}

	@Override
	public void saveOne(Computer c) {
		boolean update=false;
		ResultSet curs;

		try {
			
			selectOneComputer.setLong(1,c.getId());
			curs = selectOneComputer.executeQuery();
			int nb;
			if(curs.next())
			{
				update = true;
			}

			if(!update)
			{
				insertOneComputer.setString(1,c.getName());
				if(c.getIntroductionDate() != null) {
					insertOneComputer.setTimestamp(2, new java.sql.Timestamp(c.getIntroductionDate().getTime()));
				} else {
					insertOneComputer.setTimestamp(2, null);
				}
				if(c.getDiscontinuedDate() != null) {
					insertOneComputer.setTimestamp(3, new java.sql.Timestamp(c.getDiscontinuedDate().getTime()));
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
				updateOneComputer.setString(1,c.getName());
				if(c.getIntroductionDate() != null) {
					updateOneComputer.setTimestamp(2, new java.sql.Timestamp(c.getIntroductionDate().getTime()));
				} else {
					updateOneComputer.setTimestamp(2, null);
				}
				if(c.getDiscontinuedDate() != null) {
					updateOneComputer.setTimestamp(3, new java.sql.Timestamp(c.getDiscontinuedDate().getTime()));
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
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public void deleteOne(long id) {
		try {
			deleteOneComputer.setLong(1, id);
			int nb = deleteOneComputer.executeUpdate();
			if(nb==0)
			{
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public Company getOneCompany(long id) {
		CompanyDaoImpl companyDao = new CompanyDaoImpl(conn);
		return companyDao.getOne(id);
	}

}
