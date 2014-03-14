package com.infinities.keystone4j.identity.action.group;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;

public class DeleteGroupAction extends AbstractGroupAction<Group> {

	private final String groupid;


	public DeleteGroupAction(IdentityApi identityApi, String groupid) {
		super(identityApi);
		this.groupid = groupid;
	}

	@Override
	public Group execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);
		return this.getIdentityApi().deleteGroup(groupid, domain.getId());
	}

	@Override
	public String getName() {
		return "delete_group";
	}

}
