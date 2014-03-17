package com.infinities.keystone4j;

import java.text.MessageFormat;

import javax.validation.ValidationException;
import javax.ws.rs.BadRequestException;

import com.google.common.base.Strings;

public class KeystonePreconditions {

	private static final String REQUIRE_ATTRIBUTE = "{0} field is required and cannot be empty";
	private static final String REQUIRE_MATCHING_ID = "Cannot change ID";
	private static final String REQUIRE_DOMAIN_XOR_PROJECT = "Specify a domain or project, not both";
	private static final String REQUIRE_USER_XOR_GROUP = "Specify a user or group, not both";


	public static void requireAttribute(String attr, String value) {
		if (Strings.isNullOrEmpty(value)) {
			String message = MessageFormat.format(REQUIRE_ATTRIBUTE, attr);
			throw new ValidationException(message, null);
		}
	}

	public static void requireMatchingId(String id, BaseEntity entity) {
		if (!Strings.isNullOrEmpty(entity.getId()) && !id.equals(entity.getId())) {
			throw new BadRequestException(REQUIRE_MATCHING_ID);

		}
	}

	public static void requireDomain(String domainid) {
		if (Strings.isNullOrEmpty(domainid)) {
			throw new BadRequestException(REQUIRE_DOMAIN_XOR_PROJECT);
		}
	}

	public static void requireUser(String userid) {
		if (Strings.isNullOrEmpty(userid)) {
			throw new BadRequestException(REQUIRE_USER_XOR_GROUP);
		}
	}

	public static void requireProject(String projectid) {
		if (Strings.isNullOrEmpty(projectid)) {
			throw new BadRequestException(REQUIRE_DOMAIN_XOR_PROJECT);
		}
	}

	public static void requireGroup(String groupid) {
		if (Strings.isNullOrEmpty(groupid)) {
			throw new BadRequestException(REQUIRE_USER_XOR_GROUP);
		}
	}

}
