package com.infinities.keystone4j.identity.controller.action.user;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class GetUserAction extends AbstractUserAction implements ProtectedAction<User> {

	private final String userid;


	public GetUserAction(IdentityApi identityApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi, String userid) {
		super(identityApi, tokenProviderApi, policyApi);
		this.userid = userid;
	}

	@Override
	public MemberWrapper<User> execute(ContainerRequestContext request) throws Exception {
		User ref = this.getIdentityApi().getUser(userid);
		return wrapMember(request, ref);
	}

	@Override
	public String getName() {
		return "get_user";
	}
}
