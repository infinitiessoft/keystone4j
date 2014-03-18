package com.infinities.keystone4j.identity.action.user;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;

public class RemoveUserFromGroupAction extends AbstractUserAction<User> {

	private final String userid;
	private final String groupid;


	public RemoveUserFromGroupAction(AssignmentApi assignmentApi, TokenApi tokenApi, IdentityApi identityApi, String userid,
			String groupid) {
		super(assignmentApi, identityApi, tokenApi);
		this.userid = userid;
		this.groupid = groupid;
	}

	@Override
	public User execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(assignmentApi, tokenApi, context);
		return this.getIdentityApi().removeUserFromGroup(userid, groupid, domain.getId());
	}

	@Override
	public String getName() {
		return "remove_user_from_group";
	}
}
