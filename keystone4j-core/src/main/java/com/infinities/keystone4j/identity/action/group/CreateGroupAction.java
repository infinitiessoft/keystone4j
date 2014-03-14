package com.infinities.keystone4j.identity.action.group;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;

public class CreateGroupAction extends AbstractGroupAction<Group> {

	private final Group group;


	public CreateGroupAction(IdentityApi identityApi, Group group) {
		super(identityApi);
		this.group = group;
	}

	@Override
	public Group execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);
		group.setDomain(domain);
		Group ret = identityApi.createGroup(group);
		return ret;
	}

	@Override
	public String getName() {
		return "create_group";
	}
}
