package com.infinities.keystone4j.assignment.controller.action.domain;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class DeleteDomainAction extends AbstractDomainAction implements ProtectedAction<Domain> {

	private final String domainid;


	public DeleteDomainAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, String domainid) {
		super(assignmentApi, tokenProviderApi);
		this.domainid = domainid;
	}

	@Override
	public MemberWrapper<Domain> execute(ContainerRequestContext request) {
		this.getAssignmentApi().deleteDomain(domainid);
		return null;
	}

	@Override
	public String getName() {
		return "delete_domain";
	}

}
