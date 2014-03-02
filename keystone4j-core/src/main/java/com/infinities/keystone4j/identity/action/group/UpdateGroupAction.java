package com.infinities.keystone4j.identity.action.group;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;

public class UpdateGroupAction extends AbstractGroupAction<Group> {

	private String groupid;
	private Group group;
	private HttpServletRequest request;


	public UpdateGroupAction(IdentityApi identityApi, String groupid, Group group) {
		super(identityApi);
	}

	@Override
	public Group execute() {
		KeystonePreconditions.requireMatchingId(groupid, group);
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);

		return this.getIdentityApi().updateGroup(groupid, group, domain.getId());
	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public String getName() {
		return "update_group";
	}
}
