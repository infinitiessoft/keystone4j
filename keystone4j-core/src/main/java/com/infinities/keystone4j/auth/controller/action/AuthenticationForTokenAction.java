package com.infinities.keystone4j.auth.controller.action;

import java.util.Calendar;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.AuthDriver;
import com.infinities.keystone4j.auth.controller.AuthController;
import com.infinities.keystone4j.auth.model.AuthInfo;
import com.infinities.keystone4j.auth.model.AuthResponse;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.auth.AuthData;
import com.infinities.keystone4j.model.auth.AuthV3;
import com.infinities.keystone4j.model.auth.TokenIdAndData;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.TokenDataWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class AuthenticationForTokenAction extends AbstractAuthAction implements ProtectedAction<TokenDataWrapper> {

	private final static Logger logger = LoggerFactory.getLogger(AuthenticationForTokenAction.class);
	// private final static String AUTH = "auth";
	// private final static String METHODS = "methods";
	private final static String EXTERNAL_DRIVER = "external";
	// private final static String SEPERATOR = ",";
	private final static String USER_NOT_FOUND = "User not found";
	// private final static String DEFAULT_PROJECT_NOT_FOUND =
	// "User {0}\'s default project {1} not found."
	// + " The token will be unscoped rather than scoped to the porject.";
	private final static String DEFAULT_PROJECT_DISABLED = "User {}\'s default project {} is disabled."
			+ " The token will be unscoped rather than scoped to the porject.";
	private final static String USER_ACCESS_NOT_ALLOW = "User {} doesn't have access to default project {}. "
			+ "The token will be unscoped rather than scoped to the project.";
	private final AuthV3 auth;
	private TrustApi trustApi;


	public AuthenticationForTokenAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, TokenApi tokenApi, TrustApi trustApi, AuthV3 auth)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		super(assignmentApi, catalogApi, identityApi, tokenProviderApi, tokenApi);
		this.auth = auth;
	}

	@Override
	public TokenIdAndData execute(ContainerRequestContext request) {
		// logger.debug("--------------------------2");
		// try {
		// loadAuthMethods();
		// } catch (Exception e) {
		// throw new RuntimeException(e);
		// }
		// logger.debug("--------------------------3");
		boolean includeCatalog = !request.getUriInfo().getQueryParameters().containsKey(AuthController.NOCATALOG);
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		// logger.debug("--------------------------4");
		try {
			AuthInfo authInfo = new AuthInfo(context, auth, this.getAssignmentApi(), this.getTrustApi());
			AuthContext authContext = new AuthContext();
			this.authenticate(context, authInfo, authContext);
			if (!Strings.isNullOrEmpty(authContext.getAccessTokenid())) {
				AuthInfo.Scope scope = new AuthInfo.Scope();
				scope.setProjectid(authContext.getProjectid());
				authInfo.setScope(scope);
			}

			checkAndSetDefaultScoping(authInfo, authContext);
			AuthInfo.Scope scope = authInfo.getScope();
			String domainid = scope.getDomainid();
			String projectid = scope.getProjectid();
			Trust trust = scope.getTrustRef();

			List<String> methodNames = authInfo.getMethodNames();
			methodNames.addAll(authContext.getMethodNames());
			// make sure the list is unique
			methodNames = Lists.newArrayList(Sets.newHashSet(methodNames));
			Calendar expiresAt = authContext.getExpiresAt();
			// logger.debug("--------------------------5");

			Token metadataRef = null;

			String tokenAuditid = authContext.getAuditid();
			TokenIdAndData entry = tokenProviderApi.issueV3Token(authContext.getUserid(), methodNames, expiresAt, projectid,
					domainid, authContext, trust, metadataRef, includeCatalog, tokenAuditid);

			if (trust != null) {
				trustApi.consumeUse(trust.getId());
			}

			return entry;
		} catch (Exception e) {
			logger.warn("authenticate token failed", e);
			throw Exceptions.UnauthorizedException.getInstance(e);
		}

	}

	private void checkAndSetDefaultScoping(AuthInfo authInfo, AuthContext authContext) throws Exception {
		AuthInfo.Scope scope = authInfo.getScope();
		String domainid = scope.getDomainid();
		String projectid = scope.getProjectid();
		Trust trust = scope.getTrustRef();

		if (trust != null) {
			projectid = trust.getProject().getId();
		}
		if (!Strings.isNullOrEmpty(domainid) || !Strings.isNullOrEmpty(projectid) || trust != null) {
			// scope is specified
			return;
		}

		// TODO federation is unimplemented
		// if(federation.IDENTITY_PROVIDER in auth_context):
		// return;

		User userRef = null;
		try {
			userRef = identityApi.getUser(authContext.getUserid(), null);
		} catch (Exception e) {
			logger.error("Get user failed", e);
			throw Exceptions.UnauthorizedException.getInstance(e);
		}

		Project defaultProject = userRef.getDefault_project();
		if (defaultProject == null || Strings.isNullOrEmpty(defaultProject.getId())) {
			// User has no default project. He shall get an unscoped token.
			return;
		}
		String defaultProjectid = defaultProject.getId();

		try {
			Project defaultProjectRef = assignmentApi.getProject(defaultProjectid);
			Domain defaultDomainRef = assignmentApi.getDomain(defaultProjectRef.getDomain().getId());
			if (defaultProject.getEnabled() && defaultDomainRef.getEnabled()) {
				List<Role> roles = this.getAssignmentApi().getRolesForUserAndProject(userRef.getId(), defaultProjectid);
				if (!roles.isEmpty()) {
					AuthInfo.Scope newScope = new AuthInfo.Scope();
					newScope.setProjectid(defaultProject.getId());
					authInfo.setScope(newScope);
				} else {
					logger.warn(USER_ACCESS_NOT_ALLOW, userRef.getId(), defaultProjectRef.getId());
				}
			} else {
				logger.warn(DEFAULT_PROJECT_DISABLED, userRef.getId(), defaultProjectid);
			}

		} catch (Exception e) {
			logger.warn("User {} default project {} not found."
					+ " The token will be unscoped rather than scoped to the project.", new Object[] { userRef.getId(),
					defaultProjectid });
		}

	}

	private void authenticate(KeystoneContext context, AuthInfo authInfo, AuthContext authContext)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		if (!Strings.isNullOrEmpty(context.getEnvironment().getRemoteUser())) {
			AuthDriver external = getAuthMethod(EXTERNAL_DRIVER);
			external.authenticate(context, authInfo, authContext, tokenProviderApi, identityApi, assignmentApi);
		}

		AuthResponse authResponse = new AuthResponse();
		for (String name : authInfo.getMethodNames()) {
			AuthDriver method = getAuthMethod(name);
			AuthData methodData = authInfo.getMethodData(name);
			try {
				method.authenticate(context, methodData, authContext, tokenProviderApi, identityApi, assignmentApi);
			} catch (Exception e) {
				authResponse.getMethods().add(name);
				authResponse.set(name, e);
			}
		}

		if (!authResponse.getMethods().isEmpty()) {
			throw Exceptions.AdditionalAuthRequiredException.getInstance(authResponse.toString());
		}

		if (Strings.isNullOrEmpty(authContext.getUserid())) {
			throw Exceptions.UnauthorizedException.getInstance(USER_NOT_FOUND);
		}
	}

	public TrustApi getTrustApi() {
		return trustApi;
	}

	public void setTrustApi(TrustApi trustApi) {
		this.trustApi = trustApi;
	}

	@Override
	public String getName() {
		return "authenticate_token";
	}

}
