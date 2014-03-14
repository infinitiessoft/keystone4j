package com.infinities.keystone4j.assignment.action.domain;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;

public class ListDomainsAction extends AbstractDomainAction<List<Domain>> {

	private final String name;
	private final Boolean enabled;


	public ListDomainsAction(AssignmentApi assignmentApi, String name, Boolean enabled) {
		super(assignmentApi);
		this.name = name;
		this.enabled = enabled;
	}

	@Override
	public List<Domain> execute(ContainerRequestContext request) {
		Iterable<Domain> domains = this.getAssignmentApi().listDomains();

		List<Predicate<Domain>> filters = Lists.newArrayList();

		if (!Strings.isNullOrEmpty(name)) {
			Predicate<Domain> filter = new Predicate<Domain>() {

				@Override
				public boolean apply(Domain d) {
					return name.equals(d.getName());
				}
			};
			filters.add(filter);
		}

		if (enabled != null) {
			Predicate<Domain> filter = new Predicate<Domain>() {

				@Override
				public boolean apply(Domain d) {
					return enabled.compareTo(d.getEnabled()) == 0;
				}
			};
			filters.add(filter);
		}

		if (filters.size() > 0) {
			Predicate<Domain> filter = Predicates.and(filters);

			domains = Iterables.filter(domains, filter);
		}

		return Lists.newArrayList(domains);
	}

	@Override
	public String getName() {
		return "list_domains";
	}
}
