package com.infinities.keystone4j.assignment.api.command.grant;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.token.TokenApi;

public class CreateGrantByGroupProjectCommand extends AbstractAssignmentCommand<Role> {

	// private Logger logger =
	// LoggerFactory.getLogger(GetGrantByGroupProjectCommand.class);
	// private final static String GROUP_NOT_FOUND =
	// "Group {} not found, no tokens to invalidate.";
	private final String roleid;
	private final String groupid;
	private final String projectid;


	public CreateGrantByGroupProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String roleid, String groupid, String projectid) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.groupid = groupid;
		this.projectid = projectid;
		this.roleid = roleid;
	}

	@Override
	public Role execute() {
		this.getAssignmentDriver().createGrantByGroupProject(roleid, groupid, projectid);
		return null;
	}
}
