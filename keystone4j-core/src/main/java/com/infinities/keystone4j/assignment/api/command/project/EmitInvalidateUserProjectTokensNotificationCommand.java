package com.infinities.keystone4j.assignment.api.command.project;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class EmitInvalidateUserProjectTokensNotificationCommand extends AbstractAssignmentCommand implements
		NotifiableCommand<Project> {

	// private final static Logger logger =
	// LoggerFactory.getLogger(DeleteUserCommand.class);
	private final Payload payload;


	public EmitInvalidateUserProjectTokensNotificationCommand(CredentialApi credentialApi, IdentityApi identityApi,
			AssignmentApi assignmentApi, RevokeApi revokeApi, AssignmentDriver assignmentDriver, Payload payload) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.payload = payload;
	}

	@Override
	public Project execute() throws Exception {
		return null;
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return payload;
		}
		throw new IllegalArgumentException("invalid index");
	}
}
