package com.infinities.keystone4j.assignment.api.command.domain;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class UpdateDomainCommand extends AbstractAssignmentCommand implements NotifiableCommand<Domain> {

	private final String domainid;
	private final Domain domain;


	public UpdateDomainCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String domainid, Domain domain) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.domainid = domainid;
		this.domain = domain;
	}

	@Override
	public Domain execute() throws Exception {
		Domain originalDomain = this.getAssignmentDriver().getDomain(domainid);
		Domain ret = this.getAssignmentDriver().updateDomain(domainid, domain);

		if (originalDomain.getEnabled() && !domain.getEnabled()) {
			this.getAssignmentApi().disableDomain(domainid);
		}
		// TODO ignore invalidate cache(getDomain, getDomainByName)

		return ret;
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return domainid;
		} else if (index == 2) {
			return domain;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
