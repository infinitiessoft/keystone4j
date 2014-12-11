package com.infinities.keystone4j.identity.controller.action.user;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class AddUserToGroupAction extends AbstractUserAction implements ProtectedAction<User> {

	private final String userid;
	private final String groupid;


	public AddUserToGroupAction(IdentityApi identityApi, TokenProviderApi tokenProviderApi, String userid, String groupid) {
		super(identityApi, tokenProviderApi);
		this.userid = userid;
		this.groupid = groupid;
	}

	@Override
	public MemberWrapper<User> execute(ContainerRequestContext request) {
		this.getIdentityApi().addUserToGroup(userid, groupid);
		return null;
	}

	@Override
	public String getName() {
		return "add_user_to_group";
	}

}
