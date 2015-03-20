package com.excilys.cdb.exception;

/**
 * 
 * @author Kevin Bottero
 *
 */
public class DaoException extends RuntimeException{

	private static final long serialVersionUID = 3930740027979797558L;

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
