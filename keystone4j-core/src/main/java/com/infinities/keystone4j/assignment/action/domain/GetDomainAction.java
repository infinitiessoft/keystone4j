package com.infinities.keystone4j.assignment.action.domain;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;

public class GetDomainAction extends AbstractDomainAction<Domain> {

	private final String domainid;


	public GetDomainAction(AssignmentApi assignmentApi, String domainid) {
		super(assignmentApi);
		this.domainid = domainid;
	}

	@Override
	public Domain execute() {
		return this.getAssignmentApi().getDomain(domainid);
	}

	@Override
	public String getName() {
		return "get_domain";
	}
}
