package com.infinities.keystone4j.identity.action.user;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;

public class CreateUserAction extends AbstractUserAction<User> {

	private final User user;
	private HttpServletRequest request;


	public CreateUserAction(IdentityApi identityApi, User user) {
		super(identityApi);
		this.user = user;
	}

	@Override
	public User execute() {
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);
		user.setDomain(domain);
		User ret = identityApi.createUser(user);
		return ret;
	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public String getName() {
		return "create_user";
	}
}
