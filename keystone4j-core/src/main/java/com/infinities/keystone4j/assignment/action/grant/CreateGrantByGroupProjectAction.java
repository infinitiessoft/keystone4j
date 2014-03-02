package com.infinities.keystone4j.assignment.action.grant;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.identity.IdentityApi;

public class CreateGrantByGroupProjectAction extends AbstractGrantAction<Role> {

	private final String roleid;
	private final String groupid;
	private final String projectid;


	public CreateGrantByGroupProjectAction(AssignmentApi assignmentApi, IdentityApi identityApi, String roleid,
			String groupid, String projectid) {
		super(assignmentApi, identityApi);
		this.roleid = roleid;
		this.groupid = groupid;
		this.projectid = projectid;
	}

	@Override
	public Role execute() {
		KeystonePreconditions.requireGroup(groupid);
		KeystonePreconditions.requireProject(projectid);

		// TODO try to test if group exist?
		this.getIdentityApi().getGroup(groupid, null);

		assignmentApi.createGrantByGroupProject(roleid, groupid, projectid);

		return null;
	}

	@Override
	public String getName() {
		return "create_grant";
	}
}
