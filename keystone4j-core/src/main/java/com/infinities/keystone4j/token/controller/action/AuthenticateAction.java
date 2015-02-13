package com.infinities.keystone4j.token.controller.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.Wsgi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.auth.TokenIdAndDataV2;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.Auth;
import com.infinities.keystone4j.model.token.Bind;
import com.infinities.keystone4j.model.token.Metadata;
import com.infinities.keystone4j.model.token.v2.Access;
import com.infinities.keystone4j.model.token.v2.wrapper.TokenV2DataWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.ExternalAuthNotApplicableException;
import com.infinities.keystone4j.token.model.KeystoneToken;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi.AuthTokenData;
import com.infinities.keystone4j.token.provider.driver.BaseProvider;
import com.infinities.keystone4j.trust.TrustApi;

public class AuthenticateAction extends AbstractTokenAction implements ProtectedAction<Access> {

	private final static Logger logger = LoggerFactory.getLogger(AuthenticateAction.class);
	private final Auth auth;
	private final String DEFAULT_DOMAIN_ID = Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText();


	public AuthenticateAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, TrustApi trustApi, PolicyApi policyApi, Auth auth)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		super(assignmentApi, catalogApi, identityApi, tokenProviderApi, trustApi, policyApi);
		this.auth = auth;
	}

	@Override
	public MemberWrapper<Access> execute(ContainerRequestContext request) throws Exception {
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
		Calendar expiry = authInfo.getExpiry();
		Bind bind = authInfo.getBind();
		String auditId = authInfo.getAuditId();

		try {
			identityApi.assertUserEnabled(userRef.getId(), userRef);
			if (tenantRef != null) {
				assignmentApi.assertProjectEnabled(tenantRef.getId(), tenantRef);
			}
		} catch (Exception e) {
			throw e;
		}
		logger.debug("user exist? {}", new Object[] { String.valueOf(userRef != null) });
		userRef = v3ToV2User(userRef);
		if (tenantRef != null) {
			tenantRef = filterDomainId(tenantRef);
		}
		AuthTokenData authTokenData = getAuthTokenData(userRef, tenantRef, metadataRef, expiry, auditId);

		Map<String, Map<String, Map<String, String>>> catalogRef;
		if (tenantRef != null) {
			logger.debug("user exist? {}, tenant exist? {}",
					new Object[] { String.valueOf(userRef != null), String.valueOf(tenantRef != null) });
			catalogRef = catalogApi.getCatalog(userRef.getId(), tenantRef.getId(), metadataRef);
		} else {
			catalogRef = new HashMap<String, Map<String, Map<String, String>>>();
		}

		logger.debug("catalog exist? {}", new Object[] { String.valueOf(catalogRef != null) });

		authTokenData.setId("placeholder");
		if (bind != null) {
			authTokenData.setBind(bind);
		}

		List<Role> rolesRef = new ArrayList<Role>();
		for (String roleid : metadataRef.getRoles()) {
			rolesRef.add(assignmentApi.getRole(roleid));
		}

		TokenIdAndDataV2 ret = tokenProviderApi.issueV2Token(authTokenData, rolesRef, catalogRef);

		if (Config.Instance.getOpt(Config.Type.trust, "enabled").asBoolean() && !Strings.isNullOrEmpty(auth.getTrustId())) {
			trustApi.consumeUse(auth.getTrustId());
		}

		return ret.getTokenData();
	}

	private AuthTokenData getAuthTokenData(User userRef, Project tenantRef, Metadata metadataRef, Calendar expiry,
			String auditId) {
		AuthTokenData token = new AuthTokenData();
		token.setUser(userRef);
		token.setTenant(tenantRef);
		token.setMetadata(metadataRef);
		token.setExpires(expiry);
		token.setParentAuditId(auditId);
		return token;
	}

	private User v3ToV2User(User ref) {
		return normalizeAndFilterUserProperties(ref);
	}

	private User normalizeAndFilterUserProperties(User ref) {
		formatDefaultProjectId(ref);
		filterDomainId(ref);
		normalizeUsernameIdResponse(ref);
		return ref;
	}

	private void normalizeUsernameIdResponse(User ref) {
		if (Strings.isNullOrEmpty(ref.getUsername()) && !Strings.isNullOrEmpty(ref.getName())) {
			ref.setUsername(ref.getName());
		}
	}

	private void formatDefaultProjectId(User ref) {
		String defaultProjectId = ref.getDefaultProjectId();
		if (!Strings.isNullOrEmpty(defaultProjectId)) {
			ref.setTenantId(defaultProjectId);
		}
	}

	private Project filterDomainId(Project tenantRef) {
		// tenantRef.setDomainId(null);
		return tenantRef;
	}

	private User filterDomainId(User userRef) {
		// userRef.setDomainId(null);
		return userRef;
	}

	private AuthInfo authenticateExternal(KeystoneContext context, Auth auth) throws ExternalAuthNotApplicableException {
		Environment environment = context.getEnvironment();
		if (Strings.isNullOrEmpty(environment.getRemoteUser())) {
			throw new ExternalAuthNotApplicableException();
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
		metadataRef.setRoles(new ArrayList<String>(entry.getValue()));

		Calendar expiry = BaseProvider.getDefaultExpireTime();
		Bind bind = null;

		List<String> binds = Config.Instance.getOpt(Config.Type.token, "bind").asList();
		if (binds.contains("kerberos") && "negotiate".equals(environment.getAuthType().toLowerCase())) {
			bind = new Bind();
			bind.setBindType("kerberos");
			bind.setIdentifier(username);
		}
		String auditId = null;

		return new AuthInfo(userRef, tenantRef, metadataRef, expiry, bind, auditId);
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
			userRef = identityApi.authenticate(userid, password);
		} catch (Exception e) {
			throw Exceptions.UnauthorizedException.getInstance(e);
		}

		Metadata metadataRef = new Metadata();
		String tenantid = getProjectIdFromAuth(auth);
		Entry<Project, Set<String>> entry = getProjectRolesAndRef(userid, tenantid);
		Project tenantRef = entry.getKey();
		metadataRef.setRoles(new ArrayList<String>(entry.getValue()));

		Calendar expiry = BaseProvider.getDefaultExpireTime();
		Bind bind = null;
		String auditId = null;

		return new AuthInfo(userRef, tenantRef, metadataRef, expiry, bind, auditId);
	}

	private AuthInfo authenticateToken(KeystoneContext context, Auth auth) throws Exception {
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

		KeystoneToken tokenModelRef = null;

		try {
			tokenModelRef = new KeystoneToken(oldToken, tokenProviderApi.validateToken(oldToken, null));
		} catch (WebApplicationException e) {
			throw Exceptions.UnauthorizedException.getInstance(e);
		}

		Wsgi.validateTokenBind(context, tokenModelRef);

		if (tokenModelRef.isTrustScoped()) {
			throw Exceptions.ForbiddenException.getInstance();
		}

		String userid = tokenModelRef.getUserId();
		String tenantid = getProjectIdFromAuth(auth);
		User currentUserRef = null;
		Trust trustRef = null;

		boolean enabled = Config.Instance.getOpt(Config.Type.trust, "enabled").asBoolean();
		if (!enabled && !Strings.isNullOrEmpty(auth.getTrustId())) {
			throw Exceptions.ForbiddenException.getInstance("Trusts are disabled.");
		} else if (enabled && !Strings.isNullOrEmpty(auth.getTrustId())) {
			trustRef = trustApi.getTrust(auth.getTrustId(), false);
			if (trustRef == null) {
				throw Exceptions.ForbiddenException.getInstance();
			}
			if (!userid.equals(trustRef.getTrusteeUserId())) {
				throw Exceptions.ForbiddenException.getInstance();
			}
			if (!Strings.isNullOrEmpty(trustRef.getProjectId()) && !tenantid.equals(trustRef.getProjectId())) {
				throw Exceptions.ForbiddenException.getInstance();
			}
			if (trustRef.getExpiresAt() != null) {
				Calendar now = Calendar.getInstance();
				Calendar expires = trustRef.getExpiresAt();
				if (now.after(expires)) {
					throw Exceptions.ForbiddenException.getInstance();
				}
			}
			userid = trustRef.getTrustorUserId();
			User trustorUserRef = identityApi.getUser(trustRef.getTrustorUserId());
			if (!trustorUserRef.getEnabled()) {
				throw Exceptions.ForbiddenException.getInstance();
			}
			User trusteeUserRef = identityApi.getUser(trustRef.getTrusteeUserId());
			if (!trusteeUserRef.getEnabled()) {
				throw Exceptions.ForbiddenException.getInstance();
			}
			if (trustRef.getImpersonation()) {
				currentUserRef = trustorUserRef;
			} else {
				currentUserRef = trusteeUserRef;
			}

		} else {
			currentUserRef = identityApi.getUser(userid);
		}

		Metadata metadataRef = new Metadata();
		Entry<Project, Set<String>> entry = getProjectRolesAndRef(userid, tenantid);
		Project tenantRef = entry.getKey();
		metadataRef.setRoles(new ArrayList<String>(entry.getValue()));

		Calendar expiry = tokenModelRef.getExpires();
		if (enabled && !Strings.isNullOrEmpty(auth.getTrustId())) {
			String trustid = auth.getTrustId();
			Set<String> trustRoles = new HashSet<String>();
			for (Role role : trustRef.getRoles()) {
				if (metadataRef.getRoles() == null || metadataRef.getRoles().isEmpty()) {
					throw Exceptions.ForbiddenException.getInstance();
				}
				if (metadataRef.getRoles().contains(role.getId())) {
					trustRoles.add(role.getId());
				} else {
					throw Exceptions.ForbiddenException.getInstance();
				}
			}
			if (trustRef.getExpiresAt() != null) {
				Calendar trustExpiry = trustRef.getExpiresAt();
				if (trustExpiry.before(expiry)) {
					expiry = trustExpiry;
				}
			}
			metadataRef.setRoles(new ArrayList<String>(trustRoles));
			metadataRef.setTrusteeUserId(trustRef.getTrusteeUserId());
			metadataRef.setTrustId(trustid);
		}
		Bind bind = tokenModelRef.getBind();
		String auditId = tokenModelRef.getAutitChainId();
		return new AuthInfo(currentUserRef, tenantRef, metadataRef, expiry, bind, auditId);

	}

	private Entry<Project, Set<String>> getProjectRolesAndRef(String userid, String tenantid) {
		Project tenantRef = null;
		Set<String> roleList = new HashSet<String>();

		if (!Strings.isNullOrEmpty(tenantid)) {
			try {
				tenantRef = assignmentApi.getProject(tenantid);
				roleList = new HashSet<String>(assignmentApi.getRolesForUserAndProject(userid, tenantid));
			} catch (Exception e) {
				// pass
			}

			if (roleList.isEmpty()) {
				String msg = String.format("User %s is unauthorized for tenant %s", userid, tenantid);
				logger.warn(msg);
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


	private class AuthInfo {

		private final User currentUserRef;
		private final Project tenantRef;
		private final Metadata metadataRef;
		private final Calendar expiry;
		private final Bind bind;
		private final String auditId;


		public AuthInfo(User currentUserRef, Project tenantRef, Metadata metadataRef, Calendar expiry, Bind bind,
				String auditId) {
			this.currentUserRef = currentUserRef;
			this.tenantRef = tenantRef;
			this.metadataRef = metadataRef;
			this.expiry = expiry;
			this.bind = bind;
			this.auditId = auditId;
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

		public Calendar getExpiry() {
			return expiry;
		}

		public Bind getBind() {
			return bind;
		}

		public String getAuditId() {
			return auditId;
		}

	}


	@Override
	public String getCollectionName() {
		return null;
	}

	@Override
	public String getMemberName() {
		return null;
	}

	@Override
	public MemberWrapper<Access> getMemberWrapper() {
		return new TokenV2DataWrapper();
	}

}
