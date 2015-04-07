package com.excilys.cdb.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.DaoException;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;


/**
 * Singleton returning Connection to the database.
 * Connection properties are store in the db.properties file
 * @author Kevin Bottero
 *
 */
public enum TransactionFactory {
	
	INSTANCE;
	private static Logger logger = LoggerFactory.getLogger(TransactionFactory.class); 
	private Properties properties;
	private BoneCP connectionPool = null;
	ThreadLocal<Connection> connectionTL = new ThreadLocal<Connection>();
	
	TransactionFactory () {
		properties = new Properties();
		String config;
		try {
			if ("TEST".equals(System.getProperty("env"))) {
				config = "db-test.properties";
			} else {
				Class.forName("com.mysql.jdbc.Driver");
				config = "db.properties";
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(TransactionFactory.class.getClassLoader().getResourceAsStream(config)));
			properties.load(in);
			in.close();
		} catch (ClassNotFoundException | IOException e) {
			throw new DaoException(DaoException.CAN_NOT_LOAD_PROPERTIES, e);
		}

		try {
			BoneCPConfig configBoneCP = new BoneCPConfig(properties);
			configBoneCP.setJdbcUrl(properties.getProperty("url")+properties.getProperty("database")+"?zeroDateTimeBehavior=convertToNull");
			configBoneCP.setMinConnectionsPerPartition(5);
			configBoneCP.setMaxConnectionsPerPartition(10);
			configBoneCP.setPartitionCount(1);
			connectionPool = new BoneCP(configBoneCP);
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_SETUP_CONNECTION_POOL, e);
		} catch (Exception e) {
			throw new DaoException(DaoException.CAN_NOT_SETUP_CONNECTION_POOL, e);
		}
	}
	
	private Connection getConnection() throws DaoException {
		logger.debug("getConnection()");
		if (connectionTL.get() == null) {
			try {
				connectionTL.set(connectionPool.getConnection());
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
	
	public void startTransaction () throws DaoException {
		try {
			getConnection().setAutoCommit(false);
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_CHANGE_AUTOCOMMIT,e);
		}
	}
	
	public void commitTransaction () throws DaoException {
		try {
			getConnection().commit();
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_COMMIT,e);
		}
	}
	
	public void cancelTransaction () throws DaoException {
		try {
			getConnection().rollback();
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_ROLLBACK,e);
		}
	}
	
	public void endTransaction () throws DaoException {
		try {
			getConnection().setAutoCommit(true);
		} catch (SQLException e) {
			throw new DaoException(DaoException.CAN_NOT_CHANGE_AUTOCOMMIT,e);
		}
	}	
}
