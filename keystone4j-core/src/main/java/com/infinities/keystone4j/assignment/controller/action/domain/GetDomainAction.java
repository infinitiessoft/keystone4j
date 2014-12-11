package com.infinities.keystone4j.assignment.controller.action.domain;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class GetDomainAction extends AbstractDomainAction implements ProtectedAction<Domain> {

	private final String domainid;


	public GetDomainAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, String domainid) {
		super(assignmentApi, tokenProviderApi);
		this.domainid = domainid;
	}

	@Override
	public MemberWrapper<Domain> execute(ContainerRequestContext context) {
		Domain ref = this.getAssignmentApi().getDomain(domainid);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "get_domain";
	}
}
