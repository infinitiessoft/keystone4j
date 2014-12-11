package com.infinities.keystone4j.assignment.api.command.domain;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.token.TokenApi;

public class DeleteDomainCommand extends AbstractAssignmentCommand<Domain> {

	private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final static String DELETE_DEFAULT_DOMAIN = "delete the default domain";
	private final static String DOMAIN_IS_ENABLED = "delete a domain that is not disabled";
	private final String domainid;


	public DeleteDomainCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String domainid) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.domainid = domainid;
	}

	@Override
	public Domain execute() {
		String defaultDomainid = Config.Instance.getOpt(Config.Type.identity, DEFAULT_DOMAIN_ID).asText();

		if (defaultDomainid.equals(domainid)) {
			throw Exceptions.ForbiddenActionException.getInstance(null, DELETE_DEFAULT_DOMAIN);
		}

		Domain domain = this.getAssignmentDriver().getDomain(domainid);

		// avoid inadvertent deletes.
		if (domain.getEnabled()) {
			throw Exceptions.ForbiddenActionException.getInstance(null, DOMAIN_IS_ENABLED);
		}

		deleteDomainContents(domainid);
		this.getAssignmentDriver().deleteDomain(domainid);
		// invalidate getDomain, getDomainByName

		return null;
	}

	private void deleteDomainContents(String domainid) {
		// cacade remove project,group,user
		// hibernate handle this automately
	}
}
