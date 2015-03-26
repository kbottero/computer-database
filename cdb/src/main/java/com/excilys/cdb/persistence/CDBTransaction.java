package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.excilys.cdb.exception.DaoException;

public class CDBTransaction {

	public enum TransactionType {
		ATOMIC,
		CONSISTENT;
	}
	
	private final TransactionType state;
	private Connection connection = null;
	
	public CDBTransaction(TransactionType state) {
		super();
		this.state = state;
	}
	
	public synchronized TransactionType getState() {
		return state;
	}

	public synchronized void init() throws DaoException {
		if (connection == null) {
			connection = DaoManager.INSTANCE.getConnection();
			if (state == TransactionType.CONSISTENT) {
				try {
					connection.setAutoCommit(false);
				} catch (SQLException e) {
					throw new DaoException(DaoException.CAN_NOT_CREATE_CONNECTION,e);
				}
			}
		}
	}
	
	public synchronized Statement createStatement () throws DaoException {
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			try {
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e2) {
				throw new DaoException(DaoException.CAN_NOT_CLOSE_CONNECTION,e2);
			}
			throw new DaoException(DaoException.CAN_NOT_CREATE_STATEMENT,e);
		}
		return statement;
	}
	
	public PreparedStatement createPreparedStatement(String request)
			throws DaoException {
		return createPreparedStatement(request, null);
	}

	public PreparedStatement createPreparedStatement(String request,
			Integer returnGeneratedKeys) throws DaoException {
		PreparedStatement statement = null;
		request.trim();
		try {
			if (returnGeneratedKeys != null) {
				statement = connection.prepareStatement(request, returnGeneratedKeys);
			} else {
				statement = connection.prepareStatement(request);
			}
		} catch (SQLException e) {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e2) {
				throw new DaoException(DaoException.CAN_NOT_CLOSE_CONNECTION,
						e2);
			}
			throw new DaoException(DaoException.CAN_NOT_CREATE_STATEMENT, e);
		}
		return statement;
	}

	
	public synchronized void commit() throws SQLException {
		connection.commit();
	}
	
	public synchronized void rollback() throws SQLException {
		connection.rollback();
	}
	
	public synchronized void setAutoCommit(boolean value) throws SQLException {
		connection.setAutoCommit(value);
	}
	
	public synchronized void close() throws SQLException {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e2) {
			throw new DaoException(DaoException.CAN_NOT_CLOSE_CONNECTION,e2);
		}
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
