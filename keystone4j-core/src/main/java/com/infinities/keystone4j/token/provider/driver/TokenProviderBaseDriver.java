package com.infinities.keystone4j.token.provider.driver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.model.AuthContext;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.auth.TokenMetadata;
import com.infinities.keystone4j.model.catalog.Catalog;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.model.common.Link;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.Metadata;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.TokenDataWrapper;
import com.infinities.keystone4j.model.token.v2.Access;
import com.infinities.keystone4j.model.token.v2.TokenV2;
import com.infinities.keystone4j.model.token.v2.TokenV2DataWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDataHelper;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;
import com.infinities.keystone4j.trust.TrustApi;

public abstract class TokenProviderBaseDriver implements TokenProviderDriver {

	private final static Logger logger = LoggerFactory.getLogger(TokenProviderBaseDriver.class);
	private final AssignmentApi assignmentApi;
	private final CatalogApi catalogApi;
	// private CatalogApi catalogApi;
	private final TokenApi tokenApi;
	private final TrustApi trustApi;
	private final TokenDataHelper helper;
	private final String DEFAULT_DOMAIN_ID = Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText();


	public TokenProviderBaseDriver(IdentityApi identityApi, AssignmentApi assignmentApi, CatalogApi catalogApi,
			TokenApi tokenApi, TrustApi trustApi) {
		helper = new TokenDataHelper(identityApi, assignmentApi, catalogApi);
		this.tokenApi = tokenApi;
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.trustApi = trustApi;
	}

	@Override
	public TokenMetadata issueV3Token(String userid, List<String> methodNames, Date expiresAt, String projectid,
			String domainid, AuthContext authContext, Trust trust, Token token, boolean includeCatalog) {
		boolean enabled = Config.Instance.getOpt(Config.Type.trust, "enabled").asBoolean();
		if (enabled && trust == null && token != null && token.getTrust() != null) {
			trust = token.getTrust();
		}

		if (methodNames.contains("oauth1")) {
			throw Exceptions.ForbiddenException.getInstance("Oauth is disabled.");
		}

		TokenDataWrapper tokenData = helper.getTokenData(userid, methodNames, expiresAt, projectid, domainid,
				authContext.getBind(), trust, token, includeCatalog);

		String tokenid = getTokenId(tokenData);
		if (token == null) {
			token = new Token();
		}

		List<Role> roles = Lists.newArrayList();
		if (tokenData.getToken().getProject() != null) {
			roles.addAll(tokenData.getToken().getRoles());
		}
		if (trust != null) {
			token.setTrust(trust);
		}
		token.setUser(tokenData.getToken().getUser());
		token.setTokenData(tokenData);
		token.setId(tokenid);
		token = this.tokenApi.createToken(token);
		tokenData.getToken().setToken(token);

		return new TokenMetadata(tokenid, tokenData);
	}

	@Override
	public void revokeToken(String tokenid) {
		this.tokenApi.deleteToken(tokenid);
	}

	@Override
	public TokenDataWrapper validateV3Token(String uniqueid) {
		Token token = this.verifyToken(uniqueid);
		logger.debug("validate token uniqueid: {}", uniqueid);
		TokenDataWrapper tokenData = validateV3TokenRef(token);

		return tokenData;
	}

	private Token verifyToken(String tokenid) {
		Token token = this.tokenApi.getToken(tokenid);
		return verifyTokenRef(token);
	}

	private Token verifyTokenRef(Token token) {
		if (token == null) {
			throw Exceptions.UnauthorizedException.getInstance();
		}
		return token;

	}

	private TokenDataWrapper validateV3TokenRef(Token token) {
		// Project project = token.getTrust().getProject();
		List<String> methodNames = Lists.newArrayList();
		methodNames.add("password");
		methodNames.add("token");
		TokenDataWrapper tokenData = token.getTokenData();
		if (tokenData == null || tokenData.getToken() == null) {
			String projectid = null;
			if (token.getProject() != null) {
				projectid = token.getProject().getId();
			}
			logger.debug("validate token");
			return this.helper.getTokenData(token.getUser().getId(), methodNames, token.getExpires(), projectid, null,
					token.getBind(), null, null, true);
		}
		return tokenData;
	}

	@Override
	public TokenV2DataWrapper validateV2Token(String uniqueid) {
		Token tokenRef = verifyToken(uniqueid);
		return validateV2TokenRef(tokenRef);
	}

	private TokenV2DataWrapper validateV2TokenRef(Token tokenRef) {
		try {
			assertDefaultDomain(tokenRef);
			Metadata metadataRef = tokenRef.getMetadata();
			List<Role> rolesRef = new ArrayList<Role>();
			for (String roleid : metadataRef.getRoles()) {
				rolesRef.add(assignmentApi.getRole(roleid));
			}
			Catalog catalogRef = null;
			if (tokenRef.getProject() != null) {
				catalogRef = catalogApi.getV3Catalog(tokenRef.getUser().getId(), tokenRef.getProject().getId());
			}

			TokenV2DataWrapper tokenData = formatToken(tokenRef, rolesRef, catalogRef);
			return tokenData;
		} catch (Exception e) {
			logger.error("Failed to validate token");
			throw Exceptions.TokenNotFoundException.getInstance(e);
		}

	}

	private void assertDefaultDomain(Token tokenRef) {
		String msg = "Non-default domain is not supported";
		TokenDataWrapper tokenData = tokenRef.getTokenData();

		if (!DEFAULT_DOMAIN_ID.equals(tokenData.getToken().getUser().getDomain().getId())) {
			throw Exceptions.UnauthorizedException.getInstance(msg);
		}

		if (tokenData.getToken().getProject() != null) {
			Project project = tokenData.getToken().getProject();
			String projectDomainId = project.getDomain().getId();

			if (!DEFAULT_DOMAIN_ID.equals(projectDomainId)) {
				throw Exceptions.UnauthorizedException.getInstance(msg);
			}
		}

		Metadata metadataRef = tokenRef.getMetadata();
		boolean enabled = Config.Instance.getOpt(Config.Type.trust, "enabled").asBoolean();
		if (enabled && !Strings.isNullOrEmpty(metadataRef.getTrustId())) {
			Trust trustRef = trustApi.getTrust(metadataRef.getTrustId());
			User trusteeUserRef = trustRef.getTrustee();
			if (!DEFAULT_DOMAIN_ID.equals(trusteeUserRef.getDomainid())) {
				throw Exceptions.UnauthorizedException.getInstance(msg);
			}
			User trustorUserRef = trustRef.getTrustor();
			if (!DEFAULT_DOMAIN_ID.equals(trustorUserRef.getDomainid())) {
				throw Exceptions.UnauthorizedException.getInstance(msg);
			}
			Project projectRef = trustRef.getProject();
			if (!DEFAULT_DOMAIN_ID.equals(projectRef.getDomainid())) {
				throw Exceptions.UnauthorizedException.getInstance(msg);
			}
		}

	}

	@Override
	public TokenV2DataWrapper formatToken(Token tokenRef, List<Role> rolesRef, Catalog catalogRef) {
		User userRef = tokenRef.getUser();
		Metadata metadataRef = tokenRef.getMetadata();
		if (rolesRef == null) {
			rolesRef = new ArrayList<Role>();
		}
		Date expires = tokenRef.getExpires();
		if (expires == null) {
			expires = getDefaultExpireTime();
		}
		Access access = new Access();
		TokenV2 token = new TokenV2();
		token.setId(tokenRef.getId());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(expires);
		token.setExpires(calendar);
		token.setIssued_at(Calendar.getInstance());
		access.setToken(token);

		Access.User user = new Access.User();
		user.setId(userRef.getId());
		user.setName(userRef.getName());
		user.setUsername(userRef.getName());
		List<Access.User.Role> roles = new ArrayList<Access.User.Role>();
		for (Role roleRef : rolesRef) {
			Access.User.Role role = new Access.User.Role();
			role.setName(roleRef.getName());
			role.setId(roleRef.getId());
			roles.add(role);
		}
		user.setRoles(roles);
		user.setRolesLinks(new ArrayList<Link>());
		if (tokenRef.getBind() != null) {
			access.getToken().setBind(tokenRef.getBind());
		}
		if (tokenRef.getProject() != null) {
			access.getToken().setTenant(tokenRef.getProject());
		}
		if (catalogRef != null) {
			access.setServiceCatalog(formatCatalog(catalogRef));
		}
		access.setMetadata(new Access.Metadata());
		access.getMetadata().setIsAdmin(0);
		if (metadataRef.getRoles() != null) {
			access.getMetadata().setRoles(new ArrayList<String>(metadataRef.getRoles()));
		}
		boolean enabled = Config.Instance.getOpt(Config.Type.trust, "enabled").asBoolean();
		if (enabled && !Strings.isNullOrEmpty(metadataRef.getTrustId())) {
			Access.Trust trust = new Access.Trust();
			trust.setId(metadataRef.getTrustId());
			trust.setTrusteeUserId(metadataRef.getTrusteeUserId());
			access.setTrust(trust);
		}

		TokenV2DataWrapper wrapper = new TokenV2DataWrapper();
		wrapper.setAccess(access);
		return wrapper;
	}

	private List<Access.Service> formatCatalog(Catalog catalogRef) {
		if (catalogRef == null) {
			return new ArrayList<Access.Service>();
		}
		List<Access.Service> services = new ArrayList<Access.Service>();
		for (com.infinities.keystone4j.model.catalog.Service service : catalogRef.getServices()) {
			Access.Service s = new Access.Service();
			s.setName(service.getName());
			s.setType(service.getType());
			s.setEndpointsLinks(new ArrayList<Link>());
			Access.Service.Endpoint e = new Access.Service.Endpoint();
			for (Endpoint endpoint : service.getEndpoints()) {
				e.setRegion(endpoint.getRegion());
				if (endpoint.getInterfaceType().toLowerCase().contains("public")) {
					e.setPublicURL(endpoint.getUrl());
				}
				if (endpoint.getInterfaceType().toLowerCase().contains("internal")) {
					e.setInternalURL(endpoint.getUrl());
				}
				if (endpoint.getInterfaceType().toLowerCase().contains("admin")) {
					e.setAdminURL(endpoint.getUrl());
				}
			}
			s.getEndpoints().add(e);
		}

		return services;
	}

	private Date getDefaultExpireTime() {
		Calendar calendar = Calendar.getInstance();
		int expiration = Config.Instance.getOpt(Config.Type.token, "expiration").asInteger();
		calendar.add(Calendar.SECOND, expiration);
		return calendar.getTime();
	}

}
