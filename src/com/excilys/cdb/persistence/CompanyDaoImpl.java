package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.excilys.cdb.model.Company;

public class CompanyDaoImpl implements ICompanyDao{

	private Connection conn;
	private java.sql.PreparedStatement selectAllCompany;
	private java.sql.PreparedStatement selectOneCompany;
	private java.sql.PreparedStatement deleteOneCompany;
	private java.sql.PreparedStatement updateOneCompany;
	private java.sql.PreparedStatement insertOneCompany;
	
	public CompanyDaoImpl() {
		super();

		try {
			conn = DAOManager.getDAOManager().getConnection();
			
			String request = "SELECT * FROM computer;";
			selectAllCompany = conn.prepareStatement(request);
			
			request = "SELECT * FROM company WHERE id=?;";
			selectOneCompany = conn.prepareStatement(request);
			
			request = "DELETE FROM company WHERE id=?;";
			deleteOneCompany = conn.prepareStatement(request);
	
			request = "INSERT INTO company (name) VALUES (?) ;";
			insertOneCompany = conn.prepareStatement(request);
			
			request = "UPDATE company SET name=? WHERE id=?;";
			updateOneCompany = conn.prepareStatement(request);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	public Collection<Company> getAll() {

		ResultSet curs;
		Collection<Company> list = new ArrayList<Company>();
		
		try {
			curs = selectAllCompany.executeQuery();
			while ( curs.next() ) {
				Company comp = new Company(curs.getLong("id"),
						curs.getString("name"));
				list.add(comp);
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
				comp = new Company(curs.getLong("id"),
						curs.getString("name"));
    		}
		} catch (SQLException | NumberFormatException e) {
			////////////////////////////////////////////////////////
			e.printStackTrace();
		}
		return comp;
	}

//	@Override
//	public void saveOne(Company c) {
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
//				//throw new DaoException("Impossible de sauvegarder l'eleve " ,6) ;
//			}
//			
//		} catch (SQLException e) {
//			/*
//			if(!update)
//				throw new DaoException("Impossible de sauvegarder l'eleve " +e1+" "+stInsert ,6) ;
//			else
//				throw new DaoException("Impossible de sauvegarder l'eleve " +e1+" "+stUpdate ,6) ;
//				*/
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void deleteOne(long id) {
//		try {
//			deleteOneCompany.setLong(1, id);
//			int nb = deleteOneCompany.executeUpdate();
//			if(nb==0)
//			{
//				//throw new DaoException("(Delete)Eleve d'id "+id+" inconnu "+stDelete ,6) ;
//			}
//		} catch (SQLException e) {
//			//throw new DaoException("Impossible de charger tous les eleves " +e ,6) ;
//			e.printStackTrace();
//		}
//	}

	
}
