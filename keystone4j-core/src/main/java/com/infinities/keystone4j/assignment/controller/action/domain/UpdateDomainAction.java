package com.infinities.keystone4j.assignment.controller.action.domain;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class UpdateDomainAction extends AbstractDomainAction implements ProtectedAction<Domain> {

	private final String domainid;
	private final Domain domain;


	public UpdateDomainAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String domainid, Domain domain) {
		super(assignmentApi, tokenProviderApi, policyApi);
		this.domain = domain;
		this.domainid = domainid;
	}

	@Override
	public MemberWrapper<Domain> execute(ContainerRequestContext context) {
		requireMatchingId(domainid, domain);
		Domain ref = this.getAssignmentApi().updateDomain(domainid, domain);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "update_domain";
	}

}
