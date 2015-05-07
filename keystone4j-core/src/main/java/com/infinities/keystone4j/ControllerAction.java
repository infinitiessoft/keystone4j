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

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.common.Authorization.AuthContext;
import com.infinities.keystone4j.common.Wsgi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.model.KeystoneToken;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

// keystone.common.controller 20141209
public abstract class ControllerAction {

	private final static Logger logger = LoggerFactory.getLogger(ControllerAction.class);
	// private final TokenApi tokenApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	public ControllerAction(TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	protected void checkProtection(KeystoneContext context, ContainerRequestContext request, Action command,
			Target targetAttr) throws Exception {
		if (context.isAdmin()) {
			logger.warn("RBAC: Bypassing authorization");
		} else {
			String action = String.format("identity:%s", command.getName());
			Authorization.AuthContext creds = buildPolicyCheckCredentials(action, context, request);

			Map<String, Object> policyDict = new HashMap<String, Object>();
			if (targetAttr != null) {
				policyDict.put("target", targetAttr);
			}

			policyApi.enforce(creds, action, policyDict);
			logger.debug("RBAC: Authorization granted");
		}
	}

	protected Authorization.AuthContext buildPolicyCheckCredentials(String action, KeystoneContext context,
			ContainerRequestContext request) {
		logger.debug("RBAC: AUTHORIZING {}", action);

		// Token token = (Token)
		// AuthContext context =
		// request.getProperty(Authorization.AUTH_CONTEXT_ENV);
		if (request.getHeaders().containsKey(Authorization.AUTH_CONTEXT_ENV)) {
			logger.debug("RBAC: using auth context from the request environment");
			return (Authorization.AuthContext) request.getProperty(Authorization.AUTH_CONTEXT_ENV);
		}

		try {
			logger.debug("RBAC: building auth context from the incoming auth token");
			KeystoneToken tokenRef = new KeystoneToken(context.getTokenid(), tokenProviderApi.validateToken(
					context.getTokenid(), null));
			Wsgi.validateTokenBind(context, tokenRef);
			AuthContext authContext = Authorization.tokenToAuthContext(tokenRef);
			return authContext;
		} catch (Exception e) {
			logger.warn("RBAC:Invalid token");
			throw Exceptions.UnauthorizedException.getInstance();
		}

	}


	public static class Target {

		private Role role;
		private Group group;
		private Domain domain;
		private Project project;
		private User user;


		public Role getRole() {
			return role;
		}

		public void setRole(Role role) {
			this.role = role;
		}

		public Group getGroup() {
			return group;
		}

		public void setGroup(Group group) {
			this.group = group;
		}

		public Domain getDomain() {
			return domain;
		}

		public void setDomain(Domain domain) {
			this.domain = domain;
		}

		public Project getProject() {
			return project;
		}

		public void setProject(Project project) {
			this.project = project;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

	}

}
