package com.infinities.keystone4j.auth.model;

import java.util.List;

import javax.ws.rs.ForbiddenException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.controller.action.AbstractControllerAction;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.auth.AuthData;
import com.infinities.keystone4j.model.auth.AuthV3;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.trust.TrustApi;

public class AuthInfo extends AbstractControllerAction {

	private final static Logger logger = LoggerFactory.getLogger(AuthInfo.class);
	private final static String IDENTITY = "identity";
	private KeystoneContext context;
	private AuthV3 auth;
	private final TrustApi trustApi;
	private final AssignmentApi assignmentApi;
	private Scope scope;


	public AuthInfo(KeystoneContext context, AuthV3 auth, AssignmentApi assignmentApi, TrustApi trustApi)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		super();
		this.setContext(context);
		this.auth = auth;
		this.trustApi = trustApi;
		this.assignmentApi = assignmentApi;
		this.scope = new Scope();
		validateAndNormalizeAuthData();
	}

	public AuthV3 getAuth() {
		return auth;
	}

	public void setAuth(AuthV3 auth) {
		this.auth = auth;
	}

	public KeystoneContext getContext() {
		return context;
	}

	public void setContext(KeystoneContext context) {
		this.context = context;
	}

	public List<String> getMethodNames() {
		return auth.getIdentity().getMethods();
	}

	public AuthData getMethodData(String name) {
		if (!getMethodNames().contains(name)) {
			throw Exceptions.ValidationException.getInstance(null, name, IDENTITY);
		}

		return auth.getIdentity().getAuthMethods().get(name);
	}

	private void validateAndNormalizeAuthData() {
		if (auth == null) {
			throw Exceptions.ValidationException.getInstance(null, "auth", "request body");
		}

		validateAuthMethods();
		validateAndNormalizeScopeData();
	}

	private void validateAndNormalizeScopeData() {
		if (auth.getScope() == null) {
			return;
		}

		int sum = 0;
		if (auth.getScope().getProject() != null) {
			sum++;
		}
		if (auth.getScope().getDomain() != null) {
			sum++;
		}
		if (auth.getScope().getTrust() != null) {
			sum++;
		}

		if (sum != 1) {
			throw Exceptions.ValidationException.getInstance(null, "project, domain, or OS-TRUST:trust", "scope");
		}

		if (auth.getScope().getProject() != null) {
			Project project = lookupProject(auth.getScope().getProject());
			Scope scopeData = new Scope();
			scopeData.setProjectid(project.getId());
			this.setScope(scopeData);
		} else if (auth.getScope().getDomain() != null) {
			Domain domain = lookupDomain(auth.getScope().getDomain());
			Scope scopeData = new Scope();
			scopeData.setDomainid(domain.getId());
			this.setScope(scopeData);
		} else if (auth.getScope().getTrust() != null) {
			boolean enabled = Config.Instance.getOpt(Config.Type.trust, "enabled").asBoolean();
			if (!enabled) {
				throw new ForbiddenException("Trusts are disabled.");
			}
			Trust trust = lookupTrust(auth.getScope().getTrust());
			if (!Strings.isNullOrEmpty(trust.getProjectId())) {
				Project project = new Project();
				project.setId(trust.getProjectId());
				project = lookupProject(project);
				Scope scopeData = new Scope();
				scopeData.setProjectid(project.getId());
				scopeData.setTrustRef(trust);
				this.setScope(scopeData);
			} else {
				Scope scopeData = new Scope();
				scopeData.setTrustRef(trust);
				this.setScope(scopeData);
			}
		}
	}

	private Trust lookupTrust(Trust trust) {
		String trustid = trust.getId();
		if (Strings.isNullOrEmpty(trustid)) {
			throw Exceptions.ValidationException.getInstance(null, "trust_id", "trust");
		}
		Trust ret = trustApi.getTrust(trustid, false);
		if (ret == null) {
			throw Exceptions.TrustNotFoundException.getInstance(null, trustid);
		}

		return ret;
	}

	private Domain lookupDomain(Domain domain) {
		String domainid = domain.getId();
		String domainName = domain.getName();
		if (Strings.isNullOrEmpty(domainid) && Strings.isNullOrEmpty(domainName)) {
			throw Exceptions.ValidationException.getInstance(null, "id or name", "domain");
		}

		Domain ret;
		try {
			if (!Strings.isNullOrEmpty(domainName)) {
				ret = assignmentApi.getDomainByName(domainName);
			} else {
				ret = assignmentApi.getDomain(domainid);
			}
		} catch (Exception e) {
			logger.error("Lookup domain failed", e);
			throw Exceptions.UnauthorizedException.getInstance();
		}
		assertDomainIsEnabled(domain);

		return ret;
	}

	private void assertDomainIsEnabled(Domain domain) {
		try {
			this.assignmentApi.assertDomainEnabled(domain.getId(), domain);
		} catch (Exception e) {
			throw Exceptions.UnauthorizedException.getInstance(e);
		}
	}

	private Project lookupProject(Project project) {
		String projectid = project.getId();
		String projectName = project.getName();

		if (Strings.isNullOrEmpty(projectName) && Strings.isNullOrEmpty(projectid)) {
			throw Exceptions.ValidationException.getInstance(null, "id or name", "project");
		}
		Project ret;

		try {
			if (!Strings.isNullOrEmpty(projectName)) {
				if (project.getDomain() == null) {
					throw Exceptions.ValidationException.getInstance(null, "domain", "project");
				}
				Domain domain = lookupDomain(project.getDomain());
				ret = assignmentApi.getProjectByName(projectName, domain.getId());
			} else {
				ret = assignmentApi.getProject(projectid);
				lookupDomain(ret.getDomain());
			}

		} catch (Exception e) {
			logger.error("Lookup project failed", e);
			throw Exceptions.UnauthorizedException.getInstance(e);
		}
		assertProjectIsEnabled(project);
		return ret;
	}

	private void assertProjectIsEnabled(Project project) {
		try {
			this.assignmentApi.assertProjectEnabled(project.getId(), project);
		} catch (Exception e) {
			throw Exceptions.UnauthorizedException.getInstance(e);
		}
	}

	private void validateAuthMethods() {
		if (auth.getIdentity() == null) {
			throw Exceptions.ValidationException.getInstance(null, "identity", "auth");
		}

		if (auth.getIdentity().getMethods().isEmpty()) {
			throw Exceptions.ValidationException.getInstance(null, "methods", "identity");
		}

		for (String methodName : getMethodNames()) {
			if (!auth.getIdentity().getMethods().contains(methodName)) {
				throw Exceptions.ValidationException.getInstance(null, methodName, "identity");
			}
		}

		for (String methodName : getMethodNames()) {
			if (!AUTH_METHODS.containsKey(methodName)) {
				throw Exceptions.AuthMethodNotSupportedException.getInstance();
			}
		}
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}


	public static class Scope {

		private String domainid;
		private String projectid;
		private Trust trustRef;


		public String getDomainid() {
			return domainid;
		}

		public void setDomainid(String domainid) {
			this.domainid = domainid;
		}

		public String getProjectid() {
			return projectid;
		}

		public void setProjectid(String projectid) {
			this.projectid = projectid;
		}

		public Trust getTrustRef() {
			return trustRef;
		}

		public void setTrustRef(Trust trustRef) {
			this.trustRef = trustRef;
		}

	}
}
