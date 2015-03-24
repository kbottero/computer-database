package com.excilys.cdb.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
	
	public Connection getConnection() throws SQLException {
	    return DriverManager.getConnection(
	    		properties.getProperty("url")+properties.getProperty("database"),
	    		properties);
	}
}
