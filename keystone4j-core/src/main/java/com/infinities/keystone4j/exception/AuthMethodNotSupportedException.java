package com.infinities.keystone4j.exception;

public class AuthMethodNotSupportedException extends AuthPluginException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2658883806817438487L;


	public AuthMethodNotSupportedException() {
		this(null);
	}

	public AuthMethodNotSupportedException(String message, Object... arguments) {
		super(message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "Attempted to authenticate with an unsupported method.";
	}

}
