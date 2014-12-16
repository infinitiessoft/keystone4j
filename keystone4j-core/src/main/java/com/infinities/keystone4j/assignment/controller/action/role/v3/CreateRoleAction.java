package com.infinities.keystone4j.assignment.controller.action.role.v3;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CreateRoleAction extends AbstractRoleAction implements ProtectedAction<Role> {

	private final Role role;


	public CreateRoleAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi, Role role) {
		super(assignmentApi, tokenProviderApi, policyApi);
		this.role = role;
	}

	@Override
	public MemberWrapper<Role> execute(ContainerRequestContext request) {
		assignUniqueId(role);
		Role ref = assignmentApi.createRole(role.getId(), role);
		return this.wrapMember(request, ref);
	}

	@Override
	public String getName() {
		return "create_role";
	}
}
