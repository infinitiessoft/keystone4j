package com.infinities.keystone4j.assignment.api.command.grant;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.token.TokenApi;

public class CreateGrantByUserDomainCommand extends AbstractAssignmentCommand<Role> {

	private final String roleid;
	private final String userid;
	private final String domainid;
	private final boolean inherited;


	public CreateGrantByUserDomainCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String roleid, String userid, String domainid,
			boolean inherited) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.roleid = roleid;
		this.userid = userid;
		this.domainid = domainid;
		this.inherited = inherited;
	}

	@Override
	public Role execute() {
		this.getAssignmentDriver().createGrantByUserDomain(roleid, userid, domainid, inherited);
		return null;
	}
}
