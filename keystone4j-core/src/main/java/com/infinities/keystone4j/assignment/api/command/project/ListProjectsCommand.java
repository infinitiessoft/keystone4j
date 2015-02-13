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

public class ListProjectsCommand extends AbstractAssignmentCommand implements TruncatedCommand<Project> {

	public ListProjectsCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
	}

	@Override
	public List<Project> execute(Hints hints) throws Exception {
		if (hints == null) {
			hints = new Hints();
		}

		return this.getAssignmentDriver().listProjects(hints);
	}
}
