package com.infinities.keystone4j.exception;

public class UnauthorizedException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1342361745239805662L;


	public UnauthorizedException(String message, Object... arguments) {
		super(401, message, arguments);
	}

	public UnauthorizedException() {
		this(null);
	}

	@Override
	protected String getMessageFormat() {
		return "The request you have made requires authentication.";
	}

	@Override
	protected String getTittle() {
		return "Unauthorized";
	}

}
