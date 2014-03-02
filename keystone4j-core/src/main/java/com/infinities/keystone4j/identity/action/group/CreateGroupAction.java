package com.infinities.keystone4j.identity.action.group;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;

public class CreateGroupAction extends AbstractGroupAction<Group> {

	private final Group group;
	private HttpServletRequest request;


	public CreateGroupAction(IdentityApi identityApi, Group group) {
		super(identityApi);
		this.group = group;
	}

	@Override
	public Group execute() {
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);
		group.setDomain(domain);
		Group ret = identityApi.createGroup(group);
		return ret;
	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public String getName() {
		return "create_group";
	}
}
