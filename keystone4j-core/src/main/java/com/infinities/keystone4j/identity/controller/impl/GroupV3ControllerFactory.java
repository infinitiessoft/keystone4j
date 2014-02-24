package com.infinities.keystone4j.identity.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.controller.GroupV3Controller;

public class GroupV3ControllerFactory implements Factory<GroupV3Controller> {

	private final IdentityApi identityApi;


	@Inject
	public GroupV3ControllerFactory(IdentityApi identityApi) {
		this.identityApi = identityApi;
	}

	@Override
	public void dispose(GroupV3Controller arg0) {

	}

	@Override
	public GroupV3Controller provide() {
		return new GroupV3ControllerImpl(identityApi);
	}

}
