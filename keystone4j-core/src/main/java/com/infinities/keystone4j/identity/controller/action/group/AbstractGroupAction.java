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
package com.infinities.keystone4j.identity.controller.action.group;

import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.wrapper.GroupWrapper;
import com.infinities.keystone4j.model.identity.wrapper.GroupsWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public abstract class AbstractGroupAction extends AbstractAction<Group> {

	protected IdentityApi identityApi;


	public AbstractGroupAction(IdentityApi identityApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
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
	public CollectionWrapper<Group> getCollectionWrapper() {
		return new GroupsWrapper();
	}

	@Override
	public MemberWrapper<Group> getMemberWrapper() {
		return new GroupWrapper();
	}

	@Override
	public String getCollectionName() {
		return "groups";
	}

	@Override
	public String getMemberName() {
		return "group";
	}

}
