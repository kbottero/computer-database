package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.mysql.jdbc.PreparedStatement;

public class ComputerDaoImpl  implements IComputerDao {
		
	private Connection conn;
	private java.sql.PreparedStatement selectAllComputer;
	private java.sql.PreparedStatement selectOneComputer;
	private java.sql.PreparedStatement deleteOneComputer;
	private java.sql.PreparedStatement updateOneComputer;
	private java.sql.PreparedStatement insertOneComputer;
	private java.sql.PreparedStatement selectOneCompany;
	
	public ComputerDaoImpl() {
		
		try {
			conn = DAOManager.getDAOManager().getConnection();
			
			String request = "SELECT * FROM computer;";
			selectAllComputer = conn.prepareStatement(request);
			
			request = "SELECT * FROM computer WHERE id=?;";
			selectOneComputer = conn.prepareStatement(request);
			
			request = "DELETE FROM computer WHERE id=?;";
			deleteOneComputer = conn.prepareStatement(request);
	
			request = "INSERT INTO computer (name, introduced, discontinued,company_id) VALUES (?,?,?,?) ;";
			insertOneComputer = conn.prepareStatement(request,PreparedStatement.RETURN_GENERATED_KEYS);
			
			request = "UPDATE computer SET name=?,introduced=?,discontinued=?,company_id=? WHERE id=?;";
			updateOneComputer = conn.prepareStatement(request);
			
			request = "SELECT * FROM company WHERE id=?;";
			selectOneCompany = conn.prepareStatement(request);
			
			
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
	public Collection<Computer> getAll(){
		
		ResultSet curs;
		Collection<Computer> list = new ArrayList<Computer>();
		
		try {
			curs = selectAllComputer.executeQuery();
			while ( curs.next() ) {
				list.add(ComputerMapper.INSTANCE.parserComputer(curs, getOneCompany(curs.getLong("company_id"))));
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
				comp = ComputerMapper.INSTANCE.parserComputer(curs, getOneCompany(curs.getLong("company_id")));
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
				//throw new DaoException("Impossible de sauvegarder l'eleve " ,6) ;
			}
			
		} catch (SQLException e) {
			/*
			if(!update)
				throw new DaoException("Impossible de sauvegarder l'eleve " +e1+" "+stInsert ,6) ;
			else
				throw new DaoException("Impossible de sauvegarder l'eleve " +e1+" "+stUpdate ,6) ;
				*/
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
				//throw new DaoException("(Delete)Eleve d'id "+id+" inconnu "+stDelete ,6) ;
			}
		} catch (SQLException e) {
			//throw new DaoException("Impossible de charger tous les eleves " +e ,6) ;
			e.printStackTrace();
		}
		
	}
	
	public Company getOneCompany(long id) {
		
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

}
