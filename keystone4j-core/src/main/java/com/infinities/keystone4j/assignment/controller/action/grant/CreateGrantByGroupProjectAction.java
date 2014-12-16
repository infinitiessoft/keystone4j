package com.infinities.keystone4j.assignment.controller.action.grant;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CreateGrantByGroupProjectAction extends AbstractGrantAction implements ProtectedAction<Role> {

	private final String roleid;
	private final String groupid;
	private final String projectid;


	public CreateGrantByGroupProjectAction(AssignmentApi assignmentApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi, String roleid, String groupid, String projectid) {
		super(assignmentApi, identityApi, tokenProviderApi, policyApi);
		this.roleid = roleid;
		this.groupid = groupid;
		this.projectid = projectid;
	}

	@Override
	public MemberWrapper<Role> execute(ContainerRequestContext request) {
		assignmentApi.createGrantByGroupProject(roleid, groupid, projectid);
		return null;
	}

	@Override
	public String getName() {
		return "create_grant";
	}
}
