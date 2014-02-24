package com.infinities.keystone4j.assignment.action.grant;

import java.util.List;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.identity.IdentityApi;

public class ListGrantsByUserProjectAction extends AbstractGrantAction<List<Role>> {

	private String userid;
	private String projectid;
	private boolean inherited;


	public ListGrantsByUserProjectAction(AssignmentApi assignmentApi, IdentityApi identityApi, String userid,
			String projectid, boolean inherited) {
		super(assignmentApi, identityApi);
		this.userid = userid;
		this.projectid = projectid;
		this.inherited = inherited;
	}

	@Override
	public List<Role> execute() {
		KeystonePreconditions.requireUser(userid);
		KeystonePreconditions.requireProject(projectid);
		return assignmentApi.listGrantsByUserProject(userid, projectid, inherited);
	}
}
