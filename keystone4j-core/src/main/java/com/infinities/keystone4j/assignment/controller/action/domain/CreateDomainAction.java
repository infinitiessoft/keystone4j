package com.infinities.keystone4j.assignment.controller.action.domain;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CreateDomainAction extends AbstractDomainAction implements ProtectedAction<Domain> {

	private final Domain domain;


	public CreateDomainAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, Domain domain) {
		super(assignmentApi, tokenProviderApi);
		this.domain = domain;
	}

	@Override
	public MemberWrapper<Domain> execute(ContainerRequestContext context) {
		assignUniqueId(domain);
		Domain ref = assignmentApi.createDomain(domain.getId(), domain);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "create_domain";
	}
}
