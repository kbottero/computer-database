package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DAOManager {
	
	private static DAOManager manager;

	public static DAOManager getDAOManager() {
		if (manager == null) {
			synchronized(DAOManager.class) {
				if (manager == null) {
					manager = new DAOManager();
				}
			}
		}
		return manager;
	}
	
	public Connection getConnection() throws SQLException {

	    Connection conn = null;
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", "admincdb");
	    connectionProps.put("password", "qwerty1234");

        conn = DriverManager.getConnection(
                   "jdbc:mysql://localhost:3306/computer-database-db",
                   connectionProps);
        
	    return conn;
	}
}
