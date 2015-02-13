package com.infinities.keystone4j.assignment.controller.action.role.v3;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class DeleteRoleAction extends AbstractRoleAction implements ProtectedAction<Role> {

	private final String roleid;


	public DeleteRoleAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String roleid) {
		super(assignmentApi, tokenProviderApi, policyApi);
		this.roleid = roleid;
	}

	@Override
	public MemberWrapper<Role> execute(ContainerRequestContext context) throws Exception {
		this.getAssignmentApi().deleteRole(roleid);
		return null;
	}

	@Override
	public String getName() {
		return "delete_role";
	}
}
