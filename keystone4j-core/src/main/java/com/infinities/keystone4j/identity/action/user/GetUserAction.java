package com.infinities.keystone4j.identity.action.user;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;

public class GetUserAction extends AbstractUserAction<User> {

	private final String userid;
	private HttpServletRequest request;


	public GetUserAction(IdentityApi identityApi, String userid) {
		super(identityApi);
		this.userid = userid;
	}

	@Override
	public User execute() {
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);
		return this.getIdentityApi().getUser(userid, domain.getId());
	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public String getName() {
		return "get_user";
	}
}
