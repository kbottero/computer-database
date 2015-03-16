package com.excilys.cdb.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public enum DaoManager {
	
	INSTANCE;
	
	private Properties properties;
	
	DaoManager (){
		properties = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream("db.properties");
			properties.load(in);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() throws SQLException {
	    Connection conn = null;
	    conn = DriverManager.getConnection(
	    		properties.getProperty("url")+properties.getProperty("database"),
	    		properties);
        
	    return conn;
	}
}
