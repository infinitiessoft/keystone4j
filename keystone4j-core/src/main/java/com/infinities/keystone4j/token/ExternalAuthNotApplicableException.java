package com.infinities.keystone4j.token;

public class ExternalAuthNotApplicableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public ExternalAuthNotApplicableException() {
	}

	public ExternalAuthNotApplicableException(String message) {
		super(message);
	}

	public ExternalAuthNotApplicableException(Throwable cause) {
		super(cause);
	}

	public ExternalAuthNotApplicableException(String message, Throwable cause) {
		super(message, cause);
	}

}
