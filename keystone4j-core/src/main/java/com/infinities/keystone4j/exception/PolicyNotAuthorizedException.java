package com.infinities.keystone4j.exception;

public class PolicyNotAuthorizedException extends UnauthorizedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3956247277082677480L;


	public PolicyNotAuthorizedException(String message, Object... arguments) {
		super(message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "Policy doesn't allow {0} to be performed.";
	}

}
