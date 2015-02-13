package com.infinities.keystone4j.assignment.api.command.project;

import java.util.List;

import com.google.common.base.Strings;
import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;

public class ListProjectParentsCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<List<Project>> {

	private final String projectid;
	private final String userid;


	public ListProjectParentsCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String projectid, String userid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.projectid = projectid;
		this.userid = userid;
	}

	@Override
	public List<Project> execute() throws Exception {
		List<Project> parents = this.getAssignmentDriver().listProjectParents(projectid);
		if (!Strings.isNullOrEmpty(userid)) {
			filterProjectsList(parents, userid);
		}
		return parents;
	}
}
