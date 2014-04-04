package com.infinities.keystone4j.assignment.action.grant;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;

public class DeleteGrantByGroupProjectAction extends AbstractGrantAction<Role> {

	private final String roleid;
	private final String groupid;
	private final String projectid;
	private final boolean inherited;


	public DeleteGrantByGroupProjectAction(AssignmentApi assignmentApi, IdentityApi identityApi, String roleid,
			String groupid, String projectid, boolean inherited) {
		super(assignmentApi, identityApi);
		this.roleid = roleid;
		this.groupid = groupid;
		this.projectid = projectid;
		this.inherited = inherited;
	}

	@Override
	public Role execute(ContainerRequestContext request) {
		KeystonePreconditions.requireGroup(groupid);
		KeystonePreconditions.requireProject(projectid);
		assignmentApi.deleteGrantByGroupProject(roleid, groupid, projectid, inherited);
		return null;
	}

	@Override
	public String getName() {
		return "revoke_grant";
	}
}
