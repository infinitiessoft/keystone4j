package com.infinities.keystone4j.exception;

public class AuthPluginException extends UnauthorizedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3956247277082677480L;


	public AuthPluginException(String message, Object[] arguments) {
		super(message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "Authenication plugin error.";
	}

}
