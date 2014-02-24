package com.infinities.keystone4j.assignment.action.grant;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.identity.IdentityApi;

public class DeleteGrantByUserProjectAction extends AbstractGrantAction<Role> {

	private String roleid;
	private String userid;
	private String projectid;
	private boolean inherited;


	public DeleteGrantByUserProjectAction(AssignmentApi assignmentApi, IdentityApi identityApi, String roleid,
			String userid, String projectid, boolean inherited) {
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
		assignmentApi.deleteGrantByUserProject(roleid, userid, projectid, inherited);
		return null;
	}
}
