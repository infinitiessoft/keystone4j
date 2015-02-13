package com.infinities.keystone4j.assignment.api.command.domain;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;

//TODO useless
public class GetDomainByNameCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<Domain> {

	private final String domainName;


	public GetDomainByNameCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String domainName) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.domainName = domainName;
	}

	@Override
	public Domain execute() {
		return this.getAssignmentDriver().getDomainByName(domainName);
	}
}
