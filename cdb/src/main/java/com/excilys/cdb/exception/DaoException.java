package com.excilys.cdb.exception;

/**
 * 
 * @author Kevin Bottero
 *
 */
public class DaoException extends RuntimeException{

	private static final long serialVersionUID = 3930740027979797558L;
	
	public static final String CAN_NOT_CREATE_CONNECTION = "can not create connection";
	public static final String CAN_NOT_CREATE_STATEMENT = "can not create statement";
	public static final String CAN_NOT_SET_PREPAREDSTATEMENT = "can not set preparedStatement";
	public static final String CAN_NOT_UPDATE_ELEMENT = "can not update the element";
	public static final String CAN_NOT_DELETE_ELEMENT = "can not delete the element";
	public static final String CAN_NOT_GET_ELEMENT = "can not get the element";
	public static final String CAN_NOT_INSERT_ELEMENT = "can not insert the element";
	public static final String CAN_NOT_CLOSE_CONNECTION = "can not close connection";
	public static final String CAN_NOT_CLOSE_STATEMENT = "can not close statement";
	public static final String INVALID_ARGUMENT = "invalid argument";
	public static final String CAN_NOT_LOAD_PROPERTIES = "can not load properties";
	public static final String CAN_NOT_ROLLBACK = "can not rollback";
	public static final String CAN_NOT_CHANGE_AUTOCOMMIT = "can not change autocommit";
	public static final String CAN_NOT_SETUP_CONNECTION_POOL = "can not setup the connection pool.";
	public static final String CAN_NOT_COMMIT = "can not commit.";

	public DaoException() {
		super();
	}
	
	public DaoException(String message) {
		super(message);
	}
	
	public DaoException(String message,Throwable throwable) {
		super(message, throwable);
	}
	
	public DaoException(String message,Throwable throwable,  boolean enableSuppression, boolean writableStackTrace) {
		super(message, throwable,enableSuppression,writableStackTrace);
	}
	
	public DaoException(Throwable throwable) {
		super(throwable);
	}
	
}
