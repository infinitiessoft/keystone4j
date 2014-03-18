package com.infinities.keystone4j.assignment.command.project;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;

public class GetProjectUsersCommand extends AbstractAssignmentCommand<List<User>> {

	private final String projectid;


	public GetProjectUsersCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String projectd) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.projectid = projectd;
	}

	@Override
	public List<User> execute() {
		List<User> users = this.getAssignmentApi().listUsersForProject(projectid);
		// List<String> groupids = Lists.newArrayList();
		// for (Group group : groups) {
		// groupids.add(group.getId());
		// }

		return users;
	}
}
