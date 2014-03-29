package com.infinities.keystone4j.token;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.model.Catalog;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.model.Bind;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.token.model.TokenData;
import com.infinities.keystone4j.token.model.TokenDataWrapper;
import com.infinities.keystone4j.trust.model.Trust;
import com.infinities.keystone4j.trust.model.TrustRole;

public class TokenDataHelper {

	private final IdentityApi identityApi;
	private final AssignmentApi assignmentApi;
	private final CatalogApi catalogApi;


	public TokenDataHelper(IdentityApi identityApi, AssignmentApi assignmentApi, CatalogApi catalogApi) {
		this.identityApi = identityApi;
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
	}

	public TokenDataWrapper getTokenData(String userid, List<String> methodNames, Date expiresAt, String projectid,
			String domainid, Bind bind, Trust trust, Token token, boolean includeCatalog) {
		TokenData tokenData = new TokenData();
		tokenData.setMethods(methodNames);

		if (token != null) {
			if (token.getUser() != null) {
				tokenData.setUser(token.getUser());
			}
		}

		boolean enabled = Config.Instance.getOpt(Config.Type.trust, "enabled").asBoolean();

		if (enabled && trust != null) {
			if (!userid.equals(trust.getTrustee().getId())) {
				Exceptions.ForbiddenException.getInstance("User is not a trustee");
			}
		}

		if (bind != null) {
			tokenData.setBind(bind);
		}

		populateScope(tokenData, domainid, projectid);
		populateUser(tokenData, userid, trust);
		populateRoles(tokenData, userid, domainid, projectid, trust);
		if (includeCatalog) {
			populateServiceCatalog(tokenData, userid, domainid, projectid, trust);
		}
		populateTokenDates(tokenData, expiresAt, trust);
		return new TokenDataWrapper(tokenData);
	}

	private void populateTokenDates(TokenData tokenData, Date expiresAt, Trust trust) {
		if (expiresAt == null) {
			int expireSec = Config.Instance.getOpt(Config.Type.token, "expiration").asInteger();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, expireSec);
			expiresAt = calendar.getTime();
		}

		tokenData.setExpireAt(expiresAt);
		tokenData.setIssuedAt(new Date());
	}

	private void populateServiceCatalog(TokenData tokenData, String userid, String domainid, String projectid, Trust trust) {
		if (tokenData.getCatalog() != null) {
			return;
		}
		boolean enabled = Config.Instance.getOpt(Config.Type.trust, "enabled").asBoolean();

		if (enabled && trust != null) {
			userid = trust.getTrustor().getId();
		}

		if (!Strings.isNullOrEmpty(projectid) || !Strings.isNullOrEmpty(domainid)) {
			Catalog catalog;
			try {
				catalog = catalogApi.getV3Catalog(userid, projectid);
			} catch (Exception e) {
				catalog = new Catalog();
			}
			tokenData.setCatalog(catalog);
		}

	}

	private void populateRoles(TokenData tokenData, String userid, String domainid, String projectid, Trust trust) {
		if (!tokenData.getRoles().isEmpty()) {
			return;
		}

		boolean enabled = Config.Instance.getOpt(Config.Type.trust, "enabled").asBoolean();

		String tokenUserid;
		String tokenProjectid;
		String tokenDomainid;
		if (enabled && trust != null) {
			tokenUserid = trust.getTrustor().getId();
			tokenProjectid = trust.getProject().getId();
			tokenDomainid = null;
		} else {
			tokenUserid = userid;
			tokenProjectid = projectid;
			tokenDomainid = domainid;
		}

		List<Role> roles = Lists.newArrayList();
		if (!Strings.isNullOrEmpty(tokenDomainid) || !Strings.isNullOrEmpty(tokenProjectid)) {
			roles = getRolesForUser(tokenUserid, tokenDomainid, tokenProjectid);
			List<Role> filteredRoles = Lists.newArrayList();
			if (enabled && trust != null) {
				for (TrustRole trustRole : trust.getTrustRoles()) {
					List<Role> matchRoles = Lists.newArrayList();
					for (Role role : roles) {
						if (role.getId().equals(trustRole.getRole().getId())) {
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

	private List<Role> getRolesForUser(String userid, String domainid, String projectid) {
		List<Role> roles = Lists.newArrayList();
		if (!Strings.isNullOrEmpty(domainid)) {
			roles = assignmentApi.getRolesForUserAndDomain(userid, domainid);
		}

		if (!Strings.isNullOrEmpty(projectid)) {
			roles = assignmentApi.getRolesForUserAndProject(userid, projectid);
		}
		return roles;
	}

	private void populateUser(TokenData tokenData, String userid, Trust trust) {
		if (tokenData.getUser() != null) {
			return;
		}

		User user = this.identityApi.getUser(userid, null);
		boolean enabled = Config.Instance.getOpt(Config.Type.trust, "enabled").asBoolean();

		if (enabled && trust != null && tokenData.getTrust() != null) {
			User trustor = this.identityApi.getUser(trust.getTrustor().getId(), null);
			if (!trustor.getEnabled()) {
				Exceptions.ForbiddenException.getInstance("Trustor is disabled");
			}
			if (trust.getImpersonation()) {
				user = trustor;
			}
			tokenData.setTrust(trust);
		}

		tokenData.setUser(user);
	}

	private void populateScope(TokenData tokenData, String domainid, String projectid) {
		if (tokenData.getDomain() != null && tokenData.getProject() != null) {
			return;
		}

		if (!Strings.isNullOrEmpty(domainid)) {
			tokenData.setDomain(getFilteredDomain(domainid));
		}

		if (!Strings.isNullOrEmpty(projectid)) {
			tokenData.setProject(getFilteredProject(projectid));
		}
	}

	private Project getFilteredProject(String projectid) {
		Project project = assignmentApi.getProject(projectid);
		return project;
	}

	private Domain getFilteredDomain(String domainid) {
		Domain domain = this.assignmentApi.getDomain(domainid);
		return domain;
	}
}
