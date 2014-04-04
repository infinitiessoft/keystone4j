package com.infinities.keystone4j.assignment.action.domain;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.assignment.Domain;

public class UpdateDomainAction extends AbstractDomainAction<Domain> {

	private final String domainid;
	private final Domain domain;


	public UpdateDomainAction(AssignmentApi assignmentApi, String domainid, Domain domain) {
		super(assignmentApi);
		this.domain = domain;
		this.domainid = domainid;
	}

	@Override
	public Domain execute(ContainerRequestContext request) {
		KeystonePreconditions.requireMatchingId(domainid, domain);
		return this.getAssignmentApi().updateDomain(domainid, domain);
	}

	@Override
	public String getName() {
		return "update_domain";
	}

}
