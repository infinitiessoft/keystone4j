package com.infinities.keystone4j.identity.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.controller.GroupV3Controller;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public class GroupV3ControllerFactory implements Factory<GroupV3Controller> {

	private final IdentityApi identityApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;


	@Inject
	public GroupV3ControllerFactory(IdentityApi identityApi, TokenApi tokenApi, PolicyApi policyApi) {
		this.identityApi = identityApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(GroupV3Controller arg0) {

	}

	@Override
	public GroupV3Controller provide() {
		return new GroupV3ControllerImpl(identityApi, tokenApi, policyApi);
	}

}
