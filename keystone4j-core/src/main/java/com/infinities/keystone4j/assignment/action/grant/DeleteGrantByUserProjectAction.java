package com.infinities.keystone4j.assignment.action.grant;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.identity.IdentityApi;

public class DeleteGrantByUserProjectAction extends AbstractGrantAction<Role> {

	private final String roleid;
	private final String userid;
	private final String projectid;
	private final boolean inherited;


	public DeleteGrantByUserProjectAction(AssignmentApi assignmentApi, IdentityApi identityApi, String roleid,
			String userid, String projectid, boolean inherited) {
		super(assignmentApi, identityApi);
		this.roleid = roleid;
		this.userid = userid;
		this.projectid = projectid;
		this.inherited = inherited;
	}

	@Override
	public Role execute(ContainerRequestContext request) {
		KeystonePreconditions.requireUser(userid);
		KeystonePreconditions.requireProject(projectid);
		assignmentApi.deleteGrantByUserProject(roleid, userid, projectid, inherited);
		return null;
	}

	@Override
	public String getName() {
		return "revoke_grant";
	}
}
