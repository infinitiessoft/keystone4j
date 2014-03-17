package com.infinities.keystone4j.auth.model;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;

public class UserAuthInfo {

	private final static Logger logger = LoggerFactory.getLogger(UserAuthInfo.class);
	private String domainid;
	private String userid;
	private String password;
	private User user;
	private final IdentityApi identityApi;
	private final AssignmentApi assignmentApi;


	public UserAuthInfo(AuthData authData, IdentityApi identityApi, AssignmentApi assignmentApi) {
		this.identityApi = identityApi;
		this.assignmentApi = assignmentApi;
		validateAndNormalizeAuthData(authData);
	}

	private void validateAndNormalizeAuthData(AuthData authData) {
		if (authData.getUser() == null) {
			throw Exceptions.ValidationException.getInstance(null, "user", "password");
		}

		User userInfo = authData.getUser();
		String userid = userInfo.getId();
		String userName = userInfo.getName();

		if (Strings.isNullOrEmpty(userid) && Strings.isNullOrEmpty(userName)) {
			throw Exceptions.ValidationException.getInstance(null, "id or name", "user");
		}

		this.password = userInfo.getPassword();
		Domain domain;
		User user;

		try {
			if (!Strings.isNullOrEmpty(userName)) {
				if (userInfo.getDomain() == null) {
					throw Exceptions.ValidationException.getInstance(null, "domain", "user");
				}
				domain = lookupDomain(userInfo.getDomain());
				user = identityApi.getUserByName(userName, domain.getId());
			} else {
				user = identityApi.getUser(userid, null);
				domain = user.getDomain();
				assertDomainIsEnabled(domain);
			}
		} catch (Exception e) {
			logger.error("user not found", e);
			throw Exceptions.UnauthorizedException.getInstance();
		}

		assertUserIsEnabled(user);
		this.user = user;
		this.userid = user.getId();
		this.domainid = domain.getId();

	}

	private void assertUserIsEnabled(User user) {
		if (!user.getEnabled()) {
			String msgf = "User is disabled: {0}";
			String msg = MessageFormat.format(msgf, user.getId());
			logger.warn(msg);
			throw Exceptions.UnauthorizedException.getInstance(msg);
		}
	}

	private void assertDomainIsEnabled(Domain domain) {
		if (!domain.getEnabled()) {
			String msgf = "Domain is disabled: {0}";
			String msg = MessageFormat.format(msgf, domain.getId());
			logger.warn(msg);
			throw Exceptions.UnauthorizedException.getInstance(msg);
		}
	}

	private Domain lookupDomain(Domain domain) {
		String domainid = domain.getId();
		String domainName = domain.getName();
		Domain ret;
		if (Strings.isNullOrEmpty(domainid) && Strings.isNullOrEmpty(domainName)) {
			throw Exceptions.ValidationException.getInstance(null, "id or name", "domain");
		}

		try {
			if (!Strings.isNullOrEmpty(domainName)) {
				ret = assignmentApi.getDomainByName(domainName);
			} else {
				ret = assignmentApi.getDomain(domainid);
			}
		} catch (Exception e) {
			logger.error("domain not found", e);
			throw Exceptions.UnauthorizedException.getInstance();
		}
		assertDomainIsEnabled(ret);
		return ret;
	}

	public String getDomainid() {
		return domainid;
	}

	public void setDomainid(String domainid) {
		this.domainid = domainid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
