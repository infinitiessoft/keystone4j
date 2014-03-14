package com.infinities.keystone4j.assignment.command.grant;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;

public class ListGrantsByUserDomainCommand extends AbstractAssignmentCommand<List<Role>> {

	private final String userid;
	private final String domainid;
	private final boolean inherited;


	public ListGrantsByUserDomainCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String userid, String domainid, boolean inherited) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.userid = userid;
		this.domainid = domainid;
		this.inherited = inherited;
	}

	@Override
	public List<Role> execute() {
		return this.getAssignmentDriver().listGrantsByUserDomain(userid, domainid, inherited);
	}
}
