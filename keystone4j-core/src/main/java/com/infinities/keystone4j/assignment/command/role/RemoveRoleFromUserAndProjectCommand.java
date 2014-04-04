package com.infinities.keystone4j.assignment.command.role;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.token.TokenApi;

public class RemoveRoleFromUserAndProjectCommand extends AbstractAssignmentCommand<Role> {

	private final String userid;
	private final String projectid;
	private final String roleid;


	public RemoveRoleFromUserAndProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String userid, String projectid, String roleid) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.userid = userid;
		this.projectid = projectid;
		this.roleid = roleid;
	}

	@Override
	public Role execute() {
		this.getAssignmentDriver().removeRoleFromUserAndProject(userid, projectid, roleid);
		this.getTokenApi().deleteTokensForUser(userid, null);
		return null;
	}
}
