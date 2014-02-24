package com.infinities.keystone4j.exception;

//replace by BadRequestException
public class ValidationException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 623708327715468946L;


	public ValidationException(String message, Object... arguments) {
		super(400, message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "Expecting to find {0} in {1}." + " The server could not comply with the request"
				+ " since it is either malformed or otherwise" + " incorrect. The client is assumed to be in error";
	}

	@Override
	protected String getTittle() {
		return "Bad Request";
	}

}
