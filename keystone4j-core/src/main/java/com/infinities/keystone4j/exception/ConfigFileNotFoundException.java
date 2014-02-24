package com.infinities.keystone4j.exception;

public class ConfigFileNotFoundException extends UnexpectedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3956247277082677480L;


	public ConfigFileNotFoundException(String message, Object[] arguments) {
		super(message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "The Keystone configuration file %s could not be found.";
	}

}
