package com.excilys.cdb.exception;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceException() {
		super();
	}
	
	public ServiceException(String message) {
		super(message);
	}
	
	public ServiceException(String message,Throwable throwable) {
		super(message, throwable);
	}
	
	public ServiceException(String message,Throwable throwable,  boolean enableSuppression, boolean writableStackTrace) {
		super(message, throwable,enableSuppression,writableStackTrace);
	}
	
	public ServiceException(Throwable throwable) {
		super(throwable);
	}
	
}
