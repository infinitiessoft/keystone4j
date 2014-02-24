package com.infinities.keystone4j.exception;

public class NotFoundException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1342361745239805662L;


	public NotFoundException(String message, Object... arguments) {
		super(404, message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "Could not find, {0}.";
	}

	@Override
	protected String getTittle() {
		return "Not Found";
	}

}
