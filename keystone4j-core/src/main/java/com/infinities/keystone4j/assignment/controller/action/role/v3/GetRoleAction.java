package com.infinities.keystone4j.assignment.controller.action.role.v3;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class GetRoleAction extends AbstractRoleAction implements ProtectedAction<Role> {

	private final String roleid;


	public GetRoleAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi, String roleid) {
		super(assignmentApi, tokenProviderApi, policyApi);
		this.roleid = roleid;
	}

	@Override
	public MemberWrapper<Role> execute(ContainerRequestContext context) {
		Role ref = this.getAssignmentApi().getRole(roleid);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "get_role";
	}
}
