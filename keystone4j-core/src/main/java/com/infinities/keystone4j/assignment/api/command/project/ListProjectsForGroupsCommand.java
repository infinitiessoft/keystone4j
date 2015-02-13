package com.infinities.keystone4j.assignment.api.command.project;

import java.util.List;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;

public class ListProjectsForGroupsCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<List<Project>> {

	private final List<String> groupids;


	public ListProjectsForGroupsCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, List<String> groupids) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.groupids = groupids;
	}

	@Override
	public List<Project> execute() {
		return this.getAssignmentDriver().listProjectsForGroups(groupids);
	}
}
