package com.infinities.keystone4j.identity.action.group;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;

public class GetGroupAction extends AbstractGroupAction<Group> {

	private final String groupid;
	private HttpServletRequest request;


	public GetGroupAction(IdentityApi identityApi, String groupid) {
		super(identityApi);
		this.groupid = groupid;
	}

	@Override
	public Group execute() {
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);
		Domain domain = new KeystoneUtils().getDomainForRequest(context);
		return this.getIdentityApi().getGroup(groupid, domain.getId());
	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public String getName() {
		return "get_group";
	}
}
