package com.infinities.keystone4j.assignment.controller.action.domain;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class DeleteDomainAction extends AbstractDomainAction implements ProtectedAction<Domain> {

	private final static Logger logger = LoggerFactory.getLogger(DeleteDomainAction.class);
	private final String domainid;


	public DeleteDomainAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String domainid) {
		super(assignmentApi, tokenProviderApi, policyApi);
		this.domainid = domainid;
	}

	@Override
	public MemberWrapper<Domain> execute(ContainerRequestContext request) throws Exception {
		logger.debug("delete domain: {}", domainid);
		this.getAssignmentApi().deleteDomain(domainid);
		return null;
	}

	@Override
	public String getName() {
		return "delete_domain";
	}

}
