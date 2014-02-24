package com.infinities.keystone4j.exception;

public class MetadataNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3956247277082677480L;


	public MetadataNotFoundException(String message, Object... arguments) {
		super(message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "An unhadled exception has occurred: Could not find metadata.";
	}

}
