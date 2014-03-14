package com.infinities.keystone4j.assignment.action.grant;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.identity.IdentityApi;

public class ListGrantsByUserProjectAction extends AbstractGrantAction<List<Role>> {

	private final String userid;
	private final String projectid;
	private final boolean inherited;


	public ListGrantsByUserProjectAction(AssignmentApi assignmentApi, IdentityApi identityApi, String userid,
			String projectid, boolean inherited) {
		super(assignmentApi, identityApi);
		this.userid = userid;
		this.projectid = projectid;
		this.inherited = inherited;
	}

	@Override
	public List<Role> execute(ContainerRequestContext request) {
		KeystonePreconditions.requireUser(userid);
		KeystonePreconditions.requireProject(projectid);
		return assignmentApi.listGrantsByUserProject(userid, projectid, inherited);
	}

	@Override
	public String getName() {
		return "list_grants";
	}
}
