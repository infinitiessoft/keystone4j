/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
package com.infinities.keystone4j.common.model;

import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

public enum CustomResponseStatus implements StatusType {
	MULTIPLE_CHOICES(300, "Multiple Choices"), NO_CONTENT(204, "No Content"), CREATE_TOKEN(201, "Created"), VALIDATE_TOKEN(
			200, "OK"), REQUEST_TOO_LARGE(413, "Request is too large."), UNAUTHORIZED(401,
			"The request you have made requires authentication.");

	private final int code;
	private final String reason;
	private Family family;


	CustomResponseStatus(final int statusCode, final String reasonPhrase) {
		this.code = statusCode;
		this.reason = reasonPhrase;
		switch (code / 100) {
		case 1:
			this.family = Family.INFORMATIONAL;
			break;
		case 2:
			this.family = Family.SUCCESSFUL;
			break;
		case 3:
			this.family = Family.REDIRECTION;
			break;
		case 4:
			this.family = Family.CLIENT_ERROR;
			break;
		case 5:
			this.family = Family.SERVER_ERROR;
			break;
		default:
			this.family = Family.OTHER;
			break;
		}
	}

	/**
	 * Get the class of status code
	 * 
	 * @return the class of status code
	 */
	@Override
	public Family getFamily() {
		return family;
	}

	/**
	 * Get the associated status code
	 * 
	 * @return the status code
	 */
	@Override
	public int getStatusCode() {
		return code;
	}

	/**
	 * Get the reason phrase
	 * 
	 * @return the reason phrase
	 */
	@Override
	public String getReasonPhrase() {
		return toString();
	}

	/**
	 * Get the reason phrase
	 * 
	 * @return the reason phrase
	 */
	@Override
	public String toString() {
		return reason;
	}

	/**
	 * Convert a numerical status code into the corresponding Status
	 * 
	 * @param statusCode
	 *            the numerical status code
	 * @return the matching Status or null is no matching Status is defined
	 */
	public static CustomResponseStatus fromStatusCode(final int statusCode) {
		for (CustomResponseStatus s : CustomResponseStatus.values()) {
			if (s.code == statusCode) {
				return s;
			}
		}
		return null;
	}

}
