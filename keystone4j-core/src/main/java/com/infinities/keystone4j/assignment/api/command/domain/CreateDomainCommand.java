package com.infinities.keystone4j.assignment.api.command.domain;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class CreateDomainCommand extends AbstractAssignmentCommand implements NotifiableCommand<Domain> {

	private final String domainid;
	private final Domain domain;


	public CreateDomainCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String domainid, Domain domain) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.domainid = domainid;
		this.domain = domain;
	}

	@Override
	public Domain execute() throws Exception {
		if (!this.getIdentityApi().isMultipleDomainsSupported()
				&& !domainid.equals(Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText())) {
			throw Exceptions.ForbiddenException.getInstance("Multiple domains are not supported");
		}
		Domain ret = this.getAssignmentDriver().createDomain(domainid, domain);
		// TODO if SHOULD_CACHE(ret):
		// self.get_domain.set(ret, self, domain_id)
		// self.get_domain_by_name.set(ret, self, ret['name'])

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
