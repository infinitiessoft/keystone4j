package com.infinities.keystone4j.exception;

public class DomainNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3956247277082677480L;


	public DomainNotFoundException(String message, Object... arguments) {
		super(message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "Could not find domain, {0}.";
	}

}
