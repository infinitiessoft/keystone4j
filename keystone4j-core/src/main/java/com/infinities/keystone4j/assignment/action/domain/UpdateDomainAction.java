package com.infinities.keystone4j.assignment.action.domain;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;

public class UpdateDomainAction extends AbstractDomainAction<Domain> {

	private String domainid;
	private Domain domain;


	public UpdateDomainAction(AssignmentApi assignmentApi, String domainid, Domain domain) {
		super(assignmentApi);
	}

	@Override
	public Domain execute() {
		KeystonePreconditions.requireMatchingId(domainid, domain);
		return this.getAssignmentApi().updateDomain(domainid, domain);
	}

	@Override
	public String getName() {
		return "update_domain";
	}

}
