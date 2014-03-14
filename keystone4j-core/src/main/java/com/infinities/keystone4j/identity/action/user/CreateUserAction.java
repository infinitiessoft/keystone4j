package com.infinities.keystone4j.identity.action.user;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;

public class CreateUserAction extends AbstractUserAction<User> {

	private final User user;


	public CreateUserAction(IdentityApi identityApi, User user) {
		super(identityApi);
		this.user = user;
	}

	@Override
	public User execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);
		user.setDomain(domain);
		User ret = identityApi.createUser(user);
		return ret;
	}

	@Override
	public String getName() {
		return "create_user";
	}
}
