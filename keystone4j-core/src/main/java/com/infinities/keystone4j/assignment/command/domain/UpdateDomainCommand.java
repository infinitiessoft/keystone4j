package com.infinities.keystone4j.assignment.command.domain;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;

public class UpdateDomainCommand extends AbstractAssignmentCommand<Domain> {

	private String domainid;
	private Domain domain;


	public UpdateDomainCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String domainid, Domain domain) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.domainid = domainid;
		this.domain = domain;
	}

	@Override
	public Domain execute() {
		Domain ret = this.getAssignmentDriver().updateDomain(domainid, domain);
		if (!domain.getEnabled()) {
			this.getTokenApi().deleteTokensForDomain(domainid);
		}
		// invalidate cache(getDomain, getDomainByName)

		return ret;
	}
}
