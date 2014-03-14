package com.infinities.keystone4j.identity.action.user;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;

public class DeleteUserAction extends AbstractUserAction<User> {

	private final String userid;


	public DeleteUserAction(IdentityApi identityApi, String userid) {
		super(identityApi);
		this.userid = userid;
	}

	@Override
	public User execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);
		return this.getIdentityApi().deleteUser(userid, domain.getId());
	}

	@Override
	public String getName() {
		return "delete_user";
	}

}
