package com.infinities.keystone4j.assignment.command.grant;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;

public class DeleteGrantByUserProjectCommand extends AbstractAssignmentCommand<Role> {

	private String roleid;
	private String userid;
	private String projectid;
	private boolean inherited;


	public DeleteGrantByUserProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String roleid, String userid, String projectid,
			boolean inherited) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.roleid = roleid;
		this.userid = userid;
		this.projectid = projectid;
		this.inherited = inherited;
	}

	@Override
	public Role execute() {
		this.getAssignmentDriver().deleteGrantByUserProject(roleid, userid, projectid, inherited);
		this.getTokenApi().deleteTokensForUser(userid, null);
		return null;
	}

}
