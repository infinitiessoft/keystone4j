package com.infinities.keystone4j.assignment.action.grant;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;

public class CheckGrantByUserProjectAction extends AbstractGrantAction<Role> {

	private final String roleid;
	private final String userid;
	private final String projectid;
	private final boolean inherited;


	public CheckGrantByUserProjectAction(AssignmentApi assignmentApi, IdentityApi identityApi, String roleid, String userid,
			String projectid, boolean inherited) {
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

		// TODO try to test if user exist?
		this.getIdentityApi().getUser(userid, null);

		assignmentApi.getGrantByUserProject(roleid, userid, projectid, inherited);

		return null;
	}

	@Override
	public String getName() {
		return "check_grant";
	}
}
