package com.infinities.keystone4j.identity.action.user;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;

public class UpdateUserAction extends AbstractUserAction<User> {

	private String userid;
	private User user;


	public UpdateUserAction(IdentityApi identityApi, String userid, User user) {
		super(identityApi);
	}

	@Override
	public User execute(ContainerRequestContext request) {
		KeystonePreconditions.requireMatchingId(userid, user);
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);

		return this.getIdentityApi().updateUser(userid, user, domain.getId());
	}

	@Override
	public String getName() {
		return "update_user";
	}
}
