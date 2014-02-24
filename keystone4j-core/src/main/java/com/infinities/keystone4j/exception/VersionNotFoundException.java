package com.infinities.keystone4j.exception;

public class VersionNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3956247277082677480L;


	public VersionNotFoundException(String message, Object... arguments) {
		super(message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "Could not find version, {0}.";
	}

}
