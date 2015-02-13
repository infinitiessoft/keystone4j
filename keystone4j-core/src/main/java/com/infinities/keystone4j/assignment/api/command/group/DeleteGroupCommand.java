package com.infinities.keystone4j.assignment.api.command.group;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.identity.Group;

public class DeleteGroupCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<Group> {

	// private final static Logger logger =
	// LoggerFactory.getLogger(DeleteUserCommand.class);
	private final String groupid;


	public DeleteGroupCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String groupid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.groupid = groupid;
	}

	@Override
	public Group execute() throws Exception {
		this.getAssignmentDriver().deleteGroup(groupid);
		return null;
	}
}
