package com.infinities.keystone4j.policy.action;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.model.Policy;

public class ListPoliciesAction extends AbstractPolicyAction<List<Policy>> {

	private String type;


	public ListPoliciesAction(PolicyApi policyApi, String type) {
		super(policyApi);
		this.type = type;
	}

	@Override
	public List<Policy> execute() {
		Iterable<Policy> policies = this.getPolicyApi().listPolicies();

		List<Predicate<Policy>> filters = Lists.newArrayList();

		if (!Strings.isNullOrEmpty(type)) {
			Predicate<Policy> filter = new Predicate<Policy>() {

				@Override
				public boolean apply(Policy p) {
					return type.equals(p.getType());
				}
			};
			filters.add(filter);
		}

		if (filters.size() > 0) {
			Predicate<Policy> filter = Predicates.and(filters);

			policies = Iterables.filter(policies, filter);
		}

		return Lists.newArrayList(policies);
	}
}
