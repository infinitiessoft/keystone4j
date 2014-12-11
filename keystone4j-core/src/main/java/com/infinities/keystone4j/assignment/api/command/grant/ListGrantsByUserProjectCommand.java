package com.infinities.keystone4j.assignment.api.command.grant;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.token.TokenApi;

public class ListGrantsByUserProjectCommand extends AbstractAssignmentCommand<List<Role>> {

	private final String userid;
	private final String projectid;
	private final boolean inherited;


	public ListGrantsByUserProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String userid, String projectid,
			boolean inherited) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.userid = userid;
		this.projectid = projectid;
		this.inherited = inherited;
	}

	@Override
	public List<Role> execute() {
		return this.getAssignmentDriver().listGrantsByUserProject(userid, projectid, inherited);
	}

}
