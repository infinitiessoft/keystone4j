package com.infinities.keystone4j.identity.controller.action.user;

import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.UserWrapper;
import com.infinities.keystone4j.model.identity.UsersWrapper;
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
	protected CollectionWrapper<User> getCollectionWrapper() {
		return new UsersWrapper();
	}

	@Override
	protected MemberWrapper<User> getMemberWrapper() {
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
