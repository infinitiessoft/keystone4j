package com.infinities.keystone4j.identity.controller.action.group;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class GetGroupAction extends AbstractGroupAction implements ProtectedAction<Group> {

	private final String groupid;


	public GetGroupAction(IdentityApi identityApi, TokenProviderApi tokenProviderApi, String groupid) {
		super(identityApi, tokenProviderApi);
		this.groupid = groupid;
	}

	@Override
	public MemberWrapper<Group> execute(ContainerRequestContext request) {
		Group ref = this.getIdentityApi().getGroup(groupid);
		return wrapMember(request, ref);
	}

	@Override
	public String getName() {
		return "get_group";
	}
}
