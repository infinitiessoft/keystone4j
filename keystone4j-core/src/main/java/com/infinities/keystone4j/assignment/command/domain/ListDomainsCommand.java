package com.infinities.keystone4j.assignment.command.domain;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.token.TokenApi;

public class ListDomainsCommand extends AbstractAssignmentCommand<List<Domain>> {

	public ListDomainsCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
	}

	@Override
	public List<Domain> execute() {
		return this.getAssignmentDriver().listDomains();
	}
}
