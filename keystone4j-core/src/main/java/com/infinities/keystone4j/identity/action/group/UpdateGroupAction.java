package com.infinities.keystone4j.identity.action.group;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;

public class UpdateGroupAction extends AbstractGroupAction<Group> {

	private String groupid;
	private Group group;


	public UpdateGroupAction(IdentityApi identityApi, String groupid, Group group) {
		super(identityApi);
	}

	@Override
	public Group execute(ContainerRequestContext request) {
		KeystonePreconditions.requireMatchingId(groupid, group);
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);

		return this.getIdentityApi().updateGroup(groupid, group, domain.getId());
	}

	@Override
	public String getName() {
		return "update_group";
	}
}
