package com.infinities.keystone4j.assignment.controller.action.domain;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class ListDomainsAction extends AbstractDomainAction implements FilterProtectedAction<Domain> {

	public ListDomainsAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		super(assignmentApi, tokenProviderApi, policyApi);
	}

	@Override
	public CollectionWrapper<Domain> execute(ContainerRequestContext request, String... filters) throws Exception {
		Hints hints = buildDriverHints(request, filters);
		List<Domain> domains = this.getAssignmentApi().listDomains(hints);
		CollectionWrapper<Domain> wrapper = wrapCollection(request, domains, hints);
		return wrapper;
	}

	@Override
	public String getName() {
		return "list_domains";
	}
}
