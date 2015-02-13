package com.infinities.keystone4j.assignment.api.command.project;

import java.util.List;

import com.infinities.keystone4j.TruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;

public class ListProjectsForUserCommand extends AbstractAssignmentCommand implements TruncatedCommand<Project> {

	private final String userid;


	public ListProjectsForUserCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String userid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.userid = userid;
	}

	@Override
	public List<Project> execute(Hints hints) throws Exception {
		List<String> groupids = this.getGroupIdsForUserId(userid);

		if (hints == null) {
			hints = new Hints();
		}

		return this.getAssignmentDriver().listProjectsForUser(userid, groupids, hints);
	}
}
