/*******************************************************************************
 * Copyright 2015 InfinitiesSoft Solutions Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/

package com.infinities.keystone4j;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	private final Logger logger = LoggerFactory.getLogger(GenericExceptionMapper.class);


	@Override
	public Response toResponse(Throwable ex) {
		logger.error("catch exception", ex);
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setCode(Response.Status.BAD_REQUEST.getStatusCode());
		errorMessage.setTitle(Response.Status.BAD_REQUEST.getReasonPhrase());
		Throwable root = ExceptionUtils.getRootCause(ex);
		if (root == null) {
			root = ex;
		}
		String msg = root.getClass().getSimpleName().replace("Exception", "") + ": " + root.getMessage();
		errorMessage.setMessage(msg);
		setHttpStatus(ex, errorMessage);
		ErrorMessageWrapper wrapper = new ErrorMessageWrapper();
		wrapper.setError(errorMessage);
		return Response.status(errorMessage.getCode()).entity(wrapper).header("safe", true).type(MediaType.APPLICATION_JSON)
				.build();
	}

	private void setHttpStatus(Throwable ex, ErrorMessage errorMessage) {
		if (ex instanceof WebApplicationException) {
			errorMessage.setCode(((WebApplicationException) ex).getResponse().getStatus());
			errorMessage.setTitle(((WebApplicationException) ex).getResponse().getStatusInfo().getReasonPhrase());
		} else {
			errorMessage.setTitle(Response.Status.INTERNAL_SERVER_ERROR.name());
			errorMessage.setCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()); // defaults
																							// to
																							// internal
																							// server
																							// error
																							// 500
		}
	}


	public class ErrorMessageWrapper {

		private ErrorMessage error;


		public ErrorMessage getError() {
			return error;
		}

		public void setError(ErrorMessage error) {
			this.error = error;
		}

	}

	public class ErrorMessage {

		private String message;
		private int code = 500;
		private String title;


		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

	}

}
