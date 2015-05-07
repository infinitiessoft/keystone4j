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
package com.infinities.keystone4j.assignment.api.command.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Metadata;

public class GetRolesForUserAndDomainCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<List<String>> {

	private final static Logger logger = LoggerFactory.getLogger(GetRolesForUserAndDomainCommand.class);
	private final String userid;
	private final String domainid;


	public GetRolesForUserAndDomainCommand(CredentialApi credentialApi, IdentityApi identityApi,
			AssignmentApi assignmentApi, RevokeApi revokeApi, AssignmentDriver assignmentDriver, String userid,
			String domainid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.userid = userid;
		this.domainid = domainid;
	}

	@Override
	public List<String> execute() throws Exception {
		// TODO testing?
		this.getAssignmentDriver().getDomain(domainid);
		List<String> userRoleList = getUserDomainRoles(userid, domainid);
		List<String> groupRoleList = getGroupDomainRoles(userid, domainid);

		Set<String> set = Sets.newHashSet();
		set.addAll(userRoleList);
		set.addAll(groupRoleList);

		return Lists.newArrayList(set);
	}

	private List<String> getGroupDomainRoles(String userid, String domainid) throws Exception {
		List<String> roleList = Lists.newArrayList();
		List<String> groupIds = getGroupIdsForUserId(userid);
		for (String groupid : groupIds) {
			// _get_metadata
			try {
				Metadata metadataRef = this.getAssignmentDriver().getMetadata(null, null, domainid, groupid);
				// Set<GroupDomainGrantMetadata> metadatas =
				// grant.getGroupDomainGrantMetadatas();
				roleList.addAll(this.getAssignmentDriver().rolesFromRoleDicts(metadataRef.getRoles(), false));
			} catch (Exception e) {
				logger.debug("get GroupDomainRoles failed", e);
				// no group grant, skip
			}
		}
		return roleList;
	}

	private List<String> getUserDomainRoles(String userid, String domainid) {
		Metadata metadataRef = null;
		// _get_metadata
		try {
			metadataRef = this.getAssignmentDriver().getMetadata(userid, null, domainid, null);
			return this.getAssignmentDriver().rolesFromRoleDicts(metadataRef.getRoles(), false);
		} catch (Exception e) {
			logger.debug("get UserDomainRoles failed", e);
			// no group grant, skip
		}

		return this.getAssignmentDriver().rolesFromRoleDicts(new ArrayList<Metadata.Role>(), false);
	}
}
