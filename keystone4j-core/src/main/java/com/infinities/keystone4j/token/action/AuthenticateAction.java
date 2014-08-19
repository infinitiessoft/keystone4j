package com.infinities.keystone4j.token.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.infinities.keystone4j.Environment;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.catalog.Catalog;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.Auth;
import com.infinities.keystone4j.model.token.Bind;
import com.infinities.keystone4j.model.token.Metadata;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.v2.TokenV2DataWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.model.trust.TrustRole;
import com.infinities.keystone4j.token.ExternalAuthNotApplicableException;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class AuthenticateAction extends AbstractTokenAction<TokenV2DataWrapper> {

	private final static Logger logger = LoggerFactory.getLogger(AuthenticateAction.class);
	private final Auth auth;
	private final String DEFAULT_DOMAIN_ID = Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText();


	public AuthenticateAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenApi tokenApi, TokenProviderApi tokenProviderApi, TrustApi trustApi, Auth auth) {
		super(assignmentApi, catalogApi, identityApi, tokenApi, tokenProviderApi, trustApi);
		this.auth = auth;
	}

	@Override
	public TokenV2DataWrapper execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		if (auth == null) {
			throw Exceptions.ValidationException.getInstance(null, "auth", "request body");
		}
		AuthInfo authInfo = null;

		if (auth.getToken() != null) {
			authInfo = authenticateToken(context, auth);
		} else {
			try {
				authInfo = authenticateExternal(context, auth);
			} catch (ExternalAuthNotApplicableException e) {
				authInfo = authenticateLocal(context, auth);
			}
		}

		User userRef = authInfo.getCurrentUserRef();
		Project tenantRef = authInfo.getTenantRef();
		Metadata metadataRef = authInfo.getMetadataRef();
		Date expiry = authInfo.getExpiry();
		Bind bind = authInfo.getBind();

		validateAuthInfo(userRef, tenantRef);
		userRef = filterDomainId(userRef);
		if (tenantRef != null) {
			tenantRef = filterDomainId(tenantRef);
		}

		Token authTokenData = new Token();
		authTokenData.setUser(userRef);
		authTokenData.setProject(tenantRef);
		authTokenData.setMetadata(metadataRef);
		authTokenData.setExpires(expiry);

		Catalog catalogRef = new Catalog();
		if (tenantRef != null) {
			catalogRef = catalogApi.getV3Catalog(userRef.getId(), tenantRef.getId());
		}

		authTokenData.setId("placeholder");
		if (bind != null) {
			authTokenData.setBind(bind);
		}

		List<Role> rolesRef = new ArrayList<Role>();
		for (String roleid : metadataRef.getRoles()) {
			rolesRef.add(assignmentApi.getRole(roleid));
		}

		Entry<String, TokenV2DataWrapper> ret = tokenProviderApi.issueV2Token(authTokenData, rolesRef, catalogRef);

		return ret.getValue();
	}

	private Project filterDomainId(Project tenantRef) {
		return tenantRef;
	}

	private User filterDomainId(User userRef) {
		return userRef;
	}

	private void validateAuthInfo(User userRef, Project tenantRef) {
		if (userRef.getEnabled() == null || !userRef.getEnabled()) {
			String msg = String.format("User is disabled: %s", userRef.getId());
			logger.warn(msg);
			throw Exceptions.UnauthorizedException.getInstance(msg);
		}

		Domain userDomainRef = userRef.getDomain();
		if (userDomainRef != null && (userDomainRef.getEnabled() == null || !userDomainRef.getEnabled())) {
			String msg = String.format("Domain is disabled: %s", userRef.getDomainid());
			logger.warn(msg);
			throw Exceptions.UnauthorizedException.getInstance(msg);
		}

		if (tenantRef != null) {
			if (tenantRef.getEnabled() == null || !tenantRef.getEnabled()) {
				String msg = String.format("Tenant is disabled: %s", tenantRef.getId());
				logger.warn(msg);
				throw Exceptions.UnauthorizedException.getInstance(msg);
			}

			Domain projectDomainRef = tenantRef.getDomain();
			if (projectDomainRef != null && (projectDomainRef.getEnabled() == null || !projectDomainRef.getEnabled())) {
				String msg = String.format("Domain is disabled: %s", projectDomainRef.getId());
				logger.warn(msg);
				throw Exceptions.UnauthorizedException.getInstance(msg);
			}

		}

	}

	private AuthInfo authenticateExternal(KeystoneContext context, Auth auth) throws ExternalAuthNotApplicableException {
		Environment environment = context.getEnvironment();
		if (Strings.isNullOrEmpty(environment.getRemoteUser())) {
			throw new ExternalAuthNotApplicableException();
		}

		if (auth == null) {
			auth = new Auth();
		}

		String username = environment.getRemoteUser();
		User userRef = null;
		String userid;
		try {
			userRef = identityApi.getUserByName(username, Config.Instance.getOpt(Config.Type.identity, "default_domain_id")
					.asText());
			userid = userRef.getId();
		} catch (Exception e) {
			throw Exceptions.UnauthorizedException.getInstance(e);
		}

		Metadata metadataRef = new Metadata();
		String tenantid = getProjectIdFromAuth(auth);
		Entry<Project, Set<String>> entry = getProjectRolesAndRef(userid, tenantid);
		Project tenantRef = entry.getKey();
		metadataRef.setRoles(entry.getValue());

		Date expiry = getDefaultExpireTime();
		Bind bind = null;

		List<String> binds = Config.Instance.getOpt(Config.Type.token, "bind").asList();
		if (binds.contains("kerberos") && "negotiate".equals(environment.getAuthType().toLowerCase())) {
			bind = new Bind();
			bind.setBindType("kerberos");
			bind.setIdentifier(username);
		}

		return new AuthInfo(userRef, tenantRef, metadataRef, expiry, bind);
	}

	private Date getDefaultExpireTime() {
		Calendar calendar = Calendar.getInstance();
		int expiration = Config.Instance.getOpt(Config.Type.token, "expiration").asInteger();
		calendar.add(Calendar.SECOND, expiration);
		return calendar.getTime();
	}

	private AuthInfo authenticateLocal(KeystoneContext context, Auth auth) {
		if (auth.getPasswordCredentials() == null) {
			throw Exceptions.ValidationException.getInstance(null, "passwordCredentials", "auth");
		}

		if (Strings.isNullOrEmpty(auth.getPasswordCredentials().getPassword())) {
			throw Exceptions.ValidationException.getInstance(null, "password", "passwordCredentials");
		}
		String password = auth.getPasswordCredentials().getPassword();
		int maxPasswordSize = Config.Instance.getOpt(Config.Type.identity, "max_password_length").asInteger();
		if (password.length() > maxPasswordSize) {
			throw Exceptions.ValidationSizeException.getInstance(null, "password", maxPasswordSize);
		}

		if (Strings.isNullOrEmpty(auth.getPasswordCredentials().getUserId())
				&& Strings.isNullOrEmpty(auth.getPasswordCredentials().getUsername())) {
			throw Exceptions.ValidationException.getInstance(null, "username or userId", "passwordCredentials");
		}

		String userid = auth.getPasswordCredentials().getUserId();
		int maxParamSize = Config.Instance.getOpt(Config.Type.DEFAULT, "max_param_size").asInteger();
		if (!Strings.isNullOrEmpty(userid) && userid.length() > maxParamSize) {
			throw Exceptions.ValidationSizeException.getInstance(null, "userId", maxParamSize);
		}

		User userRef = null;
		String username = auth.getPasswordCredentials().getUsername();
		if (!Strings.isNullOrEmpty(username)) {
			if (username.length() > maxParamSize) {
				throw Exceptions.ValidationSizeException.getInstance(null, "username", maxParamSize);
			}
			try {
				userRef = identityApi.getUserByName(username,
						Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText());
				userid = userRef.getId();
			} catch (Exception e) {
				throw Exceptions.UnauthorizedException.getInstance(e);
			}
		}

		try {
			userRef = identityApi.authenticate(userid, password, null);
		} catch (Exception e) {
			throw Exceptions.UnauthorizedException.getInstance(e);
		}

		Metadata metadataRef = new Metadata();
		String tenantid = getProjectIdFromAuth(auth);
		Entry<Project, Set<String>> entry = getProjectRolesAndRef(userid, tenantid);
		Project tenantRef = entry.getKey();
		metadataRef.setRoles(entry.getValue());

		Date expiry = getDefaultExpireTime();

		return new AuthInfo(userRef, tenantRef, metadataRef, expiry, null);
	}

	private AuthInfo authenticateToken(KeystoneContext context, Auth auth) {
		if (auth.getToken() == null) {
			throw Exceptions.ValidationException.getInstance(null, "token", "auth");
		}
		if (Strings.isNullOrEmpty(auth.getToken().getId())) {
			throw Exceptions.ValidationException.getInstance(null, "id", "token");
		}

		String oldToken = auth.getToken().getId();
		int maxTokenSize = Config.Instance.getOpt(Config.Type.DEFAULT, "max_token_size").asInteger();
		if (oldToken.length() > maxTokenSize) {
			throw Exceptions.ValidationSizeException.getInstance(null, "id", "token");
		}

		Token oldTokenRef = null;

		try {
			oldTokenRef = tokenApi.getToken(oldToken);
		} catch (WebApplicationException e) {
			throw Exceptions.UnauthorizedException.getInstance(e);
		}

		validateTokenBind(context, oldTokenRef);

		if (oldTokenRef.getTrust() != null) {
			throw Exceptions.ForbiddenException.getInstance();
		}
		if (!Strings.isNullOrEmpty(oldTokenRef.getMetadata().getTrustId())) {
			throw Exceptions.ForbiddenException.getInstance();
		}

		User user = oldTokenRef.getUser();
		String userid = user.getId();
		User currentUserRef = null;
		Trust trustRef = null;
		String tenantid = getProjectIdFromAuth(auth);
		boolean enabled = Config.Instance.getOpt(Config.Type.trust, "enabled").asBoolean();
		if (!enabled && !Strings.isNullOrEmpty(auth.getTrustId())) {
			throw Exceptions.ForbiddenException.getInstance("Trusts are disabled.");
		} else if (enabled && !Strings.isNullOrEmpty(auth.getTrustId())) {
			trustRef = trustApi.getTrust(auth.getTrustId());
			if (trustRef == null) {
				throw Exceptions.ForbiddenException.getInstance();
			}
			if (!userid.equals(trustRef.getTrustee().getUser().getId())) {
				throw Exceptions.ForbiddenException.getInstance();
			}
			if (trustRef.getProject() != null && !tenantid.equals(trustRef.getProject().getId())) {
				throw Exceptions.ForbiddenException.getInstance();
			}
			if (trustRef.getExpiresAt() != null) {
				Calendar now = Calendar.getInstance();
				Calendar expires = Calendar.getInstance();
				expires.setTime(trustRef.getExpiresAt());
				if (now.after(expires)) {
					throw Exceptions.ForbiddenException.getInstance();
				}
			}
			userid = trustRef.getTrustor().getId();
			User trustorUserRef = trustRef.getTrustor();
			if (!trustorUserRef.getEnabled()) {
				throw Exceptions.ForbiddenException.getInstance();
			}
			User trusteeUserRef = trustRef.getTrustee();
			if (!trusteeUserRef.getEnabled()) {
				throw Exceptions.ForbiddenException.getInstance();
			}
			if (trustRef.getImpersonation()) {
				currentUserRef = trustorUserRef;
			} else {
				currentUserRef = trusteeUserRef;
			}

		} else {
			currentUserRef = user;
		}

		Entry<Project, Set<String>> entry = getProjectRolesAndRef(userid, tenantid);
		Project tenantRef = entry.getKey();
		Metadata metadataRef = new Metadata();
		metadataRef.setRoles(entry.getValue());

		Date expiry = oldTokenRef.getExpires();
		if (enabled && !Strings.isNullOrEmpty(auth.getTrustId())) {
			String trustid = auth.getTrustId();
			Set<String> trustRoles = new HashSet<String>();
			for (TrustRole trustRole : trustRef.getTrustRoles()) {
				if (metadataRef.getRoles() == null) {
					throw Exceptions.ForbiddenException.getInstance();
				}
				if (metadataRef.getRoles().contains(trustRole.getRole().getId())) {
					trustRoles.add(trustRole.getRole().getId());
				} else {
					throw Exceptions.ForbiddenException.getInstance();
				}
			}
			if (trustRef.getExpiresAt() != null) {
				Date trustExpiry = trustRef.getExpiresAt();
				if (trustExpiry.before(expiry)) {
					expiry = trustExpiry;
				}
			}
			metadataRef.setRoles(trustRoles);
			metadataRef.setTrusteeUserId(trustRef.getTrustee().getId());
			metadataRef.setTrustId(trustid);
		}
		Bind bind = oldTokenRef.getBind();
		return new AuthInfo(currentUserRef, tenantRef, metadataRef, expiry, bind);

	}

	private Entry<Project, Set<String>> getProjectRolesAndRef(String userid, String tenantid) {
		Project tenantRef = null;
		Set<String> roleList = new HashSet<String>();

		if (!Strings.isNullOrEmpty(tenantid)) {
			try {
				tenantRef = assignmentApi.getProject(tenantid);
				List<Role> roles = assignmentApi.getRolesForUserAndProject(userid, tenantid);
				for (Role role : roles) {
					roleList.add(role.getId());
				}
			} catch (Exception e) {
				// pass
			}

			if (roleList.isEmpty()) {
				String msg = String.format("User %s is unauthorized for tenant %s", userid, tenantid);
				throw Exceptions.UnauthorizedException.getInstance(msg);
			}
		}
		return Maps.immutableEntry(tenantRef, roleList);
	}

	private String getProjectIdFromAuth(Auth auth) {
		String tenantid = auth.getTenantId();
		int maxParamSize = Config.Instance.getOpt(Config.Type.DEFAULT, "max_param_size").asInteger();
		if (!Strings.isNullOrEmpty(tenantid) && tenantid.length() > maxParamSize) {
			throw Exceptions.ValidationSizeException.getInstance(null, "tenantId", maxParamSize);
		}
		String tenantName = auth.getTenantName();
		if (!Strings.isNullOrEmpty(tenantName) && tenantName.length() > maxParamSize) {
			throw Exceptions.ValidationSizeException.getInstance(null, "tenantName", maxParamSize);
		}

		if (!Strings.isNullOrEmpty(tenantName)) {
			try {
				Project tenantRef = assignmentApi.getProjectByName(tenantName, DEFAULT_DOMAIN_ID);
				tenantid = tenantRef.getId();
			} catch (Exception e) {
				throw Exceptions.UnauthorizedException.getInstance(e);
			}
		}

		return tenantid;
	}

	@Override
	public String getName() {
		return "authenticate";
	}


	// public class AuthTokenData {
	//
	// private String id;
	// private User user;
	// private Project tenant;
	// private Metadata metadata;
	// private Date expires;
	// private Bind bind;
	//
	//
	// public AuthTokenData(User user, Project tenant, Metadata metadata, Date
	// expires) {
	// super();
	// this.user = user;
	// this.tenant = tenant;
	// this.metadata = metadata;
	// this.expires = expires;
	// }
	//
	// public User getUser() {
	// return user;
	// }
	//
	// public void setUser(User user) {
	// this.user = user;
	// }
	//
	// public Project getTenant() {
	// return tenant;
	// }
	//
	// public void setTenant(Project tenant) {
	// this.tenant = tenant;
	// }
	//
	// public Metadata getMetadata() {
	// return metadata;
	// }
	//
	// public void setMetadata(Metadata metadata) {
	// this.metadata = metadata;
	// }
	//
	// public Date getExpires() {
	// return expires;
	// }
	//
	// public void setExpires(Date expires) {
	// this.expires = expires;
	// }
	//
	// public String getId() {
	// return id;
	// }
	//
	// public void setId(String id) {
	// this.id = id;
	// }
	//
	// public Bind getBind() {
	// return bind;
	// }
	//
	// public void setBind(Bind bind) {
	// this.bind = bind;
	// }
	//
	// }

	private class AuthInfo {

		private final User currentUserRef;
		private final Project tenantRef;
		private final Metadata metadataRef;
		private final Date expiry;
		private final Bind bind;


		public AuthInfo(User currentUserRef, Project tenantRef, Metadata metadataRef, Date expiry, Bind bind) {
			this.currentUserRef = currentUserRef;
			this.tenantRef = tenantRef;
			this.metadataRef = metadataRef;
			this.expiry = expiry;
			this.bind = bind;
		}

		public User getCurrentUserRef() {
			return currentUserRef;
		}

		public Project getTenantRef() {
			return tenantRef;
		}

		public Metadata getMetadataRef() {
			return metadataRef;
		}

		public Date getExpiry() {
			return expiry;
		}

		public Bind getBind() {
			return bind;
		}

	}

}
