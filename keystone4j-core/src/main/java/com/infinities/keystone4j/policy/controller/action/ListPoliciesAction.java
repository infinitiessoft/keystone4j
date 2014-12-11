package com.infinities.keystone4j.policy.controller.action;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class ListPoliciesAction extends AbstractPolicyAction implements FilterProtectedAction<Policy> {

	public ListPoliciesAction(PolicyApi policyApi, TokenProviderApi tokenProviderApi) {
		super(policyApi, tokenProviderApi);
	}

	@Override
	public CollectionWrapper<Policy> execute(ContainerRequestContext request, String... filters) throws Exception {
		Hints hints = buildDriverHints(request, filters);
		List<Policy> policies = this.getPolicyApi().listPolicies(hints);
		CollectionWrapper<Policy> wrapper = wrapCollection(request, policies, hints);
		return wrapper;
	}

	@Override
	public String getName() {
		return "list_policies";
	}
}
