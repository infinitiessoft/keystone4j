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
package com.infinities.keystone4j.exception;

import java.text.MessageFormat;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.google.common.base.Strings;

public enum Exceptions {
	AdditionalAuthRequiredException(401, "Additional authentications steps required."), AuthMethodNotSupportedException(401,
			"Attempted to authenticate with an unsupported method."), CertificateFilesUnavailableException(500,
			"An unexpected error prevented the server from fulfilling your request. {0}"), ConfigFileNotFoundException(500,
			"The Keystone configuration file {0} could not be found."), CredentialNotFoundException(404,
			"Could not find credential, {0}."), DomainNotFoundException(404, "Could not find domain, {0}."), EndpointNotFoundException(
			404, "Could not find endpoint, {0}."), GroupNotFoundException(404, "Could not find group, {0}."), MalformedEndpointException(
			500, "Malformed endpoint URL {0}, see ERROR log for details."), MetadataNotFoundException(404,
			"An unhadled exception has occurred: Could not find metadata."), NotImplementedException(501,
			"The action you have requested has not been implemented."), PolicyNotAuthorizedException(401,
			"Policy doesn't allow {0} to be performed."), PolicyNotFoundException(404, "Could not find policy, {0}."), ProjectNotFoundException(
			404, "Could not find project, {0}."), RequestTooLargeException(401, "Request is too large."), RoleNotFoundException(
			404, "Could not find role, {0}."), ServiceNotFoundException(404, "Could not find service, {0}."), StringLengthExceededException(
			400, "String length exceeded.The length of string {0} exceeded the limit of column {1}."), TokenNotFoundException(
			404, "Could not find token, {0}."), TrustNotFoundException(404, "Could not find trust, {0}."), UserNotFoundException(
			404, "Could not find user, {0}."), ValidationException(
			400,
			"Expecting to find {0} in {1}. The server could not comply with the request since it is either malformed or otherwise incorrect. The client is assumed to be in error"), ValidationSizeException(
			400,
			"Request attribute (0) must be less than or equal to {1}. The server could not comply with the request because the attribute size is invalid (too large). The client is assumed to be in error."), ValidationTimeStampException(
			400,
			"Timestamp not in expected format. The server could not comply with the request since it is either malformed or otherwise incorrect. The client is assumed to be in error."), VersionNotFoundException(
			404, "Could not find version, {0}."), AuthPluginException(401, "Authenication plugin error."), ConflictException(
			409, "Conflict occured attempting to store {0}. {1}"), ForbiddenActionException(403,
			"You are not authorized to perform the request action, {0}."), ForbiddenException(403,
			"You are not authorized to perform the request action."), NotFoundException(404, "Could not find, {0}."), UnauthorizedException(
			401, "The request you have made requires authentication."), UnexpectedException(500,
			"An unexpected error prevented the server from fulfilling your request. {0}"), UnsupportedTokenVersionException(
			500, "Unsupported token version."), Gone(410,
			"The service you have requested is no longer available on this server."), RegionNotFoundException(404,
			"Could not find region, {0}."), MultipleSQLDriversInConfigException(500,
			"The Keystone domain configuration file {0} defines an additional SQL driver - only one is permitted."), CrossBackendNotAllowedException(
			403, "Group membership across backend boundaries is not allowed, group in question is {0}, user is {1}"), RegionDeletionError(
			403, "Unable to delete region {0} because it or its child regions have associated endpoints."), PasswordVerificationError(
			403,
			"The password length must be less than or equal to {0}. The server could not comply with the request because the password is invalid."), TrustUseLimitReached(
			403, "No remaining uses for trust: {0}"), TrustConsumeMaximumAttempt(500,
			"Unable to consume trust {0}, unable to acquire lock.");

	private String messageFormat;
	private int status;


	private String buildMessage(String message, Object... arguments) {

		if (Strings.isNullOrEmpty(message)) {
			message = MessageFormat.format(messageFormat, arguments);
		}
		return message;
	}

	private Exceptions(int status, String messageFormat) {
		this.status = status;
		this.messageFormat = messageFormat;
	}

	public WebApplicationException getInstance(Throwable e) {
		return new WebApplicationException(e, status);
	}

	public WebApplicationException getInstance() {
		return getInstance(null);
	}

	public WebApplicationException getInstance(String message, Object... arguments) {
		return new WebApplicationException(Response.status(status).entity(buildMessage(message, arguments)).build());
	}

}
