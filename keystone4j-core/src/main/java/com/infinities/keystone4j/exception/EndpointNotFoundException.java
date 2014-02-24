package com.infinities.keystone4j.exception;

public class EndpointNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3956247277082677480L;


	public EndpointNotFoundException(String message, Object... arguments) {
		super(message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "Could not find endpoint, {0}.";
	}

}
