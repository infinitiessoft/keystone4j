package com.infinities.keystone4j.assignment.command.domain;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;

//TODO useless
public class GetDomainByNameCommand extends AbstractAssignmentCommand<Domain> {

	private final String domainName;


	public GetDomainByNameCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String domainName) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.domainName = domainName;
	}

	@Override
	public Domain execute() {
		return this.getAssignmentDriver().getDomainByName(domainName);
	}
}
