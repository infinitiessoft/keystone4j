package com.infinities.keystone4j.assignment.action.grant;

import java.util.List;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.identity.IdentityApi;

public class ListGrantsByGroupProjectAction extends AbstractGrantAction<List<Role>> {

	private final String groupid;
	private final String projectid;
	private final boolean inherited;


	public ListGrantsByGroupProjectAction(AssignmentApi assignmentApi, IdentityApi identityApi, String groupid,
			String projectid, boolean inherited) {
		super(assignmentApi, identityApi);
		this.groupid = groupid;
		this.projectid = projectid;
		this.inherited = inherited;
	}

	@Override
	public List<Role> execute() {
		KeystonePreconditions.requireGroup(groupid);
		KeystonePreconditions.requireProject(projectid);
		return assignmentApi.listGrantsByGroupProject(groupid, projectid, inherited);
	}

	@Override
	public String getName() {
		return "list_grants";
	}
}
