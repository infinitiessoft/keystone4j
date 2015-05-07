/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
package com.infinities.keystone4j.identity.controller.action.user;

import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.wrapper.UserWrapper;
import com.infinities.keystone4j.model.identity.wrapper.UsersWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public abstract class AbstractUserAction extends AbstractAction<User> {

	protected IdentityApi identityApi;


	public AbstractUserAction(IdentityApi identityApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		super(tokenProviderApi, policyApi);
		this.identityApi = identityApi;
	}

	public IdentityApi getIdentityApi() {
		return identityApi;
	}

	public void setIdentityApi(IdentityApi identityApi) {
		this.identityApi = identityApi;
	}

	@Override
	public CollectionWrapper<User> getCollectionWrapper() {
		return new UsersWrapper();
	}

	@Override
	public MemberWrapper<User> getMemberWrapper() {
		return new UserWrapper();
	}

	@Override
	public String getCollectionName() {
		return "users";
	}

	@Override
	public String getMemberName() {
		return "user";
	}
}
