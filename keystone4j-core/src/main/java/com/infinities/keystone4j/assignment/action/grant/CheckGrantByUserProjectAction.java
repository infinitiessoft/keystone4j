package com.infinities.keystone4j.assignment.action.grant;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.identity.IdentityApi;

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
	public Role execute() {
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
