package com.infinities.keystone4j.assignment.command.project;

import java.util.List;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.token.TokenApi;

public class ListProjectsForUserCommand extends AbstractAssignmentCommand<List<Project>> {

	private final String userid;


	public ListProjectsForUserCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String userid) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.userid = userid;
	}

	@Override
	public List<Project> execute() {
		List<Group> groups = this.getIdentityApi().listGroupsForUser(userid, null);
		List<String> groupids = Lists.newArrayList();
		for (Group group : groups) {
			groupids.add(group.getId());
		}

		return this.getAssignmentDriver().listProjectsForUser(userid, groupids);
	}
}
