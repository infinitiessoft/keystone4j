package com.infinities.keystone4j.identity.controller.action.user;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class UpdateUserAction extends AbstractUserAction implements ProtectedAction<User> {

	private final String userid;
	private final User user;


	public UpdateUserAction(IdentityApi identityApi, TokenProviderApi tokenProviderApi, String userid, User user) {
		super(identityApi, tokenProviderApi);
		this.user = user;
		this.userid = userid;
	}

	@Override
	public MemberWrapper<User> execute(ContainerRequestContext request) {
		requireMatchingId(userid, user);
		User existedRef = identityApi.getUser(userid);
		requireMatchingDomainId(user, existedRef);
		User ref = this.getIdentityApi().updateUser(userid, user);
		return wrapMember(request, ref);
	}

	@Override
	public String getName() {
		return "update_user";
	}
}
