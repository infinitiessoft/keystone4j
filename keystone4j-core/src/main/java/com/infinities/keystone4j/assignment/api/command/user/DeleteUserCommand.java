package com.infinities.keystone4j.assignment.api.command.user;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.identity.User;

public class DeleteUserCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<User> {

	// private final static Logger logger =
	// LoggerFactory.getLogger(DeleteUserCommand.class);
	private final String userid;


	public DeleteUserCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String userid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.userid = userid;
	}

	@Override
	public User execute() throws Exception {
		this.getAssignmentDriver().deleteUser(userid);
		return null;
	}
}
