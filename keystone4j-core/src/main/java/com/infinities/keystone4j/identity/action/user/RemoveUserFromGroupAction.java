package com.infinities.keystone4j.identity.action.user;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;

public class RemoveUserFromGroupAction extends AbstractUserAction<User> {

	private final String userid;
	private final String groupid;
	private HttpServletRequest request;


	public RemoveUserFromGroupAction(IdentityApi identityApi, String userid, String groupid) {
		super(identityApi);
		this.userid = userid;
		this.groupid = groupid;
	}

	@Override
	public User execute() {
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);
		return this.getIdentityApi().removeUserFromGroup(userid, groupid, domain.getId());
	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public String getName() {
		return "remove_user_from_group";
	}
}
