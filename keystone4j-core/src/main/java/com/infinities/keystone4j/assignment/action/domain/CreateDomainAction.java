package com.infinities.keystone4j.assignment.action.domain;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;

public class CreateDomainAction extends AbstractDomainAction<Domain> {

	private final Domain domain;


	public CreateDomainAction(AssignmentApi assignmentApi, Domain domain) {
		super(assignmentApi);
		this.domain = domain;
	}

	@Override
	public Domain execute() {
		Domain ret = assignmentApi.createDomain(domain);
		return ret;
	}

	@Override
	public String getName() {
		return "create_domain";
	}
}
