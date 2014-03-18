package com.infinities.keystone4j.identity.action.group;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.token.TokenApi;

public class GetGroupAction extends AbstractGroupAction<Group> {

	private final String groupid;


	public GetGroupAction(AssignmentApi assignmentApi, TokenApi tokenApi, IdentityApi identityApi, String groupid) {
		super(assignmentApi, tokenApi, identityApi);
		this.groupid = groupid;
	}

	@Override
	public Group execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(assignmentApi, tokenApi, context);
		return this.getIdentityApi().getGroup(groupid, domain.getId());
	}

	@Override
	public String getName() {
		return "get_group";
	}
}
