package com.infinities.keystone4j.exception;

public class MalformedEndpointException extends UnexpectedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3956247277082677480L;


	public MalformedEndpointException(String message, Object[] arguments) {
		super(message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "Malformed endpoint URL %s, see ERROR log for details.";
	}

}
