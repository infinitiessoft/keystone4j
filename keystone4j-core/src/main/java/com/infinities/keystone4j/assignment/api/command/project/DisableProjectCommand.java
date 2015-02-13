package com.infinities.keystone4j.assignment.api.command.project;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class DisableProjectCommand extends AbstractAssignmentCommand implements NotifiableCommand<Project> {

	private final String projectid;


	public DisableProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String projectid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.projectid = projectid;
	}

	@Override
	public Project execute() {
		return this.getAssignmentDriver().getProject(projectid);
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return projectid;
		}
		throw new IllegalArgumentException("invalid index");
	}
}
