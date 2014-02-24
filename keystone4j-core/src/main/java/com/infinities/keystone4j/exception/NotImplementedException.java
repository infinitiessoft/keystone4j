package com.infinities.keystone4j.exception;

public class NotImplementedException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1342361745239805662L;


	public NotImplementedException() {
		this(null, null);
	}

	public NotImplementedException(String message, Object[] arguments) {
		super(501, message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "The action you have requested has not been implemented.";
	}

	@Override
	protected String getTittle() {
		return "Not Implemented";
	}

}
