package com.infinities.keystone4j.assignment.controller.impl;

import java.util.List;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.action.domain.CreateDomainAction;
import com.infinities.keystone4j.assignment.action.domain.DeleteDomainAction;
import com.infinities.keystone4j.assignment.action.domain.GetDomainAction;
import com.infinities.keystone4j.assignment.action.domain.ListDomainsAction;
import com.infinities.keystone4j.assignment.action.domain.UpdateDomainAction;
import com.infinities.keystone4j.assignment.controller.DomainV3Controller;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.DomainWrapper;
import com.infinities.keystone4j.assignment.model.DomainsWrapper;
import com.infinities.keystone4j.decorator.FilterCheckDecorator;
import com.infinities.keystone4j.decorator.PaginateDecorator;
import com.infinities.keystone4j.decorator.PolicyCheckDecorator;

public class DomainV3ControllerImpl implements DomainV3Controller {

	private AssignmentApi assignmentApi;


	public DomainV3ControllerImpl(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	@Override
	public DomainWrapper createDomain(Domain domain) {
		Action<Domain> command = new PolicyCheckDecorator<Domain>(new CreateDomainAction(assignmentApi, domain));
		Domain ret = command.execute();
		return new DomainWrapper(ret);
	}

	@Override
	public DomainsWrapper listDomains(String name, Boolean enabled, int page, int perPage) {
		Action<List<Domain>> command = new FilterCheckDecorator<List<Domain>>(new PaginateDecorator<Domain>(
				new ListDomainsAction(assignmentApi, name, enabled), page, perPage));

		List<Domain> ret = command.execute();
		return new DomainsWrapper(ret);
	}

	@Override
	public DomainWrapper getDomain(String domainid) {
		Action<Domain> command = new PolicyCheckDecorator<Domain>(new GetDomainAction(assignmentApi, domainid));
		Domain ret = command.execute();
		return new DomainWrapper(ret);
	}

	@Override
	public DomainWrapper updateDomain(String domainid, Domain domain) {
		Action<Domain> command = new PolicyCheckDecorator<Domain>(new UpdateDomainAction(assignmentApi, domainid, domain));
		Domain ret = command.execute();
		return new DomainWrapper(ret);
	}

	@Override
	public void deleteDomain(String domainid) {
		Action<Domain> command = new PolicyCheckDecorator<Domain>(new DeleteDomainAction(assignmentApi, domainid));
		command.execute();
	}

}
