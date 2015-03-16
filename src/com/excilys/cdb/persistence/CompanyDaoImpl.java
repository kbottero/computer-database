package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.model.Company;
import java.sql.PreparedStatement;

public class CompanyDaoImpl implements IDao<Company>{

	private Connection conn;
	private PreparedStatement selectAllCompany;
	private PreparedStatement selectOneCompany;
	private PreparedStatement deleteOneCompany;
	private PreparedStatement updateOneCompany;
	private PreparedStatement insertOneCompany;
	
	private static enum preparedStatement {
		SELECT_ALL ("SELECT * FROM company;"),
		SELECT_ONE ("SELECT * FROM company WHERE id=?;"),
		INSERT_ONE ("INSERT INTO company (name) VALUES (?) ;"),
		UPDATE_ONE ("UPDATE company SET name=? WHERE id=?;"),
		DELETE_ONE ("DELETE FROM company WHERE id=?;");
		
		private final String request;
		
		preparedStatement(String request) {
	        this.request = request;
	    }
	    private String getRequest() { return request; }
	}
	
	
	public CompanyDaoImpl() {
		super();

		try {
			conn = DaoManager.INSTANCE.getConnection();
			initPreparedStatement(conn);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public CompanyDaoImpl(Connection conn) {
		super();
		try {
			this.conn = conn;
	        initPreparedStatement(conn);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void initPreparedStatement(Connection conn) throws SQLException {
		selectAllCompany = conn.prepareStatement(preparedStatement.SELECT_ALL.getRequest());
		
		selectOneCompany = conn.prepareStatement(preparedStatement.SELECT_ONE.getRequest());
		
		deleteOneCompany = conn.prepareStatement(preparedStatement.DELETE_ONE.getRequest());

		insertOneCompany = conn.prepareStatement(preparedStatement.INSERT_ONE.getRequest(),
								PreparedStatement.RETURN_GENERATED_KEYS);
		
		updateOneCompany = conn.prepareStatement(preparedStatement.UPDATE_ONE.getRequest());
	}
	
	@Override
	protected void finalize() {
		
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			selectAllCompany.close();
			selectOneCompany.close();
			deleteOneCompany.close();
			insertOneCompany.close();
			updateOneCompany.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Company> getAll() {

		ResultSet curs;
		ArrayList<Company> list = new ArrayList<Company>();
		
		try {
			curs = selectAllCompany.executeQuery();
			while ( curs.next() ) {
				list.add(CompanyMapper.INSTANCE.parseCompany(curs));
    		}
		} catch (SQLException | NumberFormatException e) {
			////////////////////////////////////////////////////////
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Company getOne(long id) {
		
		ResultSet curs;
		Company comp=null;
		try {
			selectOneCompany.setLong(1,id);
			curs = selectOneCompany.executeQuery();
			if ( curs.next() ) {
				comp = CompanyMapper.INSTANCE.parseCompany(curs);
    		}
		} catch (SQLException | NumberFormatException e) {
			////////////////////////////////////////////////////////
			e.printStackTrace();
		}
		return comp;
	}

	@Override
	public void saveOne(Company c) {
//		boolean update=false;
//		ResultSet curs;
//
//		try {
//			
//			selectOneCompany.setLong(1,c.getId());
//			curs = selectOneCompany.executeQuery();
//			int nb;
//			if(curs.next())
//			{
//				update = true;
//			}
//
//			if(!update)
//			{
//				insertOneCompany.setString(1,c.getName());
//				nb = insertOneCompany.executeUpdate();
//				ResultSet rs = insertOneCompany.getGeneratedKeys();
//				if (rs.next()) {
//					c.setId(rs.getLong(1));
//				}
//			}
//			else
//			{
//				updateOneCompany.setString(1,c.getName());
//				updateOneCompany.setLong(2,c.getId());
//				nb = updateOneCompany.executeUpdate();
//			}
//			if(nb==0)
//			{
//			}
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void deleteOne(long id) {
//		try {
//			deleteOneCompany.setLong(1, id);
//			int nb = deleteOneCompany.executeUpdate();
//			if(nb==0)
//			{
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}

	
}
