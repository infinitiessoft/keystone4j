package com.infinities.keystone4j.assignment.controller.impl;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.action.domain.CreateDomainAction;
import com.infinities.keystone4j.assignment.action.domain.DeleteDomainAction;
import com.infinities.keystone4j.assignment.action.domain.GetDomainAction;
import com.infinities.keystone4j.assignment.action.domain.ListDomainsAction;
import com.infinities.keystone4j.assignment.action.domain.UpdateDomainAction;
import com.infinities.keystone4j.assignment.controller.DomainV3Controller;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.decorator.FilterCheckDecorator;
import com.infinities.keystone4j.decorator.PaginateDecorator;
import com.infinities.keystone4j.decorator.PolicyCheckDecorator;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.DomainWrapper;
import com.infinities.keystone4j.model.assignment.DomainsWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public class DomainV3ControllerImpl extends BaseController implements DomainV3Controller {

	private final AssignmentApi assignmentApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;
	private final Map<String, Object> parMap;


	public DomainV3ControllerImpl(AssignmentApi assignmentApi, TokenApi tokenApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
		parMap = Maps.newHashMap();
	}

	@Override
	public DomainWrapper createDomain(Domain domain) {
		parMap.put("domain", domain);
		Action<Domain> command = new PolicyCheckDecorator<Domain>(new CreateDomainAction(assignmentApi, domain), null,
				tokenApi, policyApi, parMap);
		Domain ret = command.execute(getRequest());
		return new DomainWrapper(ret, getRequest());
	}

	@Override
	public DomainsWrapper listDomains(String name, Boolean enabled, int page, int perPage) {
		parMap.put("name", name);
		parMap.put("enabled", enabled);
		Action<List<Domain>> command = new FilterCheckDecorator<List<Domain>>(new PaginateDecorator<Domain>(
				new ListDomainsAction(assignmentApi, name, enabled), page, perPage), tokenApi, policyApi, parMap);

		List<Domain> ret = command.execute(getRequest());
		return new DomainsWrapper(ret, getRequest());
	}

	@Override
	public DomainWrapper getDomain(String domainid) {
		parMap.put("domainid", domainid);
		Action<Domain> command = new PolicyCheckDecorator<Domain>(new GetDomainAction(assignmentApi, domainid), null,
				tokenApi, policyApi, parMap);
		Domain ret = command.execute(getRequest());
		return new DomainWrapper(ret, getRequest());
	}

	@Override
	public DomainWrapper updateDomain(String domainid, Domain domain) {
		parMap.put("domainid", domainid);
		parMap.put("domain", domain);
		Action<Domain> command = new PolicyCheckDecorator<Domain>(new UpdateDomainAction(assignmentApi, domainid, domain),
				null, tokenApi, policyApi, parMap);
		Domain ret = command.execute(getRequest());
		return new DomainWrapper(ret, getRequest());
	}

	@Override
	public void deleteDomain(String domainid) {
		parMap.put("domainid", domainid);
		Action<Domain> command = new PolicyCheckDecorator<Domain>(new DeleteDomainAction(assignmentApi, domainid), null,
				tokenApi, policyApi, parMap);
		command.execute(getRequest());
	}

}
