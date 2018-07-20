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
package com.infinities.keystone4j.token;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.Bind;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.TokenData;
import com.infinities.keystone4j.model.token.wrapper.TokenDataWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.token.provider.driver.BaseProvider;

public class TokenDataHelper {

	private final static Logger logger = LoggerFactory.getLogger(TokenDataHelper.class);
	private final IdentityApi identityApi;
	private final AssignmentApi assignmentApi;
	private final CatalogApi catalogApi;


	public TokenDataHelper(IdentityApi identityApi, AssignmentApi assignmentApi, CatalogApi catalogApi) {
		this.identityApi = identityApi;
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.identityApi.setAssignmentApi(assignmentApi);
		this.assignmentApi.setIdentityApi(identityApi);
	}

	private void populateTokenDates(TokenData tokenData, Calendar expires, Trust trust, Calendar issuedAt) {
		if (expires == null) {
			expires = BaseProvider.getDefaultExpireTime();
		}
		tokenData.setExpireAt(expires);
		tokenData.setIssuedAt(issuedAt == null ? Calendar.getInstance() : issuedAt);
	}

	private void populateServiceCatalog(TokenData tokenData, String userid, String domainid, String projectid, Trust trust)
			throws Exception {
		if (tokenData.getCatalog() != null) {
			return;
		}
		boolean enabled = Config.getOpt(Config.Type.trust, "enabled").asBoolean();

		if (enabled && trust != null) {
			userid = trust.getTrustorUserId();
		}
		if (!Strings.isNullOrEmpty(projectid) || !Strings.isNullOrEmpty(domainid)) {
			List<Service> catalog = catalogApi.getV3Catalog(userid, projectid);
			tokenData.setCatalog(catalog);
		}

	}

	private void populateRoles(TokenData tokenData, String userid, String domainid, String projectid, Trust trust)
			throws Exception {
		if (!tokenData.getRoles().isEmpty()) {
			return;
		}

		boolean enabled = Config.getOpt(Config.Type.trust, "enabled").asBoolean();

		String tokenUserid;
		String tokenProjectid;
		String tokenDomainid;
		if (enabled && trust != null) {
			tokenUserid = trust.getTrustorUserId();
			tokenProjectid = trust.getProjectId();
			tokenDomainid = null;
		} else {
			tokenUserid = userid;
			tokenProjectid = projectid;
			tokenDomainid = domainid;
		}

		List<Role> roles = Lists.newArrayList();
		logger.debug("tokenDomainid: {}, tokenProjectid: {}", new Object[] { tokenDomainid, tokenProjectid });
		if (!Strings.isNullOrEmpty(tokenDomainid) || !Strings.isNullOrEmpty(tokenProjectid)) {
			roles = getRolesForUser(tokenUserid, tokenDomainid, tokenProjectid);
			logger.debug("get Roles for user: {}", roles);
			List<Role> filteredRoles = Lists.newArrayList();
			if (enabled && trust != null) {
				for (Role trustRole : trust.getRoles()) {
					List<Role> matchRoles = Lists.newArrayList();
					for (Role role : roles) {
						if (role.getId().equals(trustRole.getId())) {
							matchRoles.add(role);
						}
					}

					if (matchRoles.isEmpty()) {
						Exceptions.ForbiddenException.getInstance("Trustee has no delegated roles");
					} else {
						filteredRoles.add(matchRoles.get(0));
					}

				}
			} else {
				filteredRoles.addAll(roles);
			}

			if (filteredRoles.isEmpty()) {
				if (!Strings.isNullOrEmpty(tokenProjectid)) {
					String msg = "User {0} has no access to project {1}";
					msg = MessageFormat.format(msg, userid, tokenProjectid);
					throw Exceptions.UnauthorizedException.getInstance(msg);
				} else {
					String msg = "User {0} has no access to domain {1}";
					msg = MessageFormat.format(msg, userid, domainid);
					throw Exceptions.UnauthorizedException.getInstance(msg);
				}
			}
			tokenData.setRoles(filteredRoles);
		}

	}

	private List<Role> getRolesForUser(String userid, String domainid, String projectid) throws Exception {
		List<String> roles = Lists.newArrayList();
		if (!Strings.isNullOrEmpty(domainid)) {
			roles = assignmentApi.getRolesForUserAndDomain(userid, domainid);
			logger.debug("get roles {} for user: {} and domain: {}", new Object[] { roles.size(), userid, domainid });
		}

		if (!Strings.isNullOrEmpty(projectid)) {
			roles = assignmentApi.getRolesForUserAndProject(userid, projectid);
			logger.debug("get roles {} for user: {} and project: {}", new Object[] { roles.size(), userid, projectid });
		}

		List<Role> rets = Lists.newArrayList();

		for (String roleid : roles) {
			rets.add(assignmentApi.getRole(roleid));
		}

		return rets;
	}

	private void populateUser(TokenData tokenData, String userid, Trust trust) throws Exception {
		if (tokenData.getUser() != null) {
			return;
		}

		User userRef = this.identityApi.getUser(userid);
		boolean enabled = Config.getOpt(Config.Type.trust, "enabled").asBoolean();
		if (enabled && trust != null && tokenData.getTrust() != null) {
			User trustorUserRef = this.identityApi.getUser(trust.getTrustorUserId());
			try {
				identityApi.assertUserEnabled(trust.getTrustorUserId(), null);
			} catch (Exception e) {
				throw Exceptions.ForbiddenException.getInstance("Trustor id disabled.");
			}
			if (trust.getImpersonation()) {
				userRef = trustorUserRef;
			}
			TokenData.Trust t = new TokenData.Trust();
			t.setId(trust.getId());
			User trustee = new User();
			trustee.setId(trust.getTrusteeUserId());
			t.setTrusteeUser(trustee);
			User trustor = new User();
			trustor.setId(trust.getTrustorUserId());
			t.setTrustorUser(trustor);
			t.setImpersonation(trust.getImpersonation());
			tokenData.setTrust(t);
		}
		User filteredUser = new User();
		filteredUser.setId(userRef.getId());
		filteredUser.setName(userRef.getName());
		filteredUser.setDomain(getFilteredDomain(userRef.getDomainId()));
		filteredUser.setIp(userRef.getIp());
		filteredUser.setPort(userRef.getPort());
		tokenData.setUser(filteredUser);
	}

	private void populateScope(TokenData tokenData, String domainid, String projectid) throws Exception {
		if (tokenData.getDomain() != null || tokenData.getProject() != null) {
			return;
		}

		if (!Strings.isNullOrEmpty(domainid)) {
			tokenData.setDomain(getFilteredDomain(domainid));
		}

		if (!Strings.isNullOrEmpty(projectid)) {
			tokenData.setProject(getFilteredProject(projectid));
		}
	}

	private Project getFilteredProject(String projectid) throws Exception {
		Project project = assignmentApi.getProject(projectid);
		return project;
	}

	private Domain getFilteredDomain(String domainid) throws Exception {
		Domain domain = this.assignmentApi.getDomain(domainid);
		return domain;
	}

	// domainid=null,projectid==null,expires=null,trust=null,token=null,includeCatalog=true,bind=null,issuedAt=null,auditInfo=null
	public TokenDataWrapper getTokenData(String userid, List<String> methodNames, Map<String, String> extras,
			String domainid, String projectid, Calendar expires, Trust trust, Bind bind, Token token,
			boolean includeCatalog, Calendar issuedAt, Object auditInfo) throws Exception {
		TokenData tokenData = new TokenData();
		tokenData.setMethods(methodNames);
		tokenData.setExtras(extras);

		// TODO ignore federation
		// # We've probably already written these to the token
		// if token:
		// for x in ('roles', 'user', 'catalog', 'project', 'domain'):
		// if x in token:
		// token_data[x] = token[x]

		if (Config.getOpt(Config.Type.trust, "enabled").asBoolean() && trust != null) {
			if (!trust.getTrusteeUserId().equals(userid)) {
				throw Exceptions.ForbiddenException.getInstance("User is not a trustee");
			}
		}

		if (bind != null) {
			tokenData.setBind(bind);
		}

		logger.debug("userid: {}, domainid: {}, projectid: {}", new Object[] { userid, domainid, projectid });
		populateScope(tokenData, domainid, projectid);
		populateUser(tokenData, userid, trust);
		populateRoles(tokenData, userid, domainid, projectid, trust);
		logger.debug("roles: {}", tokenData.getRoles());
		populateAuditInfo(tokenData, auditInfo);

		if (includeCatalog) {
			populateServiceCatalog(tokenData, userid, domainid, projectid, trust);
		}

		populateTokenDates(tokenData, expires, trust, issuedAt);
		// TODO ignore populateOauthSection(tokenData,accessToken);

		TokenDataWrapper wrapper = new TokenDataWrapper();
		wrapper.setToken(tokenData);
		return wrapper;
	}

	// auditInfo=null
	@SuppressWarnings("unchecked")
	private void populateAuditInfo(TokenData tokenData, Object auditInfo) throws UnsupportedEncodingException {
		if (auditInfo == null || auditInfo instanceof String) {
			tokenData.setAuditIds(BaseProvider.auditInfo(auditInfo == null ? null : (String) auditInfo));
		} else if (auditInfo instanceof List) {
			tokenData.setAuditIds((List<String>) auditInfo);
		} else {
			String msg = "Invalid audit info data type";
			logger.error(msg);
			throw Exceptions.UnexpectedException.getInstance(msg);
		}
	}
}
