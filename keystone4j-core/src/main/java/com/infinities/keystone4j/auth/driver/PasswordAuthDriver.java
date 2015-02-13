package com.infinities.keystone4j.auth.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.AuthDriver;
import com.infinities.keystone4j.auth.model.AuthInfo;
import com.infinities.keystone4j.auth.model.UserAuthInfo;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.auth.AuthData;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class PasswordAuthDriver implements AuthDriver {

	private final static Logger logger = LoggerFactory.getLogger(PasswordAuthDriver.class);
	private final static String METHOD = "password";


	@Override
	public void authenticate(KeystoneContext context, AuthData authPayload,
			com.infinities.keystone4j.auth.controller.action.AbstractAuthAction.AuthContext authContext,
			TokenProviderApi tokenProviderApi, IdentityApi identityApi, AssignmentApi assignmentApi) {
		UserAuthInfo userInfo = new UserAuthInfo(authPayload, identityApi, assignmentApi);

		try {
			identityApi.authenticate(userInfo.getUserid(), userInfo.getPassword());
		} catch (Exception e) {
			logger.warn("authenticate fail", e);
			throw Exceptions.UnauthorizedException.getInstance("Invalid username or password");
		}
		authContext.setUserid(userInfo.getUserid());

	}

	@Override
	public void authenticate(KeystoneContext context, AuthInfo authInfo,
			com.infinities.keystone4j.auth.controller.action.AbstractAuthAction.AuthContext authContext,
			TokenProviderApi tokenProviderApi, IdentityApi identityApi, AssignmentApi assignmentApi) {
		throw Exceptions.NotImplementedException.getInstance();
	}

	@Override
	public String getMethod() {
		return METHOD;
	}
}
