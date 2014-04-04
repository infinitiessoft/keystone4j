package com.infinities.keystone4j.identity.action.group;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.utils.KeystoneUtils;

public class CreateGroupAction extends AbstractGroupAction<Group> {

	private final Group group;


	public CreateGroupAction(AssignmentApi assignmentApi, TokenApi tokenApi, IdentityApi identityApi, Group group) {
		super(assignmentApi, tokenApi, identityApi);
		this.group = group;
	}

	@Override
	public Group execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		normalizeDomainid(context, group);
		Group ret = identityApi.createGroup(group);
		return ret;
	}

	private void normalizeDomainid(KeystoneContext context, Group group) {
		if (Strings.isNullOrEmpty(group.getDomainid())) {
			Domain domain = new KeystoneUtils().getDomainForRequest(assignmentApi, tokenApi, context);
			group.setDomain(domain);
		}
	}

	@Override
	public String getName() {
		return "create_group";
	}
}
