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
package com.infinities.keystone4j.assignment.controller.callback;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.infinities.keystone4j.CollectionCallback;
import com.infinities.keystone4j.ControllerAction;
import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.assignment.controllers._check_grant_protection 20141209

public class CheckGrantCollectionProtectionCallback extends ControllerAction implements CollectionCallback {

	private final String groupid;
	private final String domainid;
	private final String projectid;
	private final String roleid;
	private final String userid;
	private final IdentityApi identityApi;
	private final AssignmentApi assignmentApi;
	private final boolean allowNoUser;


	public CheckGrantCollectionProtectionCallback(TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			IdentityApi identityApi, AssignmentApi assignmentApi, String roleid, String userid, String groupid,
			String domainid, String projectid) {
		this(tokenProviderApi, policyApi, identityApi, assignmentApi, roleid, userid, groupid, domainid, projectid, false);
	}

	public CheckGrantCollectionProtectionCallback(TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			IdentityApi identityApi, AssignmentApi assignmentApi, String roleid, String userid, String groupid,
			String domainid, String projectid, boolean allowNoUser) {
		super(tokenProviderApi, policyApi);
		this.identityApi = identityApi;
		this.assignmentApi = assignmentApi;
		this.groupid = groupid;
		this.userid = userid;
		this.roleid = roleid;
		this.projectid = projectid;
		this.domainid = domainid;
		this.allowNoUser = allowNoUser;
	}

	@Override
	public void execute(ContainerRequestContext request, FilterProtectedAction<?> command) throws Exception {
		Target target = new Target();
		if (!Strings.isNullOrEmpty(roleid)) {
			Role role = this.assignmentApi.getRole(roleid);
			target.setRole(role);
		}
		if (!Strings.isNullOrEmpty(userid)) {
			try {
				User user = this.identityApi.getUser(userid);
				target.setUser(user);
			} catch (WebApplicationException e) {
				if (!allowNoUser) {
					throw e;
				}
			}
		} else {
			Group group = this.identityApi.getGroup(groupid);
			target.setGroup(group);
		}

		if (!Strings.isNullOrEmpty(domainid)) {
			Domain domain = this.assignmentApi.getDomain(domainid);
			target.setDomain(domain);
		} else {
			Project project = this.assignmentApi.getProject(projectid);
			target.setProject(project);
		}

		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		checkProtection(context, request, command, target);
	}
}
