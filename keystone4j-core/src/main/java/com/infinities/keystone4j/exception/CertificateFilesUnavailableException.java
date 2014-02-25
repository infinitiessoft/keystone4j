package com.infinities.keystone4j.exception;

public class CertificateFilesUnavailableException extends UnexpectedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7653030769306377472L;


	public CertificateFilesUnavailableException(String message, Object... arguments) {
		super(message, arguments);
	}

	@Override
	protected String getMessageFormat() {
		return "Expected signing certificates are not available on the server. Please check Keystone configuration.";
	}

}
