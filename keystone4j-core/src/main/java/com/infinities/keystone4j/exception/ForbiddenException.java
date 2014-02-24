package com.infinities.keystone4j.exception;

public class ForbiddenException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1342361745239805662L;


	public ForbiddenException() {
		this(null);
	}

	public ForbiddenException(String message, Object... arguments) {
		super(403, message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "You are not authorized to perform the request action.";
	}

	@Override
	protected String getTittle() {
		return "Forbidden";
	}

}
