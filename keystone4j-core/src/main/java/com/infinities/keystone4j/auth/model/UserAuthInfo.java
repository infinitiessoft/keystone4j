package com.infinities.keystone4j.auth.model;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.auth.AuthData;
import com.infinities.keystone4j.model.identity.User;

public class UserAuthInfo {

	private final static Logger logger = LoggerFactory.getLogger(UserAuthInfo.class);
	private String domainid;
	private String userid;
	private String password;
	private User userRef;
	private final IdentityApi identityApi;
	private final AssignmentApi assignmentApi;


	public UserAuthInfo(AuthData authPayload, IdentityApi identityApi, AssignmentApi assignmentApi) {
		this.identityApi = identityApi;
		this.assignmentApi = assignmentApi;
		validateAndNormalizeAuthData(authPayload);
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
		Domain domainRef = null;
		User userRef = null;

		try {
			if (!Strings.isNullOrEmpty(userName)) {
				if (userInfo.getDomain() == null) {
					throw Exceptions.ValidationException.getInstance(null, "domain", "user");
				}
				domainRef = lookupDomain(userInfo.getDomain());
				userRef = identityApi.getUserByName(userName, domainRef.getId());
			} else {
				userRef = identityApi.getUser(userid);
				domainRef = assignmentApi.getDomain(userRef.getDomainId());
				assertDomainIsEnabled(domainRef);
			}
		} catch (Exception e) {
			logger.error("user not found", e);
			throw Exceptions.UnauthorizedException.getInstance();
		}

		assertUserIsEnabled(userRef);
		this.userRef = userRef;
		this.userid = userRef.getId();
		this.domainid = domainRef.getId();

	}

	private void assertUserIsEnabled(User user) {
		try {
			identityApi.assertUserEnabled(user.getId(), user);
		} catch (Exception e) {
			logger.warn("user is disabled", e);
			String msgf = "User is disabled: {0}";
			String msg = MessageFormat.format(msgf, user.getId());
			logger.warn(msg);
			throw Exceptions.UnauthorizedException.getInstance(msg);
		}
	}

	private void assertDomainIsEnabled(Domain domainRef) {
		try {
			assignmentApi.assertDomainEnabled(domainRef.getId(), domainRef);
		} catch (Exception e) {
			String msgf = "Domain is disabled: {0}";
			String msg = MessageFormat.format(msgf, domainRef.getId());
			logger.warn(msg, e);
			throw Exceptions.UnauthorizedException.getInstance(msg);
		}
	}

	private Domain lookupDomain(Domain domain) {
		String domainid = domain.getId();
		String domainName = domain.getName();
		Domain domainRef;
		if (Strings.isNullOrEmpty(domainid) && Strings.isNullOrEmpty(domainName)) {
			throw Exceptions.ValidationException.getInstance(null, "id or name", "domain");
		}

		try {
			if (!Strings.isNullOrEmpty(domainName)) {
				domainRef = assignmentApi.getDomainByName(domainName);
			} else {
				domainRef = assignmentApi.getDomain(domainid);
			}
		} catch (Exception e) {
			logger.error("domain not found", e);
			throw Exceptions.UnauthorizedException.getInstance();
		}
		assertDomainIsEnabled(domainRef);
		return domainRef;
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
		return userRef;
	}

	public void setUser(User user) {
		this.userRef = user;
	}

}
