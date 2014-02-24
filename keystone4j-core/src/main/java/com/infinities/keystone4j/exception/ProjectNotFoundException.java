package com.infinities.keystone4j.exception;

public class ProjectNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3956247277082677480L;


	public ProjectNotFoundException(String message, Object... arguments) {
		super(message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "Could not find project, {0}.";
	}

}
