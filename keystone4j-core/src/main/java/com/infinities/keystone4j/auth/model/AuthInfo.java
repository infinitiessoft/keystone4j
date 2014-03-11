package com.infinities.keystone4j.auth.model;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ForbiddenException;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.auth.AuthDriver;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.model.Trust;

public class AuthInfo {

	private final static String IDENTITY = "identity";
	private KeystoneContext context;
	private AuthV3 auth;
	private String domainid;
	private String projectid;
	private Trust trust;
	private final Map<String, AuthDriver> AUTH_METHODS;
	private final TrustApi trustApi;
	private final AssignmentApi assignmentApi;


	public AuthInfo(KeystoneContext context, AuthV3 auth, Map<String, AuthDriver> authMethods, AssignmentApi assignmentApi,
			TrustApi trustApi) {
		this.setContext(context);
		this.auth = auth;
		this.AUTH_METHODS = authMethods;
		this.trustApi = trustApi;
		this.assignmentApi = assignmentApi;
		validateAndNormalizeAuthData();
	}

	public AuthV3 getAuth() {
		return auth;
	}

	public void setAuth(AuthV3 auth) {
		this.auth = auth;
	}

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

	public Trust getTrust() {
		return trust;
	}

	public void setTrust(Trust trust) {
		this.trust = trust;
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

	public Identity getMethodData(String name) {
		if (!getMethodNames().contains(name)) {
			throw Exceptions.ValidationException.getInstance(null, name, IDENTITY);
		}
		return auth.getIdentity();
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
			this.setProjectid(project.getId());
		} else if (auth.getScope().getDomain() != null) {
			Domain domain = lookupDomain(auth.getScope().getDomain());
			this.setDomainid(domain.getId());
		} else if (auth.getScope().getTrust() != null) {
			boolean enabled = Config.Instance.getOpt(Config.Type.trust, "enabled").asBoolean();
			if (!enabled) {
				throw new ForbiddenException("Trusts are disabled.");
			}
			Trust trust = lookupTrust(auth.getScope().getTrust());
			if (trust.getProject() != null) {
				Project project = lookupProject(trust.getProject());
				this.setProjectid(project.getId());
				this.setTrust(trust);
			} else {
				this.setTrust(trust);
			}
		}
	}

	private Trust lookupTrust(Trust trust) {
		String trustid = trust.getId();
		if (Strings.isNullOrEmpty(trustid)) {
			throw Exceptions.ValidationException.getInstance(null, "trust_id", "trust");
		}
		Trust ret = trustApi.getTrust(trustid);
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
			throw Exceptions.UnauthorizedException.getInstance();
		}
		assertDomainIsEnabled(domain);

		return ret;
	}

	private void assertDomainIsEnabled(Domain domain) {
		if (!domain.getEnabled()) {
			String msg = MessageFormat.format("Domain is disabled: {}", domain.getId());
			throw Exceptions.UnauthorizedException.getInstance(msg);
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
			}

		} catch (Exception e) {
			throw Exceptions.UnauthorizedException.getInstance();
		}
		assertProjectIsEnabled(project);

		return ret;
	}

	private void assertProjectIsEnabled(Project project) {
		if (!project.getEnabled()) {
			String msg = MessageFormat.format("Project is disabled: {}", project.getId());
			throw Exceptions.UnauthorizedException.getInstance(msg);
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
}
