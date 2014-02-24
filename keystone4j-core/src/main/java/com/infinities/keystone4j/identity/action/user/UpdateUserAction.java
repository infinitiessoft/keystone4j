package com.infinities.keystone4j.identity.action.user;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;

public class UpdateUserAction extends AbstractUserAction<User> {

	private String userid;
	private User user;
	private HttpServletRequest request;


	public UpdateUserAction(IdentityApi identityApi, String userid, User user) {
		super(identityApi);
	}

	@Override
	public User execute() {
		KeystonePreconditions.requireMatchingId(userid, user);
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);

		return this.getIdentityApi().updateUser(userid, user, domain.getId());
	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
}
