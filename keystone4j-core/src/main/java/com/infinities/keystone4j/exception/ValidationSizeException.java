package com.infinities.keystone4j.exception;

public class ValidationSizeException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5296649695546157429L;


	public ValidationSizeException(String message, Object[] arguments) {
		super(400, message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "Request attribute %(attribute)s must be less than or equal to %s."
				+ " The server could not comply with the request because the attribute size is invalid (too large)."
				+ " The client is assumed to be in error.";
	}

	@Override
	protected String getTittle() {
		return "Bad Request";
	}

}
