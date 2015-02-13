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
