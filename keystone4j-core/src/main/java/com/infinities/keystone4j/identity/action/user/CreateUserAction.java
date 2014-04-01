package com.infinities.keystone4j.identity.action.user;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.utils.KeystoneUtils;

public class CreateUserAction extends AbstractUserAction<User> {

	private final User user;


	public CreateUserAction(AssignmentApi assignmentApi, TokenApi tokenApi, IdentityApi identityApi, User user) {
		super(assignmentApi, identityApi, tokenApi);
		this.user = user;
	}

	@Override
	public User execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		normalizeDomainid(context, user);
		User ret = identityApi.createUser(user);
		return ret;
	}

	private void normalizeDomainid(KeystoneContext context, User user) {
		if (Strings.isNullOrEmpty(user.getDomainid())) {
			Domain domain = new KeystoneUtils().getDomainForRequest(assignmentApi, tokenApi, context);
			user.setDomain(domain);
		}
	}

	@Override
	public String getName() {
		return "create_user";
	}
}
