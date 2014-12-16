package com.infinities.keystone4j.assignment.controller.impl;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.DomainV3Controller;
import com.infinities.keystone4j.assignment.controller.action.domain.CreateDomainAction;
import com.infinities.keystone4j.assignment.controller.action.domain.DeleteDomainAction;
import com.infinities.keystone4j.assignment.controller.action.domain.GetDomainAction;
import com.infinities.keystone4j.assignment.controller.action.domain.ListDomainsAction;
import com.infinities.keystone4j.assignment.controller.action.domain.UpdateDomainAction;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.decorator.FilterProtectedDecorator;
import com.infinities.keystone4j.decorator.ProtectedDecorator;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.assignment.controllers.DomainV3 20141208

public class DomainV3ControllerImpl extends BaseController implements DomainV3Controller {

	private final AssignmentApi assignmentApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	public DomainV3ControllerImpl(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	// TODO Ignore validation.validated(schema.domain_create,'domain')
	@Override
	public MemberWrapper<Domain> createDomain(Domain domain) throws Exception {
		// parMap.put("domain", domain);
		ProtectedAction<Domain> command = new ProtectedDecorator<Domain>(new CreateDomainAction(assignmentApi,
				tokenProviderApi, policyApi, domain), tokenProviderApi, policyApi);
		MemberWrapper<Domain> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public CollectionWrapper<Domain> listDomains() throws Exception {
		FilterProtectedAction<Domain> command = new FilterProtectedDecorator<Domain>(new ListDomainsAction(assignmentApi,
				tokenProviderApi, policyApi), tokenProviderApi, policyApi);
		CollectionWrapper<Domain> ret = command.execute(getRequest(), "name", "enabled");
		return ret;
	}

	@Override
	public MemberWrapper<Domain> getDomain(String domainid) throws Exception {
		Domain ref = getMemberFromDriver(domainid);
		ProtectedAction<Domain> command = new ProtectedDecorator<Domain>(new GetDomainAction(assignmentApi,
				tokenProviderApi, policyApi, domainid), tokenProviderApi, policyApi, ref);
		MemberWrapper<Domain> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public MemberWrapper<Domain> updateDomain(String domainid, Domain domain) throws Exception {
		Domain ref = getMemberFromDriver(domainid);
		ProtectedAction<Domain> command = new ProtectedDecorator<Domain>(new UpdateDomainAction(assignmentApi,
				tokenProviderApi, policyApi, domainid, domain), tokenProviderApi, policyApi, ref);
		MemberWrapper<Domain> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public void deleteDomain(String domainid) throws Exception {
		Domain ref = getMemberFromDriver(domainid);
		ProtectedAction<Domain> command = new ProtectedDecorator<Domain>(new DeleteDomainAction(assignmentApi,
				tokenProviderApi, policyApi, domainid), tokenProviderApi, policyApi, ref);
		command.execute(getRequest());
	}

	public Domain getMemberFromDriver(String domainid) {
		return assignmentApi.getDomain(domainid);
	}

}
