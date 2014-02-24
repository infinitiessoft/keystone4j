package com.infinities.keystone4j.exception;

public class AdditionalAuthRequiredException extends AuthPluginException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2658883806817438487L;


	public AdditionalAuthRequiredException(String message, Object[] arguments) {
		super(message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "Additional authentications steps required.";
	}

}
