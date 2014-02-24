package com.infinities.keystone4j.exception;

public class ConflictException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1342361745239805662L;


	public ConflictException(String message, Object... arguments) {
		super(409, message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "Conflict occured attempting to store {0}. {1}";
	}

	@Override
	protected String getTittle() {
		return "Conflict";
	}

}
