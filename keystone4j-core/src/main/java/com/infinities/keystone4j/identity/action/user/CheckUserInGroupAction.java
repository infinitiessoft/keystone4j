package com.infinities.keystone4j.identity.action.user;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;

public class CheckUserInGroupAction extends AbstractUserAction<User> {

	private final String userid;
	private final String groupid;


	public CheckUserInGroupAction(IdentityApi identityApi, String userid, String groupid) {
		super(identityApi);
		this.userid = userid;
		this.groupid = groupid;
	}

	@Override
	public User execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);
		return this.getIdentityApi().checkUserInGroup(userid, groupid, domain.getId());
	}

	@Override
	public String getName() {
		return "check_user_in_group";
	}

}
