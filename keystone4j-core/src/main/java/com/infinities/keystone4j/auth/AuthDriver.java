package com.infinities.keystone4j.auth;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.controller.action.AbstractAuthAction;
import com.infinities.keystone4j.auth.model.AuthInfo;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.auth.AuthData;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public interface AuthDriver {

	void authenticate(KeystoneContext context, AuthData methodData, AbstractAuthAction.AuthContext authContext,
			TokenProviderApi tokenProviderApi, IdentityApi identityApi, AssignmentApi assignmentApi);

	void authenticate(KeystoneContext context, AuthInfo authInfo, AbstractAuthAction.AuthContext authContext,
			TokenProviderApi tokenProviderApi, IdentityApi identityApi, AssignmentApi assignmentApi);

	String getMethod();

}
