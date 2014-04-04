package com.infinities.keystone4j.assignment.action.domain;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.assignment.Domain;

public class DeleteDomainAction extends AbstractDomainAction<Domain> {

	private final String domainid;


	public DeleteDomainAction(AssignmentApi assignmentApi, String domainid) {
		super(assignmentApi);
		this.domainid = domainid;
	}

	@Override
	public Domain execute(ContainerRequestContext request) {
		return this.getAssignmentApi().deleteDomain(domainid);
	}

	@Override
	public String getName() {
		return "delete_domain";
	}

}
