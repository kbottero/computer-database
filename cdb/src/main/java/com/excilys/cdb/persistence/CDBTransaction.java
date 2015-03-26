package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.SQLException;

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

	public synchronized Connection getConnection() throws SQLException {
		if (connection == null) {
			connection = DaoManager.INSTANCE.getConnection();
			if (state == TransactionType.CONSISTENT) {
				connection.setAutoCommit(false);
			}
			return connection;
		} else {
			return connection;
		}
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
}
