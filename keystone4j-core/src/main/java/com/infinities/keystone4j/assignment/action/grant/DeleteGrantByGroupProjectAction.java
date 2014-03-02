package com.infinities.keystone4j.assignment.action.grant;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.identity.IdentityApi;

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
	public Role execute() {
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
