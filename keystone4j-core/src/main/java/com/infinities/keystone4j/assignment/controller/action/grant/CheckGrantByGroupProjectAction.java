package com.infinities.keystone4j.assignment.controller.action.grant;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CheckGrantByGroupProjectAction extends AbstractGrantAction implements ProtectedAction<Role> {

	private final String roleid;
	private final String groupid;
	private final String projectid;


	public CheckGrantByGroupProjectAction(AssignmentApi assignmentApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi, String roleid, String groupid, String projectid) {
		super(assignmentApi, identityApi, tokenProviderApi, policyApi);
		this.roleid = roleid;
		this.groupid = groupid;
		this.projectid = projectid;
	}

	@Override
	public MemberWrapper<Role> execute(ContainerRequestContext request) {
		assignmentApi.getGrantByGroupProject(roleid, groupid, projectid, checkIfInherited(request));
		return null;
	}

	@Override
	public String getName() {
		return "check_grant";
	}
}
