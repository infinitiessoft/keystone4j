package com.infinities.keystone4j.exception;

import java.text.MessageFormat;

import javax.xml.ws.http.HTTPException;

import com.google.common.base.Strings;

public abstract class BaseException extends HTTPException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7031664962352685426L;
	private String message;


	// private Logger logger = LoggerFactory.getLogger(BaseException.class);

	public BaseException(int statusCode, String message, Object... arguments) {
		super(statusCode);
		this.setMessage(buildMessage(message, arguments));
	}

	protected String buildMessage(String message, Object... arguments) {

		if (Strings.isNullOrEmpty(message)) {
			message = MessageFormat.format(getMessageFormat(), arguments);
		}
		return message;
	}

	protected abstract String getMessageFormat();

	protected abstract String getTittle();

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
