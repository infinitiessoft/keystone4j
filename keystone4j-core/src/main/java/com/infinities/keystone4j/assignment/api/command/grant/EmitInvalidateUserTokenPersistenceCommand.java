package com.infinities.keystone4j.assignment.api.command.grant;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class EmitInvalidateUserTokenPersistenceCommand extends AbstractAssignmentCommand implements NotifiableCommand<User> {

	// private final static Logger logger =
	// LoggerFactory.getLogger(DeleteUserCommand.class);
	private final String userid;


	public EmitInvalidateUserTokenPersistenceCommand(CredentialApi credentialApi, IdentityApi identityApi,
			AssignmentApi assignmentApi, RevokeApi revokeApi, AssignmentDriver assignmentDriver, String userid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.userid = userid;
	}

	@Override
	public User execute() throws Exception {
		this.getIdentityApi().emitInvalidateUserTokenPersistence(userid);
		return null;
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return userid;
		}
		throw new IllegalArgumentException("invalid index");
	}
}
