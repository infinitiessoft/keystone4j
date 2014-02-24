package com.infinities.keystone4j.exception;

public class RequestTooLargeException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1342361745239805662L;


	public RequestTooLargeException(String message, Object[] arguments) {
		super(413, message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "Request is too large.";
	}

	@Override
	protected String getTittle() {
		return "Request is too large.";
	}

}
