package com.infinities.keystone4j.assignment.controller.action.grant;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class DeleteGrantByUserProjectAction extends AbstractGrantAction implements ProtectedAction<Role> {

	private final String roleid;
	private final String userid;
	private final String projectid;


	public DeleteGrantByUserProjectAction(AssignmentApi assignmentApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, String roleid, String userid, String projectid) {
		super(assignmentApi, identityApi, tokenProviderApi);
		this.roleid = roleid;
		this.userid = userid;
		this.projectid = projectid;
	}

	@Override
	public MemberWrapper<Role> execute(ContainerRequestContext request) {
		assignmentApi.deleteGrantByUserProject(roleid, userid, projectid, checkIfInherited(request));
		return null;
	}

	@Override
	public String getName() {
		return "revoke_grant";
	}
}
