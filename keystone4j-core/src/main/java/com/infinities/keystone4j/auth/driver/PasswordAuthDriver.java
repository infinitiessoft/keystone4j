package com.infinities.keystone4j.auth.driver;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.AuthDriver;
import com.infinities.keystone4j.auth.model.AuthContext;
import com.infinities.keystone4j.auth.model.AuthInfo;
import com.infinities.keystone4j.auth.model.Identity;
import com.infinities.keystone4j.auth.model.UserAuthInfo;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class PasswordAuthDriver implements AuthDriver {

	// private final static Logger logger =
	// LoggerFactory.getLogger(PasswordAuthDriver.class);
	private final static String METHOD = "password";


	@Override
	public void authenticate(KeystoneContext context, AuthInfo authInfo, AuthContext userContext,
			TokenProviderApi tokenProviderApi, IdentityApi identityApi, AssignmentApi assignmentApi) {
		Identity authPayload = authInfo.getMethodData(METHOD);
		UserAuthInfo userInfo = new UserAuthInfo(authPayload, identityApi, assignmentApi);

		try {
			identityApi.authenticate(userInfo.getUserid(), userInfo.getPassword(), userInfo.getDomainid());
		} catch (Exception e) {
			throw Exceptions.UnauthorizedException.getInstance("Invalid username or password");
		}

		if (Strings.isNullOrEmpty(userContext.getUserid())) {
			userContext.setUserid(userInfo.getUserid());
		}
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return METHOD;
	}
}
