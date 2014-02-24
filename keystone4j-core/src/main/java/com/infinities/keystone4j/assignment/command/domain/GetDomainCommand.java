package com.infinities.keystone4j.assignment.command.domain;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;

public class GetDomainCommand extends AbstractAssignmentCommand<Domain> {

	private String domainid;


	public GetDomainCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String domainid) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.domainid = domainid;
	}

	@Override
	public Domain execute() {
		return this.getAssignmentDriver().getDomain(domainid);
	}
}
