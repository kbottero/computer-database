package com.excilys.cdb.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.excilys.cdb.exception.DaoException;


/**
 * Singleton returning Connection to the database.
 * Connection properties are store in the db.properties file
 * @author Kevin Bottero
 *
 */
public enum DaoManager {
	
	INSTANCE;
	
	private Properties properties;
	
	DaoManager () {
		properties = new Properties();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(DaoManager.class.getClassLoader().getResourceAsStream("db.properties")));
			properties.load(in);
			in.close();
			Class.forName("com.mysql.jdbc.Driver");
		} catch (IOException e) {
			throw new DaoException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Connection getConnection() throws DaoException {
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(
		    		properties.getProperty("url")+properties.getProperty("database"),
		    		properties);
			
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_CREATE_CONNECTION, e);
		}
		
	    return conn;
	}
	
	public PreparedStatement createPreparedStatement (Connection conn, String request) {
		return createPreparedStatement(conn,request, null);
	}
	
	public PreparedStatement createPreparedStatement(Connection conn,
			String request, Integer returnGeneratedKeys) {
		PreparedStatement statement = null;
		request.trim();
		try {
			if (returnGeneratedKeys != null) {
				statement = conn.prepareStatement(request,returnGeneratedKeys);
			} else {
				statement = conn.prepareStatement(request);
			}
		} catch (SQLException e) {
			try {
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e2) {
				throw new DaoException(DaoException.CAN_NOT_CLOSE_CONNECTION,e2);
			}
			throw new DaoException(DaoException.CAN_NOT_CREATE_STATEMENT,e);
		}
		return statement;
	}
	
	public Statement createStatement (Connection conn) {
		Statement statement = null;
		try {
			statement = conn.createStatement();
		} catch (SQLException e) {
			try {
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e2) {
				throw new DaoException(DaoException.CAN_NOT_CLOSE_CONNECTION,e2);
			}
			throw new DaoException(DaoException.CAN_NOT_CREATE_STATEMENT,e);
		}
		return statement;
	}

	public void closeConnAndStat (Statement statement, Connection connection) throws DaoException{
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_CLOSE_STATEMENT,e);
		}
		try {
			if(connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_CLOSE_CONNECTION,e);
		}
	}


	
}
