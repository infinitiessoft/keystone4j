package com.infinities.keystone4j.exception;

public class UnexpectedException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1342361745239805662L;


	public UnexpectedException(String message, Object... arguments) {
		super(500, message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "An unexpected error prevented the server from fulfilling your request. %s";
	}

	@Override
	protected String getTittle() {
		return "Internal Server Error";
	}

}
