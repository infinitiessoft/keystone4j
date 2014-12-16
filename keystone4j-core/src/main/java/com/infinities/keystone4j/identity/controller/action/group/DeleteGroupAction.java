package com.infinities.keystone4j.identity.controller.action.group;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class DeleteGroupAction extends AbstractGroupAction implements ProtectedAction<Group> {

	private final String groupid;


	public DeleteGroupAction(IdentityApi identityApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi, String groupid) {
		super(identityApi, tokenProviderApi, policyApi);
		this.groupid = groupid;
	}

	@Override
	public MemberWrapper<Group> execute(ContainerRequestContext request) {
		this.getIdentityApi().deleteGroup(groupid);
		return null;
	}

	@Override
	public String getName() {
		return "delete_group";
	}

}
