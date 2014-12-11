package com.infinities.keystone4j.identity.controller.action.user;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class DeleteUserAction extends AbstractUserAction implements ProtectedAction<User> {

	private final String userid;


	public DeleteUserAction(IdentityApi identityApi, TokenProviderApi tokenProviderApi, String userid) {
		super(identityApi, tokenProviderApi);
		this.userid = userid;
	}

	@Override
	public MemberWrapper<User> execute(ContainerRequestContext request) {
		this.getIdentityApi().deleteUser(userid);
		return null;
	}

	@Override
	public String getName() {
		return "delete_user";
	}

}
