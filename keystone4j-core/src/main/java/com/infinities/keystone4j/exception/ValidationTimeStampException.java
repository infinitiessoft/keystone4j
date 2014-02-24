package com.infinities.keystone4j.exception;

public class ValidationTimeStampException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5055862523097345882L;


	public ValidationTimeStampException(String message, Object[] arguments) {
		super(400, message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "Timestamp not in expected format." + " The server could not comply with the request since it is"
				+ " either malformed or otherwise incorrect." + " The client is assumed to be in error.";
	}

	@Override
	protected String getTittle() {
		return "Bad Request";
	}

}
