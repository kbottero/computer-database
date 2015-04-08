package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.exception.DaoException;


/**
 * Singleton returning Connection to the database.
 * Connection properties are store in the db.properties file
 * @author Kevin Bottero
 *
 */
@Component
public class TransactionFactory {
	
	private static Logger logger = LoggerFactory.getLogger(TransactionFactory.class); 
	private ThreadLocal<Connection> connectionTL = new ThreadLocal<Connection>();

	@Autowired
	private BasicDataSource dataSource;
	
	TransactionFactory () {
		
	}
	
	private Connection getConnection() throws DaoException {
		logger.debug("getConnection()");
		if (connectionTL.get() == null) {
			try {
				connectionTL.set(dataSource.getConnection());
			} catch (SQLException e) {
				throw new DaoException(DaoException.CAN_NOT_CREATE_CONNECTION, e);
			}
		}
	    return connectionTL.get();
	}
	
	public PreparedStatement createPreparedStatement (String request) throws DaoException {
		return createPreparedStatement(request, null);
	}
	
	public PreparedStatement createPreparedStatement(String request, Integer returnGeneratedKeys) throws DaoException {
		PreparedStatement statement = null;
		request.trim();
		Connection conn = getConnection();
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
	
	public Statement createStatement () throws DaoException {
		Statement statement = null;
		Connection conn = getConnection();
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
	
	public void closeStat (Statement statement) throws DaoException {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_CLOSE_STATEMENT,e);
		}
	}
}
