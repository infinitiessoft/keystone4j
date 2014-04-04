package com.infinities.keystone4j.identity.action.user;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.utils.KeystoneUtils;

public class DeleteUserAction extends AbstractUserAction<User> {

	private final String userid;


	public DeleteUserAction(AssignmentApi assignmentApi, TokenApi tokenApi, IdentityApi identityApi, String userid) {
		super(assignmentApi, identityApi, tokenApi);
		this.userid = userid;
	}

	@Override
	public User execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(assignmentApi, tokenApi, context);
		return this.getIdentityApi().deleteUser(userid, domain.getId());
	}

	@Override
	public String getName() {
		return "delete_user";
	}

}
