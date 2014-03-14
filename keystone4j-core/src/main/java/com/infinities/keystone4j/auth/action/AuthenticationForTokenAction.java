package com.infinities.keystone4j.auth.action;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.auth.AuthDriver;
import com.infinities.keystone4j.auth.controller.AuthController;
import com.infinities.keystone4j.auth.model.AuthContext;
import com.infinities.keystone4j.auth.model.AuthData;
import com.infinities.keystone4j.auth.model.AuthInfo;
import com.infinities.keystone4j.auth.model.AuthV3;
import com.infinities.keystone4j.auth.model.TokenMetadata;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.model.Trust;

public class AuthenticationForTokenAction extends AbstractTokenAction<TokenMetadata> {

	private final static Logger logger = LoggerFactory.getLogger(AuthenticationForTokenAction.class);
	// private final static String AUTH = "auth";
	// private final static String METHODS = "methods";
	private final static String EXTERNAL_DRIVER = "external";
	// private final static String SEPERATOR = ",";
	private final static String USER_NOT_FOUND = "User not found";
	private final static String DEFAULT_PROJECT_NOT_FOUND = "User {0}\'s default project {1} not found."
			+ " The token will be unscoped rather than scoped to the porject.";
	private final static String DEFAULT_PROJECT_DISABLED = "User {0}\'s default project {1} is disabled."
			+ " The token will be unscoped rather than scoped to the porject.";
	private final static String USER_ACCESS_NOT_ALLOW = "User {0} doesn't have access to default project {1}. "
			+ "The token will be unscoped rather than scoped to the project.";
	private final static Map<String, AuthDriver> AUTH_METHODS = new ConcurrentHashMap<String, AuthDriver>();
	private static boolean AUTH_PLUGINS_LOADED = false;
	private final AuthV3 auth;
	private TrustApi trustApi;


	public AuthenticationForTokenAction(AssignmentApi assignmentApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, TokenApi tokenApi, TrustApi trustApi, AuthV3 auth) {
		super(assignmentApi, identityApi, tokenProviderApi, tokenApi);
		this.auth = auth;
	}

	private void loadAuthMethods() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (AUTH_PLUGINS_LOADED) {
			return;
		}

		List<String> methods = Config.Instance.getOpt(Config.Type.auth, "methods").asList();
		AuthDriver driver;
		for (String plugin : methods) {
			if (plugin.contains(".")) {
				String className = plugin;
				Class<?> c = Class.forName(className);
				driver = (AuthDriver) c.newInstance();
				if (Strings.isNullOrEmpty(driver.getMethod())) {
					throw new RuntimeException(
							"Cannot load an auth-plugin by class-name without a 'method' attribute define:" + className);
				}
			} else {
				String className = Config.Instance.getOpt(Config.Type.auth, plugin).asText();
				Class<?> c = Class.forName(className);
				driver = (AuthDriver) c.newInstance();
				if (Strings.isNullOrEmpty(driver.getMethod())) {
					throw new RuntimeException(
							"Cannot load an auth-plugin by class-name without a 'method' attribute define:" + className);
				} else {
					if (!plugin.equals(driver.getMethod())) {
						String msg = MessageFormat.format("Driver requested method {0} does not match plugin name {1}",
								driver.getMethod(), plugin);
						throw new RuntimeException(msg);
					}

				}
			}

			if (AUTH_METHODS.containsKey(driver.getMethod())) {
				String msg = MessageFormat.format("Auth plugin {0} is requesting previously registered method {1}", plugin,
						driver.getMethod());
				throw new RuntimeException(msg);
			}
			AUTH_METHODS.put(driver.getMethod(), driver);
		}
		AUTH_PLUGINS_LOADED = true;
	}

	@Override
	public TokenMetadata execute(ContainerRequestContext request) {
		try {
			loadAuthMethods();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		boolean includeCatalog = !request.getUriInfo().getQueryParameters().containsKey(AuthController.NOCATALOG);
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);

		try {
			AuthInfo authInfo = new AuthInfo(context, auth, AUTH_METHODS, this.getAssignmentApi(), this.getTrustApi());
			AuthContext authContext = new AuthContext();
			this.authenticate(context, authInfo, authContext);
			if (!Strings.isNullOrEmpty(authContext.getAccessTokenid())) {
				authInfo.setProjectid(authContext.getProjectid());
			}
			checkAndSetDefaultScoping(authInfo, authContext);
			String domainid = authInfo.getDomainid();
			String projectid = authInfo.getProjectid();
			Trust trust = authInfo.getTrust();

			List<String> methodNames = authInfo.getMethodNames();
			methodNames.addAll(authContext.getMethodNames());
			// make sure the list is unique
			methodNames = Lists.newArrayList(Sets.newHashSet(methodNames));
			Date expiresAt = authContext.getExpiresAt();

			Token token = new Token();

			String userid = authContext.getUserid();
			TokenMetadata tokenMetadata = tokenProviderApi.issueV3Token(userid, methodNames, expiresAt, projectid, domainid,
					authContext, trust, token, includeCatalog);

			return tokenMetadata;

		} catch (Exception e) {
			logger.warn("authenticate token failed", e);
			throw Exceptions.UnauthorizedException.getInstance();
		}

	}

	private void checkAndSetDefaultScoping(AuthInfo authInfo, AuthContext authContext) throws Exception {
		String domainid = authInfo.getDomainid();
		String projectid = authInfo.getProjectid();
		Trust trust = authInfo.getTrust();

		// TODO wired
		if (trust != null) {
			projectid = trust.getProject().getId();
		}

		if (!Strings.isNullOrEmpty(domainid) || !Strings.isNullOrEmpty(projectid) || trust != null) {
			return;
		}

		User user = null;
		try {
			user = identityApi.getUser(authContext.getUserid(), null);
		} catch (Exception e) {
			throw Exceptions.UnauthorizedException.getInstance(e);
		}

		Project defaultProject = user.getDefault_project();
		if (defaultProject == null || Strings.isNullOrEmpty(defaultProject.getId())) {
			return;
		}

		try {
			defaultProject = assignmentApi.getProject(defaultProject.getId());
		} catch (Exception e) {
			logger.warn("User {} default project {} not found."
					+ " The token will be unscoped rather than scoped to the project.", new Object[] { user.getId(),
					defaultProject.getId() });
		}

		Domain defaultDomain = defaultProject.getDomain();
		if (defaultDomain == null) {
			String msg = MessageFormat.format(DEFAULT_PROJECT_NOT_FOUND, user.getId(), defaultProject.getId());
			logger.warn(msg);
			return;
		}

		if (defaultProject.getEnabled() && defaultDomain.getEnabled()) {
			List<Role> roles = this.getAssignmentApi().getRolesForUserAndProject(user.getId(), defaultProject.getId());
			if (!roles.isEmpty()) {
				authInfo.setProjectid(defaultProject.getId());
			} else {
				String msg = MessageFormat.format(USER_ACCESS_NOT_ALLOW, user.getId(), defaultProject.getId());
				logger.warn(msg);
			}
		} else {
			String msg = MessageFormat.format(DEFAULT_PROJECT_DISABLED, user.getId(), defaultProject.getId());
			logger.warn(msg);
		}

	}

	private void authenticate(KeystoneContext context, AuthInfo authInfo, AuthContext authContext)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		if (!Strings.isNullOrEmpty(context.getEnvironment().getRemoteUser())) {
			AuthDriver external = getAuthMethod(EXTERNAL_DRIVER);
			external.authenticate(context, authInfo, authContext, tokenProviderApi, identityApi, assignmentApi);
		}

		// AuthResponse authResponse = new AuthResponse();
		// List<String> authResponse = Lists.newArrayList();
		for (String name : authInfo.getMethodNames()) {
			AuthDriver driver = getAuthMethod(name);
			AuthData methodData = authInfo.getMethodData(name);
			driver.authenticate(context, methodData, authContext, tokenProviderApi, identityApi, assignmentApi);
			// TODO trace if resp:, wired
			// authResponse.getMethods().add(name);
			// authResponse.
			// authResponse.add(name);

		}

		if (Strings.isNullOrEmpty(authContext.getUserid())) {
			throw Exceptions.UnauthorizedException.getInstance(USER_NOT_FOUND);
		}
	}

	private AuthDriver getAuthMethod(String name) {
		if (!AUTH_METHODS.containsKey(name)) {
			throw Exceptions.AuthMethodNotSupportedException.getInstance();
			// AuthDriver driver = loadAuthMethod(name);
			// AUTH_METHODS.put(name, driver);
		}
		return AUTH_METHODS.get(name);
	}

	// private AuthDriver loadAuthMethod(String name) throws
	// ClassNotFoundException, InstantiationException,
	// IllegalAccessException {
	// String config = Config.getConfig().get(AUTH, METHODS);
	// ArrayList<String> methods = Lists.newArrayList(config.split(SEPERATOR));
	// if (!methods.contains(name)) {
	// throw new AuthMethodNotSupportedException();
	// }
	// String className = Config.getConfig().get(AUTH, name);
	// Class<?> c = Class.forName(className);
	// AuthDriver driver = (AuthDriver) c.newInstance();
	// return driver;
	// }

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
