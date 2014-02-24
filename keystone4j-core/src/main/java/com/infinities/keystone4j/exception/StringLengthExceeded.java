package com.infinities.keystone4j.exception;

public class StringLengthExceeded extends ValidationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4413016523507110741L;


	public StringLengthExceeded(String message, Object[] arguments) {
		super(message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "String length exceeded.The length of string \'%s\' exceeded the limit of column %s(CHAR(%(s)d)).";
	}
}
