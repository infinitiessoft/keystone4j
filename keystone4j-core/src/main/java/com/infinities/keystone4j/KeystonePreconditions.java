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
package com.infinities.keystone4j;

import java.text.MessageFormat;

import javax.validation.ValidationException;
import javax.ws.rs.BadRequestException;

import com.google.common.base.Strings;
import com.infinities.keystone4j.model.BaseEntity;

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
