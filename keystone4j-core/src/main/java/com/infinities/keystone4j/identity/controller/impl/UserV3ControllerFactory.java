package com.infinities.keystone4j.identity.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.controller.UserV3Controller;

public class UserV3ControllerFactory implements Factory<UserV3Controller> {

	private final IdentityApi identityApi;


	@Inject
	public UserV3ControllerFactory(IdentityApi identityApi) {
		this.identityApi = identityApi;
	}

	@Override
	public void dispose(UserV3Controller arg0) {

	}

	@Override
	public UserV3Controller provide() {
		return new UserV3ControllerImpl(identityApi);
	}

}
