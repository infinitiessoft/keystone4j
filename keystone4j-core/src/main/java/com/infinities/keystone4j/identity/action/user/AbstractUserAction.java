package com.infinities.keystone4j.identity.action.user;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.identity.IdentityApi;

public abstract class AbstractUserAction<T> implements Action<T> {

	protected IdentityApi identityApi;


	public AbstractUserAction(IdentityApi identityApi) {
		this.identityApi = identityApi;
	}

	public IdentityApi getIdentityApi() {
		return identityApi;
	}

	public void setIdentityApi(IdentityApi identityApi) {
		this.identityApi = identityApi;
	}

}
