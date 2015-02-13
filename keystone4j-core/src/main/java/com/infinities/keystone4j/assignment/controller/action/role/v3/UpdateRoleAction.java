package com.infinities.keystone4j.assignment.controller.action.role.v3;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class UpdateRoleAction extends AbstractRoleAction implements ProtectedAction<Role> {

	private final String roleid;
	private final Role role;


	public UpdateRoleAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String roleid, Role role) {
		super(assignmentApi, tokenProviderApi, policyApi);
		this.role = role;
		this.roleid = roleid;
	}

	@Override
	public MemberWrapper<Role> execute(ContainerRequestContext context) throws Exception {
		requireMatchingId(roleid, role);
		Role ref = this.getAssignmentApi().updateRole(roleid, role);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "update_role";
	}
}
